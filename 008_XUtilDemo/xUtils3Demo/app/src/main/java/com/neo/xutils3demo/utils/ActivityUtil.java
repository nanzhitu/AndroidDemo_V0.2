package com.neo.xutils3demo.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.neo.xutils3demo.utils.callback.MyActivityLifecycleCallbacks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Neo on 2018/12/17.
 */

public class ActivityUtil {

    private static WeakReference<Activity> sTopActivityWeakRef;
    private static List<Activity> sActivityList = new LinkedList<>();
    private static List<Activity> sResumeActivity=new ArrayList<>();

    private static Application.ActivityLifecycleCallbacks mCallbacks = new MyActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            sActivityList.add(0,activity);
            setTopActivityWeakRef(activity);
            LogUtils.d(activity.getLocalClassName(), "onActivityCreated(), savedInstanceState = [" + bundle + "]");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setTopActivityWeakRef(activity);
            LogUtils.d(activity.getLocalClassName(), "onActivityStarted()");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivityWeakRef(activity);
            if (!sResumeActivity.contains(activity)) {
                sResumeActivity.add(activity);
            }
            LogUtils.d(activity.getLocalClassName(), "onActivityResumed() ");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtils.d(activity.getLocalClassName(), "onActivityPaused() ");

        }

        @Override
        public void onActivityStopped(Activity activity) {
            sResumeActivity.remove(activity);
            LogUtils.d(activity.getLocalClassName(), "onActivityStopped() ");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            LogUtils.d(activity.getLocalClassName(), "onActivitySaveInstanceState(), outState = [" + bundle + "]");

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            sActivityList.remove(activity);
            LogUtils.d(activity.getLocalClassName(), "onActivityDestroyed() ");
        }

        @Override
        public Activity current() {
            return sActivityList.size()>0 ? sActivityList.get(0): null;
        }

        @Override
        public int count() {
            return sActivityList.size();
        }

        @Override
        public boolean isFront() {
            return sResumeActivity.size() > 0;
        }
    };

    /**
     * 初始化activity工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        app.registerActivityLifecycleCallbacks(mCallbacks);

    }


    public static Application.ActivityLifecycleCallbacks getAcitvityManager(){
        return mCallbacks;
    }

    private static void setTopActivityWeakRef(Activity activity) {
        if (sTopActivityWeakRef == null || !activity.equals(sTopActivityWeakRef.get())) {
            sTopActivityWeakRef = new WeakReference<>(activity);
        }
    }

}

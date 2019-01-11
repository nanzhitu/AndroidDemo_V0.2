package com.neo.xutils3demo.utils.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.neo.xutils3demo.utils.interfac.IActivityState;

/**
 * Created by Neo on 2018/3/13.
 */

public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, IActivityState {
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public Activity current() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean isFront() {
        return false;
    }
}

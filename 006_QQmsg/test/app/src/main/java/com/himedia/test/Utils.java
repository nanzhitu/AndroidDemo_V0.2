package com.himedia.test;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neo on 2018/5/11.
 */

public class Utils {

    private static Context mContext;
    private static Handler sHandler;
    public static boolean isThreadRunning = true;

    private Utils() {}

    /**
     * 初始化工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        Utils.mContext = app;
        sHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 去初始化工具类
     *
     */
    public static void unInit() {
        isThreadRunning = false;
        sHandler = null;
        mContext = null;
    }


    /**
     * 获取 Application
     *
     * @return Context
     */
    public static Context getAppContext() {
        if (mContext != null) return mContext;
        throw new NullPointerException("u should init first");
    }

    public static int getDisplayWidth(){
        return getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeight(){
        return getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取 Handler
     *
     * @return Handler
     */
    public static Handler getAppHandler(){
        if (sHandler != null) return sHandler;
        throw new NullPointerException("u should init first");
    }

    public static void sendBroadCast(String action){
        sendBroadCast(new Intent(action));
    }

    public static void sendBroadCast(Intent intent){
        mContext.sendBroadcast(intent);
    }




    /**
     * 检查app是否安装
     *
     * @param context     context
     * @param packageName 包名
     * @return 是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        List<String> list = new ArrayList<>();
        if (packages != null) {
            for (int i = 0; i < packages.size(); i++) {
                String pn = packages.get(i).packageName;
                list.add(pn);
            }
        }
        return list.contains(packageName);
    }

    public static int getTextWidth(TextView textView){
            TextPaint tp = textView.getPaint();
            Rect rect = new Rect();
            String strTxt = textView.getText().toString();
            tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
            return rect.width();
    }
}

package com.neo.xutils3demo.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private static Context mContext;
    private static Handler sHandler;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        Utils.sApplication = app;
        Utils.mContext = app;
        sHandler = new Handler(Looper.getMainLooper());
        otherUtilInit();
    }


    private static void otherUtilInit(){
        if(sApplication != null) {
            ActivityUtil.init(sApplication);
        }
    }

    /**
     * 去初始化工具类
     *
     */
    public static void unInit() {
        sHandler = null;
        mContext = null;
    }

    /**
     * 获取 Application
     *
     * @return Application
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
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


}

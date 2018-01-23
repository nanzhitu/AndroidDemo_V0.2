package com.example.neo.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by neo on 2017/12/14.
 */

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();
    private static Context mContext;
    private static Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();
    }

    public static Context getContext(){
        return mContext;
    }

    public static Handler getHandler(){
        return mHandler;
    }
}

package com.neo.xutils3demo;

import android.app.Application;

import com.neo.xutils3demo.utils.Utils;

import org.xutils.x;

/**
 * Created by Neo on 2018/12/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        x.Ext.init(this);
    }
}

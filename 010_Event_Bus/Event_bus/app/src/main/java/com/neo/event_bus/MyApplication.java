package com.neo.event_bus;

import android.app.Application;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Neo on 2019/3/1.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
    }
}

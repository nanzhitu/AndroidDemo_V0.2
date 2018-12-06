package com.neo.webserver.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.support.annotation.Nullable;

import com.neo.webserver.server.WebServer;

/**
 * Created by Neo on 2018/12/5.
 */

public class WebService extends Service {
    private final static String TAG = "WebService";
    public static final int PORT = 8898;
    public static final String WEBROOT = "/";
    private WebServer mWebServer;


    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(NetworkStateReceiver, mFilter);

        mWebServer = new WebServer(PORT, WEBROOT, m_handler);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mWebServer != null) {
            mWebServer.close();
        }
        mWebServer = new WebServer(PORT, WEBROOT, m_handler);

        mWebServer.setDaemon(true);
        mWebServer.start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(NetworkStateReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver NetworkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String str = intent.getAction();

                if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
                }
            } catch (Exception e) {

            }
        }
    };

    private Handler m_handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            Bundle bundle = msg.getData();
            switch (what) {
                case 11111:
                    break;
                default:
                    break;
            }
        }
    };
}

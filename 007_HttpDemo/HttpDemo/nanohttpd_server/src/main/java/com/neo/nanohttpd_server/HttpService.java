package com.neo.nanohttpd_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.neo.nanohttpd_server.utils.LogUtils;

import java.io.IOException;

/**
 * Created by Neo on 2018/12/6.
 */

public class HttpService extends Service{

    private static final String TAG = "HttpService";
    private HttpServer mHttpServer;
    private boolean mServerStarted;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        mHttpServer = new HttpServer(8080);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startServer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopServer();
        super.onDestroy();
    }

    /**
     * Start server.
     */
    private void startServer() {
        if (mHttpServer != null  && !mServerStarted)
        {
            try {
                mHttpServer.start();
                mServerStarted = true;

            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            if(mHttpServer!=null)
            {
                LogUtils.d( TAG,String.format("Http服务已经启动了 mHttpServer=%s bServerStarted=%s  ....", mHttpServer.toString(), mServerStarted ? "true" : "false"));
            }
        }
        return;
    }


    /**
     * Stop server.
     */
    private void stopServer() {
        if (mHttpServer != null)
        {
            try {
                mHttpServer.stop();
                mServerStarted = false;
                LogUtils.d( TAG, "mHttpServer Stop().....");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return;
    }
}

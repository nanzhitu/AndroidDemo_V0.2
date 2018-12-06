package com.neo.webserver.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neo.webserver.service.WebService;
import com.neo.webserver.utils.LogUtils;

public class WebServiceBootReceiver extends BroadcastReceiver { 
    private final static String TAG="WebServiceBootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG,"WebService start");
    	try{
            Intent myIntent = new Intent(context, WebService.class);
            context.startService(myIntent);
    	}catch(Exception e){
    		
    	}

    } 

} 

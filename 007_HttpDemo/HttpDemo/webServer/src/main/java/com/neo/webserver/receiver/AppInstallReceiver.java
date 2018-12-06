package com.neo.webserver.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.neo.webserver.service.WebService;

public class AppInstallReceiver extends BroadcastReceiver {
	private final static String TAG="AppInstallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
    	try{
    	       PackageManager manager = context.getPackageManager();
    	        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
    	            String packageName = intent.getData().getSchemeSpecificPart();
    				Log.i (TAG, "packageName = "+packageName);
    	            //Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
    				startService (context, packageName);
    	        }
    	        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
    	            String packageName = intent.getData().getSchemeSpecificPart();
    	            //Toast.makeText(context, "卸载成功"+packageName, Toast.LENGTH_LONG).show();
    	        }
    	        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
    	            String packageName = intent.getData().getSchemeSpecificPart();
    	            //Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();
    				startService (context, packageName);
    	        }

    	}catch(Exception e){
    		
    	}
 

    }

	private void startService (Context context, String packageName)
	{
        if (TextUtils.isEmpty(packageName)){
        	return;
        }
		if (packageName.equalsIgnoreCase("com.neo.webserver"))
		{
			try{
				Intent myIntent = new Intent(context, WebService.class);
				context.startService(myIntent);
			}catch(Exception e){
				
			}
		}
	}

}

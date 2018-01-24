package com.example.neo.utils.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by neo on 2017/12/14.
 */

public class SystemUtil {



    /**
     * 获取所有运行的服务
     *
     * @return 服务名集合
     */
    public static Set getAllRunningService() {
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
        Set<String> names = new HashSet<>();
        if (info == null || info.size() == 0) return null;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            names.add(aInfo.service.getClassName());
        }
        return names;
    }

    /**
     * 判断服务是否运行
     *
     * @param className 完整包名的服务类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isServiceRunning(final String className) {
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

    private static String getMac() {
        String mac = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStream fis = p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line;
            //直到读完为止
            while ((line = br.readLine()) != null) {
                if(mac == null)
                    mac = line;
                else
                    mac += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mac;
    }
}

package com.example.myapplication.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/6/19.
 *
 */

public class LogUtil {

    //过滤登记  v<d<i<w<e

    public static int isOpenLog(){
       return 1;
    }

    public static void v(String tag,String info){
        if (isOpenLog() <= 1){
            Log.v(tag,info);
        }
    }
    public static void d(String tag,String info){
        if (isOpenLog() <= 2){
            Log.d(tag,info);
        }
    }
    public static void i(String tag,String info){
        if (isOpenLog() <= 3){
            Log.i(tag,info);
        }
    }
    public static void w(String tag,String info){
        if (isOpenLog() <= 4){
            Log.w(tag,info);
        }
    }
    public static void e(String tag,String info){
        if (isOpenLog() <= 5){
            Log.e(tag,info);
        }
    }
}

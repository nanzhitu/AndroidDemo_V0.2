package com.neo.xutils3demo.utils;

import android.util.Log;

/**
 * Created by Neo on 2017/6/19.
 *
 */

public class LogUtils {

    //过滤登记  v<d<i<w<e

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;

    private static String  sGlobalTag         = "ygj";  // log 标签

    private static int debugLevel = V;

    public static void setDebugLevel(int level){
        debugLevel = level;
    }

    public static int getDebugLevel(){
       return debugLevel;
    }

    public static void v(String tag,String info){
        if (debugLevel <= V){
            Log.v(tag,"["+sGlobalTag+"]"+info);
        }
    }
    public static void d(String tag,String info){
        if (debugLevel <= D){
            Log.d(tag,"["+sGlobalTag+"]"+info);
        }
    }
    public static void i(String tag,String info){
        if (debugLevel <= I){
            Log.i(tag,"["+sGlobalTag+"]"+info);
        }
    }
    public static void w(String tag,String info){
        if (debugLevel <= W){
            Log.w(tag,"["+sGlobalTag+"]"+info);
        }
    }
    public static void e(String tag,String info){
        if (debugLevel <= E){
            Log.e(tag,"["+sGlobalTag+"]"+info);
        }
    }
}

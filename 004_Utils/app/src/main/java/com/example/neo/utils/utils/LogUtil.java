package com.example.neo.utils.utils;

import android.util.Config;
import android.util.Log;

import com.example.neo.utils.BaseApplication;
import com.example.neo.utils.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by Administrator on 2017/6/19.
 *
 */

public class LogUtil {

    private static final int V = Log.VERBOSE;
    private static final int D = Log.DEBUG;
    private static final int I = Log.INFO;
    private static final int W = Log.WARN;
    private static final int E = Log.ERROR;
    private static final int A = Log.ASSERT;

    private static final int FILE = 0x10;
    private static final int JSON = 0x20;
    private static final int XML  = 0x30;

    private static String          sDefaultDir;// log 默认存储目录
    private static String          sDir;       // log 存储目录
    private static String  sFilePrefix        = "util";// log 文件前缀

    private static boolean sLogSwitch         = true;  // log 总开关，默认开
    private static boolean sLog2ConsoleSwitch = true;  // logcat 是否打印，默认打印
    private static String  sGlobalTag         = null;  // log 标签

    private static boolean sLogHeadSwitch     = true;  // log 头部开关，默认开
    private static boolean sLog2FileSwitch    = false; // log 写入文件开关，默认关

    private static int     sConsoleFilter     = V;     // log 控制台过滤器
    private static int     sFileFilter        = V;     // log 文件过滤器

    private static final String FILE_SEP       = System.getProperty("file.separator");
    private static final String LINE_SEP       = System.getProperty("line.separator");
    private static final int    MAX_LEN        = 4000;
    private static final Format FORMAT         = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault());

    private LogUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    //过滤登记  v<d<i<w<e

    public static void v(final Object... contents) {
        log(V, sGlobalTag, contents);
    }

    public static void vTag(final String tag, final Object... contents) {
        log(V, tag, contents);
    }

    public static void d(final Object... contents) {
        log(D, sGlobalTag, contents);
    }

    public static void dTag(final String tag, final Object... contents) {
        log(D, tag, contents);
    }

    public static void i(final Object... contents) {
        log(I, sGlobalTag, contents);
    }

    public static void iTag(final String tag, final Object... contents) {
        log(I, tag, contents);
    }

    public static void w(final Object... contents) {
        log(W, sGlobalTag, contents);
    }

    public static void wTag(final String tag, final Object... contents) {
        log(W, tag, contents);
    }

    public static void e(final Object... contents) {
        log(E, sGlobalTag, contents);
    }

    public static void eTag(final String tag, final Object... contents) {
        log(E, tag, contents);
    }

    public static void a(final Object... contents) {
        log(A, sGlobalTag, contents);
    }

    public static void aTag(final String tag, final Object... contents) {
        log(A, tag, contents);
    }

    private static void log(final int type, final String tag, final Object... contents) {
        if (!sLogSwitch || (!sLog2ConsoleSwitch && !sLog2FileSwitch)) return;
        int type_low = type & 0x0f;
        if (type_low < sConsoleFilter && type_low < sFileFilter) return;
        Log.println(type, tag, contents.toString());

    }

    public static int isOpenLog(){
       return SPUtils.getIntFromPreferencesManager(BaseApplication.getContext().getString(R.string.enable_log_key),1);
    }
}

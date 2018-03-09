package com.example.administrator.jni_demo;

/**
 * Created by Administrator on 2017/10/31.
 */

public class Hijni {

    static {
        System.loadLibrary("hiJni");//加载so文件，不要带上前缀lib和后缀.so
    }
    public native String hiJni();//定义本地方法接口，这个方法类似虚方法，实现是用c或者c++实现的
}

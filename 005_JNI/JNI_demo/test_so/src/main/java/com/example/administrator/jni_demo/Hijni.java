package com.example.administrator.jni_demo;
public class Hijni
{
   static {
      System.loadLibrary("hiJni");//加载so文件，不要带上前缀lib和后缀.so
   }
   public native String hiJni(); 
}
package com.example.neo.utils.utils;


import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/9/5.
 * Gson 工具类
 */

public class GsonUtils {

    private static GsonUtils instance;
    private Gson gson;

    private GsonUtils(){
        gson = new Gson();
    }

    public static GsonUtils getInstance(){
        if (instance==null){
            synchronized (GsonUtils.class){
                if (instance == null){
                    instance = new GsonUtils();
                }
            }
        }
        return instance;
    }

    public Gson getGson() {
        return gson;
    }
}

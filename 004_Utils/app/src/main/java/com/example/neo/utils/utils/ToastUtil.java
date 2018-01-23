package com.example.neo.utils.utils;

import android.widget.Toast;

import com.example.neo.utils.BaseApplication;


/**
 * Created by wing on 17/6/13.
 *
 */

public class ToastUtil {

    public static void showShort(String message){
        BaseApplication.getHandler().post(() -> Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_SHORT).show());
    }

    public static void longShort(String message){
        BaseApplication.getHandler().post(() -> Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_LONG).show());
    }

}

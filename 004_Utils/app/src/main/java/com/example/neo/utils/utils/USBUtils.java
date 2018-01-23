package com.example.neo.utils.utils;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by Administrator on 2017/11/20.
 */

public class USBUtils {

    public static final String TAG = USBUtils.class.getName();
    public static final int USB_SUCCESS = 0;
    public static final int USB_FAIL = 1;
    public static final int USB_ERROR = -1;
    public static int ret = USB_ERROR;

    private static Iterator<UsbDevice> GetDeviceList(Context context){
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        LogUtil.i(TAG,"GetDeviceList-->deviceList: "+deviceList);
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        return deviceIterator;
    }

}


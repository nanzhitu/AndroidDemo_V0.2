package com.example.myapplication.gesture;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import com.example.myapplication.gesture.singleThreadOper.ThinkJoyGestureEngineer;
import com.example.myapplication.utils.LogUtil;

/**
 * Created by neo on 2017/11/29.
 */

public class MainGesture {

    private static final String TAG = MainGesture.class.getSimpleName();

    private static MainGesture mainGesture;

    public static MainGesture getInstance(){
        if(mainGesture == null){
            synchronized (MainGesture.class){
                if(mainGesture == null){
                    mainGesture = new MainGesture();
                }
            }
        }
        return mainGesture;
    }

    public void init(Context context, Camera.PreviewCallback callback,ThinkJoyGestureEngineer.OnGestureCheckedListener onGestureCheckedListener){
        if(checkCameraHardware(context)) {
            int num;
            int device = 0;
            num = Camera.getNumberOfCameras();
            LogUtil.i(TAG,"camera device num  is "+num);
            if (num>1) {
                device = 1;
            }
            int finalDevice = device;
            CameraService.getInstance().init(context, finalDevice,callback);
            LogUtil.i(TAG,"CameraService init");
            ThinkJoyGestureEngineer.getInstance().init(context, onGestureCheckedListener);
            LogUtil.i(TAG,"ThinkJoyGestureEngineer init");
        }
        else{
            LogUtil.i(TAG,"no camera on this device");
        }
    }

    public void unInit(Context context){
        CameraService.getInstance().close();
        CameraService.getInstance().uninstallView(context);
        ThinkJoyGestureEngineer.getInstance().unInit(context);
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}

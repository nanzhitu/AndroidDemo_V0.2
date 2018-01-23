package com.example.neo.utils.utils;

import android.hardware.Camera;
import android.util.Log;

public class CameraUtil {

    /*
     <uses-permission android:name = "android.permission.CAMERA" />
     <uses-feature android:name = "android.hardware.camera" />
     <uses-feature android:name = "android.hardware.camera.autofocus" />
     */

	private static final String TAG = CameraUtil.class.getSimpleName();

    private static final int INVALID_CAMERA_ID = -1;
   /**
     * 获取前置摄像头id
     * @return
     */
    public static int findFrontFacingCamera() {
        int cameraId = INVALID_CAMERA_ID;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 获取后置摄像头id
     * @return
     */
    public static int findBackFacingCamera() {
        int cameraId = INVALID_CAMERA_ID;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private static boolean isCameraIdValid(int cameraId) {
        return cameraId != INVALID_CAMERA_ID;
    }
}
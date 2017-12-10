package com.example.myapplication.gesture.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.gesture.model.GestureDetectResult;

/**
 * Created by Administrator on 2017/11/28.
 * Camera 用surface view
 * 如果开启预览,则会显示摄像头预览,否则,只显示一个像素
 */

public class CameraWindow {

    private static CameraWindow instance;
    private View mRoot;
    private SurfaceView mCameraView;
    private FaceOverlayView mFaceOverlayView;
    private WindowManager mWindowManager;

    private CameraWindow(){}

    public static CameraWindow getInstance(){
        if (instance==null){
            synchronized (CameraWindow.class){
                if (instance == null){
                    instance = new CameraWindow();
                }
            }
        }
        return instance;
    }

    public void showWindow(Context context,boolean isShowPreview){
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        if(isShowPreview) {
            layoutParams.width = dip2px(context, 240);
            layoutParams.height = dip2px(context, 200);
            mRoot = LayoutInflater.from(context).inflate(R.layout.activity_test_surface, null);
            mCameraView = (SurfaceView) mRoot.findViewById(R.id.surface_view);
            mFaceOverlayView = (FaceOverlayView)mRoot.findViewById(R.id.view_faceoverlay);
            mWindowManager.addView(mRoot, layoutParams);
        }else {
            layoutParams.height = 1;
            layoutParams.width = 1;
            mRoot = mCameraView = new SurfaceView(context.getApplicationContext());
        }
        if (mRoot.getParent()!=null){
            mWindowManager.removeViewImmediate(mRoot);
        }
        mWindowManager.addView(mRoot, layoutParams);
    }

    /**
     * 更新指示框
     */
    public void updateOverlay(GestureDetectResult gestureResult, long processTime){
        if (mFaceOverlayView!=null){
            mFaceOverlayView.setGestureResult(gestureResult, processTime);
            mFaceOverlayView.invalidate();
        }
    }

    public void hideWindow(){
        if (mRoot!=null && mRoot.getParent()!=null){
            mWindowManager.removeViewImmediate(mRoot);
        }
    }

    private  int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public SurfaceView getView() {
        return mCameraView;
    }

}

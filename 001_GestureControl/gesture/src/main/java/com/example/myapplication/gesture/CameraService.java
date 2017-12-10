package com.example.myapplication.gesture;


import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.gesture.model.GestureDetectResult;
import com.example.myapplication.gesture.singleThreadOper.ThinkJoyGestureEngineer;
import com.example.myapplication.gesture.view.FaceOverlayView;
import com.example.myapplication.utils.Constants;
import com.thinkjoy.zhthinkjoygesturedetectlib.GestureInfo;

import java.util.List;


/**
 * Created by neo on 2017/11/29.
 */
public class CameraService implements SurfaceHolder.Callback{
    private static CameraService cameraService;
    private boolean isStart;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private Camera.PreviewCallback callback;
    private static final String TAG = CameraService.class.getSimpleName();

    private SurfaceView hideSurfaceView;
    private FaceOverlayView faceOverlayView;
    private View rootView;
    private WindowManager windowManager;
    private boolean isShowCameraWindow;
    private int mdevice;
    private Handler handler;
    private CameraService(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<GestureInfo> gestureInfoList = (List<GestureInfo>)msg.obj;

                GestureDetectResult gestureDetectResult = new GestureDetectResult();
                if (gestureInfoList.size() > 0) {
                    gestureDetectResult.shape = gestureInfoList.get(0).type;
                    gestureDetectResult.left = gestureInfoList.get(0).gestureRectangle[0].x;
                    gestureDetectResult.top = gestureInfoList.get(0).gestureRectangle[0].y;
                    gestureDetectResult.right = gestureInfoList.get(0).gestureRectangle[1].x;
                    gestureDetectResult.bottom = gestureInfoList.get(0).gestureRectangle[1].y;
                    faceOverlayView.setGestureResult(gestureDetectResult, 0);
                    faceOverlayView.postInvalidate();
                }
                faceOverlayView.setGestureResult(gestureDetectResult, 0);
                faceOverlayView.postInvalidate();
            }
        };
    }

    public void init(Context context,int device, Camera.PreviewCallback callback){
        boolean isEnableShow = Constants.isEnableCameraPreview();
        //不是第一次显示窗口且显示状态一致
        if(isShowCameraWindow == isEnableShow && hideSurfaceView != null){
            return;
        }
        installView(context, Constants.isEnableCameraPreview());
        surfaceHolder = hideSurfaceView.getHolder();
        this.callback = callback;
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mdevice = device;
    }


//    public void init(Context context, Camera.PreviewCallback callback, boolean isShow){
//        installView(context, isShow);
//        surfaceHolder = hideSurfaceView.getHolder();
//        this.callback = callback;
//        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//    }

    private void installView(Context context, boolean isShow) {
        if(isShowCameraWindow){
            uninstallView(context, rootView);
            rootView = null;
            hideSurfaceView = null;
            faceOverlayView = null;
        }else{
            uninstallView(context, hideSurfaceView);
            hideSurfaceView = null;
        }

        close();

        isShowCameraWindow = isShow;

        if(windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        if(isShow) {
            layoutParams.width = dip2px(context, 240);
            layoutParams.height = dip2px(context, 200);
            rootView = LayoutInflater.from(context).inflate(R.layout.activity_test_surface, null);
            hideSurfaceView = (SurfaceView) rootView.findViewById(R.id.surface_view);
            faceOverlayView = (FaceOverlayView)rootView.findViewById(R.id.view_faceoverlay);
            windowManager.addView(rootView, layoutParams);
            ThinkJoyGestureEngineer.getInstance().setHandler(handler);

        }else {
            layoutParams.height = 1;
            layoutParams.width = 1;
            hideSurfaceView = new SurfaceView(context.getApplicationContext());
            windowManager.addView(hideSurfaceView, layoutParams);
        }
    }

    public void uninstallView(Context context){
        if(isShowCameraWindow){
            uninstallView(context, rootView);
        }else {
            uninstallView(context, hideSurfaceView);
        }
    }

    public void uninstallView(Context context, View view){
        if(view == null){
            return;
        }
        if(windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (view.getParent()!=null){
            windowManager.removeViewImmediate(view);
        }
        ThinkJoyGestureEngineer.getInstance().setHandler(null);
    }

    private  int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    private void open(){
        if(camera == null) {
            try {
                camera = Camera.open(mdevice);
                if(camera == null) {
                    return;
                }
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = calBestPreviewSize(parameters, ThinkJoyGestureEngineer.IMAGE_WIDTH,
                        ThinkJoyGestureEngineer.IMAGE_HEIGHT);
                parameters.setPictureFormat(ImageFormat.NV21);
                parameters.setPictureSize(size.width, size.height);
                parameters.setPreviewSize(size.width, size.height);
                camera.setPreviewDisplay(surfaceHolder);
                camera.setPreviewCallback(callback);

                ThinkJoyGestureEngineer.IMAGE_WIDTH = size.width;
                ThinkJoyGestureEngineer.IMAGE_HEIGHT = size.height;
            } catch (Exception e) {
                e.printStackTrace();
//                close();
            }
        }
    }


    public void close(){
        if(camera != null){
            isStart = false;
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
            cameraService = null;
        }
    }

    public static CameraService getInstance(){
        if(cameraService == null){
            synchronized (CameraService.class){
                if(cameraService == null){
                    cameraService = new CameraService();
                }
            }
        }
        return cameraService;
    }


    /**
     * 通过传入的宽高算出最接近于宽高值的相机大小
     */

    public Camera.Size calBestPreviewSize(Camera.Parameters camPara, int w, int h) {
        List<Camera.Size> sizes = camPara.getSupportedPreviewSizes();
        if (sizes == null)
            return null;

        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;


        Camera.Size optimalSize = null;

        // Start with max value and refine as we iterate over available preview sizes. This is the
        // minimum difference between view and camera height.
        double minDiff = Double.MAX_VALUE;

        // Target view height
        int targetHeight = h;

        // Try to find a preview size that matches aspect ratio and the target view size.
        // Iterate over all available sizes and pick the largest size that can fit in the view and
        // still maintain the aspect ratio.
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find preview size that matches the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public Camera getCamera(){
        return camera;
    }

    private void startPreview(){
        if(camera == null){
            Log.e(TAG, "startPreview: camera == null");
            return;
        }
        camera.startPreview();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(!isStart) {
            isStart = true;
            startPreview();
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

}

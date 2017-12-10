package com.example.myapplication.gesture.singleThreadOper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

import com.example.myapplication.gesture.GestureConstants;
import com.example.myapplication.gesture.model.GestureDetectResult;
import com.thinkjoy.zhthinkjoygesturedetectlib.GestureInfo;
import com.thinkjoy.zhthinkjoygesturedetectlib.ZHThinkjoyGesture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neo on 2017/11/29.
 * 习悦的手势识别引擎(单线程版)   http://www.zhthinkjoy.com/
 */

public class ThinkJoyGestureEngineer implements Camera.PreviewCallback{
    private static ThinkJoyGestureEngineer thinkJoyGestureEngineer;
    private Context context;
    private DealGestureThread dealGestureThread;

    private static final String TAG = ThinkJoyGestureEngineer.class.getSimpleName();

    public static int IMAGE_WIDTH = 640;
    public static  int IMAGE_HEIGHT = 480;

    private OnGestureCheckedListener onGestureCheckedListener;
    BitmapFactory.Options gestureImgOptions;

    private  List<GestureInfo> gestureInfoList;

    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType, rgbaType;
    private Allocation in, out;
    private GestureProcess mGestureProcess;


    private Handler handler;
    private GestureOperContext gestureOperContext;
    private ThinkJoyGestureEngineer(){
        gestureInfoList = new ArrayList<>();
        gestureOperContext = new GestureOperContext();
        mGestureProcess = new GestureProcess();
    }

    public static ThinkJoyGestureEngineer getInstance(){
        if(thinkJoyGestureEngineer == null){
            synchronized (ThinkJoyGestureEngineer.class){
                thinkJoyGestureEngineer = new ThinkJoyGestureEngineer();
            }
        }
        return thinkJoyGestureEngineer;
    }

    public void init(Context context, OnGestureCheckedListener onGestureCheckedListener){
        this.context = context;
        ZHThinkjoyGesture.getInstance(context).init();
        this.onGestureCheckedListener = onGestureCheckedListener;
        gestureOperContext = new GestureOperContext();
    }

    public  void unInit(Context context) {
        ZHThinkjoyGesture.getInstance(context).release();
        gestureOperContext = null;
        thinkJoyGestureEngineer = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(isStartDealGestureThread()) {
            //开启检测手势线程
            dealGestureThread = new DealGestureThread(camera.getParameters().getPreviewSize(), data);
            dealGestureThread.start();
        }
    }

    /**
     * 检测手势
     */
    private synchronized void checkGesture( GestureInfo gestureInfo ) {
        //检测到V型手势
        if (gestureInfo.type == GestureConstants.V_GESTURE) {
            gestureOperContext.checkFirstVGestureUsable();
            if (gestureOperContext.getVGestureCount() >= 1) {
                //某段时间捕获手势的次数超过1(不算本次)，确认真的识别到了手势，没有意外触发
                if (onGestureCheckedListener != null) {
                    onGestureCheckedListener.onGestureChecked(gestureInfo.type);
                    gestureOperContext.resetVGesture();
                }
            } else {
                gestureOperContext.addVGesture();
            }
        }
        mGestureProcess.processGesture(new GestureDetectResult(gestureInfo));
    }

    /**
     * 处理图片并检测图片是否有V型手势的线程
     *
     */
    class DealGestureThread extends Thread {
        private Camera.Size previewSize;
        private byte[] data;
        public DealGestureThread(Camera.Size previewSize, byte[] data){
            this.previewSize = previewSize;
            this.data = data;
        }
        @Override
        public void run() {
            dealWithGesture(previewSize, data);
        }
    }


    /**
     * nv21格式数据转为bmp(此方法相对耗时70-100ms)
     * @return
     */
    private Bitmap nv21ToBmp(Camera.Size previewSize, byte[] data){
        if(gestureImgOptions == null){
            gestureImgOptions = new BitmapFactory.Options();
            gestureImgOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, gestureImgOptions);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * nv21格式数据转为bmp，速度较快（7-10ms）注意,此方法只能用于API >=17
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap nv21ToBmpByRenderScript(Camera.Size previewSize, byte[] data){
        if(rs == null){
            rs = RenderScript.create(context);
            yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        }

        if (yuvType == null)
        {
            yuvType = new Type.Builder(rs, Element.U8(rs))
                    .setX(data.length);
            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs))
                    .setX(previewSize.width)
                    .setY(previewSize.height);
            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        }

        in.copyFrom(data);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);

        Bitmap bmpout = Bitmap.createBitmap(previewSize.width,
                previewSize.height, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);
        return bmpout;
    }

    private synchronized void dealWithGesture(Camera.Size previewSize, byte[] data){

        if(gestureInfoList != null && gestureInfoList.size() > 0){
            gestureInfoList.clear();
        }
        //距离越远，识别手势需要的时间越长,识别越不准确
        ZHThinkjoyGesture.getInstance(context).gestureDetect(data,
                ZHThinkjoyGesture.IMAGE_FORMAT_NV21,
                previewSize.width, previewSize.height,
                gestureInfoList);

        if(handler != null){
            Message msg = Message.obtain();
            msg.obj = gestureInfoList;
            handler.sendMessage(msg);
        }
        //当前的图片中是否有手势(目前只有一个手势，所以为了提高识别率，
        // 只要检测到V型/单指手势，就默认为V型手势)
        if (null != gestureInfoList && gestureInfoList.size() > 0) {
            checkGesture(gestureInfoList.get(0));
        }
        gestureOperContext.setThreadEnd();
    }

    /**
     * 是否启动处理手势线程
     * 启动处理手势线程的2个条件：
     * 1.当前没有线程处理手势
     * 2.手势结束且2次手势间隔不能超过DEAL_GESTURE_INTERVAL
     * @return
     */
    private synchronized boolean isStartDealGestureThread(){
        //打开程序第一次处理手势
        if(!gestureOperContext.isThreadStart()){
            return false;
        }
        gestureOperContext.setThreadStart();
        return true;
    }


    public interface OnGestureCheckedListener{
        void onGestureChecked(int gestureType);
    }


    public void setHandler(Handler handler){
        this.handler = handler;
    }

}

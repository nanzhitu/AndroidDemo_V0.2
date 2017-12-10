package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;

import com.example.myapplication.gesture.utils.GestureConstants;
import com.example.myapplication.gesture.MainGesture;
import com.example.myapplication.gesture.singleThreadOper.ThinkJoyGestureEngineer;
import com.example.myapplication.inputmethod.MyInputMethodService;
import com.example.myapplication.utils.LogUtil;

import static com.example.myapplication.utils.Constants.TAG0;

public class GestureService extends Service {
    public static Boolean mInited = false;
    private static int num = 0;
    public GestureService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG0, "GestureService onCreate");
        if(mInited )  {
            LogUtil.i(TAG0, "CameraService close in onCreate");
            MainGesture.getInstance().unInit(this);
        }

        MainGesture.getInstance().init(this, ThinkJoyGestureEngineer.getInstance(), gestureType -> {
            if(gestureType == GestureConstants.V_GESTURE){
                num++;
                //检测到V型手势
                LogUtil.i(TAG0, "check VGesture success");
                Intent intent_test = new Intent(GestureService.this,MyService.class);
                startService(intent_test);
                int s = num %4;
                LogUtil.i(TAG0, "check num : "+s);
                if(MyInputMethodService.getInstance()!=null) {
                    switch (s) {
                        case 0:
                            MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                            break;
                        case 1:
                            MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                            break;
                        case 2:
                            MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case 3:
                            MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                        default:
                            break;
                    }
                }

            }
        });
    }
/*
    private void sendKeyCode2(final int keyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建一个Instrumentation对象
                    Instrumentation inst = new Instrumentation();
                    // 调用inst对象的按键模拟方法
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}

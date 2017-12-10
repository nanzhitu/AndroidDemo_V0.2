package com.example.myapplication.gesture.singleThreadOper;

/**
 * Created by neo on 2017/11/29.
 */

public class GestureOperContext {
    //与线程有关的参数
    private boolean isStartedThread;
    private long threadEndMS; //记录线程结束时间
    public static final long THREAD_RESTART_DELTA = 400;

    //与单指手势有关的参数
    private long firstIndexFingerMS; // 记录首次检测到单指手势的时间
    public static final long FIRST_INDEX_FINGER_DELTA = 1000 *4;


    //与V型手势有关的参数
    private int vGestureCount;
    private long firstVGestureDetectionMS;  //记录第一次检测到V型手势的时间
    public static final long V_GESTURE_DETECTION_DELTA = THREAD_RESTART_DELTA + 500;

    public int getVGestureCount(){
        return vGestureCount;
    }

    public void resetVGesture(){
        vGestureCount = 0;
        firstVGestureDetectionMS = 0;
    }

    public void addVGesture(){
        firstVGestureDetectionMS = System.currentTimeMillis();
        vGestureCount++;
    }

    /**
     * 检测上轮检测的第一次V型手势时间在不在有效时间内，不再的话重置并重新开始新的一轮检测
     */
    public void checkFirstVGestureUsable(){
        long nowMilliSeconds = System.currentTimeMillis();
        if(nowMilliSeconds - firstVGestureDetectionMS >= V_GESTURE_DETECTION_DELTA){
            resetVGesture();
        }
    }

    /**
     * 设置线程结束
     */
    public void setThreadEnd(){
        threadEndMS = System.currentTimeMillis();
        isStartedThread = false;
    }

    /**
     * 设置启动线程
     */
    public void setThreadStart(){
        isStartedThread = true;
    }

    /**
     * 是否开启新的线程
     * @return
     */
    public boolean isThreadStart(){
        //已经有已启动的线程，不在启动
        if(isStartedThread){
            return false;
        }

        //第一次启动线程
        if(threadEndMS == 0){
            return true;
        }
        //根据上一次程序启动结束和此时的时间间隔决定是否启动线程
        long nowMilliSeconds = System.currentTimeMillis();
        if(nowMilliSeconds - threadEndMS < THREAD_RESTART_DELTA){
            return false;
        }

        return true;
    }


    public void setFirstIndexFingerMS(){
        firstIndexFingerMS = System.currentTimeMillis();
    }

    public boolean checkFirstIndexFingerUsable(){
        long nowMilliSeconds = System.currentTimeMillis();
        if(nowMilliSeconds - firstIndexFingerMS >= FIRST_INDEX_FINGER_DELTA){
            firstVGestureDetectionMS = 0;
            return false;
        }
        return true;

    }

}

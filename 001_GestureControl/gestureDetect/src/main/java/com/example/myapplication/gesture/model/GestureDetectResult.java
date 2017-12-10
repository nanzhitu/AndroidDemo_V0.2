package com.example.myapplication.gesture.model;

import com.thinkjoy.zhthinkjoygesturedetectlib.GestureInfo;

/**
 * Created by neo on 2017/11/29.
 */

public class GestureDetectResult {
    public int shape;
    public float bottom;
    public float top;
    public float right;
    public float left;
    public GestureDetectResult() {
        shape = -1;
        bottom = 0;
        top = 0;
        right = 0;
        left = 0;
    }

    public GestureDetectResult(GestureInfo gestureInfo){
        shape = gestureInfo.type;
        left = gestureInfo.gestureRectangle[0].x;
        top = gestureInfo.gestureRectangle[0].y;
        right = gestureInfo.gestureRectangle[1].x;
        bottom = gestureInfo.gestureRectangle[1].y;
    }
}

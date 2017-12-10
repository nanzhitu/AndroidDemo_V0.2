package com.example.myapplication.gesture.singleThreadOper;

import android.app.Instrumentation;
import android.util.Log;
import android.view.KeyEvent;

import com.example.myapplication.inputmethod.MyInputMethodService;
import com.thinkjoy.zhthinkjoygesturedetectlib.GestureInfo;

import com.example.myapplication.gesture.model.GestureDetectResult;

/**
 * Created by neo on 2017/11/29.
 */

public class GestureProcess {
    private GestureDetectResult mLastGesture;
    private float last_x_move;
    private float last_y_move;
    private static final float PALM_RIGHT_MOVE_PERCENT = 2.5f;
    private static final float PALM_LEFT_MOVE_PERCENT = -2.5f;
    private static final float PALM_UP_MOVE_PERCENT = 3.0f;
    private static final float PALM_DOWN_MOVE_PERCENT = -3.0f;

    private static final float INDEX_RIGHT_MOVE_PERCENT = 1.0f;
    private static final float INDEX_LEFT_MOVE_PERCENT = -1.0f;
    private static final float INDEX_UP_MOVE_PERCENT = 1.5f;
    private static final float INDEX_DOWN_MOVE_PERCENT = -1.5f;
    private String TAG = this.getClass().getName();

    private boolean mIsEnterClicked;
    private int mFistCount;
    private int mOKCount;
    private Instrumentation mInstrumentation;
    public GestureProcess(){
        mInstrumentation = new Instrumentation();
    }
    public void processGesture(GestureDetectResult gestureDetectInfo) {
        //处理点击事件
        if (mLastGesture == null) {
            mLastGesture = gestureDetectInfo;
        } else {
            float x_move = (mLastGesture.left + mLastGesture.right) / 2 - (gestureDetectInfo.left + gestureDetectInfo.right) / 2;
            float y_move = (mLastGesture.bottom + mLastGesture.top) / 2 - (gestureDetectInfo.bottom + gestureDetectInfo.top) / 2;
            float hand_width = ((mLastGesture.right - mLastGesture.left) + (gestureDetectInfo.right - gestureDetectInfo.left)) / 2;
            float hand_height = ((mLastGesture.bottom - mLastGesture.top) + (gestureDetectInfo.bottom - mLastGesture.top)) / 2;

            if (gestureDetectInfo.shape != GestureInfo.OK_GESTURE) {
                mOKCount = 0;
            }
            if (mLastGesture.shape == GestureInfo.PALM && gestureDetectInfo.shape == GestureInfo.PALM) {
                //手掌滑动事件， 用于模拟上下左右来操作
                if (x_move < hand_width / PALM_RIGHT_MOVE_PERCENT && x_move > hand_width / PALM_LEFT_MOVE_PERCENT &&
                        y_move < hand_height / PALM_UP_MOVE_PERCENT && y_move > hand_height / PALM_DOWN_MOVE_PERCENT) {
                    x_move = x_move + last_x_move;
                    y_move = y_move + last_y_move;
                }
//                Log.i(TAG, "x_move:" + Float.toString(x_move) + " y_move:" + Float.toString(y_move) +
//                            " hand_width:" + Float.toString(hand_width) + " hand_height:" + Float.toString(hand_height));
                if (y_move > hand_height / PALM_UP_MOVE_PERCENT ||
                        y_move < hand_height / PALM_DOWN_MOVE_PERCENT ||
                        x_move > hand_width / PALM_RIGHT_MOVE_PERCENT ||
                        x_move < hand_width / PALM_LEFT_MOVE_PERCENT) {
                    if (y_move > hand_height / PALM_UP_MOVE_PERCENT) {
                        //产生方向键上事件
                        Log.i(TAG, "send up event");
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                        //mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
                    } else if (y_move < hand_height / PALM_DOWN_MOVE_PERCENT) {
                        //产生方向键下事件
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                        //mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
                        Log.i(TAG, "send down event");
                    } else if (x_move > hand_width / PALM_RIGHT_MOVE_PERCENT) {
                        //产生方向键右事件
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                        //mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
                        Log.i(TAG, "send right event");
                    } else if (x_move < hand_width / PALM_LEFT_MOVE_PERCENT) {
                        //产生方向键左事件
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
                        //mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_LEFT);
                        Log.i(TAG, "send left event");
                    }
                    Log.i(TAG, "x_move:" + Float.toString(x_move) + " y_move:" + Float.toString(y_move) +
                            " hand_width:" + Float.toString(hand_width) + " hand_height:" + Float.toString(hand_height));
                    last_x_move = 0;
                    last_y_move = 0;
                } else {
                    last_x_move = x_move;
                    last_y_move = y_move;
                }

            } else if (mLastGesture.shape == GestureInfo.INDEX_FINGER && gestureDetectInfo.shape == GestureInfo.INDEX_FINGER) {
                //单指滑动事件,

                if (x_move < hand_width / INDEX_RIGHT_MOVE_PERCENT && x_move > hand_width / INDEX_LEFT_MOVE_PERCENT &&
                        y_move < hand_height / INDEX_UP_MOVE_PERCENT && y_move > hand_height / INDEX_DOWN_MOVE_PERCENT) {
                    x_move = x_move + last_x_move;
                    y_move = y_move + last_y_move;
                }
//                Log.i(TAG, "x_move:" + Float.toString(x_move) + " y_move:" + Float.toString(y_move) +
//                        " hand_width:" + Float.toString(hand_width) + " hand_height:" + Float.toString(hand_height));
                if (y_move > hand_height / INDEX_UP_MOVE_PERCENT ||
                        y_move < hand_height / INDEX_DOWN_MOVE_PERCENT ||
                        x_move > hand_width / INDEX_RIGHT_MOVE_PERCENT ||
                        x_move < hand_width / INDEX_LEFT_MOVE_PERCENT) {

//                    if (y_move > 0) {
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 200, 0));
//                    } else if (y_move < 0) {
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 200, 0));
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
//                    }else if (x_move > 0) {
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 200, 0));
//                    } else if (x_move < 0) {
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 200, 0));
//                        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
//                    }
                    Log.i(TAG, "send touch event");
                    if (y_move > hand_height / INDEX_UP_MOVE_PERCENT) {
                        //产生方向键上事件
                        Log.i(TAG, "send up event");
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                    } else if (y_move < hand_height / INDEX_DOWN_MOVE_PERCENT) {
                        //产生方向键下事件
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                        Log.i(TAG, "send down event");
                    } else if (x_move > hand_width / INDEX_RIGHT_MOVE_PERCENT) {
                        //产生方向键右事件
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                        Log.i(TAG, "send right event");
                    } else if (x_move < hand_width / INDEX_LEFT_MOVE_PERCENT) {
                        //产生方向键左事件
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
                        Log.i(TAG, "send left event");
                    }
                    Log.i(TAG, "x_move:" + Float.toString(x_move) + " y_move:" + Float.toString(y_move) +
                            " hand_width:" + Float.toString(hand_width) + " hand_height:" + Float.toString(hand_height));
                    last_x_move = 0;
                    last_y_move = 0;
                } else {
                    last_x_move = x_move;
                    last_y_move = y_move;
                }
            } else if (mLastGesture.shape == GestureInfo.PALM && gestureDetectInfo.shape == GestureInfo.FIST) {
                //点击事件
                mIsEnterClicked = true;
//                mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
            } else if (mIsEnterClicked == true && mLastGesture.shape == GestureInfo.FIST && gestureDetectInfo.shape == GestureInfo.FIST) {
                mFistCount++;
                if (mFistCount == 3) {
                    mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
                    mFistCount = 0;
                    mIsEnterClicked = false;
                }
            } else {
                mFistCount = 0;
                mIsEnterClicked = false;
                if (mLastGesture.shape == GestureInfo.OK_GESTURE && gestureDetectInfo.shape == GestureInfo.OK_GESTURE) {
                    mOKCount++;
                    if (mOKCount == 3) {
                        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
                        mOKCount = 0;
                    }
                } else {
                    mOKCount = 0;
                }
            }
            mLastGesture = gestureDetectInfo;
        }
    }
}

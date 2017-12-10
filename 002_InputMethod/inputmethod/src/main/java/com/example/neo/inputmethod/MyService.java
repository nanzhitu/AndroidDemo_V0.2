package com.example.neo.inputmethod;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import static com.example.neo.inputmethod.MainActivity.TAG;
/**
 * Created by neo on 2017/12/4.
 */
public class MyService extends InputMethodService {

    private static MyService myService;

    public static MyService getInstance(){
        if(myService != null){
            return myService;
        }
        else
            return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "MyService onCreate");
        myService = this;
    }

    @Override
    public View onCreateInputView() {
        Log.i(TAG, "onCreateInputView");
        View mkeyView = LayoutInflater.from(this).inflate(
                R.layout.layout_keyboardview, null);
        new KeyBoardUtil(this, (KeyboardView) mkeyView.findViewById(R.id.keyboardView));
        return mkeyView;
    }

    @Override
    public View onCreateCandidatesView() {
        Log.i(TAG, "onCreateCandidatesView");
        return null;
    }

    public void commitText(String data) {
        Log.i(TAG, "commitText");
        getCurrentInputConnection().commitText(data, 0); // 往输入框输出内容
        setCandidatesViewShown(false); // 隐藏 CandidatesView
    }

    public void deleteText(){
        Log.i(TAG, "deleteText");
        sendDownUpKeyEvents(21);
        //sendKeyCode2(KeyEvent.KEYCODE_DPAD_LEFT);
        getCurrentInputConnection().deleteSurroundingText(1, 0);
    }

    public void hideInputMethod() {
        Log.i(TAG, "hideInputMethod");
        hideWindow();
    }

    @Override
    public void sendKeyChar(char charCode) {
        Log.i(TAG, "charCode: "+charCode);
        super.sendKeyChar(charCode);
    }

    @Override
    public void sendDownUpKeyEvents(int keyEventCode) {
        Log.i(TAG, "sendDownUpKeyEvents: "+keyEventCode);
        super.sendDownUpKeyEvents(keyEventCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

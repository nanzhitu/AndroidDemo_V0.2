package com.example.myapplication.inputmethod;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.myapplication.R;

public class MyInputMethodService extends InputMethodService {

    private static MyInputMethodService myService;

    public static MyInputMethodService getInstance(){
        if(myService != null){
            return myService;
        }
        else
            return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("neo ==> ", "MyInputMethodService onCreate");
        myService = this;
    }

    @Override
    public View onCreateInputView() {
        Log.i("neo ==> ", "onCreateInputView");
        View mkeyView = LayoutInflater.from(this).inflate(
                R.layout.layout_keyboardview, null);
        new KeyBoardUtil(this, (KeyboardView) mkeyView.findViewById(R.id.keyboardView));
        return mkeyView;
    }

    @Override
    public View onCreateCandidatesView() {
        Log.i("neo ==> ", "onCreateCandidatesView");
        return null;
    }

    public void commitText(String data) {
        Log.i("neo ==> ", "commitText");
        getCurrentInputConnection().commitText(data, 0); // 往输入框输出内容
        setCandidatesViewShown(false); // 隐藏 CandidatesView
    }

    public void deleteText(){
        Log.i("neo ==> ", "deleteText");
        sendDownUpKeyEvents(21);
        //sendKeyCode2(KeyEvent.KEYCODE_DPAD_LEFT);
        getCurrentInputConnection().deleteSurroundingText(1, 0);
    }

    public void hideInputMethod() {
        Log.i("neo ==> ", "hideInputMethod");
        hideWindow();
    }

    @Override
    public void sendKeyChar(char charCode) {
        Log.i("neo ==> ", "charCode: "+charCode);
        super.sendKeyChar(charCode);
    }

    @Override
    public void sendDownUpKeyEvents(int keyEventCode) {
        Log.i("neo ==> ", "sendDownUpKeyEvents: "+keyEventCode);
        super.sendDownUpKeyEvents(keyEventCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

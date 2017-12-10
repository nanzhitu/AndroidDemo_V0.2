package com.example.myapplication.inputmethod;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;

import com.example.myapplication.R;

/**
 * Created by Administrator on 2017/12/4.
 */

public class KeyBoardUtil {

    private KeyboardView keyboardView;
    private MyInputMethodService myService;
    private Keyboard k1;// 字母键盘

    public KeyBoardUtil(MyInputMethodService myImeService1, KeyboardView keyboardView1) {
        super();
        keyboardView = keyboardView1;
        keyboardView.setOnKeyboardActionListener(listener);
        myService = myImeService1;
        k1 = new Keyboard(myService.getApplicationContext(), R.xml.qwerty);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);

    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void swipeUp() {
            Log.i("neo ==> ", "swipeUp");
        }

        @Override
        public void swipeRight() {
            Log.i("neo ==> ", "swipeRight");
        }

        @Override
        public void swipeLeft() {
            Log.i("neo ==> ", "swipeLeft");
        }

        @Override
        public void swipeDown() {
            Log.i("neo ==> ", "swipeDown");
        }

        @Override
        public void onText(CharSequence text) {
            Log.i("neo ==> ", "onText: "+text);
        }

        @Override
        public void onRelease(int primaryCode) {
            Log.i("neo ==> ", "onRelease: "+primaryCode);
        }

        @Override
        public void onPress(int primaryCode) {
            Log.i("neo ==> ", "onPress: "+primaryCode);
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    myService.deleteText();
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    myService.hideInputMethod();
                    break;
                default:
                    myService.commitText(Character.toString((char) primaryCode));
                    Log.i("neo ==>","primaryCode: "+ primaryCode);
                    break;
            }
        }
    };
}

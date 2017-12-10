package com.example.neo.inputmethod;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;

import static com.example.neo.inputmethod.MainActivity.TAG;

/**
 * Created by neo on 2017/12/4.
 */

public class KeyBoardUtil {

    private KeyboardView keyboardView;
    private MyService myService;
    private Keyboard k1;// 字母键盘

    public KeyBoardUtil(MyService myImeService1, KeyboardView keyboardView1) {
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
            Log.i(TAG, "swipeUp");
        }

        @Override
        public void swipeRight() {
            Log.i(TAG, "swipeRight");
        }

        @Override
        public void swipeLeft() {
            Log.i(TAG, "swipeLeft");
        }

        @Override
        public void swipeDown() {
            Log.i(TAG, "swipeDown");
        }

        @Override
        public void onText(CharSequence text) {
            Log.i(TAG, "onText: "+text);
        }

        @Override
        public void onRelease(int primaryCode) {
            Log.i(TAG, "onRelease: "+primaryCode);
        }

        @Override
        public void onPress(int primaryCode) {
            Log.i(TAG, "onPress: "+primaryCode);
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
                    Log.i(TAG,"primaryCode: "+ primaryCode);
                    break;
            }
        }
    };
}

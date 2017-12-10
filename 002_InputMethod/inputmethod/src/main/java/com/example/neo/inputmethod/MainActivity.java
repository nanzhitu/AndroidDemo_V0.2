package com.example.neo.inputmethod;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.List;
/**
 * Created by neo on 2017/12/4.
 */
public class MainActivity extends AppCompatActivity {

    Button send;
    Button up;
    Button down;
    Button left;
    Button right;
    Button mid;
    private static int num0 = 0;
    public static final String TAG = "neo ==>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button) findViewById(R.id.button);
        up = (Button) findViewById(R.id.button1);
        down = (Button) findViewById(R.id.button2);
        left = (Button) findViewById(R.id.button3);
        mid = (Button) findViewById(R.id.button4);
        right = (Button) findViewById(R.id.button5);
        send.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //拿到输入法的全部条目
            List<InputMethodInfo> list = imm.getInputMethodList();
            Log.i("neo ==> ", list.toString());
            imm.showInputMethodPicker();
            //Settings.Secure.putString(getContentResolver(),Settings.Secure.DEFAULT_INPUT_METHOD, "com.example.inputmethod/.MyService");
            //imm.setInputMethod(getCurrentFocus().getApplicationWindowToken(),"com.example.inputmethod/.MyService");
            //InputMethodService service = MyService.getInstance();
            //service.switchInputMethod("com.example.inputmethod/.MyService");
            //service.sendDownUpKeyEvents(20);
        });
        up.setOnClickListener(v -> {
            if(MyService.getInstance()!=null) {
                MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
            }
            else {
                Log.i(TAG,"need choose neo input method ");
            }
        });

        down.setOnClickListener(v -> {
            if(MyService.getInstance()!=null) {
                MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
            }
            else {
                Log.i(TAG,"need choose neo input method ");
            }
        });

        left.setOnClickListener(v -> {
            if(MyService.getInstance()!=null) {
                MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
            else {
                Log.i(TAG,"need choose neo input method ");
            }
        });

        right.setOnClickListener(v -> {
            if(MyService.getInstance()!=null) {
                MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
            }
            else {
                Log.i(TAG,"need choose neo input method ");
            }
        });
        mid.setOnClickListener(v -> {
            if(MyService.getInstance()!=null) {
                num0++;
                switch (num0) {
                    case 0:
                        break;
                    case 1:
                        MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                        break;
                    case 2:
                        MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                        break;
                    case 3:
                        MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                        break;
                    case 4:

                        MyService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);

                        break;
                    default:
                        num0 = 0;
                        break;
                }
            }
            else {
                Log.i(TAG,"need choose neo input method ");
            }
        });
    }
}

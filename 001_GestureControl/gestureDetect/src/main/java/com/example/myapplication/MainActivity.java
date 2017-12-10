package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.myapplication.inputmethod.MyInputMethodService;
import com.example.myapplication.service.GestureService;
import com.example.myapplication.utils.LogUtil;

import java.util.List;

import static com.example.myapplication.utils.Constants.TAG0;

public class MainActivity extends AppCompatActivity {

    Button send;
    Button up;
    Button down;
    Button left;
    Button right;
    Button mid;
    private static int num0 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = findViewById(R.id.button);
        up = findViewById(R.id.button1);
        down = findViewById(R.id.button2);
        left = findViewById(R.id.button3);
        mid = findViewById(R.id.button4);
        right = findViewById(R.id.button5);
        send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ServiceCast")
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //拿到输入法的全部条目
                List<InputMethodInfo> list = imm.getInputMethodList();
                Log.i("neo ==> ", list.toString());
                imm.showInputMethodPicker();
                //Settings.Secure.putString(getContentResolver(),Settings.Secure.DEFAULT_INPUT_METHOD, "com.example.inputmethod/.MyService");
                Intent start = new Intent(MainActivity.this,GestureService.class);
                startService(start);
                LogUtil.i(TAG0,"startService GestureService");
                //imm.setInputMethod(getCurrentFocus().getApplicationWindowToken(),"com.example.inputmethod/.MyService");
                //InputMethodService service = MyService.getInstance();
                //service.switchInputMethod("com.example.inputmethod/.MyService");
                //service.sendDownUpKeyEvents(20);
            }
        });
        up.setOnClickListener(v -> {
            if(MyInputMethodService.getInstance()!=null) {
                MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
            }
        });

        down.setOnClickListener(v -> {
            if(MyInputMethodService.getInstance()!=null) {
                MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
            }
        });

        left.setOnClickListener(v -> {
            if(MyInputMethodService.getInstance()!=null) {
                MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
        });

        right.setOnClickListener(v -> {
            if(MyInputMethodService.getInstance()!=null) {
                MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
            }
        });
        mid.setOnClickListener(v -> {
            if(MyInputMethodService.getInstance()!=null) {
                num0++;
                switch (num0) {
                    case 0:
                        break;
                    case 1:
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                        break;
                    case 2:
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                        break;
                    case 3:
                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                        break;
                    case 4:

                        MyInputMethodService.getInstance().sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);

                        break;
                    default:
                        num0 = 0;
                        break;
                }
            }
        });
    }
}

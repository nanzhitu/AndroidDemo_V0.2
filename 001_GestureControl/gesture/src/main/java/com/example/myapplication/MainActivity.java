package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.myapplication.utils.LogUtil;

import static com.example.myapplication.utils.Constants.TAG0;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button3 = findViewById(R.id.button3);

        button.setOnClickListener(v -> {
            Intent start = new Intent(MainActivity.this,GestureService.class);
            startService(start);
            LogUtil.i(TAG0,"startService GestureService");
        });

        button3.setOnClickListener(v -> {
            Intent stop = new Intent(MainActivity.this,GestureService.class);
            stopService(stop);
            LogUtil.i(TAG0,"stopService GestureService");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stop_destroy = new Intent(this,GestureService.class);
        stopService(stop_destroy);
    }
}

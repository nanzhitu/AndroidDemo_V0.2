package com.neo.event_bus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mText;
    private Button mButton,mButton3,mButton4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.button);
        mButton3 = findViewById(R.id.button3);
        mButton4 = findViewById(R.id.button4);
        mText = findViewById(R.id.textView);
        mText.setText("今天是星期三");
        EventBus.getDefault().register(this);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new MessageEvent("Hello Sticky!"));
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        mText.setText(messageEvent.getMessage());
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onMessageEvent1(MessageEvent event) {
        Log.i(TAG, "onMessageEvent1(), message is " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 2)
    public void onMessageEvent2(MessageEvent event) {
        Log.i(TAG, "onMessageEvent2(), message is " + event.getMessage());
        // 取消事件
        EventBus.getDefault().cancelEventDelivery(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 3)
    public void onMessageEvent3(MessageEvent event) {
        Log.i(TAG, "onMessageEvent3(), message is " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 4)
    public void onMessageEvent4(MessageEvent event) {
        Log.i(TAG, "onMessageEvent4(), message is " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 5)
    public void onMessageEvent5(MessageEvent event) {
        Log.i(TAG, "onMessageEvent5(), message is " + event.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}

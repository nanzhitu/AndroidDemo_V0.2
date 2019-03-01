package com.neo.event_bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private Button mButton2;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mButton2 = findViewById(R.id.button2);
        textView2 = findViewById(R.id.textView2);

        // 注册订阅者
        EventBus.getDefault().register(this);

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("通讯成功"));
                finish();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MessageEvent messageEvent) {
        Log.d(TAG,"onMessageEvent sticky "+messageEvent.getMessage());

        // 更新界面
        textView2.setText(messageEvent.getMessage());
        // 移除粘性事件
        EventBus.getDefault().removeStickyEvent(messageEvent);

        // 移除指定类型的粘性事件
        //EventBus.getDefault().removeStickyEvent(Class<T> eventType);

        // 移除所有的粘性事件
        //EventBus.getDefault().removeAllStickyEvents();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}

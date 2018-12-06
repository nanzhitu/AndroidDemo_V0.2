package com.neo.webclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String[] title = {"播天气","播天气", "看电影"};
    String[] mEvent = {"天气怎么样","深圳明天的天气","播放如懿传"};
    int[] eventType = {0,0,1};
    boolean[] enable = {true,true,true};
    boolean[] enableTTS = {true,true,true};
    String[] extend = {"adasasa","qqqdsadsdsds","dsdsds"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }



    private void start(){
        final String url = "http://172.20.5.130:8898/start";


        final EventBean eventBean = new EventBean();
        eventBean.setTitle("就是");
        eventBean.setExtend("testtest");
        List<EventBean.Event> eventList = new ArrayList<>();
        for(int i=0; i< title.length; i++) {
            EventBean.Event event = new EventBean.Event();
            event.setMultselectable(enable[i]);
            event.setTitle(title[i]);
            event.setEvent(mEvent[i]);
            event.setEventType(eventType[i]);
            event.setNeedTTS(enableTTS[i]);
            event.setExtend(extend[i]);
            eventList.add(event);
        }

        eventBean.setEventList(eventList);
        Log.d(TAG,"eventBean = "+eventBean.toString());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ret = OkHttpUtils.getInstance().postSyncString(url,JsonUtil.toJson(eventBean));
                    Log.d(TAG,"eventBean_json = "+JsonUtil.toJson(eventBean));
                    Log.d(TAG,"ret = "+ret);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

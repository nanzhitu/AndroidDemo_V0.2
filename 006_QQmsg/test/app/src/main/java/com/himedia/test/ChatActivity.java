package com.himedia.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.himedia.test.TimeUtil.dateToStamp;

/**
 * Created by Neo on 2018/9/5.
 */

public class ChatActivity extends Activity implements View.OnClickListener {


    private static final String TAG = "ChatActivity";
    private EditText mEditTextContent;
    //聊天内容的适配器
    private ChatMsgViewAdapter mAdapter;
    private LinkManViewAdapter mLinkManViewAdapter;
    private ListView mListView,mLinkMan;
    private int mPosition;
    private boolean isLinkManSelected;
    private boolean isSendSelected;
    private Button mSend,mVideo;
    //聊天的内容
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();

    private List<LinkManEntity> mLinkManArrays = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Utils.init(this.getApplication());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.chat);
        initView();
        initData();

    }

    //初始化视图
    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mLinkMan = (ListView)findViewById(R.id.lv_linkman);
        mSend = findViewById(R.id.btn_send);
        mVideo = findViewById(R.id.btn_video);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick mSend");
            }
        });

        mSend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"onFocusChange mSend= "+hasFocus);
                if(hasFocus){
                    isSendSelected = true;
                    mSend.setBackgroundResource(R.mipmap.dialog_btn_bg_selected);
                }else{
                    mSend.setBackgroundResource(R.mipmap.dialog_btn_bg_default);
                }
            }
        });


        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick mVideo");
            }
        });
        mVideo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"onFocusChange mVideo = "+hasFocus);
                if(hasFocus){
                    isSendSelected = false;
                    mVideo.setBackgroundResource(R.mipmap.dialog_btn_bg_selected);
                }else{
                    mVideo.setBackgroundResource(R.mipmap.dialog_btn_bg_default);
                }
            }
        });
    }

    private String[] msgArray = new String[]{"  孩子们，要好好学习，天天向上！要好好听课，不要翘课！不要挂科，多拿奖学金！三等奖学金的争取拿二等，二等的争取拿一等，一等的争取拿励志！", "还有什么吩咐...",
            "魔王：你尽管叫破喉咙吧...没有人会来救你的", "公主：破喉咙..破喉咙",
            "没有人：公主..我来救你了", "魔王：说曹操曹操就到",
            "曹操：魔王..你叫我干嘛", "魔王：哇勒..看到鬼",
            "鬼 ：靠!被发现了", "靠：阿鬼,你看的到我喔",
            "魔王：Oh,My God!", "上帝：谁叫我?",
            "谁 ：没有人叫你阿", "没有人：我哪有？装蒜啊！",
            "蒜：谁在装我？", "谁：又说我？你们找麻烦啊？"};

    private long[]dataArray = {1533557884, 1535804581, 1536149884, 1536149944,
            1536150004, 1536236404, 1536236465, 1536236466,
            1536236526, 1536279726, 1536279906, 1536283506,
            1536286026, 1536296826, 1536296886, 1536298086};

    private boolean[] hasread = {false,false,true,false,false,true,false,false,false,true,false,false,false,false,true,false};
    private int[] time = {2,5,60,2,5,11,2,5,11,2,5,11,2,5,11,1};
    private int[]msgtype = {0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
    private boolean[] isRcv = {true,false,true,false,true,false,false,false,true,false,true,false,false,false,true,false};
    private final static int COUNT = 16;

    private int[] msgnum = {100,99,1};
    private boolean[] isread = {false,false,true};
    private String[] name = {"小明","qqqqq","夏冬"};

    //初始化要显示的数据
    private void initData() {
        for(int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setTimeStamp(dataArray[i]);
//            if (i % 2 == 0)
//            {
                entity.setName("姚妈妈");
                entity.setRcv(isRcv[i]);
//            }else{
//                entity.setName("Shamoo");
//                entity.setRcv(false);
//            }

            entity.setContent(msgArray[i]);
            entity.setDuration(time[i]);
            entity.setHasRead(hasread[i]);
            entity.setMsgType(msgtype[i]);
            mDataArrays.add(entity);
        }

        for(int i =0; i< 3; i++){
            LinkManEntity linkman = new LinkManEntity();
            linkman.setRemark(name[i]);
            linkman.setHasRead(isread[i]);
            linkman.setMsgNum(msgnum[i]);

            mLinkManArrays.add(linkman);
        }
        mLinkManViewAdapter = new LinkManViewAdapter(this,mLinkManArrays);

        mLinkMan.setAdapter(mLinkManViewAdapter);

//        mLinkMan.post(new Runnable() {
//            @Override
//            public void run() {
//                mLinkMan.setItemChecked(1,true);
//            }
//        });

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);

        mLinkMan.setSelection(1);
        //init
        mLinkManViewAdapter.changeSelected(1);

        mLinkManViewAdapter.setFocusStatus(true);
        mSend.requestFocus();

        mLinkMan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"mLinkMan "+hasFocus);
                mLinkManViewAdapter.setFocusStatus(!hasFocus);
                if(hasFocus) {
                    isLinkManSelected = true;
                }
            }
        });

        mLinkMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLinkManViewAdapter.changeSelected(position);
                Log.d(TAG,"LinkMan position = "+position);
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLinkMan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ChatActivity","onItemSelected position = "+position+" , id = "+id);
                mAdapter.changeSelected(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ChatActivity","onItemClick position = "+position+" , id = "+id);
                mAdapter.changeClick(position);
            }
        });
    }

    public void onClick(View view) {
        Log.d(TAG,"keyevent = "+view.getId());
        switch(view.getId()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:

                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"keyCode = "+keyCode);
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(isSendSelected){
                    viewFocus(mLinkMan);
                    mLinkMan.setSelection(mPosition);
                    isSendSelected = false;
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(isLinkManSelected){
                    viewFocus(mSend);
                    isLinkManSelected = false;
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void viewFocus(View view){
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    private void send()
    {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0)
        {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setTimeStamp(dateToStamp(getDate()));
            entity.setName("");
            entity.setRcv(false);
            entity.setContent(contString);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    //获取日期
    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
        return sbBuffer.toString();
    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        back();
//        return true;
//    }
//
//    private void back() {
//        finish();
//    }
}

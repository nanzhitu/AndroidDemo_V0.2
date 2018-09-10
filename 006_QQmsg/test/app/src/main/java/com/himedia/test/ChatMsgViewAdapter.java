package com.himedia.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static com.himedia.test.TimeUtil.formatDate;
import static java.lang.Thread.sleep;


/**
 * Created by Neo on 2018/9/4.
 */

public class ChatMsgViewAdapter extends BaseAdapter {
//    //ListView视图的内容由IMsgViewType决定
//    public static interface IMsgViewType
//    {
//        //对方发来的信息
//        int IMVT_COM_MSG = 0;
//        //自己发出的信息
//        int IMVT_TO_MSG = 1;
//    }

    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();
    private List<ChatMsgEntity> data;
    private Context context;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LayoutInflater mInflater;
    private static final int noShowTime = 2*60;
    private int mSelect = -1;   //选中项
    private int mClick = -1;   //选中项
    private int needPlayTime = -1; //选中项需要播放的时间
    private boolean mStopPlay = true;
    private ChatMsgEntity mClickEntity = new ChatMsgEntity();
    private AnimationDrawable animationDrawable;


    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
        Log.d(TAG,"ChatMsgViewAdapter getCount = "+data.size());
    }

    //获取ListView的项个数
    public int getCount() {
        return data.size();
    }

    //获取项
    public Object getItem(int position) {
        return data.get(position);
    }

    //获取项的ID
    public long getItemId(int position) {
        return position;
    }

//    //获取项的类型
//    public int getItemViewType(int position) {
//        // TODO Auto-generated method stub
//        ChatMsgEntity entity = data.get(position);
//
//        if (entity.getRcv())
//        {
//            return IMsgViewType.IMVT_COM_MSG;
//        }else{
//            return IMsgViewType.IMVT_TO_MSG;
//        }
//
//    }

//    //获取项的类型数
//    public int getViewTypeCount() {
//        // TODO Auto-generated method stub
//        return 2;
//    }

    //获取View
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntity entity = data.get(position);
        long lastTimeStamp = 0;
        if (position > 0) {
            lastTimeStamp = data.get(position - 1).getTimeStamp();
        }
        boolean isRcv = entity.getRcv();
        int msgType = entity.getMsgType();
        boolean hasRead = entity.isHasRead();
        long timeStamp = entity.getTimeStamp();
        Log.d(TAG, "position" + position + " timeStamp = " + timeStamp + " lastTimeStamp = " + lastTimeStamp + " s: " + parent.getChildCount());
        Log.d(TAG,"isRcv = "+isRcv+" , msgType = "+msgType+" ,  hasRead = "+hasRead);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            Log.d(TAG, "position = " + position);

            convertView = mInflater.inflate(R.layout.chatting_item_mix, null);
            viewHolder = new ViewHolder();
            viewHolder.item_left = convertView.findViewById(R.id.item_left);
            viewHolder.l_bubble = convertView.findViewById(R.id.tv_l_msg);
            viewHolder.l_tvSendTime = convertView.findViewById(R.id.tv_l_sendtime);
            viewHolder.l_tvContent = convertView.findViewById(R.id.tv_l_chatcontent);
            viewHolder.l_audio = convertView.findViewById(R.id.iv_l_audio);
            viewHolder.l_time = convertView.findViewById(R.id.tv_l_audio_time);
            viewHolder.l_hasRead = convertView.findViewById(R.id.iv_l_msg_status);

            viewHolder.item_right = convertView.findViewById(R.id.item_right);
            viewHolder.r_bubble = convertView.findViewById(R.id.tv_r_msg);
            viewHolder.r_tvSendTime = convertView.findViewById(R.id.tv_r_sendtime);
            viewHolder.r_tvContent = convertView.findViewById(R.id.tv_r_chatcontent);
            viewHolder.r_audio = convertView.findViewById(R.id.iv_r_audio);
            viewHolder.r_time = convertView.findViewById(R.id.tv_r_audio_time);
            viewHolder.r_hasRead = convertView.findViewById(R.id.iv_r_msg_status);

            viewHolder.isRcv = isRcv;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isRcv) {
            //如果是对方发来的消息，则显示的是左气泡
            viewHolder.item_left.setVisibility(View.VISIBLE);
            viewHolder.item_right.setVisibility(View.GONE);
        } else {
            //如果是自己发出的消息，则显示的是右气泡
            viewHolder.item_left.setVisibility(View.GONE);
            viewHolder.item_right.setVisibility(View.VISIBLE);
        }

        //消息的类型，语音还是文本
        switch (msgType) {
            case ChatMsgEntity.MSG_TYPE_TEXT:
                if (isRcv) {
                    viewHolder.l_tvContent.setVisibility(View.VISIBLE);
                    viewHolder.l_audio.setVisibility(View.GONE);
                    viewHolder.l_time.setVisibility(View.GONE);
                    viewHolder.l_hasRead.setVisibility(View.GONE);//文本默认不显示
                }else {
                    viewHolder.r_tvContent.setVisibility(View.VISIBLE);
                    viewHolder.r_audio.setVisibility(View.GONE);
                    viewHolder.r_time.setVisibility(View.GONE);
                    viewHolder.r_hasRead.setVisibility(View.GONE);//文本默认不显示
                }
                break;
            case ChatMsgEntity.MSG_TYPE_AUDIO:
                if (isRcv) {
                    viewHolder.l_tvContent.setVisibility(View.GONE);
                    viewHolder.l_audio.setVisibility(View.VISIBLE);
                    viewHolder.l_time.setVisibility(View.VISIBLE);

                    if(!mStopPlay && mClick == position) {
                        viewHolder.l_audio.setBackgroundResource(R.drawable.frame_anim_left);
                        animationDrawable = (AnimationDrawable) viewHolder.l_audio.getBackground();
                        if (animationDrawable != null && !animationDrawable.isRunning()) {
                            animationDrawable.start();
                        }
                    }else{
                        viewHolder.l_audio.setBackgroundResource(R.mipmap.dialog_play_left_01);

                    }

                    //是否显示已读
                    if (hasRead) {
                        viewHolder.l_hasRead.setVisibility(View.GONE);
                    } else {
                        viewHolder.l_hasRead.setVisibility(View.VISIBLE);
                    }
                }else {
                    viewHolder.r_tvContent.setVisibility(View.GONE);
                    viewHolder.r_audio.setVisibility(View.VISIBLE);
                    viewHolder.r_time.setVisibility(View.VISIBLE);
                    Log.d("ygj","mStopPlay = "+mStopPlay);
                    if(!mStopPlay && mClick == position) {
                        viewHolder.r_audio.setBackgroundResource(R.drawable.frame_anim_right);
                        animationDrawable = (AnimationDrawable) viewHolder.r_audio.getBackground();
                        if (animationDrawable != null && !animationDrawable.isRunning()) {
                            animationDrawable.start();
                        }
                    }else{
                        viewHolder.r_audio.setBackgroundResource(R.mipmap.dialog_play_right_01);

                    }

                    viewHolder.r_hasRead.setVisibility(View.GONE);//己方默认已读，但是要考虑发送状态

                }
                break;
            default:
                break;
        }

        //是否显示时间戳(在0~120s内不显示)
        if (timeStamp - lastTimeStamp > noShowTime || timeStamp - lastTimeStamp < 0) {
            if(isRcv) {
                viewHolder.l_tvSendTime.setVisibility(View.VISIBLE);
            }else {
                viewHolder.r_tvSendTime.setVisibility(View.VISIBLE);
            }
        } else {
            if(isRcv) {
                viewHolder.l_tvSendTime.setVisibility(View.GONE);
            }else {
                viewHolder.r_tvSendTime.setVisibility(View.GONE);
            }
        }

        //时间戳内容
        if(isRcv) {
            viewHolder.l_tvSendTime.setText(formatDate(timeStamp));
        }else {
            viewHolder.r_tvSendTime.setText(formatDate(timeStamp));
        }

        //消息内容
        if (msgType == ChatMsgEntity.MSG_TYPE_TEXT) {
            if(isRcv) {
                viewHolder.l_tvContent.setText(entity.getContent());
            }else {
                viewHolder.r_tvContent.setText(entity.getContent());
            }
        } else if (msgType == ChatMsgEntity.MSG_TYPE_AUDIO) {
            if(isRcv) {
                viewHolder.l_time.setText(showDuration(isRcv, entity.getDuration()));
            }else {
                viewHolder.r_time.setText(showDuration(isRcv, entity.getDuration()));
            }
        }

        //选中效果
        if(mSelect == position){
            if(isRcv) {
                viewHolder.l_bubble.setBackgroundResource(R.mipmap.dialog_bg_left_selected);
            }else {
                viewHolder.r_bubble.setBackgroundResource(R.mipmap.dialog_bg_right_selected);
            }
        }else{
            if(isRcv) {
                viewHolder.l_bubble.setBackgroundResource(R.mipmap.dialog_bg_left);
            }else {
                viewHolder.r_bubble.setBackgroundResource(R.mipmap.dialog_bg_right);
            }
        }
        return convertView;
    }

    //通过ViewHolder显示项的内容
    static class ViewHolder {
        public LinearLayout item_left;
        public RelativeLayout l_bubble;
        public TextView l_tvSendTime;
        public TextView l_tvContent;
        public TextView l_time;
        public ImageView l_audio;
        public ImageView l_hasRead ;

        public LinearLayout item_right;
        public RelativeLayout r_bubble;
        public TextView r_tvSendTime;
        public TextView r_tvContent;
        public TextView r_time;
        public ImageView r_audio;
        public ImageView r_hasRead ;

        public boolean isRcv = true;
    }


    private String showDuration(boolean isRcv, int duration){
        int num = (int)(duration*1.4)+6;
        if (num > 90)num = 90;
        if(num < 7)num = 7;
        StringBuilder builder = new StringBuilder();
        if(isRcv) {
            for (int i = 0; i < num; i++)
                builder.append(" ");
            builder.append(duration + "\"");
        }else{
            builder.append(duration + "\"");
            for (int i = 0; i < num; i++)
                builder.append(" ");
        }
        return ""+builder;
    }

    public void changeSelected(int positon){ //选中效果刷新
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

    public void changeClick(int positon){ //点击效果刷新
         mClickEntity = data.get(positon);
        if(mClickEntity.getMsgType() == ChatMsgEntity.MSG_TYPE_AUDIO){
            resetAudioPlay();
            mHandler.removeCallbacksAndMessages(null);
            mClick = positon;
            needPlayTime = mClickEntity.getDuration();
            mClickEntity.setHasRead(true);
            mStopPlay = false;
            notifyDataSetChanged();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetAudioPlay();
                }
            },needPlayTime*1000);
        }
    }

    private void resetAudioPlay(){
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
            mStopPlay = true;
            mClick = -1;
            notifyDataSetChanged();
        }
    }

}

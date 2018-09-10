package com.himedia.test;

/**
 * Created by Neo on 2018/9/5.
 */

public class ChatMsgEntity {
    private static final String TAG = "ChatMsgEntity";

    /**
     * 文本消息
     */
    public static final int MSG_TYPE_TEXT = 0;

    /**
     * 图片消息
     */
    public static final int MSG_TYPE_IMAGE = 1;

    /**
     * 视频消息
     */
    public static final int MSG_TYPE_VIDEO = 2;

    /**
     * 语音消息
     */
    public static final int MSG_TYPE_AUDIO = 3;

    /**
     * 发送者id
     */
    private long sender;

    /**
     * 接收者id
     */
    private long receiver;

    //名字
    private String name;

    /**
     * 消息时间戳
     */
    private long timeStamp;

    /**
     * 文本消息就是消息的内容，语音消息为音频路径
     */
    private String content;

    /**
     * 语音消息的时长
     */
    private int duration;

    /**
     * 消息类型
     */
    private int msgType;

    /**
     * 是否为收到的消息
     */
    private boolean isRcv;

    /**
     * 是否已读
     */
    private boolean hasRead;


    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long time) {
        this.timeStamp = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setMsgType(int type) {
        this.msgType = type;
    }

    public int getMsgType() {
        return msgType;
    }
    public boolean getRcv() {
        return isRcv;
    }

    public void setRcv(boolean rcv) {
        this.isRcv = rcv;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(long sender,String name, long time, String content,int msgType, int duration, boolean rcv,boolean hasRead ) {
        this.sender = sender;
        this.name = name;
        this.timeStamp = time;
        this.content = content;
        this.msgType = msgType;
        this.duration = duration;
        this.isRcv = rcv;
        this.hasRead = hasRead;
    }
}

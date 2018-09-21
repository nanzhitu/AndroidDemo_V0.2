package com.himedia.test;

/**
 * Created by Neo on 2018/9/21.
 */

public class LinkManEntity {

    /**
     * 联系人id,用于获取头像
     */
    private long linkId = -1;
    /**
     * 联系人昵称
     */
    private String remark;

    /**
     * 是否已读
     */
    private boolean hasRead;

    /**
     * 未读的消息数量
     */
    private int msgNum;


    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long id) {
        this.linkId = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String name) {
        this.remark = name;
    }


    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int num) {
        this.msgNum = num;
    }
}

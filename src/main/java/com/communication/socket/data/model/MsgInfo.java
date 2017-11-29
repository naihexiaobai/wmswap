package com.communication.socket.data.model;

import java.io.Serializable;

/**
 * 消息
 *
 * @auther CalmLake
 * @create 2017/11/8  8:59
 */
public class MsgInfo implements Serializable {
    /**
     * 送信时间
     */
    private String time;
    /**
     * 送信次数
     */
    private short msgCountNum;
    /**
     * 任务序号
     */
    private short msgOrderNum;
    /**
     * 指令类型
     */
    private short msgCommandType;
    /**
     * 指令内容
     */
    private short msgCommandData;

    public short getMsgCommandData() {
        return msgCommandData;
    }

    public void setMsgCommandData(short msgCommandData) {
        this.msgCommandData = msgCommandData;
    }

    public short getMsgCommandType() {
        return msgCommandType;
    }

    public void setMsgCommandType(short msgCommandType) {
        this.msgCommandType = msgCommandType;
    }

    public short getMsgOrderNum() {
        return msgOrderNum;
    }

    public void setMsgOrderNum(short msgOrderNum) {
        this.msgOrderNum = msgOrderNum;
    }

    public short getMsgCountNum() {
        return msgCountNum;
    }

    public void setMsgCountNum(short msgCountNum) {
        this.msgCountNum = msgCountNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "时间-->" + getTime() + ",指令类型-->" + getMsgCommandType() + "，指令内容-->" + getMsgCommandData() + "，任务序号-->" + getMsgOrderNum() + ",送信次数-->" + getMsgCountNum();
    }
}

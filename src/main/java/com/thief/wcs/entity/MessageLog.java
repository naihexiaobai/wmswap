package com.thief.wcs.entity;

import java.util.Date;

public class MessageLog {
    private Integer id;

    private String msg;

    private String msgtype;

    private Date createtime;
    public static final String TYPE_SEND = "S";
    public static final String TYPE_RECEIVE = "R";
    public MessageLog(Integer id, String msg, String msgtype, Date createtime) {
        this.id = id;
        this.msg = msg;
        this.msgtype = msgtype;
        this.createtime = createtime;
    }

    public MessageLog() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype == null ? null : msgtype.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
package com.wap.model;

public class ErrorCodeMsg {
    private Integer id;

    private Integer code;

    private String msg;

    private Integer type;

    private String remark;

    public ErrorCodeMsg(Integer id, Integer code, String msg, Integer type, String remark) {
        this.id = id;
        this.code = code;
        this.msg = msg;
        this.type = type;
        this.remark = remark;
    }

    public ErrorCodeMsg() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
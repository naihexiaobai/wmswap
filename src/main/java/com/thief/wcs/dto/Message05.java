package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Message05 extends Message implements Serializable {
    private String id = "05";
    private String plcName = "";

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String McKey = "";
    public String Response = "";

    public Message05() {
    }

    public Message05(String str) throws MsgException {
        if (str.length() == 5) {
            McKey = str.substring(0, 4);
            Response = str.substring(4, 5);
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(McKey, 4, '0'));
        sb.append(StringUtils.rightPad(Response, 1, '0'));
        return sb.toString();
    }
}

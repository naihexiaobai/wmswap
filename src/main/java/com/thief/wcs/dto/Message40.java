package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Message40 extends Message implements Serializable {
    private String id = "40";
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

    public String Station = "";
    public String Mode = "";

    public Message40() {
    }

    public Message40(String str) throws MsgException {
        if (str.length() == 6) {
            Station = str.substring(0, 4);
            Mode = str.substring(4, 6);
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.leftPad(Station, 4, '0'));
        sb.append(StringUtils.leftPad(Mode, 2, '0'));
        return sb.toString();
    }
}

package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Message30 extends Message implements Serializable {
    private String id = "30";
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

    public String WcsNo = "";
    public String ConsoleNo = "";
    public String HeartBeatCycle = "";

    public Message30() {
    }

    public Message30(String str) throws MsgException {
        if (str.length() == 6) {
            WcsNo = str.substring(0, 1);
            ConsoleNo = str.substring(1, 5);
            HeartBeatCycle = str.substring(5, 6);
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(WcsNo, 1, '0'));
        sb.append(StringUtils.rightPad(ConsoleNo, 4, '0'));
        sb.append(StringUtils.rightPad(HeartBeatCycle, 1, '0'));
        return sb.toString();
    }
}

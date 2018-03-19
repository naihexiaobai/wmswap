package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Message23 extends Message implements Serializable {
    private String id = "23";
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
    public String MachineNo = "";
    public String CycleOrder = "";
    public String JobType = "";
    public String Height = "";
    public String Width = "";
    public String Bank = "";
    public String Bay = "";
    public String Level = "";
    public String Station = "";
    public String Dock = "";
    public String PalletLoad = "";
    public String Response = "";
    public String Error = "";

    public Message23() {
    }

    public Message23(String str) throws MsgException {
        if (str.length() == 31) {
            McKey = str.substring(0, 4);
            MachineNo = str.substring(4, 8);
            CycleOrder = str.substring(8, 10);
            JobType = str.substring(10, 12);
            Height = str.substring(12, 13);
            Width = str.substring(13, 14);
            Bank = str.substring(14, 16);
            Bay = str.substring(16, 18);
            Level = str.substring(18, 20);
            Station = str.substring(20, 24);
            Dock = str.substring(24, 28);
            PalletLoad = str.substring(28, 29);
            Response = str.substring(29, 30);
            Error = str.substring(30, 31);
        } else {
            throw new MsgException("[23] Message MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(McKey, 4, '0'));
        sb.append(StringUtils.rightPad(MachineNo, 4, '0'));
        sb.append(StringUtils.rightPad(CycleOrder, 2, '0'));
        sb.append(StringUtils.rightPad(JobType, 2, '0'));
        sb.append(StringUtils.rightPad(Height, 1, '0'));
        sb.append(StringUtils.rightPad(Width, 1, '0'));
        sb.append(StringUtils.rightPad(Bank, 2, '0'));
        sb.append(StringUtils.rightPad(Bay, 2, '0'));
        sb.append(StringUtils.rightPad(Level, 2, '0'));
        sb.append(StringUtils.rightPad(Station, 4, '0'));
        sb.append(StringUtils.rightPad(Dock, 4, '0'));
        sb.append(StringUtils.rightPad(PalletLoad, 1, '0'));
        sb.append(StringUtils.rightPad(Response, 1, '0'));
        sb.append(StringUtils.rightPad(Error, 1, '0'));
        return sb.toString();
    }
}

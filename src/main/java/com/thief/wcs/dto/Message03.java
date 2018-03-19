package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Message03 extends Message implements Serializable {
    private String id = "03";
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

    public Message03() {
    }

    public Message03(String str) throws MsgException {
        if (str.length() == 28) {
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
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.leftPad(McKey, 4, '0'));
        sb.append(StringUtils.leftPad(MachineNo, 4, '0'));
        sb.append(StringUtils.leftPad(CycleOrder, 2, '0'));
        sb.append(StringUtils.leftPad(JobType, 2, '0'));
        sb.append(StringUtils.leftPad(Height, 1, '0'));
        sb.append(StringUtils.leftPad(Width, 1, '0'));
        sb.append(StringUtils.leftPad(Bank, 2, '0'));
        sb.append(StringUtils.leftPad(Bay, 2, '0'));
        sb.append(StringUtils.leftPad(Level, 2, '0'));
        sb.append(StringUtils.leftPad(Station, 4, '0'));
        sb.append(StringUtils.leftPad(Dock, 4, '0'));
        return sb.toString();
    }


    public static class _CycleOrder {
        public static final String goBack = "01";
        public static final String pickUpGoods = "02";
        public static final String unloadGoods = "03";
        public static final String move = "04";
        public static final String loadCar = "05";
        public static final String unloadCar = "06";
        public static final String moveCarryGoods = "07";
        public static final String moveUnloadGoods = "08";
        public static final String onCar = "09";
        public static final String offCar = "10";
        public static final String scarMoveCarryGoods = "11";
        public static final String onCarCarryGoods = "12";
        public static final String offCarCarryGoods = "13";
        public static final String charge = "14";
        public static final String chargeFinish = "15";
    }
}

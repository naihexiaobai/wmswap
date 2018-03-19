package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Message35 extends Message implements Serializable {
    private String id = "35";
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
    public String FinishType = "";
    public String FinishCode = "";

    public Message35() {
    }

    public Message35(String str) throws MsgException {
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
            FinishType = str.substring(29, 30);
            FinishCode = str.substring(30, 31);
        } else {
            throw new MsgException("[35] Message MsgException.Invalid_length   " + str);
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
        sb.append(StringUtils.rightPad(FinishType, 1, '0'));
        sb.append(StringUtils.rightPad(FinishCode, 1, '0'));
        return sb.toString();
    }

    public boolean isPickingUpGoods() {
        return CycleOrder.equals(_CycleOrder.pickUpGoods);
    }

    public boolean isUnloadGoods() {
        return CycleOrder.equals(_CycleOrder.unloadGoods);
    }

    public boolean isMove() {
        return CycleOrder.equals(_CycleOrder.move);
    }

    public boolean isLoadCar() {
        return CycleOrder.equals(_CycleOrder.loadCar);
    }

    public boolean isUnLoadCar() {
        return CycleOrder.equals(_CycleOrder.unloadCar);
    }

    public boolean isMoveCarryGoods() {
        return CycleOrder.equals(_CycleOrder.moveCarryGoods);
    }

    public boolean isMoveUnloadGoods() {
        return CycleOrder.equals(_CycleOrder.moveUnloadGoods);
    }

    public boolean isOnCar() {
        return CycleOrder.equals(_CycleOrder.onCar);
    }

    public boolean isOffCar() {
        return CycleOrder.equals(_CycleOrder.offCar);
    }

    public boolean isScarMoveCarryGoods() {
        return CycleOrder.equals(_CycleOrder.scarMoveCarryGoods);
    }

    public boolean isOnCarCarryGoods() {
        return CycleOrder.equals(_CycleOrder.onCarCarryGoods);
    }

    public boolean isOffCarCarryGoods() {
        return CycleOrder.equals(_CycleOrder.offCarCarryGoods);
    }

    public boolean isCharge() {
        return CycleOrder.equals(_CycleOrder.charge);
    }

    public boolean isChargeFinish() {
        return CycleOrder.equals(_CycleOrder.chargeFinish);
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

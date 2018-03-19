package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message26 extends Message implements Serializable {
    private String id = "26";
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

    public String DataCount = "";
    public Map<String, Block> Blocks = new HashMap<String, Block>();

    public Message26() {
    }

    public Message26(String str) throws MsgException {
        if (str.length() > 1) {
            DataCount = str.substring(0, 1);
            int count = Integer.parseInt(DataCount);
            if (str.length() == 1 + (count * 26)) {
                for (int i = 1; i < str.length(); i = i + 26) {
                    String machineNo = str.substring(i, i + 4);
                    Block block = new Block();
                    block.HostMachine = str.substring(i + 4, i + 8);
                    block.Bank = str.substring(i + 8, i + 10);
                    block.Bay = str.substring(i + 10, i + 12);
                    block.Level = str.substring(i + 12, i + 14);
                    block.Run = str.substring(i + 14, i + 15);
                    block.Stop = str.substring(i + 15, i + 16);
                    block.Abnormal = str.substring(i + 16, i + 17);
                    block.EmergencyStop = str.substring(i + 17, i + 18);
                    block.Offline = str.substring(i + 18, i + 19);
                    block.BatteryOk = str.substring(i + 19, i + 20);
                    block.BatteryLow = str.substring(i + 20, i + 21);
                    block.BatteryElectricity = str.substring(i + 21, i + 24);
                    block.ErrorCode = str.substring(i + 24, i + 26);
                    Blocks.put(machineNo, block);
                }
            } else {
                throw new MsgException("MsgException.Invalid_length   " + str);
            }
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(DataCount, 1, '0'));
        for (String machineNo : Blocks.keySet()) {
            sb.append(StringUtils.rightPad(machineNo, 4, '0'));
            Block block = Blocks.get(machineNo);
            sb.append(StringUtils.rightPad(block.HostMachine, 4, '0'));
            sb.append(StringUtils.rightPad(block.Bank, 2, '0'));
            sb.append(StringUtils.rightPad(block.Bay, 2, '0'));
            sb.append(StringUtils.rightPad(block.Level, 2, '0'));
            sb.append(StringUtils.rightPad(block.Run, 1, '0'));
            sb.append(StringUtils.rightPad(block.Stop, 1, '0'));
            sb.append(StringUtils.rightPad(block.Abnormal, 1, '0'));
            sb.append(StringUtils.rightPad(block.EmergencyStop, 1, '0'));
            sb.append(StringUtils.rightPad(block.Offline, 1, '0'));
            sb.append(StringUtils.rightPad(block.BatteryOk, 1, '0'));
            sb.append(StringUtils.rightPad(block.BatteryLow, 1, '0'));
            sb.append(StringUtils.rightPad(block.BatteryElectricity, 3, '0'));
            sb.append(StringUtils.rightPad(block.ErrorCode, 2, '0'));

        }
        return sb.toString();
    }

    public class Block {
        public String HostMachine = "";
        public String Bank = "";
        public String Bay = "";
        public String Level = "";
        public String Run = "";
        public String Stop = "";
        public String Abnormal = "";
        public String EmergencyStop = "";
        public String Offline = "";
        public String BatteryOk = "";
        public String BatteryLow = "";
        public String BatteryElectricity = "";
        public String ErrorCode = "";

        public String getHostMachine() {
            return HostMachine;
        }

        public void setHostMachine(String hostMachine) {
            HostMachine = hostMachine;
        }

        public String getBank() {
            return Bank;
        }

        public void setBank(String bank) {
            Bank = bank;
        }

        public String getBay() {
            return Bay;
        }

        public void setBay(String bay) {
            Bay = bay;
        }

        public String getLevel() {
            return Level;
        }

        public void setLevel(String level) {
            Level = level;
        }

        public String getRun() {
            return Run;
        }

        public void setRun(String run) {
            Run = run;
        }

        public String getStop() {
            return Stop;
        }

        public void setStop(String stop) {
            Stop = stop;
        }

        public String getAbnormal() {
            return Abnormal;
        }

        public void setAbnormal(String abnormal) {
            Abnormal = abnormal;
        }

        public String getEmergencyStop() {
            return EmergencyStop;
        }

        public void setEmergencyStop(String emergencyStop) {
            EmergencyStop = emergencyStop;
        }

        public String getOffline() {
            return Offline;
        }

        public void setOffline(String offline) {
            Offline = offline;
        }

        public String getBatteryOk() {
            return BatteryOk;
        }

        public void setBatteryOk(String batteryOk) {
            BatteryOk = batteryOk;
        }

        public String getBatteryLow() {
            return BatteryLow;
        }

        public void setBatteryLow(String batteryLow) {
            BatteryLow = batteryLow;
        }

        public String getBatteryElectricity() {
            return BatteryElectricity;
        }

        public void setBatteryElectricity(String batteryElectricity) {
            BatteryElectricity = batteryElectricity;
        }

        public String getErrorCode() {
            return ErrorCode;
        }

        public void setErrorCode(String errorCode) {
            ErrorCode = errorCode;
        }
    }
}

package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message02 extends Message implements Serializable {
    private String id = "02";
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
    public List<String> MachineNos = new ArrayList<String>();

    public Message02() {
    }

    public Message02(String str) throws MsgException {
        if (str.length() > 3) {
            DataCount = str.substring(0, 3);
            int count = Integer.parseInt(DataCount);
            if (str.length() == 3 + (count * 4)) {
                for (int i = 3; i <= str.length(); i = i + 4) {
                    MachineNos.add(str.substring(i, i + 4));
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
        sb.append(StringUtils.rightPad(DataCount, 3, '0'));
        for (String machineNo : MachineNos) {
            sb.append(StringUtils.rightPad(machineNo, 4, '0'));
        }
        return sb.toString();
    }
}

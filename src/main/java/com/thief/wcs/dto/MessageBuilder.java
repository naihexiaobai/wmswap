package com.thief.wcs.dto;

import java.io.Serializable;

public class MessageBuilder implements Serializable {
    public String PlcName = "";
    public String SeqNo = "0000";
    public String ID = "0";
    public String IdClassification = "0";
    public String McTime = "000000";
    public String DataString = "";

    public static MessageBuilder Parse(String msgStr) throws MsgException {
        if (msgStr.length() >= 13) {
            MessageBuilder mb = new MessageBuilder();
            mb.SeqNo = msgStr.substring(0, 4);
            mb.ID = msgStr.substring(4, 6);
            mb.IdClassification = msgStr.substring(6, 7);
            mb.McTime = msgStr.substring(7, 13);
            mb.DataString = msgStr.substring(13, msgStr.length());
            return mb;
        } else {
            throw new MsgException("MsgException.Invalid_Length" + msgStr);
        }
    }


    public String toString() {
        return SeqNo + ID + IdClassification + McTime + DataString;
    }
}

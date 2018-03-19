package com.thief.wcs.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message50 extends Message implements Serializable {
    private String id = "50";
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
    public Map<String, Block> MachineNos = new HashMap<String, Block>();

    public Message50() {
    }

    public Message50(String str) throws MsgException {
        try {
            DataCount = str.substring(0, 1);
            int count = Integer.parseInt(DataCount);
            int index = 1;
            for (int i = 0; i < count; i++) {
                String machineNo = str.substring(index, index + 4);
                index += 4;
                Block block = new Block();
                block.LoadCount = str.substring(index, index + 1);
                int loadCount = Integer.parseInt(block.LoadCount);
                index += 1;
                for (int j = 1; j <= loadCount; j++) {
                    String mcKey = str.substring(index, index + 4);
                    index += 4;
                    String barcode = str.substring(index, index + 15);
                    index += 15;
                    Map<String, String> mcKeyAndBarcodes = new HashMap<String, String>();
                    mcKeyAndBarcodes.put(mcKey, barcode);
                    block.McKeysAndBarcodes.put(j, mcKeyAndBarcodes);
                }
                block.Load = str.substring(index, index + 1);
                index += 1;
                block.height = str.substring(index, ++index);
                block.width = str.substring(index, ++index);
                block.weight = str.substring(index, index + 6);
                index += 6;

                MachineNos.put(machineNo, block);
            }
        } catch (Exception ex) {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(DataCount, 1, '0'));
        for (String machineNo : MachineNos.keySet()) {
            sb.append(StringUtils.rightPad(machineNo, 4, '0'));
            Block block = MachineNos.get(machineNo);
            sb.append(StringUtils.rightPad(block.LoadCount, 1, '0'));
            int loadCount = Integer.parseInt(block.LoadCount);
            for (int j = 1; j <= loadCount; j++) {
                Map<String, String> mcKeyAndBarcode = block.McKeysAndBarcodes.get(j);

                sb.append(StringUtils.rightPad(mcKeyAndBarcode.keySet().iterator().next(), 4, '0'));
                sb.append(StringUtils.rightPad(mcKeyAndBarcode.values().iterator().next(), 10, '0'));
            }
            sb.append(StringUtils.rightPad(block.Load, 1, '0'));
        }
        return sb.toString();
    }

    public class Block {
        public String LoadCount = "";
        public Map<Integer, Map<String, String>> McKeysAndBarcodes = new HashMap<Integer, Map<String, String>>();
        public String Load = "";
        public String height = "";
        public String width = "";
        public String weight = "";
    }
}

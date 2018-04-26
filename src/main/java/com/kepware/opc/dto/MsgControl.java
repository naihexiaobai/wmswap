package com.kepware.opc.dto;

import com.kepware.opc.dto.command.BlockCommand;

import java.io.Serializable;

/**
 * 控制消息
 *
 * @auther CalmLake
 * @create 2018/4/2  15:02
 */
public class MsgControl implements Serializable {
    private int wcsInfoId;
    private String blockNo;
    private String nextBlockNo;
    private String key;
    private String type;
    private String typeMsg;
    private BlockCommand blockCommand;
    /**
     * 执行命令设备blockNo
     */
    private String controlBlockNo;

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getNextBlockNo() {
        return nextBlockNo;
    }

    public void setNextBlockNo(String nextBlockNo) {
        this.nextBlockNo = nextBlockNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeMsg() {
        return typeMsg;
    }

    public void setTypeMsg(String typeMsg) {
        this.typeMsg = typeMsg;
    }

    public String getControlBlockNo() {
        return controlBlockNo;
    }

    public void setControlBlockNo(String controlBlockNo) {
        this.controlBlockNo = controlBlockNo;
    }


    public int getWcsInfoId() {
        return wcsInfoId;
    }

    public void setWcsInfoId(int wcsInfoId) {
        this.wcsInfoId = wcsInfoId;
    }


    public BlockCommand getBlockCommand() {
        return blockCommand;
    }

    public void setBlockCommand(BlockCommand blockCommand) {
        this.blockCommand = blockCommand;
    }

    public static MsgControl createMsgControl(int wcsInfoId, String blockNo, String nextBlockNo, String controlBlockNo, String command, String key, BlockCommand blockCommand) {
        MsgControl msgControl = new MsgControl();
        msgControl.setWcsInfoId(wcsInfoId);
        msgControl.setBlockNo(blockNo);
        msgControl.setNextBlockNo(nextBlockNo);
        msgControl.setKey(key);
        msgControl.setType(command);
        msgControl.setControlBlockNo(controlBlockNo);
        msgControl.setBlockCommand(blockCommand);
        return msgControl;
    }

    @Override
    public String toString() {
        return "执行命令blockNo：" + controlBlockNo + ",blockNo：" + blockNo + ",nextBlockNo:" + nextBlockNo + ",key:" + key + ",type:" + type + ",typeMsg:" + typeMsg;
    }
}

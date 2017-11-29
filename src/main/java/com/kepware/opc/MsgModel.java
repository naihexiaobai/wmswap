package com.kepware.opc;

/**
 * 消息体
 *
 * @auther CalmLake
 * @create 2017/11/23  13:22
 */
public class MsgModel {

    /**
     * 设备编号
     */
    private int NO;
    /**
     * 任务码 0-9999
     */
    private int orderNum;

    /**
     * 任务指令
     */
    private int commandNum;

    public int getCommandNum() {
        return commandNum;
    }

    public void setCommandNum(int commandNum) {
        this.commandNum = commandNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "任务码->" + getOrderNum() + ",动作指令->" + getCommandNum();
    }

    public int getNO() {
        return NO;
    }

    public void setNO(int NO) {
        this.NO = NO;
    }
}

package com.kepware.opc;

/**
 * 子车消息
 *
 * @auther CalmLake
 * @create 2017/11/23  14:13
 */
public class ZiCheMsgModel extends MsgModel {

    /**
     * 目标层
     */
    private int targetStorey;

    /**
     * 目标列
     */
    private int targetLine;

    /**
     * 目标排
     */
    private int targetRow;

    public int getTargetStorey() {
        return targetStorey;
    }

    public void setTargetStorey(int targetStorey) {
        this.targetStorey = targetStorey;
    }

    public int getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(int targetLine) {
        this.targetLine = targetLine;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public void setTargetRow(int targetRow) {
        this.targetRow = targetRow;
    }
}

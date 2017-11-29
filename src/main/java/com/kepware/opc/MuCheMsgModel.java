package com.kepware.opc;

/**
 * 母车消息
 *
 * @auther CalmLake
 * @create 2017/11/23  14:13
 */
public class MuCheMsgModel extends MsgModel {

    /**
     * 目标列
     */
    private int targetLine;

    public int getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(int targetLine) {
        this.targetLine = targetLine;
    }
}

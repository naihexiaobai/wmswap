package com.kepware.opc;

/**
 * 升降机消息
 *
 * @auther CalmLake
 * @create 2017/11/23  14:14
 */
public class ShengJiangJiMsgModel extends MsgModel {

    /**
     * 目标层
     */
    private int targetStorey;

    public int getTargetStorey() {
        return targetStorey;
    }

    public void setTargetStorey(int targetStorey) {
        this.targetStorey = targetStorey;
    }
}

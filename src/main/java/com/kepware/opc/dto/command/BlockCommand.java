package com.kepware.opc.dto.command;

import java.io.Serializable;

/**
 * opc控制指令
 *
 * @auther CalmLake
 * @create 2018/3/21  17:03
 */
public class BlockCommand implements Serializable {
    public static final String CHANEL_ALL = "wap";
    public static final String ITEM_COMMAND = "command";
    public static final String ITEM_TASK_ID = "taskID";
    /**
     * 链条无动作
     */
    public static final String COMMAND_ZERO = "0";
    /**
     * 从左侧取托盘
     */
    public static final String COMMAND_LEFT_IN = "11";
    /**
     * 向左侧出托盘
     */
    public static final String COMMAND_LEFT_OUT = "21";
    /**
     * 从右侧取托盘
     */
    public static final String COMMAND_RIGHT_IN = "12";
    /**
     * 向右侧出托盘
     */
    public static final String COMMAND_RIGHT_OUT = "22";
    /**
     * 动作指令
     */
    private String command;
    /**
     * 动作指令任务号
     */
    private String taskID;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public static String getY(String location) {
        return location.substring(1, 2);
    }

    public static String getX(String location) {
        return location.substring(2, 3);
    }

    public static String getZ(String location) {
        return location.substring(3, 4);
    }
}

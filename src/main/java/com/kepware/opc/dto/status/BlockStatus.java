package com.kepware.opc.dto.status;

import java.io.Serializable;
import java.util.Date;

/**
 * block状态
 *
 * @auther CalmLake
 * @create 2018/3/21  9:03
 */
public class BlockStatus implements Serializable {

    private String blockNo;
    /**
     * 空闲
     */
    private boolean free;
    /**
     * 载物
     */
    private boolean load;
    /**
     * 待命
     */
    private boolean onStandby;

    /**
     * wcs->plc,动作指令
     */
    private String command;

    /**
     * wcs->plc，动作任务号
     */
    private String taskID;

    /**
     * plc,完成动作任务号
     */
    private String plcTaskID;

    /**
     * 最后一次更新时间
     */
    private Date lastUpdateTime;

    public BlockStatus(String blockNo) {
        this.blockNo = blockNo;
    }


    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

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

    public String getPlcTaskID() {
        return plcTaskID;
    }

    public void setPlcTaskID(String plcTaskID) {
        this.plcTaskID = plcTaskID;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public boolean isOnStandby() {
        return onStandby;
    }

    public void setOnStandby(boolean onStandby) {
        this.onStandby = onStandby;
    }

    @Override
    public String toString() {
        return "BlockNo:" + getBlockNo() + ",TaskID:" + getTaskID() + ",Command:" + getCommand() + ",PlcTaskID" + getPlcTaskID();
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}

package com.kepware.opc.dto.command;

/**
 * 升降机动作指令
 *
 * @auther CalmLake
 * @create 2018/3/21  17:11
 */
public class ElBlockCommand extends BlockCommand {

    public static final String ITEM_TARGET_TIER = "targetTier";

    /**
     * 目标层
     */
    private String targetTier;

    public static ElBlockCommand createElBlockCommand(String command, String tier, String taskID) {
        ElBlockCommand elBlockCommand = new ElBlockCommand();
        elBlockCommand.setCommand(command);
        elBlockCommand.setTargetTier(tier);
        elBlockCommand.setTaskID(taskID);
        return elBlockCommand;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }

    @Override
    public String toString() {
        return "targetTier:" + this.targetTier + ",command:" + this.getCommand() + ",taskID:" + this.getTaskID();
    }
}

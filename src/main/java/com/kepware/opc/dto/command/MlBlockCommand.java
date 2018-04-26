package com.kepware.opc.dto.command;

/**
 * 堆垛机动作指令
 *
 * @auther CalmLake
 * @create 2018/3/21  17:14
 */
public class MlBlockCommand extends BlockCommand {
    public static final String ITEM_TARGET_LINE = "targetLine";
    public static final String ITEM_TARGET_TIER = "targetTier";
    /**
     * 目标列
     */
    private String targetLine;

    /**
     * 目标层
     */
    private String targetTier;

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }

    public static MlBlockCommand createMlBlockCommand(String x, String y, String command, String taskID) {
        MlBlockCommand mlBlockCommand = new MlBlockCommand();
        mlBlockCommand.setCommand(command);
        mlBlockCommand.setTargetLine(x);
        mlBlockCommand.setTargetTier(y);
        mlBlockCommand.setTaskID(taskID);
        return mlBlockCommand;
    }

    @Override
    public String toString() {
        return "command:"+getCommand()+",taskID:"+getTaskID()+",targetLine:"+targetLine+",targetTier:"+getTargetTier();
    }
}

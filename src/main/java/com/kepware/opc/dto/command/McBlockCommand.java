package com.kepware.opc.dto.command;

/**
 * 母车动作指令
 *
 * @auther CalmLake
 * @create 2018/3/21  17:12
 */
public class McBlockCommand extends BlockCommand {
    public static final String ITEM_TARGET_LINE = "targetLine";
    /**
     * 目标列
     */
    private String targetLine;

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    public static McBlockCommand createMcBlockCommand(String command, String line, String taskID) {
        McBlockCommand mcBlockCommand = new McBlockCommand();
        mcBlockCommand.setCommand(command);
        mcBlockCommand.setTargetLine(line);
        mcBlockCommand.setTaskID(taskID);
        return mcBlockCommand;
    }

    @Override
    public String toString() {
        return "targetLine:" + targetLine + ",command:" + getCommand() + ",taskID:" + getTaskID();
    }
}

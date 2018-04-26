package com.kepware.opc.dto.command;

/**
 * 子车动作指令
 *
 * @auther CalmLake
 * @create 2018/3/21  17:08
 */
public class ScBlockCommand extends BlockCommand {
    public static final String ITEM_TARGET_LINE = "targetLine";
    public static final String ITEM_TARGET_TIER = "targetTier";
    public static final String ITEM_TARGET_ROW = "targetRow";

    public static final String COMMAND_A_IN = "1"; //1=A面入货
    public static final String COMMAND_A_OUT = "2"; //2=A面出货
    public static final String COMMAND_B_IN = "3"; //3=B面入货
    public static final String COMMAND_B_OUT = "4"; //4=B面出货
    public static final String COMMAND_A_CLEAR_UP = "5"; //5=A面整仓
    public static final String COMMAND_B_CLEAR_UP = "6"; //6=B面整仓
    public static final String COMMAND_TO_L_B = "7"; //7=出母车到输送线B段
    public static final String COMMAND_TO_EL = "8"; //8=上提升机
    public static final String COMMAND_TO_L_A = "9"; //9=出输送机到输送线A段
    public static final String COMMAND_RES = "10"; //10=备用
    public static final String COMMAND_A_IN_M = "11"; //11=A面上母车  ==>上
    public static final String COMMAND_A_OUT_M = "12"; //12=A面下母车  <==下
    public static final String COMMAND_B_IN_M = "13"; //13=B面上母车  <==上
    public static final String COMMAND_B_OUT_M = "14"; //14=B面下母车  ==>下
    public static final String COMMAND_A_CHECK = "15"; //15=A面盘点
    public static final String COMMAND_B_CHECK = "16"; //16=B面盘点
    public static final String COMMAND_A_TO_B = "17"; //17=A-B面切换
    public static final String COMMAND_B_TO_A = "18"; //18=B-A面切换
    public static final String COMMAND_CHARGE = "19"; //19=小车充电 WCS未控制
    public static final String COMMAND_A_IN_M_LOAD = "21"; //21=A面载货上母车 ==>上
    public static final String COMMAND_A_OUT_M_LOAD = "22"; //22=A面载货下母车 <==下
    public static final String COMMAND_B_IN_M_LOAD = "23"; //23=B面载货上母车  <==上
    public static final String COMMAND_B_OUT_M_LOAD = "24"; //24=B面载货下母车 ==>下
    /**
     * 目标列
     */
    private String targetLine;
    /**
     * 目标排
     */
    private String targetRow;
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

    public String getTargetRow() {
        return targetRow;
    }

    public void setTargetRow(String targetRow) {
        this.targetRow = targetRow;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }

    public static ScBlockCommand createMcBlockCommand(String command, String X, String Y, String Z, String taskID) {
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        scBlockCommand.setCommand(command);
        scBlockCommand.setTargetLine(X);
        scBlockCommand.setTargetTier(Y);
        scBlockCommand.setTargetRow(Z);
        scBlockCommand.setTaskID(taskID);
        return scBlockCommand;
    }


    @Override
    public String toString() {
        return "command:" + getCommand() + ",targetLine:" + getTargetLine() + ",taskID:" + getTaskID() + ",targetLine:" + targetLine + ",targetTier:" + targetTier + ",targetRow:" + targetRow;
    }
}

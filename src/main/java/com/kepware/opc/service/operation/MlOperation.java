package com.kepware.opc.service.operation;

import com.kepware.opc.dto.command.BlockCommand;
import com.kepware.opc.dto.command.ElBlockCommand;
import com.kepware.opc.dto.command.MlBlockCommand;
import com.kepware.opc.dto.command.ScBlockCommand;
import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.CalmLakeStringUtil;
import com.www.util.LoggerUtil;

import java.util.Date;

/**
 * 堆垛机
 *
 * @auther CalmLake
 * @create 2018/3/28  16:50
 */
public class MlOperation implements Operation {
    String logName = "MlOperation";

    private String blockNo;
    private String nextBlockNo;
    private String key;

    public MlOperation(String blockNo, String nextBlockNo, String key) {
        this.blockNo = blockNo;
        this.nextBlockNo = nextBlockNo;
        this.key = key;
    }


    /**
     * 移栽取货指令
     */
    public MlBlockCommand transplantingPickUp() throws Exception {
        String line;
        String tier;
        String command;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        int row_present_block_row = CalmLakeStringUtil.stringToInt(opcBlock.getRow());
        int row_next_block_row = CalmLakeStringUtil.stringToInt(nextBlock.getRow());
        line = opcBlock.getLine();
        tier = opcBlock.getTier();
        if (row_present_block_row < row_next_block_row) {
            command = ElBlockCommand.COMMAND_LEFT_IN;
        } else {
            command = ElBlockCommand.COMMAND_RIGHT_IN;
        }
        MlBlockCommand mlBlockCommand = MlBlockCommand.createMlBlockCommand(line, tier, command, taskID);
        return mlBlockCommand;
    }


    /**
     * 移栽卸货
     *
     * @return
     * @throws Exception
     */
    public MlBlockCommand transplantingTheUnloading() throws Exception {
        String line;
        String tier;
        String command;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        int row_present_block_row = CalmLakeStringUtil.stringToInt(opcBlock.getRow());
        int row_next_block_row = CalmLakeStringUtil.stringToInt(nextBlock.getRow());
        line = nextBlock.getLine();
        tier = nextBlock.getTier();
        if (row_present_block_row < row_next_block_row) {
            command = ElBlockCommand.COMMAND_RIGHT_OUT;
        } else {
            command = ElBlockCommand.COMMAND_LEFT_OUT;
        }
        MlBlockCommand mlBlockCommand = MlBlockCommand.createMlBlockCommand(line, tier, command, taskID);
        return mlBlockCommand;
    }

    /**
     * 移动向下一设备(子车)所在列
     *
     * @return
     * @throws Exception
     */
    public MlBlockCommand moveToNextBlockScLine() throws Exception {
        String line;
        String tier;
        String taskID = CalmLakeStringUtil.getRandomNum();
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(nextBlockNo);
        line = scBlockStatus.getLine();
        tier = scBlockStatus.getTier();
        MlBlockCommand mlBlockCommand = MlBlockCommand.createMlBlockCommand(line, tier, BlockCommand.COMMAND_ZERO, taskID);
        return mlBlockCommand;
    }

    /**
     * 移动向最终任务列
     *
     * @param key
     * @return
     * @throws Exception
     */
    public MlBlockCommand moveToTargetLine(String key) throws Exception {
        String location = null;
        String line;
        String tier;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            location = opcOrder.getFromlocation();
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            location = opcOrder.getTolocation();
        }
        line = BlockCommand.getX(location);
        tier = BlockCommand.getY(location);
        MlBlockCommand mlBlockCommand = MlBlockCommand.createMlBlockCommand(line, tier, BlockCommand.COMMAND_ZERO, taskID);
        return mlBlockCommand;
    }

    /**
     * 母车是否在最终任务列
     *
     * @param key
     * @return
     */
    public boolean mlIsOnTargetLine(String key) {
        String location = null;
        String line;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            location = opcOrder.getFromlocation();
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            location = opcOrder.getTolocation();
        }
        line = BlockCommand.getX(location);
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockOperationDBUtil.getInstance().getBlockStatusByBlockNo(blockNo);
        if (line.equals(mlBlockStatus.getLine())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 子车是否在堆垛机上
     *
     * @return
     * @throws Exception
     */
    public boolean mlHaveCar() throws Exception {
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(nextBlockNo);
        if (mlBlockStatus.isTheCar() && scBlockStatus.isRGV()) {
            return true;
        }
        return false;
    }

    /**
     * 子车堆垛机在同一列
     *
     * @return
     * @throws Exception
     */
    public boolean mlAndScOnSameLine() throws Exception {
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(nextBlockNo);
        if (mlBlockStatus.getLine().equals(scBlockStatus.getLine())) {
            return true;
        }
        return false;
    }

    public boolean isWriteSuccess(BlockCommand blockCommand, String presentBlockNo) throws Exception {
        MlBlockCommand mlBlockCommand = (MlBlockCommand) blockCommand;
        boolean result = false;
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(presentBlockNo);
        if (mlBlockStatus.getTaskID().equals(mlBlockCommand.getTaskID()) && mlBlockStatus.getCommand().equals(mlBlockCommand.getCommand())) {
            result = true;
        }
        if (mlBlockStatus.isError()) {
            LoggerUtil.getLoggerByName(presentBlockNo + logName).warn("设备出现故障！");
            throw new Exception();
        }
        return result;
    }
    @Override
    public boolean isFinishWork(BlockCommand blockCommand, String blockNo) throws Exception {
        MlBlockStatus MlBlockStatus;
        MlBlockCommand mlBlockCommand = (MlBlockCommand) blockCommand;
        boolean result = false;
        Date dateStart = new Date();
        Date dateNow;
        try {
            while (!result) {
                dateNow = new Date();
                long dateNowLong = dateNow.getTime();
                long dateStartLong = dateStart.getTime();
                long sss = (dateNowLong - dateStartLong) / 1000;
                MlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
                if (BlockStatusOperationUtil.isFinishedWork(MlBlockStatus) && MlBlockStatus.getTaskID().equals(mlBlockCommand.getTaskID())
                        && MlBlockStatus.getPlcTaskID().equals(MlBlockStatus.getTaskID())) {
                    if (mlBlockCommand.COMMAND_ZERO.equals(mlBlockCommand.getCommand()) && MlBlockStatus.getLine().equals(mlBlockCommand.getTargetLine()) && BlockStatusOperationUtil.isMlBlockCanWork(MlBlockStatus)) {
                        result = true;
                    } else if ((mlBlockCommand.COMMAND_LEFT_IN.equals(mlBlockCommand.getCommand()) || mlBlockCommand.COMMAND_RIGHT_IN.equals(mlBlockCommand.getCommand()))
                            && MlBlockStatus.getLine().equals(mlBlockCommand.getTargetLine()) && BlockStatusOperationUtil.isMlBlockCanWork(MlBlockStatus)
                            && MlBlockStatus.isLoad()) {
                        result = true;
                    } else if ((mlBlockCommand.COMMAND_LEFT_OUT.equals(mlBlockCommand.getCommand()) || mlBlockCommand.COMMAND_RIGHT_OUT.equals(mlBlockCommand.getCommand()))
                            && MlBlockStatus.getLine().equals(mlBlockCommand.getTargetLine()) && BlockStatusOperationUtil.isMlBlockCanWork(MlBlockStatus)
                            && !MlBlockStatus.isLoad()) {
                        result = true;
                    }
                } else if (MlBlockStatus.isError()) {
                    LoggerUtil.getLoggerByName(logName).warn("堆垛机故障！");
                    throw new Exception();
                }else if (sss > 10 && sss < 60) {
                    //判断写入是否成功
                    boolean isWriteSuccess = isWriteSuccess(blockCommand, blockNo);
                    if (!isWriteSuccess) {
                        OpcWrite.instance().writeByBlockCommand(blockCommand, blockNo);
                        dateStart = new Date();
                    }
                }else if (sss > 60) {
                    LoggerUtil.getLoggerByName(logName).warn("堆垛机工作超时！");
                    throw new Exception();
                } else {
                    //TODO   循环判断成功时问题
                }
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

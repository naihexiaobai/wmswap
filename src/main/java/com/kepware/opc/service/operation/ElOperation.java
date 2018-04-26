package com.kepware.opc.service.operation;

import com.kepware.opc.dto.command.BlockCommand;
import com.kepware.opc.dto.command.ElBlockCommand;
import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.dto.command.MlBlockCommand;
import com.kepware.opc.dto.status.ElBlockStatus;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.CalmLakeStringUtil;
import com.www.util.LoggerUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 固定提升机
 *
 * @auther CalmLake
 * @create 2018/3/27  14:40
 */
public class ElOperation implements Operation {

    static String logName = "ElOperation";
    private String blockNo;
    private String nextBlockNo;
    private String key;

    public ElOperation(String blockNo, String nextBlockNo, String key) {
        this.blockNo = blockNo;
        this.nextBlockNo = nextBlockNo;
        this.key = key;
    }

    /**
     * 移栽取货指令
     */
    public ElBlockCommand transplantingPickUp(String ElBlockNo, String otherBlockNo) throws Exception {
        String tier;
        String command;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(ElBlockNo);
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(otherBlockNo);
        int row_present_block_row = CalmLakeStringUtil.stringToInt(opcBlock.getRow());
        int row_next_block_row = CalmLakeStringUtil.stringToInt(nextBlock.getRow());
        if (row_present_block_row > row_next_block_row) {
            command = ElBlockCommand.COMMAND_LEFT_IN;
            tier = nextBlock.getTier();
        } else {
            command = ElBlockCommand.COMMAND_RIGHT_IN;
            tier = nextBlock.getTier();
        }
        ElBlockCommand elBlockCommand = ElBlockCommand.createElBlockCommand(command, tier, taskID);
        return elBlockCommand;
    }

    /**
     * 移栽卸货
     *
     * @return
     * @throws Exception
     */
    public ElBlockCommand transplantingTheUnloading(String ElBlockNo, String otherBlockNo) throws Exception {
        String tier;
        String command;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(ElBlockNo);
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(otherBlockNo);
        int row_present_block_row = CalmLakeStringUtil.stringToInt(opcBlock.getRow());
        int row_next_block_row = CalmLakeStringUtil.stringToInt(nextBlock.getRow());
        if (row_present_block_row > row_next_block_row) {
            command = ElBlockCommand.COMMAND_LEFT_OUT;
            tier = nextBlock.getTier();
        } else {
            command = ElBlockCommand.COMMAND_RIGHT_OUT;
            tier = nextBlock.getTier();
        }
        ElBlockCommand elBlockCommand = ElBlockCommand.createElBlockCommand(command, tier, taskID);
        return elBlockCommand;
    }


    /**
     * 移栽取货/卸货，update DB
     *
     * @param key
     */
    @Transactional
    public void transplantingPickUpSuccessUpdateBlock(String key) throws Exception {
        BlockOperationDBUtil.getInstance().updateTwoBlock(blockNo, nextBlockNo, key);
    }

    public boolean isWriteSuccess(BlockCommand blockCommand, String presentBlockNo) throws Exception {
        ElBlockCommand elBlockCommand = (ElBlockCommand) blockCommand;
        boolean result = false;
        ElBlockStatus elBlockStatus = (ElBlockStatus) BlockStatusOperationUtil.getBlockStatus(presentBlockNo);
        if (elBlockStatus.getTaskID().equals(elBlockCommand.getTaskID()) && elBlockStatus.getCommand().equals(elBlockCommand.getCommand())) {
            result = true;
        }
        if (elBlockStatus.isError()) {
            LoggerUtil.getLoggerByName(presentBlockNo + logName).warn("设备出现故障！");
            throw new Exception();
        }
        return result;
    }

    @Override
    public boolean isFinishWork(BlockCommand blockCommand, String blockNo) throws Exception {
        ElBlockCommand elBlockCommand = (ElBlockCommand) blockCommand;
        ElBlockStatus elBlockStatus;
        boolean result = false;
        Date dateStart = new Date();
        Date dateNow;
        try {
            while (!result) {
                elBlockStatus = (ElBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
                dateNow = new Date();
                long dateNowLong = dateNow.getTime();
                long dateStartLong = dateStart.getTime();
                long sss = (dateNowLong - dateStartLong) / 1000;
                if (BlockStatusOperationUtil.isFinishedWork(elBlockStatus) && elBlockStatus.getTaskID().equals(elBlockCommand.getTaskID())) {
                    result = true;
                } else if (ElBlockCommand.COMMAND_ZERO.equals(elBlockCommand.getCommand()) && elBlockStatus.getTier().equals(elBlockCommand.getTargetTier()) && BlockStatusOperationUtil.isElBlockCanWork(elBlockStatus)) {
                    result = true;
                } else if ((ElBlockCommand.COMMAND_LEFT_IN.equals(elBlockCommand.getCommand()) || ElBlockCommand.COMMAND_RIGHT_IN.equals(elBlockCommand.getCommand()))
                        && elBlockStatus.getTier().equals(elBlockCommand.getTargetTier()) && BlockStatusOperationUtil.isElBlockCanWork(elBlockStatus)
                        && elBlockStatus.isLoad()) {
                    result = true;
                } else if ((ElBlockCommand.COMMAND_LEFT_OUT.equals(elBlockCommand.getCommand()) || ElBlockCommand.COMMAND_RIGHT_OUT.equals(elBlockCommand.getCommand()))
                        && elBlockStatus.getTier().equals(elBlockCommand.getTargetTier()) && BlockStatusOperationUtil.isElBlockCanWork(elBlockStatus)
                        && !elBlockStatus.isLoad()) {
                    result = true;
                } else if (elBlockStatus.isError()) {
                    LoggerUtil.getLoggerByName(logName).warn("升降机故障！");
                    throw new Exception();
                } else if (sss > 10) {
                    //判断写入是否成功
                    boolean isWriteSuccess = isWriteSuccess(blockCommand, blockNo);
                    if (!isWriteSuccess) {
                        OpcWrite.instance().writeByBlockCommand(blockCommand, blockNo);
                        dateStart = new Date();
                    }
                } else if (sss > 60) {
                    LoggerUtil.getLoggerByName(logName).warn("升降机工作超时！");
                    throw new Exception();
                } else {
                    //TODO   循环判断成功时问题
                }
                Thread.sleep(55);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

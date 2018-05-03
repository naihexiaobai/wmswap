package com.kepware.opc.service.operation;

import com.kepware.opc.dto.command.*;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.CalmLakeStringUtil;
import com.www.util.LoggerUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * 母车
 *
 * @auther CalmLake
 * @create 2018/3/27  16:25
 */
public class McOperation implements Operation {
    static String logName = "McOperation";

    private String blockNo;
    private String nextBlockNo;
    private String key;

    public McOperation(String blockNo, String nextBlockNo, String key) {
        this.blockNo = blockNo;
        this.nextBlockNo = nextBlockNo;
        this.key = key;
    }

    /**
     * 移栽取货指令
     */
    public McBlockCommand transplantingPickUp() throws Exception {
        String line;
        String command;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        int row_present_block_row = CalmLakeStringUtil.stringToInt(opcBlock.getRow());
        int row_next_block_row = CalmLakeStringUtil.stringToInt(nextBlock.getRow());
        line = opcBlock.getLine();
        if (row_present_block_row > row_next_block_row) {
            command = ElBlockCommand.COMMAND_LEFT_IN;
        } else {
            command = ElBlockCommand.COMMAND_RIGHT_IN;
        }
        McBlockCommand mcBlockCommand = McBlockCommand.createMcBlockCommand(command, line, taskID);
        return mcBlockCommand;
    }


    /**
     * 移栽卸货
     *
     * @return
     * @throws Exception
     */
    public McBlockCommand transplantingTheUnloading() throws Exception {
        String line;
        String command;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        int row_present_block_row = CalmLakeStringUtil.stringToInt(opcBlock.getRow());
        int row_next_block_row = CalmLakeStringUtil.stringToInt(nextBlock.getRow());
        line = nextBlock.getLine();
        if (row_present_block_row < row_next_block_row) {
            command = ElBlockCommand.COMMAND_RIGHT_OUT;
        } else {
            command = ElBlockCommand.COMMAND_LEFT_OUT;
        }
        McBlockCommand mcBlockCommand = McBlockCommand.createMcBlockCommand(command, line, taskID);
        return mcBlockCommand;
    }

    /**
     * 移动向下一设备(子车)所在列
     *
     * @return
     * @throws Exception
     */
    public McBlockCommand moveToNextBlockScLine(String nextBlockNo) throws Exception {
        String line;
        String taskID = CalmLakeStringUtil.getRandomNum();
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(nextBlockNo);
        line = scBlockStatus.getLine();
        McBlockCommand mcBlockCommand = McBlockCommand.createMcBlockCommand(BlockCommand.COMMAND_ZERO, line, taskID);
        return mcBlockCommand;
    }

    /**
     * 移动向最终任务列
     *
     * @param key
     * @return
     * @throws Exception
     */
    public McBlockCommand moveToTargetLine(String key) throws Exception {
        String location = null;
        String line;
        String taskID = CalmLakeStringUtil.getRandomNum();
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            location = opcOrder.getFromlocation();
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            location = opcOrder.getTolocation();
        }
        line = BlockCommand.getX(location);
        McBlockCommand mcBlockCommand = McBlockCommand.createMcBlockCommand(BlockCommand.COMMAND_ZERO, line, taskID);
        return mcBlockCommand;
    }

    public McBlockCommand moveToLine(String line) throws Exception {
        String taskID = CalmLakeStringUtil.getRandomNum();
        McBlockCommand mcBlockCommand = McBlockCommand.createMcBlockCommand(BlockCommand.COMMAND_ZERO, line, taskID);
        return mcBlockCommand;
    }

    /**
     * 母车是否在最终任务列
     *
     * @param key
     * @return
     */
    public boolean mcIsOnTargetLine(String key) {
        String location = null;
        String line;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            location = opcOrder.getFromlocation();
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            location = opcOrder.getTolocation();
        }
        line = BlockCommand.getX(location);
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockOperationDBUtil.getInstance().getBlockStatusByBlockNo(blockNo);
        if (line.equals(mcBlockStatus.getLine())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 子车是否在母车上
     *
     * @return
     * @throws Exception
     */
    public boolean mcHaveCar() throws Exception {
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(nextBlockNo);
        if (mcBlockStatus.isTheCar() && scBlockStatus.isRGV()) {
            return true;
        }
        return false;
    }

    /**
     * 子母车在同一列
     *
     * @return
     * @throws Exception
     */
    public boolean mcAndScOnSameLine() throws Exception {
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(nextBlockNo);
        if (mcBlockStatus.getLine().equals(scBlockStatus.getLine())) {
            return true;
        }
        return false;
    }


    /**
     * 给设备一个预约任务
     *
     * @param blockNo
     * @param key
     */
    @Transactional
    public void createNewReceivedMcKeyWork(String blockNo, String key) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        if (!CalmLakeStringUtil.stringIsNull(opcBlock.getReservedmckey()) && key.equals(opcBlock.getReservedmckey())) {

        } else {
            opcBlock.setReservedmckey(key);
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
        }
    }


    public boolean mcIsLoad() throws Exception {
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        return mcBlockStatus.isLoad();
    }


    /**
     * 移栽取货，update DB
     *
     * @param key
     */
    @Transactional
    public void transplantingPickUpSuccessUpdateBlock(String key) {
        BlockOperationDBUtil.getInstance().updateTwoBlock(blockNo, nextBlockNo, key);
    }
    public boolean isWriteSuccess(BlockCommand blockCommand, String presentBlockNo) throws Exception {
        McBlockCommand mcBlockCommand = (McBlockCommand) blockCommand;
        boolean result = false;
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(presentBlockNo);
        if (mcBlockStatus.getTaskID().equals(mcBlockCommand.getTaskID()) && mcBlockStatus.getCommand().equals(mcBlockCommand.getCommand())) {
            result = true;
        }
        if (mcBlockStatus.isError()) {
            LoggerUtil.getLoggerByName(presentBlockNo + logName).warn("设备出现故障！");
            throw new Exception();
        }
        return result;
    }
    @Override
    public boolean isFinishWork(BlockCommand blockCommand, String blockNo) throws Exception {
        McBlockCommand mcBlockCommand = (McBlockCommand) blockCommand;
        McBlockStatus mcBlockStatus;
        boolean result = false;
        Date dateStart = new Date();
        Date dateNow;
        try {
            while (!result) {
                dateNow = new Date();
                long dateNowLong = dateNow.getTime();
                long dateStartLong = dateStart.getTime();
                long sss = (dateNowLong - dateStartLong) / 1000;
                mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
                if (BlockStatusOperationUtil.isFinishedWork(mcBlockStatus) && mcBlockStatus.getTaskID().equals(mcBlockCommand.getTaskID())) {
                    result = true;
                } else if (McBlockCommand.COMMAND_ZERO.equals(mcBlockCommand.getCommand()) && mcBlockStatus.getLine().equals(mcBlockCommand.getTargetLine()) && BlockStatusOperationUtil.isMcBlockCanWork(mcBlockStatus)) {
                    result = true;
                } else if ((McBlockCommand.COMMAND_LEFT_IN.equals(mcBlockCommand.getCommand()) || McBlockCommand.COMMAND_RIGHT_IN.equals(mcBlockCommand.getCommand()))
                        && mcBlockStatus.getLine().equals(mcBlockCommand.getTargetLine()) && BlockStatusOperationUtil.isMcBlockCanWork(mcBlockStatus)
                        && mcBlockStatus.isLoad()) {
                    result = true;
                } else if ((McBlockCommand.COMMAND_LEFT_OUT.equals(mcBlockCommand.getCommand()) || McBlockCommand.COMMAND_RIGHT_OUT.equals(mcBlockCommand.getCommand()))
                        && mcBlockStatus.getLine().equals(mcBlockCommand.getTargetLine()) && BlockStatusOperationUtil.isMcBlockCanWork(mcBlockStatus)
                        && !mcBlockStatus.isLoad()) {
                    result = true;
                } else if (mcBlockStatus.isError()) {
                    LoggerUtil.getLoggerByName(logName).warn("母车故障！");
                    throw new Exception();
                } else if (sss > 5 && sss < 60) {
                    //判断写入是否成功
                    boolean isWriteSuccess = isWriteSuccess(blockCommand, blockNo);
                    if (!isWriteSuccess) {
                        OpcWrite.instance().writeByBlockCommand(blockCommand, blockNo);
                        dateStart = new Date();
                    }else {

                    }
                }
                else if (sss > 60) {
                    LoggerUtil.getLoggerByName(logName).warn("母车工作超时！");
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

package com.kepware.opc.service.operation;

import com.kepware.opc.dto.command.BlockCommand;
import com.kepware.opc.dto.command.ScBlockCommand;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.wap.model.Storage;
import com.www.util.CalmLakeStringUtil;
import com.www.util.LoggerUtil;

import java.util.Date;
import java.util.List;

/**
 * 子车
 *
 * @auther CalmLake
 * @create 2018/3/28  10:45
 */
public class ScOperation implements Operation {
    static String logName = "ScOperation";

    private String blockNo;
    private String nextBlockNo;
    private String key;

    public ScOperation(String blockNo, String nextBlockNo, String key) {
        this.blockNo = blockNo;
        this.nextBlockNo = nextBlockNo;
        this.key = key;
    }


    /**
     * 载货出库
     *
     * @return
     * @throws Exception
     */
    public ScBlockCommand cargoOutbound(String key) throws Exception {
        String line;
        String tier;
        String row;
        String command;
        String location;
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        location = OpcOrderOperation.getFromLocation(key);
        row = BlockCommand.getZ(location);
        tier = BlockCommand.getY(location);
        line = BlockCommand.getX(location);
        if (scBlockStatus.isBOrigin()) {
            command = ScBlockCommand.COMMAND_B_OUT.trim();
        } else {
            command = ScBlockCommand.COMMAND_A_OUT.trim();
        }
        String taskID = CalmLakeStringUtil.getRandomNum();
        scBlockCommand.setTaskID(taskID);
        scBlockCommand.setTargetRow(row);
        scBlockCommand.setTargetTier(tier);
        scBlockCommand.setTargetLine(line);
        scBlockCommand.setCommand(command);
        return scBlockCommand;
    }

    /**
     * 载货入库
     *
     * @return
     */
    public ScBlockCommand cargoWarehousing() throws Exception {
        String line;
        String tier;
        String row;
        String command;
        String location;
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        location = OpcOrderOperation.getToLocation(key);
        row = BlockCommand.getZ(location);
        tier = BlockCommand.getY(location);
        line = BlockCommand.getX(location);
        if (scBlockStatus.isBOrigin()) {
            command = ScBlockCommand.COMMAND_B_IN.trim();
        } else {
            command = ScBlockCommand.COMMAND_A_IN.trim();
        }
        String taskID = CalmLakeStringUtil.getRandomNum();
        scBlockCommand.setTaskID(taskID);
        scBlockCommand.setTargetRow(row);
        scBlockCommand.setTargetTier(tier);
        scBlockCommand.setTargetLine(line);
        scBlockCommand.setCommand(command);
        return scBlockCommand;
    }

    /**
     * 载货上车
     *
     * @param presentBlockNo
     * @param key
     * @return
     */
    public ScBlockCommand freightCar(String presentBlockNo, String key) throws Exception {
        String command;
        String row;
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(presentBlockNo);
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        if (scBlockStatus.isAOrigin()) {
            command = ScBlockCommand.COMMAND_A_IN_M_LOAD.trim();
        } else {
            command = ScBlockCommand.COMMAND_B_IN_M_LOAD.trim();
        }
        row = opcBlock.getRow().equals("0") ? "1" : opcBlock.getRow();
        String taskID = CalmLakeStringUtil.getRandomNum();
        scBlockCommand.setTaskID(taskID);
        scBlockCommand.setTargetRow(row);
        scBlockCommand.setTargetTier(opcBlock.getTier());
        scBlockCommand.setTargetLine(opcBlock.getLine());
        scBlockCommand.setCommand(command);
        return scBlockCommand;
    }

    /**
     * 载货下车
     *
     * @param presentBlockNo,当前所停靠设备的blockNo
     * @return
     * @throws Exception
     */
    public ScBlockCommand carryingOutOfTheCar(String presentBlockNo, String key) throws Exception {
        String line;
        String tier;
        String row;
        String command;
        String location;
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(presentBlockNo);
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        location = OpcOrderOperation.getToLocation(key);
        row = BlockCommand.getZ(location);
        tier = BlockCommand.getY(location);
        line = BlockCommand.getX(location);
        int present_row = Integer.valueOf(opcBlock.getRow());
        int next_row = Integer.valueOf(row);
        if (present_row < next_row) {
            command = ScBlockCommand.COMMAND_B_OUT_M_LOAD.trim();
        } else {
            command = ScBlockCommand.COMMAND_A_OUT_M_LOAD.trim();
        }
        String taskID = CalmLakeStringUtil.getRandomNum();
        scBlockCommand.setTaskID(taskID);
        scBlockCommand.setTargetRow(row);
        scBlockCommand.setTargetTier(tier);
        scBlockCommand.setTargetLine(line);
        scBlockCommand.setCommand(command);
        return scBlockCommand;
    }


    /**
     * 子车空车下车
     *
     * @param key
     * @return
     * @throws Exception
     */
    public ScBlockCommand getOffMc(String key) throws Exception {
        String line;
        String tier;
        String row;
        String command;
        OpcBlock nextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        String location = OpcOrderOperation.getFromLocation(key);
        row = BlockCommand.getZ(location);
        tier = BlockCommand.getY(location);
        line = BlockCommand.getX(location);
        int present_row = Integer.valueOf(nextBlock.getRow());
        int next_row = Integer.valueOf(row);
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        if (present_row < next_row) {
            command = ScBlockCommand.COMMAND_B_OUT_M.trim();
        } else {
            command = ScBlockCommand.COMMAND_A_OUT_M.trim();
        }
        String taskID = CalmLakeStringUtil.getRandomNum();
        scBlockCommand.setTaskID(taskID);
        scBlockCommand.setTargetRow(row);
        scBlockCommand.setTargetTier(tier);
        scBlockCommand.setTargetLine(line);
        scBlockCommand.setCommand(command);
        return scBlockCommand;
    }

    /**
     * 子车空车上母车
     * toMc
     *
     * @return
     * @throws Exception
     */
    public ScBlockCommand getOnMc() throws Exception {
        String line;
        String tier;
        String row;
        String command;
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        ScBlockCommand scBlockCommand = new ScBlockCommand();
        if (scBlockStatus.isAOrigin()) {
            command = ScBlockCommand.COMMAND_A_IN_M.trim();
        } else if (scBlockStatus.isBOrigin()) {
            command = ScBlockCommand.COMMAND_B_IN_M.trim();
        } else {
            throw new Exception();
        }
        row = scBlockStatus.getRow();
        tier = scBlockStatus.getTier();
        line = scBlockStatus.getLine();
        String taskID = CalmLakeStringUtil.getRandomNum();
        scBlockCommand.setTaskID(taskID);
        scBlockCommand.setTargetRow(row);
        scBlockCommand.setTargetTier(tier);
        scBlockCommand.setTargetLine(line);
        scBlockCommand.setCommand(command);
        return scBlockCommand;
    }

    /**
     * 子车是否在源目标巷道
     *
     * @param key
     * @return
     */
    public boolean scIsOnTargetLine(String key) {
        String location = null;
        String line;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            location = opcOrder.getFromlocation();
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            location = opcOrder.getTolocation();
        }
        line = BlockCommand.getX(location);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockOperationDBUtil.getInstance().getBlockStatusByBlockNo(blockNo);
        if (line.equals(scBlockStatus.getLine())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean scIsLoad() throws Exception {
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        return scBlockStatus.isLoad();
    }

    /**
     * 是否写入成功
     *
     * @param blockCommand
     * @param presentBlockNo
     * @return
     * @throws Exception
     */
    public boolean isWriteSuccess(BlockCommand blockCommand, String presentBlockNo) throws Exception {
        ScBlockCommand scBlockCommand = (ScBlockCommand) blockCommand;
        boolean result = false;
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(presentBlockNo);
        if (scBlockStatus.getTaskID().equals(scBlockCommand.getTaskID()) && scBlockStatus.getCommand().equals(scBlockCommand.getCommand())) {
            result = true;
        }
        if (scBlockStatus.isError()) {
            LoggerUtil.getLoggerByName(presentBlockNo + logName).warn("设备出现故障！");
            throw new Exception();
        }
        return result;
    }


    /**
     * 入库订单完成
     */
    public void bePutInStorageSuccess() {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(opcBlock.getMckey());
        Storage storage = new Storage();
        storage.setStorageno(opcOrder.getTolocation());
        List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
        if (storageList.size() > 0) {
            Storage storage1 = storageList.get(0);
            if (storage1.getStatus() == Storage.STATUS_INING) {
                storage1.setStatus(Storage.STATUS_USEING);
                storage1.setUsetime(new Date());
                BlockOperationDBUtil.getInstance().updateStorageById(storage1);

                opcBlock.setMckey("");
                BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);

                opcOrder.setEndtime(new Date());
                opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
            }
        }
    }

    @Override
    public boolean isFinishWork(BlockCommand blockCommand, String blockNo) throws Exception {
        ScBlockCommand scBlockCommand = (ScBlockCommand) blockCommand;
        LoggerUtil.getLoggerByName(logName).info(blockNo + "，任务开始,scBlockCommand:" + scBlockCommand.toString());
        ScBlockStatus scBlockStatus;
        boolean result = false;
        Date dateStart = new Date();
        Date dateNow;
        try {
            while (!result) {
                dateNow = new Date();
                long dateNowLong = dateNow.getTime();
                long dateStartLong = dateStart.getTime();
                long sss = (dateNowLong - dateStartLong) / 1000;
                scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
                if (BlockStatusOperationUtil.isFinishedWork(scBlockStatus)
                        && scBlockStatus.getTaskID().equals(scBlockStatus.getPlcTaskID()) && scBlockStatus.getTaskID().equals(scBlockCommand.getTaskID())) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_A_OUT.equals(scBlockCommand.getCommand()) && scBlockStatus.isAOrigin() && scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_B_OUT.equals(scBlockCommand.getCommand()) && scBlockStatus.isBOrigin() && scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_B_OUT_M.equals(scBlockCommand.getCommand()) && scBlockStatus.isBOrigin() && !scBlockStatus.isRGV()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_A_OUT_M.equals(scBlockCommand.getCommand()) && scBlockStatus.isAOrigin() && !scBlockStatus.isRGV()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_A_IN_M.equals(scBlockCommand.getCommand()) && scBlockStatus.isRGV()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_B_IN_M.equals(scBlockCommand.getCommand()) && scBlockStatus.isRGV()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_B_IN.equals(scBlockCommand.getCommand()) && scBlockStatus.isBOrigin() && !scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_A_IN.equals(scBlockCommand.getCommand()) && scBlockStatus.isAOrigin() && !scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_A_IN_M_LOAD.equals(scBlockCommand.getCommand()) && scBlockStatus.isRGV() && scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_B_IN_M_LOAD.equals(scBlockCommand.getCommand()) && scBlockStatus.isRGV() && scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_B_OUT_M_LOAD.equals(scBlockCommand.getCommand()) && scBlockStatus.isBOrigin() && scBlockStatus.isLoad()) {
                    result = true;
                } else if (BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus) && ScBlockCommand.COMMAND_A_OUT_M_LOAD.equals(scBlockCommand.getCommand()) && scBlockStatus.isAOrigin() && scBlockStatus.isLoad()) {
                    result = true;
                } else if (scBlockStatus.isError()) {
                    LoggerUtil.getLoggerByName(logName).warn("子车故障！");
                    throw new Exception();
                } else if (sss > 10) {
                    //判断写入是否成功
                    boolean isWriteSuccess = isWriteSuccess(blockCommand, blockNo);
                    if (!isWriteSuccess) {
                        OpcWrite.instance().writeByBlockCommand(blockCommand, blockNo);
                        dateStart = new Date();
                    }
                } else if (sss > 60) {
                    LoggerUtil.getLoggerByName(logName).warn("子车工作超时！");
                    throw new Exception();
                } else {
                    //TODO   循环判断成功时问题
//                    if ((nowTime - startTime) % 60 > 60 && BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus)) {
//                        OpcDBDataCacheCenter.instance().addOrderKey(blockNo, key);
//                        LoggerUtil.getLoggerByName(logName).warn("超时写入！重新下发指令。");
//                        throw new Exception();
//                    }
                }
                Thread.sleep(55);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoggerUtil.getLoggerByName(logName).info(blockNo + "，任务完成,scBlockCommand:" + scBlockCommand.toString());
        return result;
    }
}

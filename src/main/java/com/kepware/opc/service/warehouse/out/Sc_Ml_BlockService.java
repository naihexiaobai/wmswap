package com.kepware.opc.service.warehouse.out;

import com.kepware.opc.dto.command.ScBlockCommand;
import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Sc_BlockServiceImpl;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.service.operation.ScOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.LoggerUtil;

import java.util.Date;

/**
 * 子车--堆垛机
 *
 * @auther CalmLake
 * @create 2018/4/9  11:48
 */
public class Sc_Ml_BlockService extends Sc_BlockServiceImpl {

    String logName = blockNo_sc + "_Sc_Ml_BlockService";

    public Sc_Ml_BlockService(String blockNo_sc, String blockNo_other, String key) {
        super(blockNo_sc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        Thread.sleep(110);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_sc);
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        ScOperation scOperation = new ScOperation(blockNo_sc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        String line = OpcOrderOperation.getTargetLine(key);
        String tier = OpcOrderOperation.getTargetTier(key);
        ScBlockCommand scBlockCommand;
        if (!scBlockStatus.isRGV()) {
            if (tier.equals(scBlockStatus.getTier()) && line.equals(scBlockStatus.getLine()) && scBlockStatus.getTier().equals(mlBlockStatus.getTier()) && scBlockStatus.getLine().equals(mlBlockStatus.getLine())) {
                //子车不在母车上，子母车同列,子车载货， 子车载货上车
                scBlockCommand = scOperation.freightCar(blockNo_sc, key);
                OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
            } else {
                //让母车来接车
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
                return;
            }
        } else {
            LoggerUtil.getLoggerByName(logName).info("key:" + key + "子车已经在母车上");
            return;
        }
//        if (line.equals(mlBlockStatus.getLine()) && scBlockStatus.isLoad() && !scBlockStatus.isRGV() && mlBlockStatus.getLine().equals(scBlockStatus.getLine())) {
//            //子车不在母车上，子母车同列,子车载货， 子车载货上车
//            scBlockCommand = scOperation.freightCar(blockNo_sc, key);
//            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
//        } else if (!line.equals(mlBlockStatus.getLine()) && scBlockStatus.isLoad() && !scBlockStatus.isRGV() && !mlBlockStatus.getLine().equals(scBlockStatus.getLine())) {
//            //让母车来接车
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
//            return;
//        } else {
//            LoggerUtil.getLoggerByName(logName).info("key:" + key + ",无正确状态");
//            return;
//        }
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(scBlockCommand, blockNo_sc, key);
        OpcWrite.instance().writeByBlockCommand(scBlockCommand, blockNo_sc);
        boolean result = scOperation.isFinishWork(scBlockCommand, blockNo_sc);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            BlockOperationDBUtil.getInstance().updateBlockMcKeyAndReservedMcKey(blockNo_sc, blockNo_other, key);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
        }
    }


    @Override
    public void withReceivedMcKey() throws Exception {
        Thread.sleep(110);
        int type;
        ScOperation scOperation = new ScOperation(blockNo_sc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_sc);
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        String line = OpcOrderOperation.getTargetLine(key);
        String tier = OpcOrderOperation.getTargetTier(key);
        ScBlockCommand scBlockCommand;
        if (scBlockStatus.isRGV() && mlBlockStatus.isTheCar()) {
            if (tier.equals(mlBlockStatus.getTier()) && line.equals(mlBlockStatus.getLine())) {
                //子车在母车上，且母车在目标列,层，子车空车下车
                scBlockCommand = scOperation.getOffMc(key);
                type = 2;
                OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
            } else {
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
                return;
            }
        } else {
            if (tier.equals(scBlockStatus.getTier()) && line.equals(scBlockStatus.getLine())) {
                //子车在目标列，子车出库 取得货物，mcKey 赋值 ，receivedMcKey 清除
                scBlockCommand = scOperation.cargoOutbound(key);
                type = 1;
            } else {
                //子车不在母车上，子母车在同一列,层，且不在目标列，层，子车空车上车
                scBlockCommand = scOperation.getOnMc();
                type = 3;
                OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
            }
        }
//        if (scBlockStatus.isRGV() && line.equals(mlBlockStatus.getLine())) {
//
//        } else if (!scBlockStatus.isRGV() && line.equals(scBlockStatus.getLine())) {
//            //子车在目标列，子车出库 取得货物，mcKey 赋值 ，receivedMcKey 清除
//            scBlockCommand = scOperation.cargoOutbound(key);
//            type = 1;
//        } else if (!scBlockStatus.isRGV() && mlBlockStatus.getLine().equals(scBlockStatus.getLine()) && !line.equals(scBlockStatus.getLine())) {
//            //子车不在母车上，子母车在同一列，且不在目标列，子车空车上车
//            scBlockCommand = scOperation.getOnMc();
//            type = 3;
//            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
//        } else if (scBlockStatus.isRGV() && !line.equals(mlBlockStatus.getLine())) {
//            //母车不在目标列
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
//            return;
//        } else {
//            LoggerUtil.getLoggerByName(logName).info(blockNo_sc + ",状态不符合执行条件,key:" + key);
//            Thread.sleep(1000);
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_sc, key);
//            return;
//        }
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(scBlockCommand, blockNo_sc, key);
        OpcWrite.instance().writeByBlockCommand(scBlockCommand, blockNo_sc);
        boolean result = scOperation.isFinishWork(scBlockCommand, blockNo_sc);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            if (type == 1) {
                OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_sc);
                opcBlock.setMckey(key);
                opcBlock.setReservedmckey("");
                BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
            }
            if (type == 3 || type == 1) {
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            } else {
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_sc, key);
            }
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_sc, key);
        }
    }
}

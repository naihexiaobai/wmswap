package com.kepware.opc.service.warehouse.move;

import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.block.impl.Mc_BlockServiceImpl;
import com.kepware.opc.service.operation.McOperation;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.service.warehouse.in.Mc_Sc_BlockService_in;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 母车-子车-母车
 *
 * @auther CalmLake
 * @create 2018/4/16  12:57
 */
public class Mc_Sc_Mc_BlockService extends Mc_BlockServiceImpl {

    public Mc_Sc_Mc_BlockService(String blockNo_mc, String blockNo_other, String key) {
        super(blockNo_mc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        McOperation mcOperation = new McOperation(blockNo_mc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_mc);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
//        String fromLine = OpcOrderOperation.getFromLocationLine(key);
        String toLine = OpcOrderOperation.getToLocationLine(key);
        McBlockCommand mcBlockCommand = null;
        if (!mcBlockStatus.getLine().equals(toLine) && mcBlockStatus.isTheCar() && mcBlockStatus.isLoad()) {
            mcBlockCommand = mcOperation.moveToLine(toLine);
        } else if (!mcBlockStatus.isTheCar() && !scBlockStatus.getLine().equals(mcBlockStatus.getLine())) {
            mcBlockCommand = mcOperation.moveToNextBlockScLine(blockNo_other);
        } else if (mcBlockStatus.isTheCar() && scBlockStatus.isRGV() && toLine.equals(mcBlockStatus.getLine()) && mcBlockStatus.isLoad()) {
            //子车在母车上，且母车在目标列，子车载货下车
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, true);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else {
            LoggerUtil.getLoggerByName("commandNull").info("Mc_Sc_Mc_BlockService,无符合条件:" + key);
            Thread.sleep(20);
            withKey();
        }
        if (StringUtils.isEmpty(opcBlock.getReservedmckey())) {
            opcBlock.setReservedmckey(key);
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
        }
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_mc)) {
            withKey();
        }
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, true);
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(mcBlockCommand, blockNo_mc, key);
        OpcWrite.instance().writeByBlockCommand(mcBlockCommand, blockNo_mc);
        boolean result = mcOperation.isFinishWork(mcBlockCommand, blockNo_mc);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_mc, key);
        }
    }

    @Override
    public void withReceivedMcKey() throws Exception {
        McOperation mcOperation = new McOperation(blockNo_mc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_mc);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        String fromLine = OpcOrderOperation.getFromLocationLine(key);
        String toLine = OpcOrderOperation.getToLocationLine(key);
        McBlockCommand mcBlockCommand = null;
        if (!mcBlockStatus.isLoad() && !mcBlockStatus.getLine().equals(fromLine) && mcBlockStatus.isTheCar()) {
            mcBlockCommand = mcOperation.moveToLine(fromLine);
        } else if (mcBlockStatus.isLoad() && !mcBlockStatus.getLine().equals(toLine) && mcBlockStatus.isTheCar()) {
            mcBlockCommand = mcOperation.moveToLine(toLine);
        } else if (!mcBlockStatus.isTheCar() && !scBlockStatus.getLine().equals(mcBlockStatus.getLine())) {
            mcBlockCommand = mcOperation.moveToNextBlockScLine(blockNo_other);
        } else if (!mcBlockStatus.isTheCar() && scBlockStatus.isLoad() && !scBlockStatus.getLine().equals(mcBlockStatus.getLine())) {
            mcBlockCommand = mcOperation.moveToNextBlockScLine(blockNo_other);
        } else if (scBlockStatus.isRGV() && toLine.equals(mcBlockStatus.getLine()) && mcBlockStatus.isLoad() && mcBlockStatus.isTheCar()) {
            //子车在母车上，且母车在目标列，子车载货下车
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else if (!scBlockStatus.isRGV() && fromLine.equals(scBlockStatus.getLine())) {
            //子车在目标列，子车出库 取得货物，mcKey 赋值 ，receivedMcKey 清除
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else if (!scBlockStatus.isRGV() && mcBlockStatus.getLine().equals(scBlockStatus.getLine()) && !fromLine.equals(scBlockStatus.getLine())) {
            //子车不在母车上，子母车在同一列，且不在目标列，子车空车上车
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else if (scBlockStatus.isRGV() && fromLine.equals(mcBlockStatus.getLine())) {
            //子车在母车上，且母车在目标列，子车空车下车
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else {
            LoggerUtil.getLoggerByName("commandNull").info("Mc_Sc_Mc_BlockService,无符合条件:" + key);
            Thread.sleep(20);
            withReceivedMcKey();
        }
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_mc)) {
            withReceivedMcKey();
        }
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, true);
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(mcBlockCommand, blockNo_mc, key);
        OpcWrite.instance().writeByBlockCommand(mcBlockCommand, blockNo_mc);
        boolean result = mcOperation.isFinishWork(mcBlockCommand, blockNo_mc);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_mc, key);
        }
    }
}

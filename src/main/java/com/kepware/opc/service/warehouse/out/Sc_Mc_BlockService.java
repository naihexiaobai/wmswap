package com.kepware.opc.service.warehouse.out;

import com.kepware.opc.dto.command.ScBlockCommand;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Sc_BlockServiceImpl;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.service.operation.ScOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;

import java.util.Date;

/**
 * 子母车,出库
 *
 * @auther CalmLake
 * @create 2018/4/3  17:11
 */
public class Sc_Mc_BlockService extends Sc_BlockServiceImpl {

    public Sc_Mc_BlockService(String blockNo_sc, String blockNo_other, String key) {
        super(blockNo_sc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_sc);
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        ScOperation scOperation = new ScOperation(blockNo_sc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        String line = OpcOrderOperation.getTargetLine(key);
        ScBlockCommand scBlockCommand;
        if (line.equals(mcBlockStatus.getLine()) && scBlockStatus.isLoad() && !scBlockStatus.isRGV() && mcBlockStatus.getLine().equals(scBlockStatus.getLine())) {
            //子车不在母车上，子母车同列,子车载货， 子车载货上车
            scBlockCommand = scOperation.freightCar(blockNo_sc, key);
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
        } else if (!line.equals(mcBlockStatus.getLine()) && scBlockStatus.isLoad() && !scBlockStatus.isRGV() && !mcBlockStatus.getLine().equals(scBlockStatus.getLine())) {
            //让母车来接车
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else {
            return;
        }
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
        int type;
        ScOperation scOperation = new ScOperation(blockNo_sc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_sc);
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        String line = OpcOrderOperation.getTargetLine(key);
        ScBlockCommand scBlockCommand;
        if (scBlockStatus.isRGV() && line.equals(mcBlockStatus.getLine())) {
            //子车在母车上，且母车在目标列，子车空车下车
            scBlockCommand = scOperation.getOffMc(key);
            type = 2;
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
        } else if (!scBlockStatus.isRGV() && line.equals(scBlockStatus.getLine())) {
            //子车在目标列，子车出库 取得货物，mcKey 赋值 ，receivedMcKey 清除
            scBlockCommand = scOperation.cargoOutbound(key);
            type = 1;
        } else if (!scBlockStatus.isRGV() && mcBlockStatus.getLine().equals(scBlockStatus.getLine()) && !line.equals(scBlockStatus.getLine())) {
            //子车不在母车上，子母车在同一列，且不在目标列，子车空车上车
            scBlockCommand = scOperation.getOnMc();
            type = 3;
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
        } else {
            return;
        }
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
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_sc, key);
        }
    }
}

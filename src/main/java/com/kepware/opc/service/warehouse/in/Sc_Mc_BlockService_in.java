package com.kepware.opc.service.warehouse.in;

import com.kepware.opc.dto.command.ScBlockCommand;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Sc_BlockServiceImpl;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.service.operation.ScOperation;
import com.kepware.opc.service.operation.StationOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.LoggerUtil;

import java.util.Date;

/**
 * 子车--母车--入库
 *
 * @auther CalmLake
 * @create 2018/4/10  10:24
 */
public class Sc_Mc_BlockService_in extends Sc_BlockServiceImpl {

    int countNum = 0;

    public Sc_Mc_BlockService_in(String blockNo_sc, String blockNo_other, String key) {
        super(blockNo_sc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {

    }

    @Override
    public void withReceivedMcKey() throws Exception {
        int type = 0;
        ScOperation scOperation = new ScOperation(blockNo_sc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_sc);
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        String line = OpcOrderOperation.getTargetLine(key);
        ScBlockCommand scBlockCommand = null;
        if (scBlockStatus.isRGV() && line.equals(mcBlockStatus.getLine()) && mcBlockStatus.isLoad()) {
            //子车在母车上，且母车在目标列，子车载货下车
            scBlockCommand = scOperation.carryingOutOfTheCar(blockNo_other, key);
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
            type = 1;
        } else if (!mcBlockStatus.isTheCar() && mcBlockStatus.getLine().equals(scBlockStatus.getLine())) {
            //子车不在母车上，子母车在同一列，且不在目标列，子车空车上车
            scBlockCommand = scOperation.getOnMc();
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
            type = 2;
        } else {
            //TODO   这是一个凑合使用的办法
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_sc, key);
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
                BlockOperationDBUtil.getInstance().updateBlockMcKeyAndReservedMcKey(blockNo_other, blockNo_sc, key);
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_sc, key);
            } else {
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
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

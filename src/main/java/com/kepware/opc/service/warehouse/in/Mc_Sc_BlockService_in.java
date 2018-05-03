package com.kepware.opc.service.warehouse.in;

import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Mc_BlockServiceImpl;
import com.kepware.opc.service.operation.McOperation;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 母车--子车--入库
 *
 * @auther CalmLake
 * @create 2018/4/10  10:16
 */
public class Mc_Sc_BlockService_in extends Mc_BlockServiceImpl {

    public Mc_Sc_BlockService_in(String blockNo_mc, String blockNo_other, String key) {
        super(blockNo_mc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        McOperation mcOperation = new McOperation(blockNo_mc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_mc);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        String line = OpcOrderOperation.getTargetLine(key);
        McBlockCommand mcBlockCommand = null;
        if (!mcBlockStatus.getLine().equals(line) && mcBlockStatus.isTheCar()) {
            mcBlockCommand = mcOperation.moveToTargetLine(key);
        } else if (!mcBlockStatus.isTheCar() && !scBlockStatus.getLine().equals(mcBlockStatus.getLine())) {
            mcBlockCommand = mcOperation.moveToNextBlockScLine(blockNo_other);
        } else if (mcBlockStatus.isTheCar() && scBlockStatus.isRGV() && line.equals(mcBlockStatus.getLine()) && scBlockStatus.isLoad()) {
            //子车在母车上，且母车在目标列，子车载货下车
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, true);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
            return;
        } else {
            LoggerUtil.getLoggerByName("commandNull").info("Mc_Sc_BlockService_in,无符合条件:" + key);
            Thread.sleep(20);
            withKey();
        }
        if (StringUtils.isEmpty(opcBlock.getReservedmckey())) {
            opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
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
}

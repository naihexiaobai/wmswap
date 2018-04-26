package com.kepware.opc.service.warehouse;

import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Mc_BlockServiceImpl;
import com.kepware.opc.service.operation.McOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.LoggerUtil;

import java.util.Date;

/**
 * 母车输送线
 *
 * @auther CalmLake
 * @create 2018/4/4  10:51
 */
public class Mc_Lf_BlockService extends Mc_BlockServiceImpl {

    public Mc_Lf_BlockService(String blockNo_mc, String blockNo_other, String key) {
        super(blockNo_mc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        McBlockCommand mcBlockCommand;
        McOperation mcOperation = new McOperation(blockNo_mc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        OpcBlock opcBlock_Lf = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        opcBlock_Lf.setReservedmckey(key);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock_Lf);
        mcBlockCommand = mcOperation.transplantingTheUnloading();
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
            OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_mc);
            OpcBlock opcBlock1 = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
            opcBlock.setMckey("");
            opcBlock1.setMckey(key);
            opcBlock1.setReservedmckey("");
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock1);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_mc, false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_mc, key);
        }
    }

}

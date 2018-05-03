package com.kepware.opc.service.warehouse.in;

import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Lf_BlockServiceImpl;
import com.kepware.opc.service.operation.McOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.LoggerUtil;

import java.util.Date;

/**
 * 输送线--母车
 *
 * @auther CalmLake
 * @create 2018/4/10  9:55
 */
public class Lf_Mc_BlockService extends Lf_BlockServiceImpl {
    public Lf_Mc_BlockService(String blockNo_lf, String blockNo_other, String key) {
        super(blockNo_lf, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        McBlockCommand mcBlockCommand;
        McOperation mcOperation = new McOperation(blockNo_other, blockNo_lf, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        OpcBlock opcBlock_Mc = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        opcBlock_Mc.setReservedmckey(key);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock_Mc);
        mcBlockCommand = mcOperation.transplantingPickUp();
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_other)) {
            withKey();
        }
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(mcBlockCommand, blockNo_other, key);
        OpcWrite.instance().writeByBlockCommand(mcBlockCommand, blockNo_other);
        boolean result = mcOperation.isFinishWork(mcBlockCommand, blockNo_other);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_lf);
            OpcBlock opcBlock1 = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
            opcBlock.setMckey("");
            opcBlock1.setMckey(key);
            opcBlock1.setReservedmckey("");
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock1);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        }
    }
}

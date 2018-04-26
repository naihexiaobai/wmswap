package com.kepware.opc.service.warehouse;

import com.kepware.opc.dto.command.ElBlockCommand;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.El_BlockServiceImpl;
import com.kepware.opc.service.operation.ElOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;

import java.util.Date;

/**
 * 固定提升机 输送线
 *
 * @auther CalmLake
 * @create 2018/4/4  14:45
 */
public class El_Lf_BlockService extends El_BlockServiceImpl {

    public El_Lf_BlockService(String blockNo_el, String blockNo_other, String key) {
        super(blockNo_el, blockNo_other, key);
    }


    @Override
    public void withKey() throws Exception {
        ElBlockCommand elBlockCommand;
        ElOperation elOperation = new ElOperation(blockNo_el, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        OpcBlock opcBlock_lf = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        opcBlock_lf.setReservedmckey(key);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock_lf);
        elBlockCommand = elOperation.transplantingTheUnloading(blockNo_el, blockNo_other);
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_other)){
            withKey();
        }
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_el,true);
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(elBlockCommand, blockNo_other, key);
        OpcWrite.instance().writeByBlockCommand(elBlockCommand, blockNo_el);
        boolean result = elOperation.isFinishWork(elBlockCommand, blockNo_el);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_el,false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            BlockOperationDBUtil.getInstance().updateBlockMcKeyAndReservedMcKey(blockNo_el, blockNo_other, key);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_el,false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_el, key);
        }
    }
}

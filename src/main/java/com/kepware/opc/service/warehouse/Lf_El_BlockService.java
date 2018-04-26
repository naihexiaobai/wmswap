package com.kepware.opc.service.warehouse;

import com.kepware.opc.dto.command.ElBlockCommand;
import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.dto.status.LBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.impl.Lf_BlockServiceImpl;
import com.kepware.opc.service.operation.ElOperation;
import com.kepware.opc.service.operation.McOperation;
import com.kepware.opc.service.operation.MlOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;

import java.util.Date;

/**
 * 输送线 固定提升机
 *
 * @auther CalmLake
 * @create 2018/4/4  13:10
 */
public class Lf_El_BlockService extends Lf_BlockServiceImpl {

    public Lf_El_BlockService(String blockNo_lf, String blockNo_other, String key) {
        super(blockNo_lf, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        ElBlockCommand elBlockCommand;
        ElOperation elOperation = new ElOperation(blockNo_lf, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        OpcBlock opcBlock_El = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        opcBlock_El.setReservedmckey(key);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock_El);
        elBlockCommand = elOperation.transplantingPickUp(blockNo_other, blockNo_lf);
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_other)) {
            withKey();
        }
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(elBlockCommand, blockNo_other, key);
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
        OpcWrite.instance().writeByBlockCommand(elBlockCommand, blockNo_other);
        boolean result = elOperation.isFinishWork(elBlockCommand, blockNo_other);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            BlockOperationDBUtil.getInstance().updateBlockMcKeyAndReservedMcKey(blockNo_lf, blockNo_other, key);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_lf, key);
        }
    }
}

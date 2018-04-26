package com.kepware.opc.service.warehouse;

import com.kepware.opc.dto.command.McBlockCommand;
import com.kepware.opc.dto.command.MlBlockCommand;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Ml_BlockServiceImpl;
import com.kepware.opc.service.operation.McOperation;
import com.kepware.opc.service.operation.MlOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.service.operation.StationOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;

import java.util.Date;

/**
 * 堆垛机-输送线
 *
 * @auther CalmLake
 * @create 2018/4/9  11:52
 */
public class Ml_Lf_BlockService extends Ml_BlockServiceImpl {
    public Ml_Lf_BlockService(String blockNo_ml, String blockNo_other, String key) {
        super(blockNo_ml, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {
        MlBlockCommand mlBlockCommand;
        MlOperation mlOperation = new MlOperation(blockNo_ml, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
//        OpcBlock opcBlock_Lf = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
//        opcBlock_Lf.setReservedmckey(key);
//        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock_Lf);
        mlBlockCommand = mlOperation.transplantingTheUnloading();
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_ml)) {
            withKey();
        }
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, true);
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(mlBlockCommand, blockNo_ml, key);
        OpcWrite.instance().writeByBlockCommand(mlBlockCommand, blockNo_ml);
        boolean result = mlOperation.isFinishWork(mlBlockCommand, blockNo_ml);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_ml);
            opcBlock.setMckey("");
            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
            //直接完成出库
            StationOperation stationOperation = new StationOperation(blockNo_ml, key);
            stationOperation.finishOutPutWork();
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_ml, key);
        }
    }

}

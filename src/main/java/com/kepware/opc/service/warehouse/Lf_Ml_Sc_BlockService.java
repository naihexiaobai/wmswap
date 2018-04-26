package com.kepware.opc.service.warehouse;

import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.block.impl.Ml_BlockServiceImpl;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.warehouse.out.Sc_Ml_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;

/**
 * 输送线-堆垛机-子车
 *
 * @auther CalmLake
 * @create 2018/4/9  11:56
 */
public class Lf_Ml_Sc_BlockService extends Ml_BlockServiceImpl {

    public Lf_Ml_Sc_BlockService(String blockNo_ml, String blockNo_other, String key) {
        super(blockNo_ml, blockNo_other, key);
    }


    @Override
    public void withReceivedMcKey() throws Exception {
        BlockService blockService;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        OpcBlock opcBlock_ml = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_ml);
        String blockNo_LF = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo_ml);
        String blockNo_SC = blockNo_other;
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_ml);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_SC);
        String line = OpcOrderOperation.getTargetLine(opcBlock_ml.getReservedmckey());
        if (line.equals(mlBlockStatus.getLine()) && mlBlockStatus.isTheCar() && scBlockStatus.isRGV()) {
            blockService = new Sc_Ml_BlockService(blockNo_SC, blockNo_ml, opcBlock_ml.getReservedmckey());
            blockService.withReceivedMcKey();
        } else {
            blockService = new Ml_Lf_BlockService(blockNo_ml, blockNo_LF, opcBlock_ml.getMckey());
            blockService.withKey();
        }
    }
}

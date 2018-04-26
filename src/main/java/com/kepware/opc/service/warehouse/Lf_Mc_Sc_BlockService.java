package com.kepware.opc.service.warehouse;

import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.block.impl.Mc_BlockServiceImpl;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.warehouse.out.Sc_Mc_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;

/**
 * 输送线 - 母车 - 子车,当两个任务并存时
 *
 * @auther CalmLake
 * @create 2018/4/8  9:08
 */
public class Lf_Mc_Sc_BlockService extends Mc_BlockServiceImpl {

    public Lf_Mc_Sc_BlockService(String blockNo_mc, String blockNo_other, String key) {
        super(blockNo_mc, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {

    }

    @Override
    public void withReceivedMcKey() throws Exception {
        BlockService blockService;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        OpcBlock opcBlock_mc = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_mc);
        String blockNo_LF = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo_mc);
        String blockNo_SC = blockNo_other;
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_mc);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_SC);
        String line = OpcOrderOperation.getTargetLine(opcBlock_mc.getReservedmckey());
        if (line.equals(mcBlockStatus.getLine()) && mcBlockStatus.isTheCar() && scBlockStatus.isRGV()) {
            blockService = new Sc_Mc_BlockService(blockNo_SC, blockNo_mc, opcBlock_mc.getReservedmckey());
            blockService.withReceivedMcKey();
        } else {
            blockService = new Mc_Lf_BlockService(blockNo_mc, blockNo_LF, opcBlock_mc.getMckey());
            blockService.withKey();
        }
    }
}

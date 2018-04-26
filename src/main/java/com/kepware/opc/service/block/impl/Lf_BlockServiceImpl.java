package com.kepware.opc.service.block.impl;

import com.kepware.opc.dto.status.LBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.warehouse.Lf_El_BlockService;
import com.kepware.opc.service.warehouse.in.Lf_Mc_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 输送线
 *
 * @auther CalmLake
 * @create 2018/4/4  10:50
 */
public class Lf_BlockServiceImpl implements BlockService {
    public String blockNo_lf;
    public String blockNo_other;
    public String key;

    public Lf_BlockServiceImpl(String blockNo_lf, String blockNo_other, String key) {
        this.blockNo_lf = blockNo_lf;
        this.blockNo_other = blockNo_other;
        this.key = key;
    }

    @Override
    public void withKey() throws Exception {

        BlockService blockService = null;
        OpcBlock opcBlockNext = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        if (StringUtils.isEmpty(opcBlockNext.getMckey()) && (StringUtils.isEmpty(opcBlockNext.getReservedmckey()) || key.equals(opcBlockNext.getReservedmckey()))) {
            if (blockNo_other.contains("EL")) {
                blockService = new Lf_El_BlockService(blockNo_lf, blockNo_other, key);
            } else if (blockNo_other.contains("MC")) {
                blockService = new Lf_Mc_BlockService(blockNo_lf, blockNo_other, key);
            }
            blockService.withKey();
        } else {
            Thread.sleep(1000);
            withKey();
        }
    }

    @Override
    public void withReceivedMcKey() throws Exception {

    }

}

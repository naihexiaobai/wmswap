package com.kepware.opc.service.block.impl;

import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.warehouse.El_Lf_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 固定提升机
 *
 * @auther CalmLake
 * @create 2018/4/4  13:09
 */
public class El_BlockServiceImpl implements BlockService {
    public String blockNo_el;
    public String blockNo_other;
    public String key;

    public El_BlockServiceImpl(String blockNo_el, String blockNo_other, String key) {
        this.blockNo_el = blockNo_el;
        this.blockNo_other = blockNo_other;
        this.key = key;
    }

    @Override
    public void withKey() throws Exception {
        BlockService blockService = null;
        OpcBlock opcBlockNext = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo_other);
        if (StringUtils.isEmpty(opcBlockNext.getMckey()) && (StringUtils.isEmpty(opcBlockNext.getReservedmckey()) || key.equals(opcBlockNext.getReservedmckey()))) {
            if (blockNo_other.contains("LF")){
                blockService = new El_Lf_BlockService(blockNo_el, blockNo_other, key);
            }
            blockService.withKey();
        } else {
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_el, key);
            Thread.sleep(1500);
            return;
        }
    }

    @Override
    public void withReceivedMcKey() throws Exception {

    }
}

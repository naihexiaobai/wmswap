package com.kepware.opc.service.block.impl;

import com.kepware.opc.service.block.BlockService;

/**
 * 母车
 *
 * @auther CalmLake
 * @create 2018/4/3  16:29
 */
public class Mc_BlockServiceImpl implements BlockService {

    public String blockNo_mc;
    public String blockNo_other;
    public String key;

    public Mc_BlockServiceImpl(String blockNo_mc, String blockNo_other, String key) {
        this.blockNo_mc = blockNo_mc;
        this.blockNo_other = blockNo_other;
        this.key = key;
    }

    @Override
    public void withKey() throws Exception {

    }

    @Override
    public void withReceivedMcKey() throws Exception {

    }


}

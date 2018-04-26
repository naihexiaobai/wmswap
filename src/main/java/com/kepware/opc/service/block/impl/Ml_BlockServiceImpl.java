package com.kepware.opc.service.block.impl;

import com.kepware.opc.service.block.BlockService;

/**
 * 堆垛机
 *
 * @auther CalmLake
 * @create 2018/4/9  11:40
 */
public class Ml_BlockServiceImpl implements BlockService {
    public String blockNo_ml;
    public String blockNo_other;
    public String key;

    public Ml_BlockServiceImpl(String blockNo_ml, String blockNo_other, String key) {
        this.blockNo_ml = blockNo_ml;
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

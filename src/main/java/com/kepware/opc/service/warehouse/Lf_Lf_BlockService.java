package com.kepware.opc.service.warehouse;

import com.kepware.opc.service.block.impl.Lf_BlockServiceImpl;

/**
 * 输送线-输送线
 *
 * @auther CalmLake
 * @create 2018/4/10  9:12
 */
public class Lf_Lf_BlockService extends Lf_BlockServiceImpl {
    public Lf_Lf_BlockService(String blockNo_lf, String blockNo_other, String key) {
        super(blockNo_lf, blockNo_other, key);
    }

    @Override
    public void withKey() throws Exception {

    }
}

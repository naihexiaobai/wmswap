package com.kepware.opc.thread.block;

/**
 * @auther CalmLake
 * @create 2018/4/3  16:25
 */
public class BlockThread implements Runnable {

    public String blockNo;

    public BlockThread(String blockNo) {
        this.blockNo = blockNo;
    }

    @Override
    public void run() {

    }
}

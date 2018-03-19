package com.kepware.opc;

import com.www.util.LoggerUtil;
import com.wap.model.OpcItems;

import java.util.List;

/**
 * 读取数据线程
 *
 * @auther CalmLake
 * @create 2017/11/21  13:53
 */
public class OpcReadThread implements Runnable {

//    private LoggerUtil loggerUtil = new LoggerUtil("OpcReadThread");
    private List<OpcItems> opcItemsList;

    public OpcReadThread(List<OpcItems> opcItemsList) {
        this.opcItemsList = opcItemsList;
    }

    public void run() {
//        loggerUtil.getLoggerLevelInfo().info("读取数据线程进入---");
        OpcServer.getInstance().read(opcItemsList);
    }
}

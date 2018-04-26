package com.thief.wcs.init.servlet;

import com.kepware.opc.dao.OpcBlockMapper;
import com.kepware.opc.dto.status.*;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.thread.block.*;
import com.kepware.opc.thread.monitor.MonitorThread;
import com.kepware.opc.thread.order.OrderCenterThread;
import com.thief.wcs.communication.ConnectionManager;
import com.thief.wcs.communication.thread.consumer.MsgConsumerCenterThread;
import com.thief.wcs.communication.thread.pro.HeartBeatProThread;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.entity.Plc;
import com.www.util.LoggerUtil;
import com.www.util.SpringTool;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InitServlet")
public class InitServlet extends HttpServlet {

    /**
     * 1-wcs，2-opc
     */
    static final int TYPE = 2;

    static PlcMapper plcMapper = (PlcMapper) SpringTool.getBeanByClass(PlcMapper.class);


    @Transactional(rollbackForClassName = {"RuntimeException"})
    public void doConnectManager() {
        List<Plc> plcList = new ArrayList<Plc>();
        try {
            Plc plc = new Plc();
            plcList = plcMapper.selectByPlc(plc);
            OpcDBDataCacheCenter.instance().setPlcList(plcList);
        } catch (RuntimeException ex) {
            LoggerUtil.getLoggerByName("InitServlet").warn("连接数据库出错 " + ex.toString());
        }
        ConnectionManager connectionManager = new ConnectionManager();
        for (Plc plc : plcList) {
            connectionManager.Connect(plc.getPlcname(), plc.getIpaddress(), Integer.valueOf(plc.getPort()));
        }
        Thread thread1 = new Thread(connectionManager);
        thread1.setName("ConnectionManager");
        thread1.start();
    }

    @Override
    public void init() throws ServletException {
        LoggerUtil.getLoggerByName("InitServlet").info("开始初始化服务。");
        if (TYPE == 1) {
            cwInit();
        } else if (TYPE == 2) {
            opcInit();
        } else {
            LoggerUtil.getLoggerByName("InitServletException").info("初始化服务出鬼了！");
        }
        LoggerUtil.getLoggerByName("InitServlet").info("结束初始化服务。");
    }

    private void cwInit() {
        doConnectManager();
        LoggerUtil.getLoggerByName("InitServlet").info("1.*******************************  socket自动连接线程开启  .......");
        Thread threadHeartBeatProThread = new Thread(new HeartBeatProThread());
        threadHeartBeatProThread.start();
        LoggerUtil.getLoggerByName("InitServlet").info("2.*******************************  心跳检测线程开启  .......");
        Thread threadMsgConsumerCenterThread = new Thread(new MsgConsumerCenterThread());
        threadMsgConsumerCenterThread.start();
        LoggerUtil.getLoggerByName("InitServlet").info("3.*******************************  接收消息处理线程开启  .......");
    }


    private void opcInit() {
        MonitorThread monitorThread = new MonitorThread();
        new Thread(monitorThread).start();
        LoggerUtil.getLoggerByName("InitServlet").info("1.*******************************  opc监控线程开启  .......");
        initBlockNoStatus();
        LoggerUtil.getLoggerByName("InitServlet").info("3.*******************************  block状态对象添加map  .......");
        initRoute();
        LoggerUtil.getLoggerByName("InitServlet").info("4.*******************************  路径初始化   .......");
        initBlockDock();
        LoggerUtil.getLoggerByName("InitServlet").info("5.*******************************  设备锁定hashMap默认值初始化   .......");
        initBlockThread();
        LoggerUtil.getLoggerByName("InitServlet").info("6.*******************************  opc处理线程开启  .......");
        new Thread(new OrderCenterThread()).start();
        LoggerUtil.getLoggerByName("InitServlet").info("7.*******************************  opc 订单处理 线程开启  .......");
    }

    private void initBlockList() {
        List<OpcBlock> opcBlockList = OpcDBDataCacheCenter.instance().getOpcBlockList();
        if (opcBlockList.size() > 0) {
        } else {
            OpcDBDataCacheCenter.instance().setOpcBlockList(((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).selectAll());
        }
    }

    private void initBlockNoStatus() {
        List<OpcBlock> opcBlockList = OpcDBDataCacheCenter.instance().getOpcBlockList();
        if (OpcDBDataCacheCenter.getMonitorBlockStatusMap().size() < 1) {
            for (OpcBlock opcBlock : opcBlockList) {
                BlockStatus blockStatus = null;
                if (opcBlock.getBlockno().contains(OpcItem.MACHINE_TYPE_EL)) {
                    blockStatus = new ElBlockStatus(opcBlock.getBlockno());
                } else if (opcBlock.getBlockno().contains(OpcItem.MACHINE_TYPE_LF)) {
                    blockStatus = new LBlockStatus(opcBlock.getBlockno());
                } else if (opcBlock.getBlockno().contains(OpcItem.MACHINE_TYPE_MC)) {
                    blockStatus = new McBlockStatus(opcBlock.getBlockno());
                } else if (opcBlock.getBlockno().contains(OpcItem.MACHINE_TYPE_ML)) {
                    blockStatus = new MlBlockStatus(opcBlock.getBlockno());
                } else if (opcBlock.getBlockno().contains(OpcItem.MACHINE_TYPE_SC)) {
                    blockStatus = new ScBlockStatus(opcBlock.getBlockno());
                }
                OpcDBDataCacheCenter.getMonitorBlockStatusMap().put(opcBlock.getBlockno(), blockStatus);
            }
        }
    }

    private void initBlockThread() {
        try {
            Thread.sleep(30000);
            //等待数据初始化
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<OpcBlock> opcBlockList = OpcDBDataCacheCenter.instance().getOpcBlockList();
        if (opcBlockList.size() > 0) {
        } else {
            OpcDBDataCacheCenter.instance().setOpcBlockList(((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).selectAll());
            opcBlockList = OpcDBDataCacheCenter.instance().getOpcBlockList();
        }
        for (OpcBlock opcBlock : opcBlockList) {
            if (opcBlock.getStatus() != 9) {
                if (opcBlock.getPlcname().contains("LF")) {
                    new Thread(new Lf_BlockThread(opcBlock.getBlockno())).start();
                } else if (opcBlock.getPlcname().contains("SC")) {
                    new Thread(new Sc_BlockThread(opcBlock.getBlockno())).start();
                } else if (opcBlock.getPlcname().contains("ML")) {
                    new Thread(new Ml_BlockThread(opcBlock.getBlockno())).start();
                } else if (opcBlock.getPlcname().contains("MC")) {
                    new Thread(new Mc_BlockThread(opcBlock.getBlockno())).start();
                } else if (opcBlock.getPlcname().contains("EL")) {
                    new Thread(new El_BlockThread(opcBlock.getBlockno())).start();
                }
            }
        }
    }

    public void initRoute() {
        int routeNum = RouteSingleton.getInstance().getRouteHashMap().size();
        int routeSiteNum = RouteSingleton.getInstance().getRouteSiteHashMap().size();
        if (routeNum < 1 || routeSiteNum < 1) {
            RouteSingleton.getInstance().getRouteHashMap().clear();
            RouteSingleton.getInstance().getRouteSiteHashMap().clear();
            RouteSingleton.getInstance().setHashMap();
        }
    }


    private void initBlockDock() {
        List<OpcBlock> opcBlockList = OpcDBDataCacheCenter.instance().getOpcBlockList();
        for (OpcBlock opcBlock : opcBlockList) {
            if (opcBlock.getStatus() != 9) {
                if (opcBlock.getPlcname().contains("ML")) {
                    OpcDBDataCacheCenter.getBlockDock().put(opcBlock.getBlockno(), false);
                } else if (opcBlock.getPlcname().contains("MC")) {
                    OpcDBDataCacheCenter.getBlockDock().put(opcBlock.getBlockno(), false);
                } else if (opcBlock.getPlcname().contains("EL")) {
                    OpcDBDataCacheCenter.getBlockDock().put(opcBlock.getBlockno(), false);
                }
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

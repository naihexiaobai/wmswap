package com.thief.wcs.init.servlet;

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

    static PlcMapper plcMapper = (PlcMapper) SpringTool.getBeanByClass(PlcMapper.class);


    @Transactional(rollbackForClassName = {"RuntimeException"})
    public void doConnectManager() {
        List<Plc> plcList = new ArrayList<Plc>();
        try {
            Plc plc = new Plc();
            plcList = plcMapper.selectByPlc(plc);
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
        doConnectManager();
        LoggerUtil.getLoggerByName("InitServlet").info("1.*******************************  socket自动连接线程开启  .......");
        Thread threadHeartBeatProThread = new Thread(new HeartBeatProThread());
        threadHeartBeatProThread.start();
        LoggerUtil.getLoggerByName("InitServlet").info("2.*******************************  心跳检测线程开启  .......");
        Thread threadMsgConsumerCenterThread=new Thread(new MsgConsumerCenterThread());
        threadMsgConsumerCenterThread.start();
        LoggerUtil.getLoggerByName("InitServlet").info("3.*******************************  接收消息处理线程开启  .......");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

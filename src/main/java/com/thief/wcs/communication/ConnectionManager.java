package com.thief.wcs.communication;

import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.entity.Plc;
import com.www.util.LoggerUtil;
import com.www.util.SpringTool;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;

/**
 * 连接管理
 *
 * @auther CalmLake
 * @create 2018/3/14  13:18
 */
public class ConnectionManager implements Runnable {

    PlcMapper plcMapper = (PlcMapper) SpringTool.getBeanByClass(PlcMapper.class);

    //自动检测连接间隔 /毫秒
    private int Interval = 5000;

    private boolean isAutoConnect = true;

    HashMap<String, PlcConnections> _connectionPool = new HashMap<String, PlcConnections>();

    public ConnectionManager() {
    }

    public void Connect(String name, String ip, int port) {
        PlcConnections conn = null;
        if (_connectionPool.containsKey(name)) {
            conn = _connectionPool.get(name);
        } else {
            conn = new PlcConnections(name, ip, port);
            _connectionPool.put(name, conn);
        }
        if (!conn.IsConnected()) {
            doConnect(conn);
        }

    }

    /**
     * 检测连接，自动重连
     */
    public void run() {
        while (true) {
            try {
                Thread.sleep(Interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName("ConnectionManager").warn("线程休眠错误：" + e.toString());
            }
            if (isAutoConnect) {
                for (PlcConnections conn : _connectionPool.values()) {
                    if (!conn.IsConnected()) {
                        doConnect(conn);
                    }
                }
            }
        }
    }

    public void doConnect(PlcConnections conn) {
        try {
            conn.connect();
            //PLC连接成功
            Plc plc = plcMapper.selectByPlcName(conn.getPlcName());
            plc.setStatus(Plc._STATUS_SUCCESS);
            plc.setLastheartbeat(new Date());
            plcMapper.updateByPrimaryKeySelective(plc);
            LoggerUtil.getLoggerByName("ConnectionManager").info(conn.getPlcName() + ",已成功连接！");
        } catch (CommunicationException e) {
            //PLC连接失败
            LoggerUtil.getLoggerByName("ConnectionManager").warn(conn.getPlcName() + ",连接失败!" + e.getMessage());
            Plc plc = plcMapper.selectByPlcName(conn.getPlcName());
            if (plc.getStatus() == Plc._STATUS_FAILED) {

            } else if (plc.getStatus() == Plc._STATUS_DISABLED) {

            } else {
                plc.setStatus(Plc._STATUS_FAILED);
                plcMapper.updateByPrimaryKeySelective(plc);
            }
        }
    }
}


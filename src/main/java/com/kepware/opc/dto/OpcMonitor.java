package com.kepware.opc.dto;

import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Server;

import java.io.Serializable;

/**
 * 监控
 *
 * @auther CalmLake
 * @create 2018/4/12  15:00
 */
public class OpcMonitor implements Serializable{
    private String plcName;
    private Server server;
    private AccessBase accessBase;
    private boolean monitorStatus;

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public AccessBase getAccessBase() {
        return accessBase;
    }

    public void setAccessBase(AccessBase accessBase) {
        this.accessBase = accessBase;
    }

    public boolean isMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(boolean monitorStatus) {
        this.monitorStatus = monitorStatus;
    }
}

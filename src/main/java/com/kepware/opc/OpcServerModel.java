package com.kepware.opc;

import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Server;

/**
 * opcServer信息
 *
 * @auther CalmLake
 * @create 2017/11/22  14:58
 */
public class OpcServerModel {
    private Server server;
    /**
     * 状态 0-默认，1-启用
     */
    private int status;

    private AccessBase accessBase;

    /**
     * 密匙
     */
    private String key;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AccessBase getAccessBase() {
        return accessBase;
    }

    public void setAccessBase(AccessBase accessBase) {
        this.accessBase = accessBase;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

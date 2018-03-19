package com.thief.wcs.entity;

import java.io.Serializable;
import java.util.Date;

public class Plc implements Serializable {

    public static final Byte _STATUS_DEFAULT = 0;
    public static final Byte _STATUS_DISABLED = 9;
    public static final Byte _STATUS_SUCCESS = 1;
    public static final Byte _STATUS_FAILED = 2;

    private Integer id;

    private String plcname;

    private Byte type;

    private String ipaddress;

    private String port;

    private Date createtime;

    private Date lastheartbeat;

    private Byte status;

    public Plc(Integer id, String plcname, Byte type, String ipaddress, String port, Date createtime, Date lastheartbeat, Byte status) {
        this.id = id;
        this.plcname = plcname;
        this.type = type;
        this.ipaddress = ipaddress;
        this.port = port;
        this.createtime = createtime;
        this.lastheartbeat = lastheartbeat;
        this.status = status;
    }

    public Plc() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlcname() {
        return plcname;
    }

    public void setPlcname(String plcname) {
        this.plcname = plcname == null ? null : plcname.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getLastheartbeat() {
        return lastheartbeat;
    }

    public void setLastheartbeat(Date lastheartbeat) {
        this.lastheartbeat = lastheartbeat;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
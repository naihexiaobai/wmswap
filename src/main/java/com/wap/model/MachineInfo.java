package com.wap.model;

import java.io.Serializable;
import java.util.Date;

public class MachineInfo implements Serializable {
    private Integer id;

    private String name;
    /**
     * 1-子车，2-母车，3-堆垛机，4-升降机
     */
    private Byte type;

    private String ip;

    private Date createtime;

    private Date updatetime;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String remark;

    /**
     * 1-运行，0-禁止    10-锁定，接收但不执行任务（//TODO 待细化）
     */
    private Byte status;

    public MachineInfo(Integer id, String name, Byte type, String ip, Date createtime, Date updatetime, String reserved1, String reserved2, String reserved3, String remark, Byte status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ip = ip;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.remark = remark;
        this.status = status;
    }

    public MachineInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1 == null ? null : reserved1.trim();
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2 == null ? null : reserved2.trim();
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3 == null ? null : reserved3.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
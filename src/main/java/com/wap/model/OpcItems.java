package com.wap.model;

import java.io.Serializable;
import java.util.Date;

public class OpcItems implements Serializable {
    private Integer id;

    private String groups;

    private String item;

    private Integer machineinfoid;

    private Date createtime;

    /**
     * 1-boolean，2-word
     */
    private Byte datatype;

    /**
     * 1-读，2-读写
     */
    private Byte itemtype;

    /**
     * 1-开启，2-监控
     */
    private Byte status;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String remark;

    public OpcItems(Integer id, String groups, String item, Integer machineinfoid, Date createtime, Byte datatype, Byte itemtype, Byte status, String reserved1, String reserved2, String reserved3, String remark) {
        this.id = id;
        this.groups = groups;
        this.item = item;
        this.machineinfoid = machineinfoid;
        this.createtime = createtime;
        this.datatype = datatype;
        this.itemtype = itemtype;
        this.status = status;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.remark = remark;
    }

    public OpcItems() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups == null ? null : groups.trim();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public Integer getMachineinfoid() {
        return machineinfoid;
    }

    public void setMachineinfoid(Integer machineinfoid) {
        this.machineinfoid = machineinfoid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Byte getDatatype() {
        return datatype;
    }

    public void setDatatype(Byte datatype) {
        this.datatype = datatype;
    }

    public Byte getItemtype() {
        return itemtype;
    }

    public void setItemtype(Byte itemtype) {
        this.itemtype = itemtype;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
}
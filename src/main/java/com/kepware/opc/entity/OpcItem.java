package com.kepware.opc.entity;

import java.io.Serializable;
import java.util.Date;

public class OpcItem implements Serializable {

    public static final Byte DATATYPE_BOOLEAN = 1;
    public static final Byte DATATYPE_WORD = 2;

    public static final Byte ITEMTYPE_READ = 1;
    public static final Byte ITEMTYPE_WRITE = 2;

    /**
     * 母车
     */
    public static final String MACHINE_TYPE_MC = "MC";
    /**
     * 堆垛机
     */
    public static final String MACHINE_TYPE_ML = "ML";
    /**
     * 子车
     */
    public static final String MACHINE_TYPE_SC = "SC";
    /**
     * 升降机
     */
    public static final String MACHINE_TYPE_EL = "EL";
    /**
     * 输送线
     */
    public static final String MACHINE_TYPE_LF = "LF";
    private Integer id;

    private String item;

    private Integer machineinfoid;

    private Date createtime;

    private Byte datatype;

    private Byte itemtype;

    private Byte status;

    private String remark;

    private String groups;

    private String chanels;

    public OpcItem(Integer id, String item, Integer machineinfoid, Date createtime, Byte datatype, Byte itemtype, Byte status, String remark, String groups, String chanels) {
        this.id = id;
        this.item = item;
        this.machineinfoid = machineinfoid;
        this.createtime = createtime;
        this.datatype = datatype;
        this.itemtype = itemtype;
        this.status = status;
        this.remark = remark;
        this.groups = groups;
        this.chanels = chanels;
    }

    public OpcItem() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups == null ? null : groups.trim();
    }

    public String getChanels() {
        return chanels;
    }

    public void setChanels(String chanels) {
        this.chanels = chanels == null ? null : chanels.trim();
    }
}
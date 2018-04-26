package com.kepware.opc.entity;

import java.io.Serializable;
import java.util.Date;

public class OpcWcsControlInfo implements Serializable {
    public static final Byte STATUS_NO_WRITE = 1;
    public static final Byte STATUS_WRITE = 2;
    public static final Byte STATUS_SUCCESS = 4;
    /**
     * 写入失败
     */
    public static final Byte STATUS_FAIL = 5;
    /**
     * 故障
     */
    public static final Byte STATUS_ERROR = 6;
    private Integer id;

    private String orderkey;

    private String blockno;

    private Integer machineinfoid;

    private Date endtime;

    private Date createtime;

    private Byte status;

    private String wcstaskno;

    private Byte movementid;

    private Short y;

    private Short x;

    private Short z;

    private String remark;

    public OpcWcsControlInfo(Integer id, String orderkey, String blockno, Integer machineinfoid, Date endtime, Date createtime, Byte status, String wcstaskno, Byte movementid, Short y, Short x, Short z, String remark) {
        this.id = id;
        this.orderkey = orderkey;
        this.blockno = blockno;
        this.machineinfoid = machineinfoid;
        this.endtime = endtime;
        this.createtime = createtime;
        this.status = status;
        this.wcstaskno = wcstaskno;
        this.movementid = movementid;
        this.y = y;
        this.x = x;
        this.z = z;
        this.remark = remark;
    }

    public OpcWcsControlInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderkey() {
        return orderkey;
    }

    public void setOrderkey(String orderkey) {
        this.orderkey = orderkey == null ? null : orderkey.trim();
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno == null ? null : blockno.trim();
    }

    public Integer getMachineinfoid() {
        return machineinfoid;
    }

    public void setMachineinfoid(Integer machineinfoid) {
        this.machineinfoid = machineinfoid;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getWcstaskno() {
        return wcstaskno;
    }

    public void setWcstaskno(String wcstaskno) {
        this.wcstaskno = wcstaskno == null ? null : wcstaskno.trim();
    }

    public Byte getMovementid() {
        return movementid;
    }

    public void setMovementid(Byte movementid) {
        this.movementid = movementid;
    }

    public Short getY() {
        return y;
    }

    public void setY(Short y) {
        this.y = y;
    }

    public Short getX() {
        return x;
    }

    public void setX(Short x) {
        this.x = x;
    }

    public Short getZ() {
        return z;
    }

    public void setZ(Short z) {
        this.z = z;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public void getXYZ(String location, OpcWcsControlInfo opcWcsControlInfo) {
        String y = location.substring(1, 2);
        String x = location.substring(2, 3);
        String z = location.substring(3, 4);
        opcWcsControlInfo.setY(Short.valueOf(y));
        opcWcsControlInfo.setX(Short.valueOf(x));
        opcWcsControlInfo.setZ(Short.valueOf(z));
    }
}
package com.wap.model;

import java.io.Serializable;
import java.util.Date;

/**
 * wcs下发任务-消息体
 */
public class WCSControlInfo implements Serializable {
    private Integer id;

    /**
     * 设备序号
     */
    private Integer machineinfoid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 0-默认，1-开始执行，2-执行中，3-执行成功，4-失败,5-多次写入失败,6-指令与当前设备状态条件不符合
     */
    private Byte status;

    /**
     * wcs任务码
     */
    private Short wcstaskno;

    /**
     * wcs动作指令
     */
    private Byte movementid;

    private Short y;

    private Short x;

    private Short z;

    private String remark;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    public WCSControlInfo(Integer id, Integer machineinfoid, Date createtime, Byte status, Short wcstaskno, Byte movementid, Short y, Short x, Short z, String remark, String reserved1, String reserved2, String reserved3) {
        this.id = id;
        this.machineinfoid = machineinfoid;
        this.createtime = createtime;
        this.status = status;
        this.wcstaskno = wcstaskno;
        this.movementid = movementid;
        this.y = y;
        this.x = x;
        this.z = z;
        this.remark = remark;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
    }

    public WCSControlInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Short getWcstaskno() {
        return wcstaskno;
    }

    public void setWcstaskno(Short wcstaskno) {
        this.wcstaskno = wcstaskno;
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

    @Override
    public String toString() {
        return "时间-" + getCreatetime() + ",设备序号-" + getMachineinfoid() + ",wcs任务码-" + getWcstaskno() + ",动作指令-" + getMovementid()
                + ",状态-" + getStatus() + ",总任务码-" + getReserved2() + ",优先级-" + getReserved1() + ",x-" + getX()
                + ",y-" + getY();
    }
}
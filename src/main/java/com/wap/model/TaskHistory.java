package com.wap.model;

import java.io.Serializable;
import java.util.Date;

public class TaskHistory implements Serializable {
    private Integer id;

    /**
     * 货物id
     */
    private Integer cargoid;

    /**
     * 起始货位
     */
    private String startpoint;

    /**
     * 终点
     */
    private String endpoint;

    /**
     * 设备名称
     */
    private String machineinfoname;

    /**
     * 设备id
     */
    private Integer machineinfoid;

    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 开始时间
     */
    private Date starttime;
    /**
     * 结束时间
     */
    private Date finshtime;

    /**
     * 1-未开始，2-进行中，3-成功，4-失败
     */
    private Byte status;

    /**
     * 优先级
     */
    private Byte priority;
    /**
     * 总任务码
     */
    private Short wcstaskno;
    /**
     * 动作指令
     */
    private Byte movementid;

    /**
     * 目标层
     */
    private Short y;
    /**
     * 目标列
     */
    private Short x;
    /**
     * 目标排
     */
    private Short z;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String remark;

    public TaskHistory(Integer id, Integer cargoid, String startpoint, String endpoint, String machineinfoname, Integer machineinfoid, Date createtime, Date starttime, Date finshtime, Byte status, Byte priority, Short wcstaskno, Byte movementid, Short y, Short x, Short z, String reserved1, String reserved2, String reserved3, String remark) {
        this.id = id;
        this.cargoid = cargoid;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.machineinfoname = machineinfoname;
        this.machineinfoid = machineinfoid;
        this.createtime = createtime;
        this.starttime = starttime;
        this.finshtime = finshtime;
        this.status = status;
        this.priority = priority;
        this.wcstaskno = wcstaskno;
        this.movementid = movementid;
        this.y = y;
        this.x = x;
        this.z = z;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.remark = remark;
    }

    public TaskHistory() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCargoid() {
        return cargoid;
    }

    public void setCargoid(Integer cargoid) {
        this.cargoid = cargoid;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint == null ? null : startpoint.trim();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint == null ? null : endpoint.trim();
    }

    public String getMachineinfoname() {
        return machineinfoname;
    }

    public void setMachineinfoname(String machineinfoname) {
        this.machineinfoname = machineinfoname == null ? null : machineinfoname.trim();
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

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getFinshtime() {
        return finshtime;
    }

    public void setFinshtime(Date finshtime) {
        this.finshtime = finshtime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getPriority() {
        return priority;
    }

    public void setPriority(Byte priority) {
        this.priority = priority;
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
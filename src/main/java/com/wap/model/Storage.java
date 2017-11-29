package com.wap.model;

import java.io.Serializable;
import java.util.Date;

public class Storage implements Serializable {
    private Integer id;

    /**
     * 库位编码
     */
    private String storageno;

    /**
     * 层
     */
    private Short y;
    /**
     * 列
     */
    private Short x;
    /**
     * 排
     */
    private Short z;

    /**
     * 0-空闲，1-有货，2-禁用，3-障碍物，4-无货位，5-输送线，11-母车巷道
     */
    private Byte status;

    /**
     * 库区
     */
    private String warehousearea;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 使用时间
     */
    private Date usetime;

    /**
     * 使用次数
     */
    private Integer count;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String remark;

    public Storage(Integer id, String storageno, Short y, Short x, Short z, Byte status, String warehousearea, Date createtime, Date usetime, Integer count, String reserved1, String reserved2, String reserved3, String remark) {
        this.id = id;
        this.storageno = storageno;
        this.y = y;
        this.x = x;
        this.z = z;
        this.status = status;
        this.warehousearea = warehousearea;
        this.createtime = createtime;
        this.usetime = usetime;
        this.count = count;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.remark = remark;
    }

    public Storage() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStorageno() {
        return storageno;
    }

    public void setStorageno(String storageno) {
        this.storageno = storageno == null ? null : storageno.trim();
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getWarehousearea() {
        return warehousearea;
    }

    public void setWarehousearea(String warehousearea) {
        this.warehousearea = warehousearea == null ? null : warehousearea.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUsetime() {
        return usetime;
    }

    public void setUsetime(Date usetime) {
        this.usetime = usetime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
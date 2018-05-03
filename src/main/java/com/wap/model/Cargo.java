package com.wap.model;

import java.io.Serializable;
import java.util.Date;

public class Cargo implements Serializable {
    /**
     * 状态 0-默认，1-上架中，2-上架完成，5-下架中，6-下架完成
     */
    public static final Byte STATUS_DEFAULT_ZERO = 0;
    public static final Byte STATUS_INING_ONE = 1;
    public static final Byte STATUS_INFISH_TWO = 2;
    public static final Byte STATUS_OUTTING_FIVE = 5;
    public static final Byte STATUS_DOUTFINISH_SIX = 6;

    private Integer id;

    /**
     * 货位序号
     */
    private String storageid;

    /**
     * 货物名称
     */
    private String name;

    /**
     * 货物名称编码
     */
    private String nameid;

    /**
     * 数量
     */
    private Short number;

    /**
     * 品质
     */
    private String quality;

    /**
     * 批次号
     */
    private String batchno;

    /**
     * 托盘编号
     */
    private String palletno;

    /**
     * 规格
     */
    private String specifiction;

    /**
     * 状态 0-默认，1-上架中，2-上架完成，5-下架中，6-下架完成
     */
    private Byte status;

    /**
     * 生产日期
     */
    private Date productiondate;

    /**
     * 保质期
     */
    private Short shelflife;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String remark;

    public Cargo(Integer id, String storageid, String name, String nameid, Short number, String quality, String batchno, String palletno, String specifiction, Byte status, Date productiondate, Short shelflife, String reserved1, String reserved2, String reserved3, String remark) {
        this.id = id;
        this.storageid = storageid;
        this.name = name;
        this.nameid = nameid;
        this.number = number;
        this.quality = quality;
        this.batchno = batchno;
        this.palletno = palletno;
        this.specifiction = specifiction;
        this.status = status;
        this.productiondate = productiondate;
        this.shelflife = shelflife;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.remark = remark;
    }

    public Cargo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStorageid() {
        return storageid;
    }

    public void setStorageid(String storageid) {
        this.storageid = storageid == null ? null : storageid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameid() {
        return nameid;
    }

    public void setNameid(String nameid) {
        this.nameid = nameid == null ? null : nameid.trim();
    }

    public Short getNumber() {
        return number;
    }

    public void setNumber(Short number) {
        this.number = number;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality == null ? null : quality.trim();
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno == null ? null : batchno.trim();
    }

    public String getPalletno() {
        return palletno;
    }

    public void setPalletno(String palletno) {
        this.palletno = palletno == null ? null : palletno.trim();
    }

    public String getSpecifiction() {
        return specifiction;
    }

    public void setSpecifiction(String specifiction) {
        this.specifiction = specifiction == null ? null : specifiction.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getProductiondate() {
        return productiondate;
    }

    public void setProductiondate(Date productiondate) {
        this.productiondate = productiondate;
    }

    public Short getShelflife() {
        return shelflife;
    }

    public void setShelflife(Short shelflife) {
        this.shelflife = shelflife;
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
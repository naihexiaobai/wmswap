package com.kepware.opc.entity;

import java.io.Serializable;
import java.util.Date;

public class OpcOrder implements Serializable {

    /**
     * 待执行
     */
    public static final Byte STATUS_WAIT = 1;
    /**
     * 开始执行
     */
    public static final Byte STATUS_EXEC = 2;
    /**
     * 失败
     */
    public static final Byte STATUS_FAIL = 3;
    /**
     * 成功
     */
    public static final Byte STATUS_SUCCESS = 4;
    /**
     * 禁止
     */
    public static final Byte STATUS_SUSPEND = 5;

    public static final Byte ORDERTYPE_IN = 1;
    public static final Byte ORDERTYPE_OUT = 2;
    public static final Byte ORDERTYPE_MOVE = 3;

    private Integer id;

    private String orderkey;
    //1-入库，2-出库，3-库内移动
    private Byte ordertype;

    private String barcode;

    private Date createtime;

    private Date starttime;

    private Date endtime;

    private String fromstation;

    private String tostation;

    private String tolocation;

    private String fromlocation;
    //    1-等待，2-执行，3-失败，4-成功,5-暂停
    private Byte status;

    private String errormsg;

    private Integer routeid;

    private String wmsmckey;

    private String remark;

    public OpcOrder(Integer id, String orderkey, Byte ordertype, String barcode, Date createtime, Date starttime, Date endtime, String fromstation, String tostation, String tolocation, String fromlocation, Byte status, String errormsg, Integer routeid, String wmsmckey, String remark) {
        this.id = id;
        this.orderkey = orderkey;
        this.ordertype = ordertype;
        this.barcode = barcode;
        this.createtime = createtime;
        this.starttime = starttime;
        this.endtime = endtime;
        this.fromstation = fromstation;
        this.tostation = tostation;
        this.tolocation = tolocation;
        this.fromlocation = fromlocation;
        this.status = status;
        this.errormsg = errormsg;
        this.routeid = routeid;
        this.wmsmckey = wmsmckey;
        this.remark = remark;
    }

    public OpcOrder() {
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

    public Byte getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(Byte ordertype) {
        this.ordertype = ordertype;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
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

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getFromstation() {
        return fromstation;
    }

    public void setFromstation(String fromstation) {
        this.fromstation = fromstation == null ? null : fromstation.trim();
    }

    public String getTostation() {
        return tostation;
    }

    public void setTostation(String tostation) {
        this.tostation = tostation == null ? null : tostation.trim();
    }

    public String getTolocation() {
        return tolocation;
    }

    public void setTolocation(String tolocation) {
        this.tolocation = tolocation == null ? null : tolocation.trim();
    }

    public String getFromlocation() {
        return fromlocation;
    }

    public void setFromlocation(String fromlocation) {
        this.fromlocation = fromlocation == null ? null : fromlocation.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg == null ? null : errormsg.trim();
    }

    public Integer getRouteid() {
        return routeid;
    }

    public void setRouteid(Integer routeid) {
        this.routeid = routeid;
    }

    public String getWmsmckey() {
        return wmsmckey;
    }

    public void setWmsmckey(String wmsmckey) {
        this.wmsmckey = wmsmckey == null ? null : wmsmckey.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return "ID:" + this.id + ",orderKey:" + this.orderkey + ",orderType:" + this.ordertype + ",barCode:" + this.barcode + ",createTime:" + this.createtime +
                ",startTime:" + this.starttime + ",endTime:" + this.endtime + ",fromStation:" + this.fromstation + ",toStation:" + this.tostation + ",toLocation:" + this.tolocation + ",fromlocation:" + this.fromlocation +
                ",status:" + this.status + ",errorMsg:" + this.errormsg + ",routeId:" + this.routeid + ",wmsMcKey:" + this.wmsmckey + ",remark:" + this.remark;
    }

}
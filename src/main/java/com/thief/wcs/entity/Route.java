package com.thief.wcs.entity;

import java.io.Serializable;

public class Route implements Serializable {
    private Integer id;

    private String startblockno;

    private String endblockno;

    private Byte status;

    /**
     * 1-入库，2-出库，3-移库,4-移动归位（回到接货口）
     */
    private Byte type;

    public Route(Integer id, String startblockno, String endblockno, Byte status, Byte type) {
        this.id = id;
        this.startblockno = startblockno;
        this.endblockno = endblockno;
        this.status = status;
        this.type = type;
    }

    public Route() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartblockno() {
        return startblockno;
    }

    public void setStartblockno(String startblockno) {
        this.startblockno = startblockno == null ? null : startblockno.trim();
    }

    public String getEndblockno() {
        return endblockno;
    }

    public void setEndblockno(String endblockno) {
        this.endblockno = endblockno == null ? null : endblockno.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
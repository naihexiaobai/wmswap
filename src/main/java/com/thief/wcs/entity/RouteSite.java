package com.thief.wcs.entity;

import java.io.Serializable;

public class RouteSite implements Serializable{
    private Integer id;

    private Integer routeid;

    private String presentblockno;

    private String nextblockno;

    private Byte status;

    public RouteSite(Integer id, Integer routeid, String presentblockno, String nextblockno, Byte status) {
        this.id = id;
        this.routeid = routeid;
        this.presentblockno = presentblockno;
        this.nextblockno = nextblockno;
        this.status = status;
    }

    public RouteSite() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRouteid() {
        return routeid;
    }

    public void setRouteid(Integer routeid) {
        this.routeid = routeid;
    }

    public String getPresentblockno() {
        return presentblockno;
    }

    public void setPresentblockno(String presentblockno) {
        this.presentblockno = presentblockno == null ? null : presentblockno.trim();
    }

    public String getNextblockno() {
        return nextblockno;
    }

    public void setNextblockno(String nextblockno) {
        this.nextblockno = nextblockno == null ? null : nextblockno.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
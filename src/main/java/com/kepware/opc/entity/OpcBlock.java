package com.kepware.opc.entity;

import java.io.Serializable;

public class OpcBlock implements Serializable{
    private Integer id;

    private String blockno;

    private String mckey;

    private String reservedmckey;

    private Byte status;

    private String plcname;

    private String remark;

    private String line;

    private String tier;

    private String row;

    public OpcBlock(Integer id, String blockno, String mckey, String reservedmckey, Byte status, String plcname, String remark, String line, String tier, String row) {
        this.id = id;
        this.blockno = blockno;
        this.mckey = mckey;
        this.reservedmckey = reservedmckey;
        this.status = status;
        this.plcname = plcname;
        this.remark = remark;
        this.line = line;
        this.tier = tier;
        this.row = row;
    }

    public OpcBlock() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno == null ? null : blockno.trim();
    }

    public String getMckey() {
        return mckey;
    }

    public void setMckey(String mckey) {
        this.mckey = mckey == null ? null : mckey.trim();
    }

    public String getReservedmckey() {
        return reservedmckey;
    }

    public void setReservedmckey(String reservedmckey) {
        this.reservedmckey = reservedmckey == null ? null : reservedmckey.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPlcname() {
        return plcname;
    }

    public void setPlcname(String plcname) {
        this.plcname = plcname == null ? null : plcname.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line == null ? null : line.trim();
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier == null ? null : tier.trim();
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row == null ? null : row.trim();
    }
}
package com.kepware.opc.entity;

public class OpcStation {
    private Integer id;

    private String blockno;

    private Byte type;

    private String x;

    private String y;

    private String z;

    private String remark;

    public OpcStation(Integer id, String blockno, Byte type, String x, String y, String z, String remark) {
        this.id = id;
        this.blockno = blockno;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.remark = remark;
    }

    public OpcStation() {
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x == null ? null : x.trim();
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y == null ? null : y.trim();
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z == null ? null : z.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
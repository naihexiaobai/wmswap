package com.thief.wcs.dto;

public abstract class Message {
    public String IdClassification = "0";

    public abstract void setID(String Id);

    public abstract String getID();

    public abstract String getPlcName();

    public abstract void setPlcName(String plcName);

    public abstract String toString();
}

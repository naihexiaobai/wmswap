package com.kepware.opc.dto.status;

import com.kepware.opc.entity.OpcItem;
import org.openscada.opc.lib.da.Item;

/**
 * @auther CalmLake
 * @create 2018/3/19  14:29
 */
public class Monitor {

    private String key;

    private Item item;

    private OpcItem opcItem;


    public OpcItem getOpcItem() {
        return opcItem;
    }

    public void setOpcItem(OpcItem opcItem) {
        this.opcItem = opcItem;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

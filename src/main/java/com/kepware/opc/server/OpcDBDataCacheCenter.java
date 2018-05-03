package com.kepware.opc.server;

import com.kepware.opc.dto.MsgControl;
import com.kepware.opc.dto.OpcMonitor;
import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.thief.wcs.entity.Plc;
import com.www.util.LoggerUtil;
import org.apache.poi.ss.formula.functions.T;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * db数据缓存中心
 *
 * @auther CalmLake
 * @create 2018/3/21  11:06
 */
public class OpcDBDataCacheCenter {
    private static List<OpcBlock> opcBlockList = new ArrayList<OpcBlock>();
    private static List<Plc> plcList = new ArrayList<Plc>();
    private static HashMap<String, String> monitorData = new HashMap<String, String>();
    private static HashMap<String, BlockStatus> monitorBlockStatusMap = new HashMap<String, BlockStatus>();
    private static HashMap<String, Item> itemWriteMap = new HashMap<String, Item>();
    private List<OpcMonitor> opcMonitorList = new ArrayList<OpcMonitor>();
    private static HashMap<String, BlockingQueue<MsgControl>> msgControlBlockingQueueHashMap = new HashMap<String, BlockingQueue<MsgControl>>();
    /**
     * key-String,blockNo
     * value-String
     */
    private static HashMap<String, BlockingQueue<String>> orderKey = new HashMap<String, BlockingQueue<String>>();
    /**
     * 设备是否锁定,true-是，false-否
     */
    private static HashMap<String, Boolean> blockDock = new HashMap<String, Boolean>();

    public static OpcDBDataCacheCenter instance() {
        return instance;
    }

    private static OpcDBDataCacheCenter instance;

    static {
        instance = new OpcDBDataCacheCenter();
    }

    private OpcDBDataCacheCenter() {
//        super();
    }

    public static List<OpcBlock> getOpcBlockList() {
        return opcBlockList;
    }

    public static void setOpcBlockList(List<OpcBlock> opcBlockList) {
        OpcDBDataCacheCenter.opcBlockList = opcBlockList;
    }

    public static HashMap<String, String> getMonitorData() {
        return monitorData;
    }

    public static void setMonitorData(HashMap<String, String> monitorData) {
        OpcDBDataCacheCenter.monitorData = monitorData;
    }

    public static HashMap<String, BlockStatus> getMonitorBlockStatusMap() {
        return monitorBlockStatusMap;
    }

    public static void setMonitorBlockStatusMap(HashMap<String, BlockStatus> monitorBlockStatusMap) {
        OpcDBDataCacheCenter.monitorBlockStatusMap = monitorBlockStatusMap;
    }

    public static HashMap<String, Item> getItemWriteMap() {
        return itemWriteMap;
    }

    public static void setItemWriteMap(HashMap<String, Item> itemWriteMap) {
        OpcDBDataCacheCenter.itemWriteMap = itemWriteMap;
    }

    public static List<Plc> getPlcList() {
        return plcList;
    }

    public static void setPlcList(List<Plc> plcList) {
        OpcDBDataCacheCenter.plcList = plcList;
    }

    public static HashMap<String, Boolean> getBlockDock() {
        return blockDock;
    }

    public static void setBlockDock(HashMap<String, Boolean> blockDock) {
        OpcDBDataCacheCenter.blockDock = blockDock;
    }

    public void addMsgControl(MsgControl msgControl) {
        String blockNo = msgControl.getControlBlockNo();
        if (!msgControlBlockingQueueHashMap.containsKey(blockNo)) {
            msgControlBlockingQueueHashMap.put(blockNo, new LinkedBlockingQueue<MsgControl>());
        }
        msgControlBlockingQueueHashMap.get(blockNo).add(msgControl);
        LoggerUtil.getLoggerByName("msgControl").info("put --- blockNo:" + blockNo + ",msgControl:" + msgControl.toString());
    }

    public MsgControl getMsgControl(String controlBlockNo) throws InterruptedException {
        if (!msgControlBlockingQueueHashMap.containsKey(controlBlockNo)) {
            msgControlBlockingQueueHashMap.put(controlBlockNo, new LinkedBlockingQueue<MsgControl>());
        }
        MsgControl msgControl = msgControlBlockingQueueHashMap.get(controlBlockNo).take();
        LoggerUtil.getLoggerByName("msgControl").info("get --- blockNo:" + controlBlockNo + ",msgControl:" + msgControl.toString());
        return msgControl;
    }

    public int orderKeySize(String controlBlockNo) {
        return orderKey.get(controlBlockNo).size();
    }


    public void addOrderKey(String controlBlockNo, String key) {
        if (!orderKey.containsKey(controlBlockNo)) {
            orderKey.put(controlBlockNo, new LinkedBlockingQueue<String>());
        }
        orderKey.get(controlBlockNo).add(key);
        LoggerUtil.getLoggerByName("orderKey").info("put --- blockNo:" + controlBlockNo + ",orderKey:" + key);
    }


    public String getOrderKey(String controlBlockNo) throws InterruptedException {
        if (!orderKey.containsKey(controlBlockNo)) {
            orderKey.put(controlBlockNo, new LinkedBlockingQueue<String>());
        }
        String key = orderKey.get(controlBlockNo).take();
        LoggerUtil.getLoggerByName("orderKey").info("get --- blockNo:" + controlBlockNo + ",orderKey:" + key);
        return key;
    }

    public boolean getBlockIsDock(String blockNo) {
        if (!blockDock.containsKey(blockNo)) {
            blockDock.put(blockNo, false);
        }
        return blockDock.get(blockNo);
    }

    public void setBlockDock(String blockNo, boolean dock) {
        blockDock.put(blockNo, dock);
    }

    public List<OpcMonitor> getOpcMonitorList() {
        return opcMonitorList;
    }

    public void setOpcMonitorList(List<OpcMonitor> opcMonitorList) {
        this.opcMonitorList = opcMonitorList;
    }
}

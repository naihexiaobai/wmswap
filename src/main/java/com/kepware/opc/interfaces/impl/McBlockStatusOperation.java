package com.kepware.opc.interfaces.impl;

import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.dto.status.McBlockStatus;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.CalmLakeStringUtil;

/**
 * 母车
 *
 * @auther CalmLake
 * @create 2018/3/21  13:32
 */
public class McBlockStatusOperation implements BlockStatusOperation {
    @Override
    public void addMonitorData(String key, String value, OpcItem opcItem) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        McBlockStatus mcBlockStatus = new McBlockStatus(opcItem.getGroups());
        init(opcItem, mcBlockStatus, value, valueBool);
        OpcDBDataCacheCenter.getMonitorBlockStatusMap().put(opcItem.getGroups(), mcBlockStatus);
    }

    @Override
    public void updateMonitorData(String key, String value, OpcItem opcItem, BlockStatus blockStatus) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        init(opcItem, (McBlockStatus) blockStatus, value, valueBool);
    }

    private void init(OpcItem opcItem, McBlockStatus mcBlockStatus, String value, boolean valueBool) {
        String itemString = opcItem.getItem();
        switch (itemString) {
            case "command":
                mcBlockStatus.setCommand(value);
                break;
            case "targetLine":
                mcBlockStatus.setTargetLine(value);
                break;
            case "taskID":
                mcBlockStatus.setTaskID(value);
                break;
            case "line":
                mcBlockStatus.setLine(value);
                break;
            case "plcTaskID":
                mcBlockStatus.setPlcTaskID(value);
                break;
            case "backUp":
                mcBlockStatus.setBackUp(valueBool);
                break;
            case "goAhead":
                mcBlockStatus.setGoAhead(valueBool);
                break;
            case "theCar":
                mcBlockStatus.setTheCar(valueBool);
                break;
            case "onStandby":
                mcBlockStatus.setOnStandby(valueBool);
                break;
            case "load":
                mcBlockStatus.setLoad(valueBool);
                break;
            case "free":
                mcBlockStatus.setFree(valueBool);
                break;
            case "error":
                mcBlockStatus.setError(valueBool);
                break;
            case "auto":
                mcBlockStatus.setAuto(valueBool);
                break;
            default:
                break;
        }
    }
}

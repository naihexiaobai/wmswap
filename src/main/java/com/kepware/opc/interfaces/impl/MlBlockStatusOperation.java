package com.kepware.opc.interfaces.impl;

import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.CalmLakeStringUtil;

/**
 * 堆垛机
 *
 * @auther CalmLake
 * @create 2018/3/21  13:32
 */
public class MlBlockStatusOperation implements BlockStatusOperation {
    @Override
    public void addMonitorData(String key, String value, OpcItem opcItem) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        MlBlockStatus mlBlockStatus = new MlBlockStatus(opcItem.getGroups());
        init(mlBlockStatus, opcItem, value, valueBool);
        OpcDBDataCacheCenter.getMonitorBlockStatusMap().put(opcItem.getGroups(), mlBlockStatus);
    }

    @Override
    public void updateMonitorData(String key, String value, OpcItem opcItem, BlockStatus blockStatus) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        init((MlBlockStatus) blockStatus, opcItem, value, valueBool);
    }

    private MlBlockStatus init(MlBlockStatus mlBlockStatus, OpcItem opcItem, String value, boolean valueBool) {
        String itemString = opcItem.getItem();
        switch (itemString) {
            case "auto":
                mlBlockStatus.setAuto(valueBool);
                break;
            case "backUp":
                mlBlockStatus.setBackUp(valueBool);
                break;
            case "error":
                mlBlockStatus.setError(valueBool);
                break;
            case "free":
                mlBlockStatus.setFree(valueBool);
                break;
            case "goAhead":
                mlBlockStatus.setGoAhead(valueBool);
                break;
            case "goDown":
                mlBlockStatus.setGoDown(valueBool);
                break;
            case "goUp":
                mlBlockStatus.setGoUp(valueBool);
                break;
            case "load":
                mlBlockStatus.setLoad(valueBool);
                break;
            case "onStandby":
                mlBlockStatus.setOnStandby(valueBool);
                break;
            case "theCar":
                mlBlockStatus.setTheCar(valueBool);
                break;
            case "line":
                mlBlockStatus.setLine(value);
                break;
            case "plcTaskID":
                mlBlockStatus.setPlcTaskID(value);
                break;
            case "tier":
                mlBlockStatus.setTier(value);
                break;
            case "errorCode":
                mlBlockStatus.setErrorCode(value);
                break;
            case "command":
                mlBlockStatus.setCommand(value);
                break;
            case "targetLine":
                mlBlockStatus.setTargetLine(value);
                break;
            case "targetTier":
                mlBlockStatus.setTargetTier(value);
                break;
            case "taskID":
                mlBlockStatus.setTaskID(value);
                break;
            default:
                break;
        }
        return mlBlockStatus;
    }
}

package com.kepware.opc.interfaces.impl;

import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.dto.status.ElBlockStatus;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.CalmLakeStringUtil;

/**
 * 升降机
 *
 * @auther CalmLake
 * @create 2018/3/21  13:31
 */
public class ELBlockStatusOperation implements BlockStatusOperation {
    @Override
    public void addMonitorData(String key, String value, OpcItem opcItem) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        ElBlockStatus elBlockStatus = new ElBlockStatus(opcItem.getGroups());
        init(opcItem, elBlockStatus, value, valueBool);
        OpcDBDataCacheCenter.getMonitorBlockStatusMap().put(opcItem.getGroups(), elBlockStatus);
    }

    @Override
    public void updateMonitorData(String key, String value, OpcItem opcItem, BlockStatus blockStatus) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        init(opcItem, (ElBlockStatus) blockStatus, value, valueBool);
    }

    private void init(OpcItem opcItem, ElBlockStatus elBlockStatus, String value, boolean valueBool) {
        String itemString = opcItem.getItem();
        switch (itemString) {
            case "command":
                elBlockStatus.setCommand(value);
                break;
            case "targetTier":
                elBlockStatus.setTargetTier(value);
                break;
            case "taskID":
                elBlockStatus.setTaskID(value);
                break;
            case "errorCode":
                elBlockStatus.setErrorCode(value);
                break;
            case "plcTaskID":
                elBlockStatus.setPlcTaskID(value);
                break;
            case "tier":
                elBlockStatus.setTier(value);
                break;
            case "theCar":
                elBlockStatus.setTheCar(valueBool);
                break;
            case "reset":
                elBlockStatus.setReset(valueBool);
                break;
            case "onStandby":
                elBlockStatus.setOnStandby(valueBool);
                break;
            case "load":
                elBlockStatus.setLoad(valueBool);
                break;
            case "goUp":
                elBlockStatus.setGoUp(valueBool);
                break;
            case "goDown":
                elBlockStatus.setGoDown(valueBool);
                break;
            case "free":
                elBlockStatus.setFree(valueBool);
                break;
            case "error":
                elBlockStatus.setError(valueBool);
                break;
            case "auto":
                elBlockStatus.setAuto(valueBool);
                break;
            default:
                break;
        }
    }
}

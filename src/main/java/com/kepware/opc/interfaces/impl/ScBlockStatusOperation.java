package com.kepware.opc.interfaces.impl;

import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.CalmLakeStringUtil;

/**
 * 子车
 *
 * @auther CalmLake
 * @create 2018/3/21  13:33
 */
public class ScBlockStatusOperation implements BlockStatusOperation {
    @Override
    public void addMonitorData(String key, String value, OpcItem opcItem) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        ScBlockStatus scBlockStatus = new ScBlockStatus(opcItem.getGroups());
        initBlockStatus(scBlockStatus, opcItem, valueBool, value);
        OpcDBDataCacheCenter.getMonitorBlockStatusMap().put(opcItem.getGroups(), scBlockStatus);
    }

    @Override
    public void updateMonitorData(String key, String value, OpcItem opcItem, BlockStatus blockStatus) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        initBlockStatus((ScBlockStatus) blockStatus, opcItem, valueBool, value);
    }

    private ScBlockStatus initBlockStatus(ScBlockStatus scBlockStatus, OpcItem opcItem, boolean valueBool, String value) {
        String itemString = opcItem.getItem();
        switch (itemString) {
            case "AOrigin":
                scBlockStatus.setAOrigin(valueBool);
                break;
            case "backUp":
                scBlockStatus.setBackUp(valueBool);
                break;
            case "BOrigin":
                scBlockStatus.setBOrigin(valueBool);
                break;
            case "charging":
                scBlockStatus.setCharging(valueBool);
                break;
            case "elevator":
                scBlockStatus.setElevator(valueBool);
                break;
            case "error":
                scBlockStatus.setError(valueBool);
                break;
            case "free":
                scBlockStatus.setFree(valueBool);
                break;
            case "goAhead":
                scBlockStatus.setGoAhead(valueBool);
                break;
            case "goDown":
                scBlockStatus.setGoDown(valueBool);
                break;
            case "jacking":
                scBlockStatus.setJacking(valueBool);
                break;
            case "load":
                scBlockStatus.setLoad(valueBool);
                break;
            case "onStandby":
                scBlockStatus.setOnStandby(valueBool);
                break;
            case "RGV":
                scBlockStatus.setRGV(valueBool);
                break;
            case "auto":
                scBlockStatus.setAuto(valueBool);
                break;
            case "line":
                scBlockStatus.setLine(value);
                break;
            case "row":
                scBlockStatus.setRow(value);
                break;
            case "tier":
                scBlockStatus.setTier(value);
                break;
            case "plcTaskID":
                scBlockStatus.setPlcTaskID(value);
                break;
            case "kWh":
                scBlockStatus.setkWh(value);
                break;
            case "command":
                scBlockStatus.setCommand(value);
                break;
            case "targetLine":
                scBlockStatus.setTargetLine(value);
                break;
            case "targetRow":
                scBlockStatus.setTargetRow(value);
                break;
            case "targetTier":
                scBlockStatus.setTargetTier(value);
                break;
            case "taskID":
                scBlockStatus.setTaskID(value);
                break;
            default:
                break;
        }
        return scBlockStatus;
    }
}

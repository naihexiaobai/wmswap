package com.kepware.opc.interfaces.impl;

import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.dto.status.LBlockStatus;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.CalmLakeStringUtil;

/**
 * 输送线
 *
 * @auther CalmLake
 * @create 2018/3/21  13:32
 */
public class LBlockStatusOperation implements BlockStatusOperation {
    @Override
    public void addMonitorData(String key, String value, OpcItem opcItem) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        LBlockStatus lBlockStatus = new LBlockStatus(opcItem.getGroups());
        init(opcItem, lBlockStatus, valueBool, value);
        OpcDBDataCacheCenter.getMonitorBlockStatusMap().put(opcItem.getGroups(), lBlockStatus);
    }

    @Override
    public void updateMonitorData(String key, String value, OpcItem opcItem, BlockStatus blockStatus) {
        boolean valueBool = false;
        if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
            valueBool = CalmLakeStringUtil.stringToBool(value);
        }
        init(opcItem, (LBlockStatus) blockStatus, valueBool, value);
    }

    private void init(OpcItem opcItem, LBlockStatus lBlockStatus, boolean valueBool, String value) {
        String itemString = opcItem.getItem();
        switch (itemString) {
            case "load":
                lBlockStatus.setLoad(valueBool);
                break;
            case "free":
                lBlockStatus.setFree(valueBool);
                break;
            default:
                break;
        }
    }
}

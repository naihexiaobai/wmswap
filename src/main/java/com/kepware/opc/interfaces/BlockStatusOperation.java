package com.kepware.opc.interfaces;

import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.entity.OpcItem;

/**
 * block数据操作
 *
 * @auther CalmLake
 * @create 2018/3/21  13:29
 */
public interface BlockStatusOperation {

    void addMonitorData(String key, String value, OpcItem opcItem);

    void updateMonitorData(String key, String value, OpcItem opcItem, BlockStatus blockStatus);
}

package com.kepware.opc.dto.status;

import com.www.util.LoggerUtil;

/**
 * 未知blockNo
 *
 * @auther CalmLake
 * @create 2018/3/21  13:46
 */
public class UnKnowBlockStatusException  {


    public UnKnowBlockStatusException(String msg) {
        LoggerUtil.getLoggerByName("UnKnowBlockStatusException").warn(msg);
    }

}

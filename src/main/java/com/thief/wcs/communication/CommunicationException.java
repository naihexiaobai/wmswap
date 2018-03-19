package com.thief.wcs.communication;

import com.www.util.LoggerUtil;
import org.apache.log4j.Logger;

/**
 * 异常
 *
 * @auther CalmLake
 * @create 2018/3/14  11:12
 */
public class CommunicationException extends Exception {

    public CommunicationException() {
        super();
    }

    public CommunicationException(String message) {
        super(message);
        LoggerUtil.getLoggerByName("CommunicationException").warn(message);
    }

}

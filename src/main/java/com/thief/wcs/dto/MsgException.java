package com.thief.wcs.dto;

import com.www.util.LoggerUtil;
import org.apache.log4j.Logger;

public class MsgException extends Exception {

    public MsgException() {
    }

    public MsgException(String message) {
        super(message);
        LoggerUtil.getLoggerByName("MsgException").warn(message);
    }


}

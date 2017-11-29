package com.ren.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by admin on 2017/10/27.
 */
public class LoggerUtil {

    private Logger logger = null;

    public LoggerUtil(String className) {
        logger = Logger.getLogger(className);
        PropertyConfigurator.configure("E:\\文件备份\\ideaPro\\wmswap\\src\\main\\resources\\log4j.properties");
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}

package com.www.util;


import org.apache.log4j.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/10/27.
 */
public class LoggerUtil {

    /**
     * 日志存放目录
     */
    private static String path = "D:\\logs";

    public LoggerUtil() {
    }

    /**
     * 日志名称修改
     *
     * @param filename
     */
    public static void SetLogFileName(String filename) {
        StringBuffer logPath = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHH = new SimpleDateFormat("HH");
        logPath.append(path);          //用户的主目录
        logPath.append("\\" + sdf.format(new Date()));
        logPath.append("\\" + sdfHH.format(new Date()));
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdirs();
        logPath.append("\\" + filename);
        DailyRollingFileAppender appender = (DailyRollingFileAppender) Logger.getRootLogger().getAppender("A1");
        appender.setFile(logPath.toString());//动态地修改这个文件名
        appender.activateOptions();
    }

    public static Logger getLoggerByName(String filename) {
        // 生成新的Logger
        // 如果已經有了一個Logger實例返回現有的
        Logger logger = Logger.getLogger(filename);
        // 清空Appender。特別是不想使用現存實例時一定要初期化
        logger.removeAllAppenders();
        // 設定Logger級別。
        logger.setLevel(Level.DEBUG);
        // 設定是否繼承父Logger。
        // 默認為true。繼承root輸出。
        // 設定false後將不輸出root。
        logger.setAdditivity(false);
        // 生成新的Appender
        FileAppender appender = new RollingFileAppender();
        PatternLayout layout = new PatternLayout();
        // log的输出形式
        String conversionPattern = "%-d{yyyy-MM-dd HH:mm:ss:SSS} [%t:%r] - [%p] [%c{1}:%L] [%M] %m%n";
        layout.setConversionPattern(conversionPattern);
        appender.setLayout(layout);
        // log输出路径
        StringBuffer logPath = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHH = new SimpleDateFormat("HH");
        logPath.append(path);          //用户的主目录
        logPath.append("\\" + sdf.format(new Date()));
        logPath.append("\\" + sdfHH.format(new Date()));
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdirs();
        logPath.append("\\" + filename + ".log");
        appender.setFile(logPath.toString());
        // log的文字码
        appender.setEncoding("UTF-8");
        // true:在已存在log文件后面追加 false:新log覆盖以前的log
        appender.setAppend(true);
        // 适用当前配置
        appender.activateOptions();
        // 将新的Appender加到Logger中
        logger.addAppender(appender);
        return logger;
    }

}

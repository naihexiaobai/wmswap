package com.www.util;

import com.alibaba.druid.pool.DruidDataSource;
import sun.applet.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * druid连接池工具类
 *
 * @auther CalmLake
 * @create 2018/3/14  9:09
 */
public class DruidDBUtil {

//    private LoggerUtil loggerUtil = new LoggerUtil("DruidDBUtil");

    public static DruidDBUtil instance() {
        return instance;
    }

    private static DruidDBUtil instance;

    private DruidDataSource ds = new DruidDataSource();

    static {
        instance = new DruidDBUtil();
    }

    public DruidDBUtil() {
        try {
            init();
//            loggerUtil.getLoggerLevelInfo().info("druid，初始化连接池成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        Properties prop = new Properties();
        prop.load(this.getClass().getResourceAsStream("/DruidJDBC.properties"));
        String driver = prop.getProperty("driver");
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String maxActive = prop.getProperty("maxActive");
        String initialSize = prop.getProperty("initialSize");
        String maxWait = prop.getProperty("maxWait");
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaxActive(Integer.parseInt(maxActive));
        ds.setInitialSize(Integer.valueOf(initialSize));
        ds.setMaxWait(Long.valueOf(maxWait));
    }

    public Connection getConnect() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelInfo().info("获取连接失败！");
            return null;
        } finally {
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DruidDBUtil.instance().getConnect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM machineinfo");
//        DruidDBUtil.instance().loggerUtil.getLoggerLevelInfo().info("总行数：");
    }
}

package com.thief.wcs.communication;

/**
 * 发送消息
 *
 * @auther CalmLake
 * @create 2018/3/14  11:30
 */
public class Sender implements Runnable{
    PlcConnections conn;

    public Sender(PlcConnections conn) {
        this.conn = conn;
    }

    public void run() {
        conn.sendHandler();
    }
}

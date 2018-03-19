package com.thief.wcs.communication;

/**
 * 接受消息
 *
 * @auther CalmLake
 * @create 2018/3/14  11:20
 */
public class Receiver implements Runnable{
    PlcConnections conn;

    public Receiver(PlcConnections conn)
    {
        this.conn = conn;
    }

    public void run()
    {
        conn.receiveHandler();
    }
}

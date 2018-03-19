package com.communication.socket.thread;

import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.www.util.LoggerUtil;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 测试消息处理类
 *
 * @auther CalmLake
 * @create 2017/11/6  13:08
 */
public class TestIPCThread extends IPCThread {
//    private LoggerUtil loggerUtil = new LoggerUtil(TestIPCThread.class.getName());

    public TestIPCThread(SocketInfo socketInfo) {
        super(socketInfo);
    }

    @Override
    public void run() {
        SocketInfo socketInfo = getSocketInfo();
        try {
            while (!socketInfo.getSocket().isConnected()) {
                DataInputStream dataInputStream = new DataInputStream(socketInfo.getSocket().getInputStream());
                byte[] bytes = new byte[512];
                dataInputStream.read(bytes);

//                loggerUtil.getLoggerLevelInfo().info(socketInfo.getName() + ",接收到的数据："+bytes.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (!socketInfo.getSocket().isClosed()) {
                    socketInfo.getSocket().close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                SocketInfoListsSingleton.getInstance().getSocketInfoList().remove(socketInfo);
            }
        }
    }
}

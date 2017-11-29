package com.communication.socket.thread;

import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.ren.util.LoggerUtil;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 测试消息处理类
 *
 * @auther CalmLake
 * @create 2017/11/6  13:08
 */
public class TestIPCThread extends IPCThread {
    private LoggerUtil loggerUtil = new LoggerUtil(TestIPCThread.class.getName());

    public TestIPCThread(SocketInfo socketInfo) {
        super(socketInfo);
    }

    @Override
    public void run() {
        SocketInfo socketInfo = getSocketInfo();
        try {
            while (!socketInfo.getSocket().isClosed()) {
                DataInputStream dataInputStream = new DataInputStream(socketInfo.getSocket().getInputStream());
                String msg = readMsgTest(dataInputStream);
//                int length = dataInputStream.readUnsignedShort();
//                byte[] bytes = new byte[length];
//                dataInputStream.read(bytes);

                loggerUtil.getLogger().info(socketInfo.getName() + ",接收到的数据：" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerUtil.getLogger().warn("测试消息线程异常：" + e.getMessage());
            try {
                if (!socketInfo.getSocket().isClosed()) {
                    socketInfo.getSocket().close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                loggerUtil.getLogger().warn("测试消息线程异常关闭：" + socketInfo.getName());
                SocketInfoListsSingleton.getInstance().getSocketInfoList().remove(socketInfo);
            }
        }
    }
}

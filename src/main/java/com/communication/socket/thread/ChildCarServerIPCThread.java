package com.communication.socket.thread;

import com.communication.socket.data.model.SocketInfo;
import com.www.util.LoggerUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by admin on 2017/10/31.
 * 子车信息处理中心
 */
public class ChildCarServerIPCThread extends IPCThread {

//    private LoggerUtil loggerUtil = new LoggerUtil(ChildCarServerIPCThread.class.getName());

    public ChildCarServerIPCThread(SocketInfo socketInfo) {
        super(socketInfo);
    }

    /**
     * 业务逻辑
     */
    @Override
    public void run() {
        try {
            SocketInfo socketInfo = getSocketInfo();
            Socket socket = socketInfo.getSocket();
            while (socket.isConnected()) {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                //TODO 信息处理
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}

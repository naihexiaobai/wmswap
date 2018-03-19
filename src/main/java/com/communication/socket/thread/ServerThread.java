package com.communication.socket.thread;

import com.communication.socket.connect.CreateConnectAbs;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;
import com.communication.socket.server.CreateServerConnect;
import com.www.util.LoggerUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/27.
 */
public class ServerThread implements Runnable {

    private SocketInitLinkInfo socketInitLinkInfo;
    private SocketInfo socketInfo;
//    private LoggerUtil loggerUtil = new LoggerUtil(ServerThread.class.getName());

    public ServerThread(SocketInfo socketInfo, SocketInitLinkInfo socketInitLinkInfo) {
        this.socketInfo = socketInfo;
        this.socketInitLinkInfo = socketInitLinkInfo;
    }

    public void run() {
        try {
            CreateConnectAbs createConnectAbs = new CreateServerConnect(socketInitLinkInfo.getPort());
            ServerSocket serverSocket = createConnectAbs.getServerSocket();
            socketInfo.setStatus(1);
            socketInfo.setServerSocket(serverSocket);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    socketInfo.setSocket(socket);
//                    loggerUtil.getLoggerLevelInfo().info("端口号:" + socket.getPort() + ",新增连接当前连接总数(含服务器):---" + SocketInfoListsSingleton.getInstance().getSocketInfoList().size());
                    IPCThread ipcThread = new IPCThread(socketInfo, socket);
                    new Thread(ipcThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (serverSocket != null && serverSocket.isClosed()) {
                        break;
                    }
//                    loggerUtil.getLoggerLevelWarn().info("ServerSocket服务器出现异常" + e.getMessage());
                }
            }
//            loggerUtil.getLoggerLevelInfo().info(socketInfo.getName() + ",ServerSocket服务器关闭");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                getSocketInfo().getServerSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public SocketInitLinkInfo getSocketInitLinkInfo() {
        return socketInitLinkInfo;
    }

    public void setSocketInitLinkInfo(SocketInitLinkInfo socketInitLinkInfo) {
        this.socketInitLinkInfo = socketInitLinkInfo;
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public void setSocketInfo(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }
}

package com.communication.socket.thread;

import com.communication.socket.connect.CreateConnectAbs;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInitLinkInfo;
import com.communication.socket.server.CreateServerConnect;
import com.ren.util.LoggerUtil;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/31.
 */
public class ChildCarServerThread extends ServerThread {

    private LoggerUtil loggerUtil = new LoggerUtil(ChildCarServerThread.class.getName());

    public ChildCarServerThread(SocketInfo socketInfo, SocketInitLinkInfo socketInitLinkInfo) {
        super(socketInfo, socketInitLinkInfo);
    }

    @Override
    public void run() {
        try {
            CreateConnectAbs createConnectAbs = new CreateServerConnect(getSocketInitLinkInfo().getPort());
            ServerSocket serverSocket = createConnectAbs.getServerSocket();
            while (true) {
                Socket socket = serverSocket.accept();
                SocketInfo socketInfo = getSocketInfo();
                socketInfo.setStatus(1);
                createConnectAbs.setSocketInfo(socket, serverSocket, socketInfo);
                IPCThread childIPC = new ChildCarServerIPCThread(socketInfo);
                new Thread(childIPC).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

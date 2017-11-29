package com.communication.socket.server;

import com.communication.socket.connect.CreateConnectAbs;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public class CreateServerConnect extends CreateConnectAbs {

    private int port;

    public CreateServerConnect(int port) {
        this.port = port;
    }

    public ServerSocket getServerSocket() throws Exception {
        return new ServerSocket(port);
    }

    public void setSocketInfo(Socket socket, ServerSocket serverSocket, SocketInitLinkInfo socketInitLinkInfo, SocketInfo socketInfo) {
        socketInfo.setServerSocket(serverSocket);
        socketInfo.setSocket(socket);
        SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
    }

    public void setSocketInfo(Socket socket, ServerSocket serverSocket, SocketInfo socketInfo) {
        socketInfo.setServerSocket(serverSocket);
        socketInfo.setSocket(socket);
        SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
    }
}

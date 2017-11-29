package com.communication.socket.client;

import com.communication.socket.connect.CreateConnectAbs;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public class CreateClientConnect extends CreateConnectAbs {

    private String url;

    private int port;

    public CreateClientConnect(String url, int port) {
        this.port = port;
        this.url = url;
    }

    public Socket getSocket() throws IOException {
        return new Socket(url, port);
    }

    public void setSocketInfo(Socket socket, SocketInitLinkInfo socketInitLinkInfo, SocketInfo socketInfo) {
        socketInfo.setSocket(socket);
        SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
    }

    public void setSocketInfo(Socket socket, SocketInfo socketInfo) {
        socketInfo.setSocket(socket);
        SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
    }
}

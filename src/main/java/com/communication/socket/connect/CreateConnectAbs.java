package com.communication.socket.connect;

import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public abstract class CreateConnectAbs extends CreateConnectImp{
    private CreateConnect createConnect;

    public CreateConnectAbs(CreateConnect createConnect) {
        this.createConnect = createConnect;
    }

    protected CreateConnectAbs() {
    }

    public Socket getSocket() throws Exception {
        return createConnect.getSocket();
    }

    public ServerSocket getServerSocket() throws Exception {
        return createConnect.getServerSocket();
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

    public void setSocketInfo(Socket socket, SocketInitLinkInfo socketInitLinkInfo, SocketInfo socketInfo) {
        socketInfo.setSocket(socket);
        SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
    }

    public void setSocketInfo(Socket socket, SocketInfo socketInfo) {
        socketInfo.setSocket(socket);
        SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
    }
}

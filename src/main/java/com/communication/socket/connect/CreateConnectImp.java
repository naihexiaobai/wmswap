package com.communication.socket.connect;

import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public class CreateConnectImp implements CreateConnect {
    public Socket getSocket() throws Exception {
        return new Socket(url, port);
    }

    public ServerSocket getServerSocket() throws Exception {
        return new ServerSocket(port);
    }

    public void close(SocketInfo socketInfo) throws IOException {
        boolean result = false;
        if (socketInfo.getSocket() != null && socketInfo.getSocket().isConnected()) {
            socketInfo.getSocket().close();
            result = true;
        }
        if (socketInfo.getServerSocket() != null && !socketInfo.getServerSocket().isClosed()) {
            socketInfo.getServerSocket().close();
            result = true;
        }
        if (result) {
            SocketInfoListsSingleton.getInstance().getSocketInfoList().remove(socketInfo);
        }
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

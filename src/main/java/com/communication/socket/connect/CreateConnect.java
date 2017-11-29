package com.communication.socket.connect;

import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInitLinkInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public interface CreateConnect {

    int port = 8080;

    String url = "localhost";

    Socket getSocket() throws Exception;

    ServerSocket getServerSocket() throws Exception;

    void close(SocketInfo socketInfo) throws IOException;

    void setSocketInfo(Socket socket, ServerSocket serverSocket, SocketInitLinkInfo socketInitLinkInfo, SocketInfo socketInfo);

    void setSocketInfo(Socket socket, ServerSocket serverSocket, SocketInfo socketInfo);

    void setSocketInfo(Socket socket, SocketInitLinkInfo socketInitLinkInfo, SocketInfo socketInfo);

    void setSocketInfo(Socket socket, SocketInfo socketInfo);
}

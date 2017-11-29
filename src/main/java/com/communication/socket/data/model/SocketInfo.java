package com.communication.socket.data.model;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public class SocketInfo implements Serializable {
    private Socket socket;

    private ServerSocket serverSocket;
    /**
     * 序号
     */
    private int idNum;
    /**
     * 编号
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态 0 未使用 1 使用中 2待关闭 3已关闭
     */
    private int status;
    /**
     * 服务器/客户端   1/0
     */
    private int type;

    /**
     * 描述
     */
    private String description;

    public int getIdNum() {
        return idNum;
    }

    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

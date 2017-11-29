package com.communication.socket.data.model;

/**
 * Created by admin on 2017/10/27.
 */
public class SocketInitLinkInfo {
    private String url;
    /**
     * 端口号
     */
    private int port;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

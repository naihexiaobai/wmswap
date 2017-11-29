package com.communication.socket.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/26.
 */
public class SocketInfoListsSingleton {
    private List<SocketInfo> socketInfoList = null;

    private volatile static SocketInfoListsSingleton instance;

    public static SocketInfoListsSingleton getInstance() {
        if (instance == null) {
            synchronized (SocketInfoListsSingleton.class) {
                if (instance == null) {
                    instance = new SocketInfoListsSingleton();
                }
            }
        }
        return instance;
    }

    private SocketInfoListsSingleton() {
        socketInfoList = new ArrayList<SocketInfo>();
    }

    public List<SocketInfo> getSocketInfoList() {
        return socketInfoList;
    }

    public void setSocketInfoList(List<SocketInfo> socketInfoList) {
        socketInfoList = socketInfoList;
    }
}

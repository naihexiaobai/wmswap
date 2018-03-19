package com.communication.socket.data.model;

import com.alibaba.fastjson.JSONObject;
import com.www.util.LoggerUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/26.
 */
public class SocketInfoListsSingleton {

    public static final String HEARTBEAT = "heartbeat";

//    private LoggerUtil loggerUtil = new LoggerUtil("SocketInfoListsSingleton");

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

    /**
     * 发送消息
     *
     * @param strJson {"machineID":"","data":""}
     */
    public void sendMsg(String strJson) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(strJson);
            int machineID = jsonObject.getIntValue("machineID");
            List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
            for (SocketInfo socketInfo : socketInfoList) {
                if (socketInfo.getIdNum() == machineID && socketInfo.getSocket().isConnected()) {
                    Socket socket = socketInfo.getSocket();
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(strJson);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

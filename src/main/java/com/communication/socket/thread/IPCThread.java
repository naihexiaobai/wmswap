package com.communication.socket.thread;

import com.alibaba.fastjson.JSONObject;
import com.communication.socket.data.model.MsgInfo;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.www.util.LoggerUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Created by admin on 2017/10/27.
 */
public class IPCThread implements Runnable {

    private SocketInfo socketInfo;
    private Socket socket;
//    private LoggerUtil loggerUtil = new LoggerUtil(IPCThread.class.getName());

    public IPCThread(SocketInfo socketInfo, Socket socket) {
        this.socketInfo = socketInfo;
        this.socket = socket;
    }

    public IPCThread(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }

    public void run() {
        boolean result = socket.isConnected();
        while (result) {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                String string = dataInputStream.readUTF();
//                loggerUtil.getLoggerLevelWarn().info(socket.getPort() + ",服务器接收消息--" + string);
                if (SocketInfoListsSingleton.HEARTBEAT.equals(string)) {
                    //TODO   心跳检测
                    dataOutputStream.writeUTF(string);
                } else {
                    JSONObject jsonObject = JSONObject.parseObject(string);
//                {"machineID":"","data":""}
                    int machineID = jsonObject.getIntValue("machineID");
                    for (SocketInfo socketInfo1 : SocketInfoListsSingleton.getInstance().getSocketInfoList()) {
                        if (socketInfo1.getIdNum() == machineID) {
                            dataOutputStream.writeUTF(string);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }


    public String readMsgTest(DataInputStream dataInputStream) {
        byte[] bytes = new byte[1024];
        try {
            dataInputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    /**
     * 读取消息
     *
     * @param dataInputStream
     * @return
     * @throws IOException
     */
    public MsgInfo readMsgInfo(DataInputStream dataInputStream) throws IOException {
        int length = dataInputStream.readUnsignedShort();
//        byte[] bytes = new byte[length];
        byte[] bytesTime = new byte[14];
//        dataInputStream.read(bytes);
        dataInputStream.read(bytesTime);
        String time = new String(bytesTime);

        short msgCountNum = dataInputStream.readShort();
        short msgOrderNum = dataInputStream.readShort();
        short msgCommandType = dataInputStream.readShort();
        short msgCommandData = dataInputStream.readShort();
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setTime(time);
        msgInfo.setMsgCommandData(msgCommandData);
        msgInfo.setMsgCommandType(msgCommandType);
        msgInfo.setMsgOrderNum(msgOrderNum);
        msgInfo.setMsgCountNum(msgCountNum);
//        loggerUtil.getLoggerLevelInfo().info("服务器：" + msgInfo.toString());
        return msgInfo;
    }

    /**
     * 反馈消息
     *
     * @param msgInfo
     * @param dataOutputStream
     * @return
     */
    public boolean writeMsgInfo(MsgInfo msgInfo, DataOutputStream dataOutputStream) {
//        CommandTypeEnum
        //TODO  消息的反馈未做
//        switch (msgInfo.getMsgCommandType()){
//            case HANDSHAKE.getValue():
//        }
        return false;
    }

    /**
     * 处理消息
     *
     * @param msgInfo
     * @return
     */
    public MsgInfo disposeMsg(MsgInfo msgInfo) {
        //TODO  消息的处理未做
        return msgInfo;
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public void setSocketInfo(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }
}

package com.communication.socket.thread;

import com.communication.socket.data.model.MsgInfo;
import com.communication.socket.data.model.SocketInfo;
import com.ren.util.LoggerUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Created by admin on 2017/10/27.
 */
public class IPCThread implements Runnable {

    private SocketInfo socketInfo;
    private LoggerUtil loggerUtil = new LoggerUtil(IPCThread.class.getName());

    public IPCThread(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }

    public void run() {
        Socket socket = socketInfo.getSocket();
        boolean result = socket.isConnected();
        while (result) {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                MsgInfo msgInfoRead = readMsgInfo(dataInputStream);
                MsgInfo msgInfoWrite = disposeMsg(msgInfoRead);
                boolean writeStausBool = writeMsgInfo(msgInfoWrite, dataOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                result = !socket.isClosed();
            }
        }
    }


    public String readMsgTest(DataInputStream dataInputStream) {
        byte[] bytes = new byte[1024];
        try {
            dataInputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            loggerUtil.getLogger().warn("测试接受消息异常："+e.getMessage());
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
        loggerUtil.getLogger().info("服务器：" + msgInfo.toString());
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

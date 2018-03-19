package com.communication.socket.thread;

import com.alibaba.fastjson.JSONObject;
import com.communication.socket.client.CreateClientConnect;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;
import com.www.util.LoggerUtil;
import com.wap.control.Thread.DuiDuoJiWcsInfoIPCThread;
import com.wap.control.Thread.MuCheWcsInfoWork;
import com.wap.control.Thread.ShengJiangJiWcsInfoWork;
import com.wap.control.Thread.ZiCheWcsInfoIPCThread;
import com.wap.model.OpcItemFinalString;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public class ClientThread implements Runnable {

    private SocketInitLinkInfo socketInitLinkInfo;
    private SocketInfo socketInfo;
//    private LoggerUtil loggerUtil = new LoggerUtil(ClientThread.class.getName());
//    public static final int BYTEMASK = 0xff;
//    public static final int SHORTMASK = 0xffff;
//    public static final int BYTESHIFT = 8;

    public ClientThread(SocketInfo socketInfo, SocketInitLinkInfo socketInitLinkInfo) {
        this.socketInfo = socketInfo;
        this.socketInitLinkInfo = socketInitLinkInfo;
    }

    public void run() {
        CreateClientConnect createClientConnect = new CreateClientConnect(socketInitLinkInfo.getUrl(), socketInitLinkInfo.getPort());
        try {
            Socket socket = createClientConnect.getSocket();
            socket.setKeepAlive(true);
            socketInfo.setSocket(socket);
            socketInfo.setStatus(1);
            SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
//            loggerUtil.getLoggerLevelInfo().info("名称---" + socketInfo.getName() + "--！");
            while (true) {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String strJson = dataInputStream.readUTF();
                if (SocketInfoListsSingleton.HEARTBEAT.equals(strJson)) {
                    //TODO   心跳检测
                } else {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(strJson);
                        int machineID = jsonObject.getIntValue("machineID");
                        String data = jsonObject.getString("data");
                        MuCheWcsInfoWork muCheWcsInfoWork=new MuCheWcsInfoWork();
                        if (socketInfo.getIdNum() == OpcItemFinalString.ZICHEYIMACHINEID && machineID == socketInfo.getIdNum()) {
                            //子车一号
                            workChildCar(socketInfo.getIdNum(), data);
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.ZICHEERMACHINEID && machineID == socketInfo.getIdNum()) {
                            //子车二号
                            workChildCar(socketInfo.getIdNum(), data);
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.ZICHESANMACHINEID && machineID == socketInfo.getIdNum()) {
                            //子车三号
                            workChildCar(socketInfo.getIdNum(), data);
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.MUCHEYIMACHINEID && machineID == socketInfo.getIdNum()) {
                            //母车一号
                            muCheWcsInfoWork.doWork(machineID, Integer.valueOf(data));
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.MUCHEERMACHINEID && machineID == socketInfo.getIdNum()) {
                            //母车二号
                            muCheWcsInfoWork.doWork(machineID, Integer.valueOf(data));
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.MUCHESANMACHINEID && machineID == socketInfo.getIdNum()) {
                            //母车三号
                            muCheWcsInfoWork.doWork(machineID, Integer.valueOf(data));
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.DDJMACHINEID && machineID == socketInfo.getIdNum()) {
                            //堆垛机
                            workDuiDuoJiCar(socketInfo.getIdNum(), data);
                        } else if (socketInfo.getIdNum() == OpcItemFinalString.SSJMACHINEID && machineID == socketInfo.getIdNum()) {
                            //升降机
                            muCheWcsInfoWork.doWork(machineID, Integer.valueOf(data));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] arg) {
        String string = "heat";
        System.out.println(string.getBytes().length);
    }

    /**
     * 子车
     *
     * @param machineID-设备ID
     * @param data-执行任务      WCSControlInfo_id
     */
    private void workChildCar(int machineID, String data) {
        new ZiCheWcsInfoIPCThread().doSocket(machineID, Integer.valueOf(data));
    }

    /**
     * 堆垛机
     *
     * @param machineID-设备ID
     * @param data-执行任务      WCSControlInfo_id
     */
    private void workDuiDuoJiCar(int machineID, String data) {
        new DuiDuoJiWcsInfoIPCThread().doWork(machineID, Integer.valueOf(data));
    }
}

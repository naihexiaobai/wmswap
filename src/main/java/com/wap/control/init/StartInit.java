package com.wap.control.init;


import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;
import com.communication.socket.thread.ClientThread;
import com.communication.socket.thread.ServerThread;
import com.kepware.opc.OpcReadThread;
import com.kepware.opc.OpcServer;
import com.www.util.DBUtil;
import com.www.util.LoggerUtil;
import com.wap.model.MachineInfo;
import com.wap.model.OpcItems;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 *
 */
@Service
public class StartInit implements ApplicationListener<ContextRefreshedEvent> {

//    LoggerUtil loggerUtil = new LoggerUtil("StartInit");

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            //只在初始化“根上下文”的时候执行
            if (contextRefreshedEvent.getSource() instanceof XmlWebApplicationContext) {
                if (((XmlWebApplicationContext) contextRefreshedEvent.getSource()).getDisplayName().equals("Root WebApplicationContext")) {

                    //opc监控
//                    startOpc();
//                    loggerUtil.getLogger().info("任务处理准备--");
//                    DuiDuoJiWcsInfoIPCThread duiDuoJiWcsInfoIPCThread = new DuiDuoJiWcsInfoIPCThread();
//                    new Thread(duiDuoJiWcsInfoIPCThread).start();
//                    ZiCheWcsInfoIPCThread ziCheWcsInfoIPCThread = new ZiCheWcsInfoIPCThread();
//                    new Thread(ziCheWcsInfoIPCThread).start();
                    //socket 开启
//                    loggerUtil.getLogger().info("socket Server 开启--");
//                    createServerSocketThread();
                    //socket client 开启
//                    loggerUtil.getLogger().info("socket client 开启--");
//                    createSocketClinet();
                    //socket client 心跳检测 开启
//                    loggerUtil.getLogger().info("socket client 心跳检测 开启--");
//                    HeatBeat heatBeat = new HeatBeat();
//                    new Thread(heatBeat).start();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 开启opc监控
     */
    public void startOpc() {
        OpcItems opcItems = new OpcItems();
        List<OpcItems> opcItemsList = DBUtil.dbUtil.opcItemsMapperImpl.selectByOpcItems(opcItems);
//        loggerUtil.getLoggerLevelInfo().info("获取监控数据--" + opcItemsList.size() + "-条");
        OpcServer.getInstance().read(opcItemsList);
        OpcReadThread opcReadThread = new OpcReadThread(opcItemsList);
        new Thread(opcReadThread).start();
    }

    /**
     * 心跳
     */
    private class HeatBeat implements Runnable {

        private Socket socket;

        public HeatBeat() {
        }

        public HeatBeat(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    for (SocketInfo socketInfo1 : SocketInfoListsSingleton.getInstance().getSocketInfoList()) {
                        DataOutputStream dataOutputStream = new DataOutputStream(socketInfo1.getSocket().getOutputStream());
                        String string = SocketInfoListsSingleton.HEARTBEAT;
                        dataOutputStream.writeUTF(string);
                    }
                    Thread.sleep(5000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建socket servet
     */
    public void createServerSocketThread() {
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.setId("serverSocket");
        socketInfo.setIdNum(9999);
        socketInfo.setStatus(0);
        socketInfo.setDescription("socket服务器");
        socketInfo.setType(1);
        SocketInitLinkInfo socketInitLinkInfo = new SocketInitLinkInfo();
        socketInitLinkInfo.setUrl("localhost");
        socketInitLinkInfo.setPort(1234);
        ServerThread serverThread = new ServerThread(socketInfo, socketInitLinkInfo);
        new Thread(serverThread).start();
    }


    public void createSocketClinet() {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setStatus(Integer.valueOf(1).byteValue());
        List<MachineInfo> machineInfoList = DBUtil.dbUtil.machineInfoMapperImpl.selectByMachineInfo(machineInfo);
        for (MachineInfo machineInfo1 : machineInfoList) {
            SocketInfo socketInfo = new SocketInfo();
            socketInfo.setId(machineInfo1.getIp());
            socketInfo.setName(machineInfo1.getName());
            socketInfo.setIdNum(machineInfo1.getId());
            socketInfo.setStatus(0);
            socketInfo.setDescription("socket客户端");
            socketInfo.setType(0);
            SocketInitLinkInfo socketInitLinkInfo = new SocketInitLinkInfo();
            socketInitLinkInfo.setUrl("localhost");
            socketInitLinkInfo.setPort(1234);
            ClientThread clientThread = new ClientThread(socketInfo, socketInitLinkInfo);
            new Thread(clientThread).start();
        }
    }
}

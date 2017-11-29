package com.communication.socket.thread;

import com.communication.socket.client.CreateClientConnect;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;
import com.ren.util.LoggerUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by admin on 2017/10/26.
 */
public class ClientThread implements Runnable {

    private SocketInitLinkInfo socketInitLinkInfo;
    private SocketInfo socketInfo;
    private LoggerUtil loggerUtil = new LoggerUtil(ClientThread.class.getName());
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
            socketInfo.setSocket(socket);
            SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
            while (true) {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String msg = "C --- " + socketInfo.getName() + " Echo off the sky! ";

//                dataOutputStream.write((msg.getBytes().length >> BYTESHIFT) & BYTEMASK);
//                dataOutputStream.write(msg.getBytes().length & BYTEMASK);

                dataOutputStream.writeShort(msg.getBytes().length);
                dataOutputStream.write(msg.getBytes());

                int length = dataInputStream.readUnsignedShort();
                byte[] bytes = new byte[length];
                dataInputStream.read(bytes);
                loggerUtil.getLogger().info(socketInfo.getName() + ",客户端：  --" + new String(bytes));
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info(socketInfo.getName() + ",客户端：" + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

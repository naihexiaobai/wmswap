package com.communication.socket;

import com.communication.socket.client.CreateClientConnect;
import com.communication.socket.connect.CreateConnectAbs;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;

/**
 * Created by admin on 2017/10/25.
 */
public class Client {
    public static void main(String[] args) throws Exception {
        int count = 0;
        for (int q = 0; q < 10; q++) {
            count++;
            CreateConnectAbs clientCreate = new CreateClientConnect("localhost", 8080);
            SocketInfo socketInfo = new SocketInfo();
            socketInfo.setSocket(clientCreate.getSocket());
            socketInfo.setId("id" + count);
            socketInfo.setName("name" + count);
            socketInfo.setStatus(0);
            SocketInfoListsSingleton.getInstance().getSocketInfoList().add(socketInfo);
            Thread.sleep(5000);
            System.out.println("当前循环次数" + count);
            System.out.println("socketInfoList--数量" + SocketInfoListsSingleton.getInstance().getSocketInfoList().size());
        }

//        Socket socket = new Socket("localhost", 8080);
//        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//        String msg = "Echo off the sky! 2";
//        dataOutputStream.writeInt(msg.getBytes().length);
//        dataOutputStream.write(msg.getBytes());
//        System.out.println(socket.isConnected());
//        System.out.println(socket.isClosed());
//        dataOutputStream.flush();
    }
}

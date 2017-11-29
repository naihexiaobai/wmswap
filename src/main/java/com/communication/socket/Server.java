package com.communication.socket;

import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by admin on 2017/10/25.
 */
public class Server extends Thread{

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器-----:启动！");
        while (true) {
            Socket socket = serverSocket.accept();
            List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
            System.out.println("大小---"+socketInfoList.size());
            for (SocketInfo socketInfo : socketInfoList) {
                System.out.println("id---" + socketInfo.getId());
                System.out.println("id---" + socketInfo.getSocket().isConnected());
            }
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//            int length = dataInputStream.readInt();
//            byte[] bytes = new byte[length];
//            dataInputStream.read(bytes);
//            String received = new String(bytes);
//            System.out.println("服务器 received-----：" + received);
        }
    }

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("服务器-----:启动！");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
            System.out.println("大小---"+socketInfoList.size());
            for (SocketInfo socketInfo : socketInfoList) {
                System.out.println("id---" + socketInfo.getId());
                System.out.println("id---" + socketInfo.getSocket().isConnected());
            }
        }
    }
}

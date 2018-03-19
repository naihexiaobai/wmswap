
import com.thief.wcs.communication.BccGenerator;
import com.thief.wcs.communication.MessageCenterQueue;
import com.thief.wcs.communication.SeqGenerator;
import com.thief.wcs.dto.MessageBuilder;
import com.thief.wcs.dto.MsgException;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @auther CalmLake
 * @create 2018/3/14  14:47
 */
public class SocketTest {

    public static void main(String args[]) {


        for (int i = 2001; i < 2010; i++) {
            Thread thread1 = new Thread(new ServerThread(i));
            thread1.start();
        }
//        try {
//            System.out.println("wait........");
//            Thread.sleep(15000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        for (int i = 2001; i < 2010; i++) {
//            Thread thread1 = new Thread(new ClientThread(i));
//            thread1.start();
//        }

    }

    static class ClientThread implements Runnable {


        int port;

        public ClientThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket("localhost", this.port);
                while (true) {
                    try {
                        System.out.println(port + "-----" + socket.isConnected());
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static class ServerThread implements Runnable {
        int port = 0;

        public ServerThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(this.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("socket-" + port + "-服务器初始化完成!");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(new HeartBeat_10(socket));
                    thread.start();
                    System.out.println("socket-" + port + "-建立连接!" + socket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class HeartBeat_10 implements Runnable {

        private Socket socket;

        public HeartBeat_10(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                if (socket.isConnected() && !socket.isClosed()) {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    StringBuilder dataStr = new StringBuilder();
                    while (true) {
                        byte rcvByte;
                        try {
                            rcvByte = dataInputStream.readByte();
                            if (rcvByte == 2) {
                                dataStr = new StringBuilder();
                            } else if (rcvByte == 3) {
                                System.out.println(socket.getLocalPort() + "接收---" + dataStr.toString() + "。");
                                if (dataStr.length() >= 21) {
                                    String content = dataStr.substring(0, dataStr.length() - 2);
                                    String contentData = dataStr.substring(13, dataStr.length() - 2);
                                    String bcc = dataStr.substring(dataStr.length() - 2, dataStr.length());
                                    String _plcNameS = dataStr.substring(dataStr.length() - 7, dataStr.length() - 3);
                                    if (BccGenerator.IsBccRight(contentData, bcc)) {
                                        try {
                                            MessageBuilder mb = MessageBuilder.Parse(content);
                                            mb.ID = "30";
                                            mb.PlcName = _plcNameS;
                                            mb.McTime = DateFormatUtils.format(new Date(), "HHmmss");
                                            mb.SeqNo = new SeqGenerator().getSeqNoToBeTransmitted();
                                            mb.DataString = contentData.toString();
                                            String bccS = BccGenerator.GetBcc(contentData.toString());
                                            String msgStr = "2" + mb.toString() + bccS + "3";
                                            byte[] bytes = msgStr.getBytes();
                                            bytes[0] = 0x02;
                                            bytes[bytes.length - 1] = 0x03;
                                            dataOutputStream.write(bytes);
                                            System.out.println(socket.getLocalPort() + "发送---" + msgStr.toString() + "。");
                                        } catch (MsgException e) {
                                        }
                                    }
                                }
                                dataStr = new StringBuilder();
                            } else {
                                if (dataStr.length() < 1024) {
                                    dataStr.append((char) rcvByte);
                                }
                            }
                        } catch (IOException ex) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

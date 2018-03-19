import java.io.*;
import java.net.Socket;


/**
 * Created by admin on 2017/10/27.
 */
public class Test {



    private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static void main(String[] args) throws Exception {
        //设备名称
        String machineNOString = "ML04";
        //站台名称
        String stationNameString = "ML04";
        //码头名称
        String wharfNameString = "ML04";
        //cycle命令
        String cycleString = "04";
        //cycleType命令
        String cycleTypeString = "02";
        //commandType命令类型
        String commandTypeString = "03";
        //mcKey 货物唯一标识
        String mcKeyString = "1002";
        //列
        String xString = "05";
        //层
        String yString = "01";
        //排
        String zString = "10";
        //消息序号
        String seqNOString = "1111";
//        byte[] bytes = message03(seqNOString, machineNOString, stationNameString, wharfNameString, cycleString, cycleTypeString, commandTypeString, mcKeyString, xString, yString, zString);

//        byte[] bytes2 = message03("0000",machineNOString, stationNameString, wharfNameString, cycleString, cycleTypeString, commandTypeString, mcKeyString, xString, yString, zString);

//        byte [] bytes=new byte[2];
//        bytes[0]=intToByte(0xff02);
//        bytes[1]=intToByte(0xff03);

        Socket socket = new Socket("192.168.10.145", 2000);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        System.out.println("建立连接：---");
//        dataOutputStream.write(bytes);
//        TestThread testThread = new TestThread(socket);
//        new Thread(testThread).start();
        socket.close();

//        int i = 88888;
//        while (!socket.isClosed()) {
//            System.out.println("开始工作");
//            OutputStream outputStream = socket.getOutputStream();
//            InputStream inputStream = socket.getInputStream();
//            DataInputStream dataInputStream = new DataInputStream(inputStream);
//            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//            byte[] bytes = new byte[10];
//            String msg = "22222222222222222222222222222222222222";
//            i+=8888;
//            dataOutputStream.write(i);
//            dataOutputStream.flush();
//            System.out.println("发送数据完成：---");
//            dataInputStream.read(bytes);
//            System.out.println("接收到的数据：---" + new String(bytes) + ",消息结束");
//            Thread.sleep(10000);
//        }

    }

    public static class TestThread implements Runnable {
        private Socket socket;

        public TestThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                while (socket.isConnected()) {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    byte[] byteWrite = new byte[512];
                    dataInputStream.read(byteWrite);
                    System.out.println(new String(byteWrite).toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

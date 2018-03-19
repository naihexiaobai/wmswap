package com.thief.wcs.communication;

import com.thief.wcs.dao.MessageLogMapper;
import com.thief.wcs.dto.Message;
import com.thief.wcs.dto.MessageBuilder;
import com.thief.wcs.dto.MsgException;
import com.thief.wcs.entity.MessageLog;
import com.www.util.DBUtil;
import com.www.util.LoggerUtil;
import com.www.util.SpringTool;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

/**
 * plc连接
 *
 * @auther CalmLake
 * @create 2018/3/14  11:07
 */
public class PlcConnections {


    private int RECEIVED_BUFFER_SIZE = 10000;
    private int SEND_BUFFER_SIZE = 10000;


    public String getPlcName() {
        return _plcName;
    }

    private String _plcName;
    private String _ip;
    private int _port;
    private Socket _socket;
    DataInputStream _reader;
    DataOutputStream _writer;
    private boolean flag = true;
    SeqGenerator seq = new SeqGenerator();
    Thread _threadSend;
    Thread _threadReceiver;


    public PlcConnections(String plcName, String ip, int port) {
        this._plcName = plcName;
        this._ip = ip;
        this._port = port;
    }

    public void connect() throws CommunicationException {
        if (!IsConnected()) {
            seq.reset();
            SocketAddress address = new InetSocketAddress(_ip, _port);
            _socket = new Socket();
            try {
                _socket.setReceiveBufferSize(RECEIVED_BUFFER_SIZE);
                _socket.setSendBufferSize(SEND_BUFFER_SIZE);
                _socket.connect(address);
                _reader = new DataInputStream(new BufferedInputStream(_socket.getInputStream()));
                _writer = new DataOutputStream(_socket.getOutputStream());
            } catch (IOException e) {
                String errMsg = "连接" + _plcName + "时发生异常 |" + e.getMessage();
                throw new CommunicationException(errMsg);
            }
            _threadReceiver = new Thread(new Receiver(this), "ReceiverThread");
            _threadReceiver.start();

            _threadSend = new Thread(new Sender(this), "SenderThread");
            _threadSend.start();
        }
    }

    public void disconnect() throws CommunicationException {
        if (IsConnected()) {
            try {
                _socket.close();
                _socket = null;
                _reader = null;
                _writer = null;
            } catch (IOException e) {
                String errMsg = "断开" + _plcName + "时发生异常 |" + e.getMessage();
                throw new CommunicationException(errMsg);
            }
            LoggerUtil.getLoggerByName("PlcConnections").info(_plcName + " 断开连接!");
            _threadSend.interrupt();
            _threadReceiver.interrupt();

        }
    }

    public boolean IsConnected() {
        return _socket != null
                && _socket.isConnected()
                && !_socket.isClosed();
    }

    public void send(byte[] bytes) throws CommunicationException {
        if (IsConnected()) {
            if (_writer != null) {
                try {
                    _writer.write(bytes);
                } catch (IOException e) {
                    String errMsg = String.format("发送数据给%1$s时发生错误.\n" +
                                    "%2$s\n" +
                                    e.getMessage(),
                            _plcName, new String(bytes));
                    throw new CommunicationException(errMsg);
                }
            } else {
                throw new CommunicationException("OutStream为Null,无法发送! " + new String(bytes));
            }
        } else {
            throw new CommunicationException("未连接,无法发送! " + new String(bytes));
        }
    }

    public void send(String str) throws CommunicationException {
        send(str.getBytes());
    }

    public void send(Message msg) throws CommunicationException, InterruptedException {
        MessageBuilder mb = new MessageBuilder();
        mb.ID = msg.getID();
        mb.IdClassification = msg.IdClassification;
        mb.McTime = DateFormatUtils.format(new Date(), "HHmmss");
        mb.SeqNo = seq.getSeqNoToBeTransmitted();
        mb.DataString = msg.toString();
        String bcc = BccGenerator.GetBcc(msg.toString());
        String msgStr = "2" + mb.toString() + bcc + "3";
        byte[] bytes = msgStr.getBytes();
        bytes[0] = 0x02;
        bytes[bytes.length - 1] = 0x03;
        send(bytes);
        LoggerUtil.getLoggerByName("socketMessage").info(String.format("[S] [%1$s] [%2$s]", _plcName, msgStr.substring(1, msgStr.length() - 1)));
        try {
            MessageLog log = new MessageLog();
            log.setMsgtype(MessageLog.TYPE_SEND);
            log.setMsg(msgStr.substring(1, msgStr.length() - 1));
            log.setCreatetime(new Date());
            ((MessageLogMapper) SpringTool.getBeanByClass(MessageLogMapper.class)).insertSelective(log);
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.getLoggerByName("PlcConnections").warn("消息插入数据库失败" + e.toString());
        }
    }

    public void receiveHandler() {
        if (_reader != null) {
            byte rcvByte;
            StringBuilder dataStr = new StringBuilder();
            while (flag && IsConnected()) {
                try {
                    rcvByte = _reader.readByte();
                    if (rcvByte == 2) {
                        dataStr = new StringBuilder();
                    } else if (rcvByte == 3) {
                        DoDataStr(dataStr.toString());
                        dataStr = new StringBuilder();
                    } else {
                        if (dataStr.length() < 1024) {
                            dataStr.append((char) rcvByte);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    LoggerUtil.getLoggerByName("PlcConnections").warn(ex.toString());
                    try {
                        disconnect();
                    } catch (CommunicationException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public void sendHandler() {
        if (_writer != null) {
            while (flag && IsConnected()) {
                try {
                    Message msg = MessageCenterQueue.instance().getSendMsg(_plcName);
                    send(msg);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LoggerUtil.getLoggerByName("PlcConnections").warn("SendHandler被中断");
                    break;
                } catch (CommunicationException e) {
                    e.printStackTrace();
                    LoggerUtil.getLoggerByName("PlcConnections").warn(e.toString());
                    try {
                        disconnect();
                    } catch (CommunicationException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void DoDataStr(String dataStr) {
        if (dataStr.isEmpty()) {
            return;
        }
        try {
            MessageLog log = new MessageLog();
            log.setMsgtype(MessageLog.TYPE_RECEIVE);
            log.setMsg(dataStr);
            log.setCreatetime(new Date());
            ((MessageLogMapper) SpringTool.getBeanByClass(MessageLogMapper.class)).insertSelective(log);
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.getLoggerByName("PlcConnections").warn("消息插入数据库失败" + e.toString());
        }
        LoggerUtil.getLoggerByName("socketMessage").info(String.format("[R] [%1$s] [%2$s]", _plcName, dataStr));
        if (dataStr.length() >= 21) {
            String content = dataStr.substring(0, dataStr.length() - 2);
            String contentData = dataStr.substring(13, dataStr.length() - 2);
            String bcc = dataStr.substring(dataStr.length() - 2, dataStr.length());
            if (BccGenerator.IsBccRight(contentData, bcc)) {
                try {
                    MessageBuilder mb = MessageBuilder.Parse(content);
                    mb.PlcName = _plcName;
                    MessageCenterQueue.instance().addReceivedMsg(mb);
                } catch (MsgException e) {
                    LoggerUtil.getLoggerByName("PlcConnections").warn(e.toString());
                }
            }
        }
    }
}

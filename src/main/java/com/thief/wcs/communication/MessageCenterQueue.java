package com.thief.wcs.communication;

import com.thief.wcs.dto.Message;
import com.thief.wcs.dto.MessageBuilder;
import com.www.util.LoggerUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息缓存队列
 *
 * @auther CalmLake
 * @create 2018/3/15  10:27
 */
public class MessageCenterQueue {


    BlockingQueue<MessageBuilder> _receivedMsgQ = new LinkedBlockingQueue<MessageBuilder>();
    HashMap<String, BlockingQueue<Message>> _sendMsgQ = new HashMap<String, BlockingQueue<Message>>();

    public static MessageCenterQueue instance() {
        return instance;
    }

    private static MessageCenterQueue instance;

    static {
        instance = new MessageCenterQueue();
    }

    private MessageCenterQueue() {
        super();
    }

    public void addReceivedMsg(MessageBuilder msg) {
        _receivedMsgQ.add(msg);
        log("PUT", msg.PlcName, msg.ID);
    }

    public void addSendMsg(Message msg) {
        String plcName = msg.getPlcName();
        if (!_sendMsgQ.containsKey(plcName)) {
            _sendMsgQ.put(plcName, new LinkedBlockingQueue<Message>());
        }
        _sendMsgQ.get(plcName).add(msg);
        log("PUT", plcName, msg.getID()
        );
    }

    public MessageBuilder getRceivedMsg() throws InterruptedException {
        MessageBuilder mb = _receivedMsgQ.take();
        log("GET", mb.PlcName, mb.ID);
        return mb;
    }

    public Message getSendMsg(String plcName) throws InterruptedException {
        if (!_sendMsgQ.containsKey(plcName)) {
            _sendMsgQ.put(plcName, new LinkedBlockingQueue<Message>());
        }
        Message msg = _sendMsgQ.get(plcName).take();
        log("GET", plcName, msg.getID());
        return msg;
    }

    private void log(String type, String plcName, String id) {
        String log = String.format("[%1$s] [%2$s] [%3$s]", type, plcName, id);
        LoggerUtil.getLoggerByName("MessageCenterQueue").info(log);
    }
}

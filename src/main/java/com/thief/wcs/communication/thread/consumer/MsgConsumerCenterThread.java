package com.thief.wcs.communication.thread.consumer;

import com.thief.wcs.communication.MessageCenterQueue;
import com.thief.wcs.communication.msg.consumer.MsgConsumer;
import com.thief.wcs.communication.msg.consumer.UnMsgConsumer;
import com.thief.wcs.dto.MessageBuilder;
import com.thief.wcs.dto.MsgException;
import com.www.util.LoggerUtil;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * 消息接收处理线程
 *
 * @auther CalmLake
 * @create 2018/3/16  10:15
 */
public class MsgConsumerCenterThread implements Runnable {
    HashMap<String, MsgConsumer> _procFactory = new HashMap<String, MsgConsumer>();


    private void DoMsg(MessageBuilder msg) throws RemoteException {
        try {
            if (_procFactory.containsKey(msg.ID)) {
                MsgConsumer proc = _procFactory.get(msg.ID);
                proc.Do(msg);
            } else {
                MsgConsumer proc = GetNewIDProcess(msg.ID);
                _procFactory.put(msg.ID, proc);
                proc.Do(msg);
            }
        } catch (MsgException msgEx) {
        }
    }

    public MsgConsumer GetNewIDProcess(String id) {
        String classname = MsgConsumer.class.getPackage().getName() + ".MsgConsumer" + id;
        try {
            Class c = Class.forName(classname);
            Object obj = c.newInstance();
            if (obj instanceof MsgConsumer) {
                MsgConsumer proc = (MsgConsumer) obj;
                return proc;
            } else {
                return new UnMsgConsumer();
            }
        } catch (Exception e) {
            LoggerUtil.getLoggerByName("MsgConsumerCenterThread").warn("转换消息处理类异常：" + e.toString());
        }
        return new UnMsgConsumer();
    }

    public void run() {
        while (true) {
            try {
                MessageBuilder msg = MessageCenterQueue.instance().getRceivedMsg();
                DoMsg(msg);
            } catch (Exception e) {
                LoggerUtil.getLoggerByName("MsgConsumerCenterThread").warn("消息处理线程异常：" + e.toString());
            }
        }
    }
}

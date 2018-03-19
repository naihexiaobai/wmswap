package com.thief.wcs.communication.msg.consumer;

import com.thief.wcs.dto.MessageBuilder;
import com.thief.wcs.dto.MsgException;
import com.www.util.LoggerUtil;

/**
 * 未知消息消费者
 *
 * @auther CalmLake
 * @create 2018/3/16  10:24
 */
public class UnMsgConsumer implements MsgConsumer {

    @Override
    public void Do(MessageBuilder msg) throws MsgException {
        LoggerUtil.getLoggerByName("UnMsgConsumer").warn("未知消息体，" + msg.ID + ",设备名称-" + msg.PlcName + ",消息-" + msg.DataString);
    }
}

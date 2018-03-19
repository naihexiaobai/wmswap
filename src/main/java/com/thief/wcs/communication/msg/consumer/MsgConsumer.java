package com.thief.wcs.communication.msg.consumer;

import com.thief.wcs.dto.MessageBuilder;
import com.thief.wcs.dto.MsgException;

/**
 * 接收消息消费者
 *
 * @auther CalmLake
 * @create 2018/3/16  9:54
 */
public interface MsgConsumer {
    void Do(MessageBuilder msg) throws MsgException;
}

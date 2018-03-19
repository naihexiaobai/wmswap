package com.thief.wcs.communication.msg.consumer;

import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.dto.Message30;
import com.thief.wcs.dto.MessageBuilder;
import com.thief.wcs.dto.MsgException;
import com.thief.wcs.entity.Plc;
import com.www.util.SpringTool;

import java.util.Date;

/**
 * 30,console-->wcs 心跳回复
 *
 * @auther CalmLake
 * @create 2018/3/16  9:57
 */
public class MsgConsumer30 implements MsgConsumer {
    @Override
    public void Do(MessageBuilder msg) throws MsgException {
        Message30 message30 = new Message30(msg.DataString);
        message30.setPlcName(msg.PlcName);
        doMsg(message30);

    }

    public void doMsg(Message30 message30) throws MsgException {
        Plc plc = ((PlcMapper) SpringTool.getBeanByClass(PlcMapper.class)).selectByPlcName(message30.getPlcName());
        plc.setLastheartbeat(new Date());
        ((PlcMapper) SpringTool.getBeanByClass(PlcMapper.class)).updateByPrimaryKey(plc);
    }
}

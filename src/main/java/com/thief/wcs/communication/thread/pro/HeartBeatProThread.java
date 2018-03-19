package com.thief.wcs.communication.thread.pro;

import com.thief.wcs.communication.MessageCenterQueue;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.dto.Message10;
import com.thief.wcs.entity.Plc;
import com.www.util.SpringTool;

import java.util.List;

/**
 * 心跳消息检测线程-10
 *
 * @auther CalmLake
 * @create 2018/3/15  16:07
 */
public class HeartBeatProThread implements Runnable {
    PlcMapper plcMapper = (PlcMapper) SpringTool.getBeanByClass(PlcMapper.class);

    @Override
    public void run() {
        while (true) {
            try {
                Plc _plc = new Plc();
                _plc.setStatus(Plc._STATUS_SUCCESS);
                List<Plc> plcS = plcMapper.selectByPlc(_plc);
                for (Plc plc : plcS) {
                    Message10 message10 = new Message10();
                    message10.ConsoleNo = plc.getPlcname();
                    message10.WcsNo = "1";
                    message10.setPlcName(plc.getPlcname());
                    message10.HeartBeatCycle = "5";
                    MessageCenterQueue.instance().addSendMsg(message10);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

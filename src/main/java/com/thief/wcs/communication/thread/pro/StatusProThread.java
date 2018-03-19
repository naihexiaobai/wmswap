package com.thief.wcs.communication.thread.pro;

import com.thief.wcs.communication.MessageCenterQueue;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.dto.Message06;
import com.thief.wcs.entity.Plc;
import com.www.util.SpringTool;

import java.rmi.Naming;
import java.util.List;

/**
 * 状态监控-06
 *
 * @auther CalmLake
 * @create 2018/3/15  17:21
 */
public class StatusProThread implements Runnable {
    PlcMapper plcMapper = (PlcMapper) SpringTool.getBeanByClass(PlcMapper.class);

    @Override
    public void run() {
        while (true) {
            try {
                Plc _plc = new Plc();
                _plc.setStatus(Plc._STATUS_SUCCESS);
                List<Plc> plcS = plcMapper.selectByPlc(_plc);
                for (Plc plc : plcS) {
                    Message06 message06 = new Message06();
                    message06.setPlcName(plc.getPlcname());
                    message06.Status = "0";
                    MessageCenterQueue.instance().addSendMsg(message06);
                }
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

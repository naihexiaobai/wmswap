package com.wap.control.Thread;

import com.kepware.opc.OpcServer;
import com.ren.util.LoggerUtil;
import com.wap.control.ControlCc;
import com.wap.model.OpcItemFinalString;
import com.wap.model.WCSControlInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 堆垛机处理消息线程
 *
 * @auther CalmLake
 * @create 2017/11/28  16:09
 */
@Controller
@RequestMapping("duiDuoJiWcsInfoIPCThread")
public class DuiDuoJiWcsInfoIPCThread extends ControlCc implements Runnable {

    private LoggerUtil loggerUtil = new LoggerUtil("DuiDuoJiWcsInfoIPCThread");

    public void run() {
        loggerUtil.getLogger().info("堆垛机处理线程开启");
        while (true) {
            try {
                boolean result = duiDuoJiExecuteTask();
                if (result) {
                    WCSControlInfo wcsControlInfo = new WCSControlInfo();
                    wcsControlInfo.setStatus(getByte(0));
                    List<WCSControlInfo> wcsControlInfoList = selectByWCSControlInfo(wcsControlInfo);
                    if (wcsControlInfoList.size() > 0) {
                        WCSControlInfo wcsControlInfo2 = wcsControlInfoList.get(0);
                        byte command = wcsControlInfo2.getMovementid();
                        if (checkStaus(command)) {
                            result = executeWrite(wcsControlInfo2);
                            if (result) {
                                wcsControlInfo2.setStatus(getByte(2));
                                updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                                boolean taskSuccess = duiDuoJiTaskIsSuccess();
                                if (taskSuccess) {
                                    wcsControlInfo2.setStatus(getByte(3));
                                    updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                                }
                            } else {
                                wcsControlInfo2.setStatus(getByte(5));
                                updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                                loggerUtil.getLogger().warn("堆垛机处理线程-多次写入失败请检查" + wcsControlInfo2.toString());
                            }
                        } else {
                            wcsControlInfo2.setStatus(getByte(6));
                            updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                            loggerUtil.getLogger().warn("堆垛机处理线程-指令与设备状态不统一" + wcsControlInfo2.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                loggerUtil.getLogger().warn("堆垛机处理线程-出现异常" + e.getMessage());
            }
        }
    }

    /**
     * wcs->plc  是否写入成功
     *
     * @param wcsControlInfo1
     * @return
     */
    private boolean executeWrite(WCSControlInfo wcsControlInfo1) {
        boolean writeStatus = executeDuiDuoJiCommand(wcsControlInfo1);
//        for (int i = 0; i < 3; i++) {
//        writeStatus = executeDuiDuoJiCommand(wcsControlInfo1);
//            if (writeStatus) {
//                break;
//            } else {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return writeStatus;
    }

    /**
     * 检查当前指令与状态是否符合执行条件
     *
     * @param command-指令
     * @return
     */
    public boolean checkStaus(byte command) {
        boolean result = false;
        if (command == 0) {
            result = true;
        } else if (command == 11 || command == 12 || command == 21 || command == 22) {
            String zaiWu = OpcServer.monitoringMap.get(OpcItemFinalString.DDJZAIWU).toString();
            if (zaiWu.equals("true")) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
}

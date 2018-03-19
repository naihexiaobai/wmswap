package com.wap.control.Thread;

import com.kepware.opc.OpcServer;
import com.www.util.DBUtil;
import com.www.util.LoggerUtil;
import com.wap.control.ControlCc;
import com.wap.model.OpcItemFinalString;
import com.wap.model.WCSControlInfo;

import java.util.List;


/**
 * 堆垛机处理消息线程
 *
 * @auther CalmLake
 * @create 2017/11/28  16:09
 */
public class DuiDuoJiWcsInfoIPCThread extends ControlCc implements Runnable {

//    private LoggerUtil loggerUtil = new LoggerUtil("DuiDuoJiWcsInfoIPCThread");

    public void run() {
        while (true) {
            try {
                boolean result = duiDuoJiExecuteTask();
                if (result) {
                    WCSControlInfo wcsControlInfo = new WCSControlInfo();
                    wcsControlInfo.setStatus(getByte(0));
                    wcsControlInfo.setMachineinfoid(10);
                    List<WCSControlInfo> wcsControlInfoList = selectByWCSControlInfo(wcsControlInfo);
                    if (wcsControlInfoList.size() > 0) {
                        WCSControlInfo wcsControlInfo2 = wcsControlInfoList.get(0);
                        boolean local = checkLocalDDJ(wcsControlInfo2);
                        byte command = wcsControlInfo2.getMovementid();
                        if (local) {
                            wcsControlInfo2.setStatus(getByte(7));
                            updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                        } else {
                            //TODO 锁定有任务时等待
                            if ("false".equals(OpcServer.monitoringMap.get(OpcItemFinalString.DDJLOCK)) && checkStaus(command)) {
                                result = executeWrite(wcsControlInfo2);
                                if (result) {
                                    wcsControlInfo2.setStatus(getByte(2));
                                    updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                                    boolean taskSuccess = duiDuoJiTaskIsSuccess(wcsControlInfo2.getWcstaskno().toString());
                                    if (taskSuccess) {
                                        wcsControlInfo2.setStatus(getByte(3));
                                        updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                                    }
                                } else {
                                    wcsControlInfo2.setStatus(getByte(5));
                                    updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                                }
                            } else {
                                wcsControlInfo2.setStatus(getByte(6));
                                updateWCSControlInfoByPrimaryKey(wcsControlInfo2);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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


    public void doWork(int machineID, int wcsControlInfoID) {
        boolean resultStatus = true;
        while (resultStatus) {
            try {
                boolean result = duiDuoJiExecuteTask();
                if (result) {
                    WCSControlInfo wcsControlInfo = DBUtil.dbUtil.wCSControlInfoMapperImpl.selectByPrimaryKey(wcsControlInfoID);
                    if (wcsControlInfo.getId() > 0) {
                        boolean local = checkLocalDDJ(wcsControlInfo);
                        byte command = wcsControlInfo.getMovementid();
                        if (local) {
                            wcsControlInfo.setStatus(getByte(7));
                            updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                            resultStatus = false;
                        } else {
                            //TODO 锁定有任务时等待
                            if ("false".equals(OpcServer.monitoringMap.get(OpcItemFinalString.DDJLOCK)) && checkStaus(command)) {
                                result = executeWrite(wcsControlInfo);
                                if (result) {
                                    wcsControlInfo.setStatus(getByte(2));
                                    updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                                    boolean taskSuccess = duiDuoJiTaskIsSuccess(wcsControlInfo.getWcstaskno().toString());
                                    if (taskSuccess) {
                                        wcsControlInfo.setStatus(getByte(3));
                                        updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                                        resultStatus = false;
                                    }
                                } else {
                                    wcsControlInfo.setStatus(getByte(5));
                                    updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                                    resultStatus = false;
                                }
                            } else {
                                wcsControlInfo.setStatus(getByte(6));
                                updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                                resultStatus = false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

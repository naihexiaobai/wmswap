package com.wap.control.Thread;

import com.www.util.CalmLakeStringUtil;
import com.www.util.DBUtil;
import com.www.util.LoggerUtil;
import com.wap.control.ControlCc;
import com.wap.model.MachineInfo;
import com.wap.model.WCSControlInfo;

import java.util.List;

/**
 * 子车消息处理线程
 *
 * @auther CalmLake
 * @create 2017/11/28  16:11
 */
public class ZiCheWcsInfoIPCThread extends ControlCc implements Runnable {
//    private LoggerUtil loggerUtil = new LoggerUtil("ZiCheWcsInfoIPCThread");

    private MachineInfo machineInfo;

    public ZiCheWcsInfoIPCThread(MachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }

    public ZiCheWcsInfoIPCThread() {
    }

    public void run() {
//        loggerUtil.getLoggerLevelInfo().info("子车处理消息线程开始");
        //一车一线程
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setType(getByte(1));
        machineInfo.setStatus(getByte(1));
        List<MachineInfo> machineInfoList =  machineInfoMapperImpl.selectByMachineInfo(machineInfo);
        if (machineInfoList.size() < 1) {
        } else {
            for (MachineInfo machineInfo1 : machineInfoList) {
                CarThread carThread = new CarThread(machineInfo1);
                new Thread(carThread).start();
            }
        }

    }

    public void doSocket(int machineID, int wcsControlInfoID) {
        doWork(machineID, wcsControlInfoID);
    }

    /**
     * 检查是否执行完毕
     *
     * @param resultWrite
     * @param resultExecute
     * @param wcsControlInfoMsg
     */
    public void checkTaskSuccess(boolean resultWrite, boolean resultExecute, WCSControlInfo wcsControlInfoMsg, int machineID) {
        //任务完成
        while (resultWrite && !resultExecute) {
            if (childCarTaskNoIsSuccess(machineID)) {
                wcsControlInfoMsg.setStatus(getByte(3));
                int i = wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfoMsg);
                resultExecute = true;
                String targetMachineIdString = wcsControlInfoMsg.getReserved3();
                lockNoMachine(Integer.valueOf(targetMachineIdString));
            }
        }
    }

    /**
     * 内部处理消息类
     */
    public class CarThread implements Runnable {

        private MachineInfo machineInfo;

        public CarThread(MachineInfo machineInfo) {
            this.machineInfo = machineInfo;
        }

        public void run() {
            int machineID = machineInfo.getId();
            while (true) {
                doWork(machineID, 0);
            }
        }
    }

    public void doWork(int machineID, int wcsControlInfoID) {
        WCSControlInfo wcsControlInfo = new WCSControlInfo();
        wcsControlInfo.setStatus(getByte(0));
        wcsControlInfo.setMachineinfoid(machineID);
        try {
            WCSControlInfo wcsControlInfoMsg = null;
            if (wcsControlInfoID > 0) {
                wcsControlInfoMsg =  wCSControlInfoMapperImpl.selectByPrimaryKey(wcsControlInfoID);
            } else {
                wcsControlInfoMsg =  wCSControlInfoMapperImpl.selectOneByWCSControlInfo(wcsControlInfo);
            }
            if (wcsControlInfoMsg != null && wcsControlInfoMsg.getId() > 0) {
                wcsControlInfoMsg.setStatus(getByte(2));
                DBUtil.dbUtil.wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfoMsg);
                byte command = wcsControlInfoMsg.getMovementid();
                boolean resultWrite = false;
                boolean resultExecute = false;
                if (command == 1 || command == 2 || command == 3 || command == 4) {
                    //任务下发
                    while (!resultWrite) {
                        if (childCarTaskNoIsSuccess(machineID) && childCarZaiWuStatusIsSuccess(machineID) && childCarCanWork(machineID)) {
                            resultWrite = executeChildCarCommand(wcsControlInfoMsg);
                        }
                    }
                    //任务完成
                    checkTaskSuccess(resultWrite, resultExecute, wcsControlInfoMsg, machineID);
                }
                //上下车 锁定 设备
                else if (command == 21 || command == 11 || command == 23 || command == 13 || command == 12 || command == 14 || command == 22 || command == 24) {
                    //堆垛机位置  母车位置  以及锁定
                    //目标设备ID
                    String targetMachineIdString = wcsControlInfoMsg.getReserved3();
                    int targetMachineId = 0;
                    if (!CalmLakeStringUtil.stringIsNull(targetMachineIdString)) {
                        targetMachineId = CalmLakeStringUtil.stringToInt(targetMachineIdString);
                    }
                    if (targetMachineId > 0) {
                        //子车 rgv设备位置对比
                        boolean resultCheck = false;
                        while (!resultCheck) {
                            resultCheck = checkLocationSame(wcsControlInfoMsg, targetMachineId);
                        }
                        //任务下发
                        while (resultCheck && !resultWrite) {
                            if (childCarTaskNoIsSuccess(machineID) && childCarCanWork(machineID)) {
                                resultWrite = executeChildCarCommand(wcsControlInfoMsg);
                            }
                        }
                        //任务完成
                        checkTaskSuccess(resultWrite, resultExecute, wcsControlInfoMsg, machineID);
                    }
                } else {
                    //任务下发
                    while (!resultWrite) {
                        if (childCarTaskNoIsSuccess(machineID) && childCarCanWork(machineID)) {
                            resultWrite = executeChildCarCommand(wcsControlInfoMsg);
                        }
                    }
                    checkTaskSuccess(resultWrite, resultExecute, wcsControlInfoMsg, machineID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

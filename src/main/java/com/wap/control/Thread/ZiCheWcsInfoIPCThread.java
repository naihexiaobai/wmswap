package com.wap.control.Thread;

import com.ren.util.LoggerUtil;
import com.wap.control.ControlCc;
import com.wap.model.MachineInfo;
import com.wap.model.WCSControlInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 子车消息处理线程
 *
 * @auther CalmLake
 * @create 2017/11/28  16:11
 */
@Controller
@RequestMapping("ziCheWcsInfoIPCThread")
public class ZiCheWcsInfoIPCThread extends ControlCc implements Runnable {
    private LoggerUtil loggerUtil = new LoggerUtil("ZiCheWcsInfoIPCThread");

    public void run() {
        loggerUtil.getLogger().info("子车处理消息线程开始");
        //一车一线程
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setType(getByte(1));
        machineInfo.setStatus(getByte(1));
        List<MachineInfo> machineInfoList = machineInfoMapperImpl.selectByMachineInfo(machineInfo);
        if (machineInfoList.size() < 1) {
            loggerUtil.getLogger().warn("当前无可用子车");
        } else {
            for (MachineInfo machineInfo1 : machineInfoList) {
                CarThread carThread = new CarThread(machineInfo1);
                new Thread(carThread).start();
            }
        }
        /**
         * 设备表中查取数据
         * 设备ID
         * 各自取自己的任务判断条件依次执行
         */
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
            while (true) {
                WCSControlInfo wcsControlInfo = new WCSControlInfo();
                wcsControlInfo.setStatus(getByte(0));
                wcsControlInfo.setMachineinfoid(machineInfo.getId());
                try {
                    WCSControlInfo wcsControlInfoMsg = wCSControlInfoMapperImpl.selectOneByWCSControlInfo(wcsControlInfo);
                    if (wcsControlInfoMsg != null && wcsControlInfoMsg.getId() > 0) {
                        wcsControlInfoMsg.setStatus(getByte(2));
                        wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfoMsg);
                        byte command = wcsControlInfo.getMovementid();
                        //                       <option value="1">子车A面入货</option>
                        //                                <option value="2">子车A面出货</option>
                        //                                <option value="3">子车B面入货</option>
                        //                                <option value="4">子车B面出货</option>
                        //                                <option value="5">子车A面整仓</option>
                        //                                <option value="6">子车B面整仓</option>
                        //                                <option value="7">子车出母车到输送线B</option>
                        //                                <option value="8">子车上提升机</option>
                        //                                <option value="9">子车出输送机到输送线A</option>
                        //                                <option value="11">子车A面上RGV</option>
                        //                                <option value="12">子车A面下RGV</option>
                        //                                <option value="13">子车B面上RGV</option>
                        //                                <option value="14">子车B面下RGV</option>
                        //                                <option value="15">子车A面盘点</option>
                        //                                <option value="16">子车B面盘点</option>
                        //                                <option value="17">子车A-B面切换</option>
                        //                                <option value="18">子车B-A面切换</option>
                        //                                <option value="19">子车小车充电</option>
                        //                                <option value="21">子车A面载货上RGV</option>
                        //                                <option value="22">子车A面载货下RGV</option>
                        //                                <option value="23">子车B面载货上RGV</option>
                        //                                <option value="24">子车B面载货下RGV</option>
                        boolean resultWrite = false;
                        boolean resultExecute = false;
                        if (command == 1 || command == 2 || command == 3 || command == 4) {
                            //任务下发
                            while (!resultWrite) {
                                if (childCarTaskNoIsSuccess(machineInfo.getId()) && childCarZaiWuStatusIsSuccess(machineInfo.getId()) && childCarCanWork(machineInfo.getId())) {
                                    resultWrite = executeChildCarCommand(wcsControlInfoMsg);
                                }
                            }
                            //任务完成
                            while (resultWrite && !resultExecute) {
                                if (childCarTaskNoIsSuccess(machineInfo.getId())) {
                                    wcsControlInfoMsg.setStatus(getByte(3));
                                    wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfoMsg);
                                }
                            }
                        }
                        //上车任务可以提前下
                        else if (command == 21 || command == 11 || command == 23 || command == 13) {
                            //任务下发
                            while (!resultWrite) {
                                if (childCarTaskNoIsSuccess(machineInfo.getId()) && childCarCanWork(machineInfo.getId())) {
                                    resultWrite = executeChildCarCommand(wcsControlInfoMsg);
                                }
                            }
                            //任务完成
                            while (resultWrite && !resultExecute) {
                                if (childCarTaskNoIsSuccess(machineInfo.getId())) {
                                    wcsControlInfoMsg.setStatus(getByte(3));
                                    wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfoMsg);
                                }
                            }
                        } else {
                            //任务下发
                            while (!resultWrite) {
                                if (childCarTaskNoIsSuccess(machineInfo.getId()) && childCarCanWork(machineInfo.getId())) {
                                    resultWrite = executeChildCarCommand(wcsControlInfoMsg);
                                }
                            }
                            //任务完成
                            while (resultWrite && !resultExecute) {
                                if (childCarTaskNoIsSuccess(machineInfo.getId())) {
                                    wcsControlInfoMsg.setStatus(getByte(3));
                                    wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfoMsg);
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
}

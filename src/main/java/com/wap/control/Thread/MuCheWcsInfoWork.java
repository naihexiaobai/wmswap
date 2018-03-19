package com.wap.control.Thread;

import com.kepware.opc.OpcServer;
import com.www.util.DBUtil;
import com.wap.control.ControlCc;
import com.wap.model.OpcItemFinalString;
import com.wap.model.WCSControlInfo;

/**
 * 母车处理逻辑
 *
 * @auther CalmLake
 * @create 2017/12/4  13:08
 */
public class MuCheWcsInfoWork extends ControlCc {

    /**
     * 母车业务处理
     *
     * @param machineID
     * @param wcsControlInfoID
     */
    public void doWork(int machineID, int wcsControlInfoID) {
        WCSControlInfo wcsControlInfo = DBUtil.dbUtil.wCSControlInfoMapperImpl.selectByPrimaryKey(wcsControlInfoID);
        if (wcsControlInfo.getId() > 0 && wcsControlInfo.getMachineinfoid() == machineID) {
            wcsControlInfo.setStatus(getByte(2));
            updateWCSControlInfoByPrimaryKey(wcsControlInfo);
            boolean statusLock = true;
            boolean resultStatus = false;
            boolean resultWrite = false;
            boolean resultIsSuccess = false;
            //设备是否锁定
            while (statusLock) {
                if (machineID == OpcItemFinalString.MUCHEYIMACHINEID &&
                        "false".equals(OpcServer.monitoringMap.get(OpcItemFinalString.MUCHEYILOCK))) {
                    statusLock = false;
                } else if (machineID == OpcItemFinalString.MUCHEERMACHINEID &&
                        "false".equals(OpcServer.monitoringMap.get(OpcItemFinalString.MUCHEYILOCK))) {
                    statusLock = false;
                }
                if (machineID == OpcItemFinalString.MUCHESANMACHINEID &&
                        "false".equals(OpcServer.monitoringMap.get(OpcItemFinalString.MUCHEYILOCK))) {
                    statusLock = false;
                }
            }
            while (!resultStatus) {
                //当前设备不锁定
                if (!statusLock) {
                    if (canWorkStatusMuChe(getRGVStatus(machineID))) {
                        if (wcsControlInfo.getMovementid() == 0) {
                            while (!resultWrite) {
                                resultWrite = executeMuCheCommand(wcsControlInfo);
                            }
                            while (resultWrite && !resultIsSuccess) {
                                resultIsSuccess = muCheTaskIsSuccess(wcsControlInfo);
                                wcsControlInfo.setStatus(getByte(3));
                                updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                            }
                        } else {
                            if (isZaiWu(getRGVStatus(machineID))) {
                                while (!resultWrite) {
                                    resultWrite = executeMuCheCommand(wcsControlInfo);
                                }
                                while (resultWrite && !resultIsSuccess) {
                                    resultIsSuccess = muCheTaskIsSuccess(wcsControlInfo);
                                    wcsControlInfo.setStatus(getByte(3));
                                    updateWCSControlInfoByPrimaryKey(wcsControlInfo);
                                }
                            }
                        }
                    }
                }

            }
        }
    }


}

package com.wap.control.Thread;

import com.www.util.DBUtil;
import com.wap.control.ControlCc;
import com.wap.model.WCSControlInfo;

/**
 * 升降机业务处理
 *
 * @auther CalmLake
 * @create 2017/12/4  14:16
 */
public class ShengJiangJiWcsInfoWork extends ControlCc {

    public void doWork(int machineID, int wcsControlInfoID) {
        WCSControlInfo wcsControlInfo = wCSControlInfoMapperImpl.selectByPrimaryKey(wcsControlInfoID);
        if (wcsControlInfo.getId() > 0 && wcsControlInfo.getMachineinfoid() == machineID) {
            wcsControlInfo.setStatus(getByte(2));
            updateWCSControlInfoByPrimaryKey(wcsControlInfo);
            boolean statusLock = false;
            boolean resultStatus = false;
            boolean resultWrite = false;
            boolean resultIsSuccess = false;
            while (!resultStatus) {
                resultStatus = SSJStatusZiDong();
            }
            while (resultStatus && !resultWrite) {
                resultWrite = executeSSJCommand(wcsControlInfo);
            }
            while (resultWrite && !resultIsSuccess) {
                resultIsSuccess = SSJTaskIsSuccess();
                wcsControlInfo.setStatus(getByte(3));
                updateWCSControlInfoByPrimaryKey(wcsControlInfo);
            }
        }
    }
}

package com.wap.control.Thread;

import com.alibaba.fastjson.JSONObject;
import com.www.util.DBUtil;
import com.wap.control.ControlCc;
import com.wap.model.OpcItemFinalString;
import com.wap.model.TaskHistory;

import java.util.Date;

/**
 * 出库线程
 *
 * @auther CalmLake
 * @create 2017/11/28  11:50
 */
public class OutStorageThread extends ControlCc implements Runnable {

    private TaskHistory taskHistory;

    private JSONObject jsonObjectDuiDuoJi = new JSONObject();

    private JSONObject jsonObjectZiChe = new JSONObject();

    public OutStorageThread(TaskHistory taskHistory) {
        this.taskHistory = taskHistory;
    }

    public OutStorageThread(TaskHistory taskHistory, String duiDuoJiJSON, String ziCheJSON) {
        this.jsonObjectDuiDuoJi = JSONObject.parseObject(duiDuoJiJSON);
        this.jsonObjectZiChe = JSONObject.parseObject(ziCheJSON);
        this.taskHistory = taskHistory;
    }

    public void run() {
        int i = 0;
        taskHistory.setStatus((byte) 2);
        taskHistory.setStarttime(new Date());
        DBUtil.dbUtil.taskHistoryMapperImpl.updateByPrimaryKeySelective(taskHistory);
        try {
            // 1 .   目标位置   当前位置
            JSONObject ziCheJson = getZiCheLocaltion(taskHistory, jsonObjectZiChe.toJSONString());
            if (ziCheJson.getBooleanValue("result")) {
                //子车堆垛机处于同一位置
                if (ziCheJson.getIntValue("code") == 100) {
                    if (jsonObjectDuiDuoJi.getShortValue("x") == taskHistory.getX() && jsonObjectDuiDuoJi.getShortValue("y") == taskHistory.getY()
                            && !jsonObjectDuiDuoJi.getBooleanValue("zaiWu") && jsonObjectDuiDuoJi.getBooleanValue("zaiChe")) {
                        //子车 下车 出货 载货上
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND14,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND4,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND23,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.DDJMACHINEID, getShort(7), getShort(1), getShort(5), OpcItemFinalString.COMMAND22RGV,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                    } else {
                        //4.堆垛机移动至目标位置
                        i = createTask(OpcItemFinalString.DDJMACHINEID, taskHistory.getY(), taskHistory.getX(), getShort(0), OpcItemFinalString.COMMAND0,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                        //子车 下车 出货 载货上
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND14,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND4,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND23,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.DDJMACHINEID, getShort(7), getShort(1), getShort(5), OpcItemFinalString.COMMAND22RGV,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                    }
                }
                //子车与目标货物同一巷道
                else if (ziCheJson.getIntValue("code") == 101) {
                    if (jsonObjectDuiDuoJi.getShortValue("x") == taskHistory.getX() && jsonObjectDuiDuoJi.getShortValue("y") == taskHistory.getY()
                            && !jsonObjectDuiDuoJi.getBooleanValue("zaiWu") && !jsonObjectDuiDuoJi.getBooleanValue("zaiChe")) {
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND4,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND23,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.DDJMACHINEID, getShort(7), getShort(1), getShort(5), OpcItemFinalString.COMMAND22RGV,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                    } else {
                        i = createTask(OpcItemFinalString.DDJMACHINEID, taskHistory.getY(), taskHistory.getX(), getShort(0), OpcItemFinalString.COMMAND0,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND4,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND23,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                        i = createTask(OpcItemFinalString.DDJMACHINEID, getShort(7), getShort(1), getShort(5), OpcItemFinalString.COMMAND22RGV,
                                getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                    }
                }
            } else {
                i = createTask(OpcItemFinalString.DDJMACHINEID, jsonObjectZiChe.getShortValue("y"), jsonObjectZiChe.getShortValue("x"), getShort(0), OpcItemFinalString.COMMAND0,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND13,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                i = createTask(OpcItemFinalString.DDJMACHINEID, taskHistory.getY(), taskHistory.getX(), getShort(0), OpcItemFinalString.COMMAND0,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
                i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND14,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND4,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                i = createTask(OpcItemFinalString.ZICHEERMACHINEID, taskHistory.getY(), taskHistory.getX(), taskHistory.getZ(), OpcItemFinalString.COMMAND23,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.DDJMACHINEID);
                i = createTask(OpcItemFinalString.DDJMACHINEID, getShort(7), getShort(1), getShort(5), OpcItemFinalString.COMMAND22RGV,
                        getRandomNum(10000), OpcItemFinalString.LEVEL_N, taskHistory.getWcstaskno().toString(), OpcItemFinalString.ZICHEERMACHINEID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public TaskHistory getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(TaskHistory taskHistory) {
        this.taskHistory = taskHistory;
    }
}

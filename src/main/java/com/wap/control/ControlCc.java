package com.wap.control;

import com.alibaba.fastjson.JSONObject;
import com.kepware.opc.DuiDuoJiMsgModel;
import com.kepware.opc.OpcServer;
import com.kepware.opc.ZiCheMsgModel;
import com.ren.util.LoggerUtil;
import com.ren.util.SpringTool;
import com.wap.control.dao.daoImpl.*;
import com.wap.model.OpcItemFinalString;
import com.wap.model.TaskHistory;
import com.wap.model.WCSControlInfo;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 向控制大佬低头
 *
 * @auther CalmLake
 * @create 2017/11/24  13:55
 */
@Controller
public class ControlCc {

    private LoggerUtil loggerUtil = new LoggerUtil("ControlCc");

    @Resource(name = "opcItemsMapperImpl", type = OpcItemsMapperImpl.class)
    public OpcItemsMapperImpl opcItemsMapperImpl;
    @Resource(name = "machineInfoMapperImpl", type = MachineInfoMapperImpl.class)
    public MachineInfoMapperImpl machineInfoMapperImpl;
    @Resource(name = "wCSControlInfoMapperImpl", type = WCSControlInfoMapperImpl.class)
    public WCSControlInfoMapperImpl wCSControlInfoMapperImpl;
    @Resource(name = "storageMapperImpl", type = StorageMapperImpl.class)
    public StorageMapperImpl storageMapperImpl;
    @Resource(name = "cargoMapperImpl", type = CargoMapperImpl.class)
    public CargoMapperImpl cargoMapperImpl;
    @Resource(name = "taskHistoryMapperImpl", type = TaskHistoryMapperImpl.class)
    public TaskHistoryMapperImpl taskHistoryMapperImpl;


    /**
     * 查询WCSControlInfo
     *
     * @param wcsControlInfo
     * @return
     */
    public List<WCSControlInfo> selectByWCSControlInfo(WCSControlInfo wcsControlInfo) {
        WCSControlInfoMapperImpl wCSControlInfoMapperImpl = (WCSControlInfoMapperImpl) SpringTool.getBean("wCSControlInfoMapperImpl");
        return wCSControlInfoMapperImpl.selectByWCSControlInfo(wcsControlInfo);
    }

    public WCSControlInfo selectOneByWCSControlInfo(WCSControlInfo wcsControlInfo) {
        WCSControlInfoMapperImpl wCSControlInfoMapperImpl = (WCSControlInfoMapperImpl) SpringTool.getBean("wCSControlInfoMapperImpl");
        return wCSControlInfoMapperImpl.selectOneByWCSControlInfo(wcsControlInfo);
    }

    /**
     * 修改 WCSControlInfo
     *
     * @param wcsControlInfo
     * @return
     */
    public int updateWCSControlInfoByPrimaryKey(WCSControlInfo wcsControlInfo) {
        WCSControlInfoMapperImpl wCSControlInfoMapperImpl = (WCSControlInfoMapperImpl) SpringTool.getBean("wCSControlInfoMapperImpl");
        return wCSControlInfoMapperImpl.updateByPrimaryKey(wcsControlInfo);
    }

    /**
     * 修改任务状态
     *
     * @param result
     * @param wcsControlInfo
     * @return
     */
    public int updateWcsStarus(boolean result, WCSControlInfo wcsControlInfo) {
        if (result) {
            wcsControlInfo.setStatus((byte) 1);
        } else {
            wcsControlInfo.setStatus((byte) 0);
        }
        WCSControlInfoMapperImpl wCSControlInfoMapperImpl = (WCSControlInfoMapperImpl) SpringTool.getBean("wCSControlInfoMapperImpl");
        return wCSControlInfoMapperImpl.updateByPrimaryKeySelective(wcsControlInfo);
    }


    /**
     * 获取堆垛机信息
     *
     * @return
     */
    public JSONObject getDuiDuoJiStatus() throws Exception {
        JSONObject jsonObject = new JSONObject();
        String y = OpcServer.monitoringMap.get(OpcItemFinalString.DDJCENG).toString();
        String x = OpcServer.monitoringMap.get(OpcItemFinalString.DDJLIE).toString();
        String ziDong = OpcServer.monitoringMap.get(OpcItemFinalString.DDJZISHOUDONG).toString();
        String zaiWu = OpcServer.monitoringMap.get(OpcItemFinalString.DDJZAIWU).toString();
        String zaiChe = OpcServer.monitoringMap.get(OpcItemFinalString.DDJZAICHE).toString();
        String kongXian = OpcServer.monitoringMap.get(OpcItemFinalString.DDJKONGXIAN).toString();
        String daiMing = OpcServer.monitoringMap.get(OpcItemFinalString.DDJDAIMING).toString();
        if (ziDong.equals("true") && kongXian.equals("true") && daiMing.equals("false")) {
            jsonObject.put("x", x);
            jsonObject.put("y", y);
            jsonObject.put("zaiWu", zaiWu);
            jsonObject.put("zaiChe", zaiChe);
            jsonObject.put("kongXian", kongXian);
            jsonObject.put("ziDong", ziDong);
            jsonObject.put("result", true);
        } else {
            jsonObject.put("result", false);
        }
        return jsonObject;
    }

    /**
     * 获取子车状态
     *
     * @param string - 子车group
     * @return
     */
    public JSONObject getZiCheStatus(String string) {
        JSONObject jsonObject = new JSONObject();
        String y = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHECENG).toString();
        String x = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHELIE).toString();
        String ziDong = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHEZISHOUDONG).toString();
        String kongXian = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHEKONGXIAN).toString();
        String daiMing = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHEDAIMING).toString();
        String rgvDaiJi = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHEMUCHESHANGDAIJI).toString();
        String tiShengJiDaiJi = OpcServer.monitoringMap.get(string + "." + OpcItemFinalString.ZICHETISHENGJISHANGDAIJI).toString();
        if (ziDong.equals("true") && kongXian.equals("true") && daiMing.equals("false") && (rgvDaiJi.equals("true") || tiShengJiDaiJi.equals("true"))) {
            jsonObject.put("x", x);
            jsonObject.put("y", y);
            jsonObject.put("rgvDaiJi", rgvDaiJi);
            jsonObject.put("tiShengJiDaiJi", tiShengJiDaiJi);
            jsonObject.put("result", true);
        } else {
            jsonObject.put("result", false);
        }
        return jsonObject;
    }

    /**
     * 子车位置
     *
     * @param ziCheString - json
     * @return
     */
    public JSONObject getZiCheLocaltion(TaskHistory taskHistory, String ziCheString) {
        JSONObject jsonObject = new JSONObject();
        JSONObject ziCheJSON = JSONObject.parseObject(ziCheString);
        //子车是否在堆垛机上待机
        if (ziCheJSON.getBooleanValue("rgvDaiJi") || ziCheJSON.getBooleanValue("tiShengJiDaiJi")) {
            jsonObject.put("result", true);
            jsonObject.put("msg", "子车在堆垛机上待机");
            jsonObject.put("code", 100);
            return jsonObject;
        }
        //子车是否同目标货物在同一巷道
        else if (ziCheJSON.getShortValue("x") == taskHistory.getX() && ziCheJSON.getShortValue("y") == taskHistory.getY()) {
            jsonObject.put("result", true);
            jsonObject.put("msg", "子车与货物同一巷道");
            jsonObject.put("code", 101);
            return jsonObject;
        } else {
            ziCheJSON.put("result", false);
            ziCheJSON.put("msg", "子车与堆垛机不同位");
            ziCheJSON.put("code", 200);
            return ziCheJSON;
        }
    }

    /**
     * 创建任务
     *
     * @param no-设备ID
     * @param storey-层
     * @param line-列
     * @param row-排
     * @param command-指令
     * @param wcsTaskNo-任务码
     * @param level-任务优先级
     * @param orderNO-父级任务码
     * @return
     */
    public int createTask(int no, short storey, short line, short row, byte command, short wcsTaskNo, String level, String orderNO) {
        WCSControlInfo wcsControlInfo = new WCSControlInfo();
        wcsControlInfo.setMachineinfoid(no);
        wcsControlInfo.setY(storey);
        wcsControlInfo.setX(line);
        wcsControlInfo.setZ(row);
        wcsControlInfo.setMovementid(command);
        wcsControlInfo.setWcstaskno(wcsTaskNo);
        wcsControlInfo.setCreatetime(new Date());
        wcsControlInfo.setStatus(getByte(0));
        wcsControlInfo.setReserved1(level);
        wcsControlInfo.setReserved2(orderNO);
        int i = wCSControlInfoMapperImpl.insertSelective(wcsControlInfo);
        return i;
    }

    /**
     * 类型转换
     *
     * @param i
     * @return
     */
    public short getShort(int i) {
        return (short) i;
    }

    /**
     * 类型转换
     *
     * @param i
     * @return
     */
    public byte getByte(int i) {
        return (byte) i;
    }

    /**
     * 获取i以内随机数
     *
     * @param i
     * @return
     */
    public short getRandomNum(int i) {
        Random random = new Random();
        return getShort(random.nextInt(i));
    }

    /**
     * 子车执行命令
     *
     * @param wcsControlInfo
     * @return int 1/0
     */
    public boolean executeChildCarCommand(WCSControlInfo wcsControlInfo) {
        ZiCheMsgModel msgModel = new ZiCheMsgModel();
        msgModel.setCommandNum(wcsControlInfo.getMovementid());
        msgModel.setNO(wcsControlInfo.getMachineinfoid());
        msgModel.setOrderNum(wcsControlInfo.getWcstaskno());
        msgModel.setTargetLine(wcsControlInfo.getX());
        msgModel.setTargetRow(wcsControlInfo.getZ());
        msgModel.setTargetStorey(wcsControlInfo.getY());
        boolean result = OpcServer.getInstance().write(msgModel);
        return result;
    }

    /**
     * 堆垛机执行命令
     *
     * @param wcsControlInfo
     * @return int 1/0
     */
    public boolean executeDuiDuoJiCommand(WCSControlInfo wcsControlInfo) {
        DuiDuoJiMsgModel msgModel = new DuiDuoJiMsgModel();
        msgModel.setCommandNum(wcsControlInfo.getMovementid());
        msgModel.setNO(wcsControlInfo.getMachineinfoid());
        msgModel.setOrderNum(wcsControlInfo.getWcstaskno());
        msgModel.setTargetLine(wcsControlInfo.getX());
        msgModel.setTargetStorey(wcsControlInfo.getY());
        boolean result = OpcServer.getInstance().write(msgModel);
        return result;
    }

    /**
     * 堆垛机当前是否可以执行任务
     *
     * @return boolean
     */
    public boolean duiDuoJiExecuteTask() {
        boolean result = false;
        try {
            JSONObject jsonObject = getDuiDuoJiStatus();
            if (jsonObject.getBooleanValue("kongXian") && jsonObject.getBooleanValue("ziDong")) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerUtil.getLogger().warn("获取堆垛机信息异常" + e.getMessage());
        }
        return result;
    }

    /**
     * 堆垛机当前任务是否完成
     *
     * @return
     */
    public boolean duiDuoJiTaskIsSuccess() {
        boolean result = false;
        try {
            while (!result) {
                String renWuMa = OpcServer.monitoringMap.get(OpcItemFinalString.DDJRENWUMA).toString();
                String WCSRenWuMa = OpcServer.monitoringMap.get(OpcItemFinalString.DDJDONGZUORENWUHAO).toString();
                if (renWuMa.equals(WCSRenWuMa)) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判定子车自动状态
     *
     * @param machineId
     * @return
     */
    public boolean childCarZiDongStatusIsSuccess(int machineId) {
        boolean result = false;
        String ziDong = "";
        if (machineId == 4) {
            ziDong = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEZISHOUDONG).toString();
        } else if (machineId == 5) {
            ziDong = OpcServer.monitoringMap.get(OpcItemFinalString.ERHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEZISHOUDONG).toString();
        } else if (machineId == 6) {
            ziDong = OpcServer.monitoringMap.get(OpcItemFinalString.SANHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEZISHOUDONG).toString();
        }
        if ("true".equals(ziDong)) {
            result = true;
        }
        return result;
    }

    /**
     * 子车载物状态判断
     *
     * @param machineId
     * @return
     */
    public boolean childCarZaiWuStatusIsSuccess(int machineId) {
        boolean result = false;
        String zaiWu = "";
        if (machineId == 4) {
            zaiWu = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEZAIWU).toString();
        } else if (machineId == 5) {
            zaiWu = OpcServer.monitoringMap.get(OpcItemFinalString.ERHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEZAIWU).toString();
        } else if (machineId == 6) {
            zaiWu = OpcServer.monitoringMap.get(OpcItemFinalString.SANHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEZAIWU).toString();
        }
        if ("true".equals(zaiWu)) {
            result = true;
        }
        return result;
    }

    /**
     * 子车空闲状态
     *
     * @param machineId
     * @return
     */
    public boolean childCarKongXianStatusIsSuccess(int machineId) {
        boolean result = false;
        String kongXian = "";
        if (machineId == 4) {
            kongXian = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEKONGXIAN).toString();
        } else if (machineId == 5) {
            kongXian = OpcServer.monitoringMap.get(OpcItemFinalString.ERHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEKONGXIAN).toString();
        } else if (machineId == 6) {
            kongXian = OpcServer.monitoringMap.get(OpcItemFinalString.SANHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEKONGXIAN).toString();
        }
        if ("true".equals(kongXian)) {
            result = true;
        }
        return result;
    }

    /**
     * 子车待命状态
     *
     * @param machineId
     * @return
     */
    public boolean childCarDaiMingStatusIsSuccess(int machineId) {
        boolean result = false;
        String daiMing = "";
        if (machineId == 4) {
            daiMing = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDAIMING).toString();
        } else if (machineId == 5) {
            daiMing = OpcServer.monitoringMap.get(OpcItemFinalString.ERHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDAIMING).toString();
        } else if (machineId == 6) {
            daiMing = OpcServer.monitoringMap.get(OpcItemFinalString.SANHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDAIMING).toString();
        }
        if ("true".equals(daiMing)) {
            result = true;
        }
        return result;
    }

    /**
     * 子车的任务码和wcs任务码是否相同
     *
     * @param machineId
     * @return
     */
    public boolean childCarTaskNoIsSuccess(int machineId) {
        boolean result = false;
        String taskNo = "";
        String taskNoWCS = "";
        if (machineId == 4) {
            taskNo = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHERENWUMA).toString();
            taskNoWCS = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDONGZUORENWUHAO).toString();
            result = stringIsSame(taskNo, taskNoWCS);
        } else if (machineId == 5) {
            taskNo = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHERENWUMA).toString();
            taskNoWCS = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDONGZUORENWUHAO).toString();
            result = stringIsSame(taskNo, taskNoWCS);
        } else if (machineId == 6) {
            taskNo = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHERENWUMA).toString();
            taskNoWCS = OpcServer.monitoringMap.get(OpcItemFinalString.YIHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDONGZUORENWUHAO).toString();
            result = stringIsSame(taskNo, taskNoWCS);
        }
        return result;
    }

    /**
     * 字符串是否相同
     *
     * @return
     */
    public boolean stringIsSame(String taskNo, String taskWcs) {
        boolean result = false;
        if (!"".equals(taskNo) && !"".equals(taskWcs) && taskNo.equals(taskWcs)) {
            result = true;
        }
        return result;
    }

    /**
     * 子车状态是否正常
     *
     * @param machineId
     * @return
     */
    public boolean childCarCanWork(int machineId) {
        boolean result = false;
        if (childCarKongXianStatusIsSuccess(machineId) && childCarZiDongStatusIsSuccess(machineId)) {
            result = true;
        }
        return result;
    }
}

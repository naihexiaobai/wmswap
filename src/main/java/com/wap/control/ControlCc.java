package com.wap.control;

import com.alibaba.fastjson.JSONObject;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.kepware.opc.*;
import com.wap.control.dao.daoImpl.*;
import com.wap.model.OpcItemFinalString;
import com.wap.model.TaskHistory;
import com.wap.model.WCSControlInfo;
import com.www.util.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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

//    private LoggerUtil loggerUtil = new LoggerUtil("ControlCc");

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
        return wCSControlInfoMapperImpl.selectByWCSControlInfo(wcsControlInfo);
    }

    public WCSControlInfo selectOneByWCSControlInfo(WCSControlInfo wcsControlInfo) {
        return wCSControlInfoMapperImpl.selectOneByWCSControlInfo(wcsControlInfo);
    }

    /**
     * 修改 WCSControlInfo
     *
     * @param wcsControlInfo
     * @return
     */
    public int updateWCSControlInfoByPrimaryKey(WCSControlInfo wcsControlInfo) {
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
     * 检查当前堆垛机位置与目标位
     *
     * @param wcsControlInfo
     * @return
     */
    public boolean checkLocalDDJ(WCSControlInfo wcsControlInfo) {
        boolean result = false;
        try {
            JSONObject jsonObject = getDuiDuoJiStatus();
            if (jsonObject.getShort("x") == wcsControlInfo.getX() && jsonObject.getShort("y") == wcsControlInfo.getY()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("rgvDaiJi", rgvDaiJi);
        jsonObject.put("tiShengJiDaiJi", tiShengJiDaiJi);
        jsonObject.put("ziDong", ziDong);
        jsonObject.put("kongXian", kongXian);
        jsonObject.put("daiMing", daiMing);
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
     * @param targetMachineId-目标设备ID
     * @return
     */
    public int createTask(int no, short storey, short line, short row, byte command, short wcsTaskNo, String level, String orderNO, int targetMachineId) {
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
        wcsControlInfo.setReserved3(targetMachineId + "");
        int i = wCSControlInfoMapperImpl.insertSelective(wcsControlInfo);
        //发送消息 {"machineID":"","data":""}
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("machineID", no);
        jsonObject.put("data", wcsControlInfo.getId());
        SocketInfoListsSingleton.getInstance().sendMsg(jsonObject.toJSONString());
        return i;
    }

    /**
     * 类型转换
     *
     * @param i
     * @return
     */
    public static short getShort(int i) {
        return (short) i;
    }

    /**
     * 类型转换
     *
     * @param i
     * @return
     */
    public static byte getByte(int i) {
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
     * 母车执行命令
     *
     * @param wcsControlInfo
     * @return int 1/0
     */
    public static boolean executeMuCheCommand(WCSControlInfo wcsControlInfo) {
        MuCheMsgModel msgModel = new MuCheMsgModel();
        msgModel.setCommandNum(wcsControlInfo.getMovementid());
        msgModel.setNO(wcsControlInfo.getMachineinfoid());
        msgModel.setOrderNum(wcsControlInfo.getWcstaskno());
        msgModel.setTargetLine(wcsControlInfo.getX());
        boolean result = OpcServer.getInstance().write(msgModel);
        return result;
    }

    /**
     * 升降机执行命令
     *
     * @param wcsControlInfo
     * @return int 1/0
     */
    public static boolean executeSSJCommand(WCSControlInfo wcsControlInfo) {
        ShengJiangJiMsgModel msgModel = new ShengJiangJiMsgModel();
        msgModel.setCommandNum(wcsControlInfo.getMovementid());
        msgModel.setNO(wcsControlInfo.getMachineinfoid());
        msgModel.setOrderNum(wcsControlInfo.getWcstaskno());
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
        }
        return result;
    }

    /**
     * 堆垛机当前任务是否完成
     *
     * @return
     */
    public boolean duiDuoJiTaskIsSuccess(String wcsInfoTaskNo) {
        boolean result = false;
        try {
            while (!result) {
                String renWuMa = OpcServer.monitoringMap.get(OpcItemFinalString.DDJRENWUMA).toString();
                String WCSRenWuMa = OpcServer.monitoringMap.get(OpcItemFinalString.DDJDONGZUORENWUHAO).toString();
                if (renWuMa.equals(WCSRenWuMa) && renWuMa.equals(wcsInfoTaskNo)) {
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
     * 子车的任务码是否为0
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
            result = successTaskZiChe(taskNo, taskNoWCS, machineId);
        } else if (machineId == 5) {
            taskNo = OpcServer.monitoringMap.get(OpcItemFinalString.ERHAOZICHEGROUP + "." + OpcItemFinalString.ZICHERENWUMA).toString();
            taskNoWCS = OpcServer.monitoringMap.get(OpcItemFinalString.ERHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDONGZUORENWUHAO).toString();
            result = successTaskZiChe(taskNo, taskNoWCS, machineId);
        } else if (machineId == 6) {
            taskNo = OpcServer.monitoringMap.get(OpcItemFinalString.SANHAOZICHEGROUP + "." + OpcItemFinalString.ZICHERENWUMA).toString();
            taskNoWCS = OpcServer.monitoringMap.get(OpcItemFinalString.SANHAOZICHEGROUP + "." + OpcItemFinalString.ZICHEDONGZUORENWUHAO).toString();
//            if ((!CalmLakeStringUtil.stringIsNull(taskNoWCS) && !CalmLakeStringUtil.stringIsNull(taskNo) && (stringIsSame(taskNo, taskNoWCS)))
//                    || (childCarCanWork(machineId) && !CalmLakeStringUtil.stringIsNull(taskNo) && "0".equals(taskNo))) {
            result = successTaskZiChe(taskNo, taskNoWCS, machineId);
//            }
        }
        return result;
    }

    /**
     * 判断子车任务是否完成 新旧任务码判断
     *
     * @param taskNo    任务码
     * @param taskNoWCS wcs任务码
     * @param machineId 设备序号
     * @return
     */
    public boolean successTaskZiChe(String taskNo, String taskNoWCS, int machineId) {
        boolean result = false;
        if (!CalmLakeStringUtil.stringIsNull(taskNoWCS) && !CalmLakeStringUtil.stringIsNull(taskNo)) {
            if (childCarCanWork(machineId)) {
                if (stringIsSame(taskNo, taskNoWCS)) {
                    result = true;
                } else if ("0".equals(taskNo)) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 字符串是否相同
     *
     * @return
     */
    public static boolean stringIsSame(String taskNo, String taskWcs) {
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

    /**
     * 核对两个交互设备位置是否正确
     *
     * @param wcsControlInfo
     * @param targetMachineId,目标设备id
     * @return
     */
    public boolean checkLocationSame(WCSControlInfo wcsControlInfo, int targetMachineId) {
        boolean result = false;
        byte command = wcsControlInfo.getMovementid();
        String ziCheGroup = "";
        if (wcsControlInfo.getMachineinfoid() == OpcItemFinalString.ZICHEYIMACHINEID) {
            ziCheGroup = OpcItemFinalString.YIHAOZICHEGROUP;
        } else if (wcsControlInfo.getMachineinfoid() == OpcItemFinalString.ZICHEERMACHINEID) {
            ziCheGroup = OpcItemFinalString.ERHAOZICHEGROUP;
        } else if (wcsControlInfo.getMachineinfoid() == OpcItemFinalString.ZICHESANMACHINEID) {
            ziCheGroup = OpcItemFinalString.SANHAOZICHEGROUP;
        }
        JSONObject jsonObject = getXY(targetMachineId);
        JSONObject ziCheJsonObject = getZiCheStatus(ziCheGroup);
        //上车
        if (command == 21 || command == 11 || command == 23 || command == 13) {
            //判断当前位置是否为目标位
            boolean localNow = false;
            while (!localNow) {
                jsonObject = getXY(targetMachineId);
                if (jsonObject.getShortValue("x") == ziCheJsonObject.getShortValue("x") &&
                        jsonObject.getShortValue("y") == ziCheJsonObject.getShortValue("y")) {
                    localNow = true;
                    lockMachine(targetMachineId);
                    result = true;
                }
            }
        }
        //下车
        else if (command == 12 || command == 14 || command == 22 || command == 24) {
            //判断当前位置是否为目标位
            boolean localNow = false;
            while (!localNow) {
                jsonObject = getXY(targetMachineId);
                if (jsonObject.getShortValue("x") == wcsControlInfo.getX() && jsonObject.getShortValue("y") == wcsControlInfo.getY()) {
                    localNow = true;
                }
            }
            if (jsonObject.getBooleanValue("zaiChe")) {
                result = true;
                lockMachine(targetMachineId);
            }
        }
        return result;
    }

    /**
     * 锁定设备,母车 堆垛机
     *
     * @param targetMachineId
     */
    public void lockMachine(int targetMachineId) {
        if (targetMachineId == OpcItemFinalString.MUCHEYIMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.MUCHEYILOCK, "true");
        } else if (targetMachineId == OpcItemFinalString.MUCHEERMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.MUCHEERLOCK, "true");
        } else if (targetMachineId == OpcItemFinalString.MUCHESANMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.MUCHESANLOCK, "true");
        } else if (targetMachineId == OpcItemFinalString.DDJMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.DDJLOCK, "true");
        }
    }

    /**
     * 解锁
     *
     * @param targetMachineId
     */
    public void lockNoMachine(int targetMachineId) {
        if (targetMachineId == OpcItemFinalString.MUCHEYIMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.MUCHEYILOCK, "false");
        } else if (targetMachineId == OpcItemFinalString.MUCHEERMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.MUCHEERLOCK, "false");
        } else if (targetMachineId == OpcItemFinalString.MUCHESANMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.MUCHESANLOCK, "false");
        } else if (targetMachineId == OpcItemFinalString.DDJMACHINEID) {
            OpcServer.monitoringMap.put(OpcItemFinalString.DDJLOCK, "false");
        }
    }

    /**
     * 获得设备的xy坐标
     *
     * @param targetMachineId
     * @return
     */
    public JSONObject getXY(int targetMachineId) {
        JSONObject jsonObject = new JSONObject();
        try {
            String x = "";
            String y = "";
            //堆垛机
            if (targetMachineId == OpcItemFinalString.DDJMACHINEID) {
                jsonObject = getDuiDuoJiStatus();
            }
            if (targetMachineId == OpcItemFinalString.MUCHEYIMACHINEID) {
                jsonObject = getRGVStatus(targetMachineId);
            }
            if (targetMachineId == OpcItemFinalString.MUCHEERMACHINEID) {
                jsonObject = getRGVStatus(targetMachineId);
            }
            if (targetMachineId == OpcItemFinalString.MUCHESANMACHINEID) {
                jsonObject = getRGVStatus(targetMachineId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取母车状态
     *
     * @param targetMachineId
     * @return
     */
    public static JSONObject getRGVStatus(int targetMachineId) {
        JSONObject jsonObject = new JSONObject();
        String muCheGroup = "";
        short y = 0;
        if (targetMachineId == OpcItemFinalString.MUCHEYIMACHINEID) {
            muCheGroup = OpcItemFinalString.YIHAOMUCHEGROUP;
            y = 1;
        }
        if (targetMachineId == OpcItemFinalString.MUCHEERMACHINEID) {
            muCheGroup = OpcItemFinalString.ERHAOMUCHEGROUP;
            y = 2;
        }
        if (targetMachineId == OpcItemFinalString.MUCHESANMACHINEID) {
            muCheGroup = OpcItemFinalString.SANHAOMUCHEGROUP;
            y = 3;
        }
        String x = OpcServer.monitoringMap.get(muCheGroup + "." + OpcItemFinalString.MUCHELIE).toString();
        String zaiWu = OpcServer.monitoringMap.get(muCheGroup + "." + OpcItemFinalString.MUCHEZAIWU).toString();
        String zaiChe = OpcServer.monitoringMap.get(muCheGroup + "." + OpcItemFinalString.MUCHEZAICHE).toString();
        String ziDong = OpcServer.monitoringMap.get(muCheGroup + "." + OpcItemFinalString.MUCHEZISHOUDONG).toString();
        String kongXian = OpcServer.monitoringMap.get(muCheGroup + "." + OpcItemFinalString.MUCHEKONGXIAN).toString();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("zaiWu", zaiWu);
        jsonObject.put("zaiChe", zaiChe);
        jsonObject.put("ziDong", ziDong);
        jsonObject.put("kongXian", kongXian);
        return jsonObject;
    }

    /**
     * 母车是否可以工作
     *
     * @param jsonObject
     * @return
     */
    public static boolean canWorkStatusMuChe(JSONObject jsonObject) {
        boolean result = false;
        if ("true".equals(jsonObject.getString("ziDong")) && "true".equals(jsonObject.getString("kongXian"))) {
            result = true;
        }
        return result;
    }

    /**
     * 是否载物
     *
     * @param jsonObject
     * @return
     */
    public static boolean isZaiWu(JSONObject jsonObject) {
        boolean result = false;
        if ("true".equals(jsonObject.getString("zaiWu"))) {
            result = true;
        }
        return result;
    }

    /**
     * 母车任务是否完成
     *
     * @param wcsControlInfo
     * @return
     */
    public static boolean muCheTaskIsSuccess(WCSControlInfo wcsControlInfo) {
        boolean result = false;
        String group = "";
        if (wcsControlInfo.getMachineinfoid() == OpcItemFinalString.MUCHEYIMACHINEID) {
            group = OpcItemFinalString.YIHAOMUCHEGROUP;
        } else if (wcsControlInfo.getMachineinfoid() == OpcItemFinalString.MUCHEERMACHINEID) {
            group = OpcItemFinalString.ERHAOMUCHEGROUP;
        } else if (wcsControlInfo.getMachineinfoid() == OpcItemFinalString.MUCHESANMACHINEID) {
            group = OpcItemFinalString.SANHAOMUCHEGROUP;
        }
        String taskNo = OpcServer.monitoringMap.get(group + "." + OpcItemFinalString.MUCHERENWUMA).toString();
        String wcsTaskNo = OpcServer.monitoringMap.get(group + "." + OpcItemFinalString.MUCHERENWUMA).toString();
        String ziDong = OpcServer.monitoringMap.get(group + "." + OpcItemFinalString.MUCHEZISHOUDONG).toString();
        String kongXian = OpcServer.monitoringMap.get(group + "." + OpcItemFinalString.MUCHEKONGXIAN).toString();
        if (stringIsSame(taskNo, wcsTaskNo)) {
            result = true;
        } else if ("0".equals(taskNo) && "true".equals(ziDong) && "true".equals(kongXian)) {
            result = true;
        }
        return result;
    }

    /**
     * 升降机状态,自动，空闲
     *
     * @return
     */
    public static boolean SSJStatusZiDong() {
        boolean result = false;
        String ziDong = OpcServer.monitoringMap.get(OpcItemFinalString.SSJZISHOUDONG).toString();
        String kongXian = OpcServer.monitoringMap.get(OpcItemFinalString.SSJKONGXIAN).toString();
        if ("true".equals(ziDong) && "true".equals(kongXian)) {
            result = true;
        }
        return result;
    }

    /**
     * 升降机是否完成任务
     *
     * @return
     */
    public static boolean SSJTaskIsSuccess() {
        boolean result = false;
        String taskNo = OpcServer.monitoringMap.get(OpcItemFinalString.SSJRENWUMA).toString();
        String wcsTaskNo = OpcServer.monitoringMap.get(OpcItemFinalString.SSJDONGZUORENWUHAO).toString();
        String ziDong = OpcServer.monitoringMap.get(OpcItemFinalString.SSJZISHOUDONG).toString();
        String kongXian = OpcServer.monitoringMap.get(OpcItemFinalString.SSJKONGXIAN).toString();
        if (stringIsSame(taskNo, wcsTaskNo)) {
            result = true;
        }
        if ("true".equals(ziDong) && "true".equals(kongXian) && "0".equals(taskNo)) {
            result = true;
        }
        return result;
    }

    public static byte intToByte(int i) {
        return Integer.valueOf(i).byteValue();
    }

    public static byte[] message03(String seqNOString, String machineNOString, String stationNameString, String wharfNameString, String cycleString, String cycleTypeString, String commandTypeString, String mcKeyString, String xString, String yString, String zString) {
        int commondTypeInt = 3;
        byte[] seqNO = new byte[4];
        seqNO[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(seqNOString, 0, 1)));
        seqNO[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(seqNOString, 1, 2)));
        seqNO[2] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(seqNOString, 2, 3)));
        seqNO[3] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(seqNOString, 3, 4)));
        byte[] commandType = new byte[2];
        commandType[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(commandTypeString, 0, 1)));
        commandType[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(commandTypeString, 1, 2)));
        byte[] mcKey = new byte[4];
        mcKey[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(mcKeyString, 0, 1)));
        mcKey[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(mcKeyString, 1, 2)));
        mcKey[2] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(mcKeyString, 2, 3)));
        mcKey[3] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(mcKeyString, 3, 4)));
        byte[] machineNo = new byte[4];
        machineNo[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(machineNOString, 0, 1)));
        machineNo[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(machineNOString, 1, 2)));
        machineNo[2] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(machineNOString, 2, 3)));
        machineNo[3] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(machineNOString, 3, 4)));
        byte[] station = new byte[4];
        station[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(stationNameString, 0, 1)));
        station[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(stationNameString, 1, 2)));
        station[2] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(stationNameString, 2, 3)));
        station[3] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(stationNameString, 3, 4)));
        byte[] wharf = new byte[4];
        wharf[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(wharfNameString, 0, 1)));
        wharf[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(wharfNameString, 1, 2)));
        wharf[2] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(wharfNameString, 2, 3)));
        wharf[3] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(wharfNameString, 3, 4)));
        byte[] x = new byte[2];
        x[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(xString, 0, 1)));
        x[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(xString, 1, 2)));
        byte[] y = new byte[2];
        y[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(yString, 0, 1)));
        y[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(yString, 1, 2)));
        byte[] z = new byte[2];
        z[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(zString, 0, 1)));
        z[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(zString, 1, 2)));
        byte[] cycle = new byte[2];
        cycle[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(cycleString, 0, 1)));
        cycle[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(cycleString, 1, 2)));
        byte[] cycleType = new byte[2];
        cycleType[0] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(cycleTypeString, 0, 1)));
        cycleType[1] = intToByte(HexAscIIEnum.getValue(CalmLakeStringUtil.getSubString(cycleTypeString, 1, 2)));

        byte height = intToByte(HexAscIIEnum.getValue("0"));
        byte weight = intToByte(HexAscIIEnum.getValue("0"));

        byte[] bytes = message(commondTypeInt, seqNO, commandType, mcKey, machineNo, station, wharf, x, y, z, cycle, cycleType, height, weight);
        return bytes;
    }

    public static byte[] message(int commandTypeInt, byte[] seqNO, byte[] commandType, byte[] mcKey, byte[] machineNo, byte[] station,
                                 byte[] wharf, byte[] x, byte[] y, byte[] z, byte[] cycle, byte[] cycleType, byte height, byte weight) {
        byte[] bytes = new byte[512];
        //STX-1
        bytes[0] = intToByte(0xff02);
        //消息序号-4
        bytes[1] = seqNO[0];
        bytes[2] = seqNO[1];
        bytes[3] = seqNO[2];
        bytes[4] = seqNO[3];
        //命令-2
        bytes[5] = commandType[0];
        bytes[6] = commandType[1];
        //重发标识-1
        bytes[7] = intToByte(0xff30);
        //送信时间-6
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
        String time = simpleDateFormat.format(new Date());
        bytes[8] = intToByte(getTime(CalmLakeStringUtil.getSubString(time, 0, 1)));
        bytes[9] = intToByte(getTime(CalmLakeStringUtil.getSubString(time, 1, 2)));
        bytes[10] = intToByte(getTime(CalmLakeStringUtil.getSubString(time, 2, 3)));
        bytes[11] = intToByte(getTime(CalmLakeStringUtil.getSubString(time, 3, 4)));
        bytes[12] = intToByte(getTime(CalmLakeStringUtil.getSubString(time, 4, 5)));
        bytes[13] = intToByte(getTime(CalmLakeStringUtil.getSubString(time, 5, 6)));
        if (commandTypeInt == 3) {
            //MCKey-4
            bytes[14] = mcKey[0];
            bytes[15] = mcKey[1];
            bytes[16] = mcKey[2];
            bytes[17] = mcKey[3];
            //机器号-4，SC01-54 43 30 31
            bytes[18] = machineNo[0];
            bytes[19] = machineNo[1];
            bytes[20] = machineNo[2];
            bytes[21] = machineNo[3];
            //cycle-2,13 载货下车
            bytes[22] = cycle[0];
            bytes[23] = cycle[1];
            //作业区分-2,01 入库
            bytes[24] = cycleType[0];
            bytes[25] = cycleType[1];
            //货型高度-1
            bytes[26] = height;
            //货型宽度-1
            bytes[27] = weight;
            //排-2
            bytes[28] = z[0];
            bytes[29] = z[1];
            //列-2
            bytes[30] = x[0];
            bytes[31] = x[1];
            //层-2
            bytes[32] = y[0];
            bytes[33] = y[1];
            //站台-4,ML02-4D 4C 30 32
            bytes[34] = station[0];
            bytes[35] = station[1];
            bytes[36] = station[2];
            bytes[37] = station[3];
            //码头-4
            bytes[38] = wharf[0];
            bytes[39] = wharf[1];
            bytes[40] = wharf[2];
            bytes[41] = wharf[3];
        }
        //BCC-2
        bytes[42] = intToByte(0xff30);
        bytes[43] = intToByte(0xff30);
        //ETX-1
        bytes[44] = intToByte(0xff03);
        return bytes;
    }

    public static int getTime(String string) {
        int i = HexAscIIEnum.getValue(string);
        return i;
    }
}

package com.wap.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kepware.opc.*;
import com.ren.util.CalmLakeStringUtil;
import com.ren.util.LoggerUtil;
import com.wap.control.Thread.DuiDuoJiWcsInfoIPCThread;
import com.wap.control.dao.daoImpl.*;
import com.wap.model.MachineInfo;
import com.wap.model.OpcItems;
import com.wap.model.WCSControlInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 设备监控
 *
 * @auther CalmLake
 * @create 2017/11/21  10:49
 */
@Controller
@RequestMapping("monitoringCc")
public class MonitoringCc extends ControlCc {

    private LoggerUtil loggerUtil = new LoggerUtil("MonitoringCc");

    /**
     * 监控 开启/关闭  1/0
     *
     * @param request
     */
    @RequestMapping("startMonitor")
    @ResponseBody
    public JSONObject startMonitor(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        boolean result = false;
        try {
            String type = request.getParameter("type");
            loggerUtil.getLogger().info("进入监控程序，请求类型：" + type);
            if (CalmLakeStringUtil.stringIsNull(type)) {
                jsonObject.put("result", result);
                jsonObject.put("msg", "传输数据为空");
                jsonObject.put("code", 101);
                return jsonObject;
            }
            if (CalmLakeStringUtil.stringToInt(type) == 1) {
                OpcItems opcItems = new OpcItems();
                List<OpcItems> opcItemsList = opcItemsMapperImpl.selectByOpcItems(opcItems);
                loggerUtil.getLogger().info("获取监控数据--" + opcItemsList.size() + "-条");
                OpcReadThread opcReadThread = new OpcReadThread(opcItemsList);
                new Thread(opcReadThread).start();
                result = true;
                jsonObject.put("result", result);
                jsonObject.put("msg", "成功");
                jsonObject.put("code", 100);
            } else {
                for (OpcServerModel opcServerModel : OpcServer.opcServerModels) {
                    if (opcServerModel.getStatus() == 1 && opcServerModel.getKey().equals(OpcServer.KEYREAD)) {
                        opcServerModel.getAccessBase().unbind();
                        opcServerModel.setStatus(0);
                        opcServerModel.setKey("");
                        result = true;
                        opcServerModel.setAccessBase(null);
                        jsonObject.put("result", result);
                        jsonObject.put("msg", "读取关闭成功");
                        jsonObject.put("code", 100);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", result);
            jsonObject.put("msg", "出现异常");
            jsonObject.put("code", 999);
        }
        return jsonObject;
    }

    /**
     * 获取监控数据
     *
     * @param request
     * @return
     */
    @RequestMapping("getMonitorData")
    @ResponseBody
    public JSONArray getMonitorData(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        try {
            String type = request.getParameter("type");
            if (CalmLakeStringUtil.stringIsNull(type)) {
                return null;
            }
            int typeInt = CalmLakeStringUtil.stringToInt(type);
            List<OpcItems> opcItemsListGroups = opcItemsMapperImpl.selectByGroups();
            OpcItems opcItems = new OpcItems();
            List<OpcItems> opcItemsLists = opcItemsMapperImpl.selectByOpcItems(opcItems);
            if (typeInt == 5) {
                typeInt = 4;
                for (OpcItems opcItems1 : opcItemsListGroups) {
                    MachineInfo machineInfo = machineInfoMapperImpl.selectByPrimaryKey(opcItems1.getMachineinfoid());
                    if (machineInfo.getType() == typeInt) {
                        for (int i = 1; i < 7; i++) {
                            String string = "";
                            String name = "";
                            if (i == 1) {
                                string = "YiHaoWei";
                                name = "一号位";
                            } else if (i == 2) {
                                string = "ErHaoWei";
                                name = "二号位";
                            } else if (i == 3) {
                                string = "SanHaoWei";
                                name = "三号位";
                            } else if (i == 4) {
                                string = "SiHaoWei";
                                name = "四号位";
                            } else if (i == 5) {
                                string = "WuHaoWei";
                                name = "五号位";
                            } else if (i == 6) {
                                string = "LiuHaoWei";
                                name = "六号位";
                            }
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", name);
                            for (OpcItems opcItems2 : opcItemsLists) {
                                if (opcItems2.getItem().contains(string)) {
                                    jsonObject.put(opcItems2.getItem().split("\\.")[2].substring(0, opcItems2.getItem().split("\\.")[2].length() - string.length()), booleanToInt(OpcServer.monitoringMap.get(opcItems2.getItem()).toString()));
                                }
                            }
                            jsonArray.add(jsonObject);
                        }
                    }
                }
                return jsonArray;
            }
            for (OpcItems opcItems1 : opcItemsListGroups) {
                MachineInfo machineInfo = machineInfoMapperImpl.selectByPrimaryKey(opcItems1.getMachineinfoid());
                if (machineInfo.getType() == typeInt) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", machineInfo.getName());
                    for (OpcItems opcItems2 : opcItemsLists) {
                        if (opcItems2.getItem().contains(opcItems1.getGroups())) {
                            jsonObject.put(opcItems2.getItem().split("\\.")[2], booleanToInt(OpcServer.monitoringMap.get(opcItems2.getItem()).toString()));
                        }
                    }
                    jsonArray.add(jsonObject);
                }
            }
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            loggerUtil.getLogger().error("获取数据出错：" + e.getMessage());
        }
        return null;
    }


    public String booleanToInt(String string) {
        if (string.contains("true")) {
            return "1";
        } else if (string.contains("false")) {
            return "0";
        } else {
            return string;
        }
    }

    /**
     * 同步监控状态
     *
     * @param request
     * @return
     */
    @RequestMapping("checkMonitorStatus")
    @ResponseBody
    public JSONObject checkMonitorStatus(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        boolean result = false;
        int count = 0;
        if (OpcServer.opcServerModels.size() > 0) {
            for (OpcServerModel opcServerModel : OpcServer.opcServerModels) {
                if (opcServerModel.getKey().equals(OpcServer.KEYREAD) && opcServerModel.getStatus() == 1 && opcServerModel.getAccessBase().isBound()) {
                    jsonObject.put("result", true);
                    jsonObject.put("msg", "成功，正在监控");
                    jsonObject.put("code", 100);
                    count++;
                    break;
                }
            }
            if (count < 1) {
                jsonObject.put("result", result);
                jsonObject.put("msg", "失败，监控关闭");
                jsonObject.put("code", 101);
            }
        } else {
            jsonObject.put("result", result);
            jsonObject.put("msg", "失败，无server");
            jsonObject.put("code", 101);
        }
        return jsonObject;
    }

    /**
     * 发送动作指令
     *
     * @param request
     * @return
     */
    @RequestMapping("wcsSendMsg")
    @ResponseBody
    public JSONObject wcsSendMsg(HttpServletRequest request) {
        String json = request.getParameter("msg");
        boolean result = false;
        WCSControlInfo wcsControlInfo = new WCSControlInfo();
        if (CalmLakeStringUtil.stringIsNull(json)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", false);
            return jsonObject;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        Random random = new Random();
        //设备序号
        int no = jsonObject.getIntValue("machineId");
        //层
        int storey = jsonObject.getIntValue("machineCeng");
        //列
        int line = jsonObject.getIntValue("machineLie");
        //排
        int row = jsonObject.getIntValue("machinePai");
        //指令
        int command = jsonObject.getIntValue("machineCommand");
        //任务码
        int wcsTaskNo = random.nextInt(10000);
        wcsControlInfo.setMachineinfoid(no);
        wcsControlInfo.setY((short) storey);
        wcsControlInfo.setX((short) line);
        wcsControlInfo.setZ((short) row);
        wcsControlInfo.setMovementid((byte) command);
        wcsControlInfo.setWcstaskno((short) wcsTaskNo);
        wcsControlInfo.setCreatetime(new Date());
        wcsControlInfo.setStatus((byte) 0);
        int i = wCSControlInfoMapperImpl.insertSelective(wcsControlInfo);
        if (i > 0) {
            jsonObject.put("result", true);
        } else {
            jsonObject.put("result", false);
        }
//        if (no == 4 || no == 5 || no == 6) {
//            ZiCheMsgModel msgModel = new ZiCheMsgModel();
//            msgModel.setCommandNum(command);
//            msgModel.setNO(no);
//            msgModel.setOrderNum(wcsTaskNo);
//            msgModel.setTargetLine(line);
//            msgModel.setTargetRow(row);
//            msgModel.setTargetStorey(storey);
//            result = OpcServer.getInstance().write(msgModel);
//            updateWcsStarus(result, wcsControlInfo);
//            jsonObject.put("result", result);
//        } else if (no == 7 || no == 8 || no == 9) {
//            MuCheMsgModel msgModel = new MuCheMsgModel();
//            msgModel.setCommandNum(command);
//            msgModel.setNO(no);
//            msgModel.setOrderNum(wcsTaskNo);
//            msgModel.setTargetLine(line);
//            result = OpcServer.getInstance().write(msgModel);
//            updateWcsStarus(result, wcsControlInfo);
//            jsonObject.put("result", result);
//        } else if (no == 10) {
//            DuiDuoJiMsgModel msgModel = new DuiDuoJiMsgModel();
//            msgModel.setCommandNum(command);
//            msgModel.setNO(no);
//            msgModel.setOrderNum(wcsTaskNo);
//            msgModel.setTargetLine(line);
//            msgModel.setTargetStorey(storey);
//            result = OpcServer.getInstance().write(msgModel);
//            updateWcsStarus(result, wcsControlInfo);
//            jsonObject.put("result", result);
//        } else if (no == 11) {
//            ShengJiangJiMsgModel msgModel = new ShengJiangJiMsgModel();
//            msgModel.setCommandNum(command);
//            msgModel.setNO(no);
//            msgModel.setOrderNum(wcsTaskNo);
//            msgModel.setTargetStorey(storey);
//            result = OpcServer.getInstance().write(msgModel);
//            updateWcsStarus(result, wcsControlInfo);
//            jsonObject.put("result", result);
//        }
        return jsonObject;
    }


    public static void main(String[] arg) {
        String sss = "machine.shengJiangJi.ZhuangTaiWuHaoWei";
    }
}

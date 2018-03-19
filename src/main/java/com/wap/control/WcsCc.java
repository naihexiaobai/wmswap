package com.wap.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.www.util.CalmLakeStringUtil;
import com.www.util.LoggerUtil;
import com.wap.control.Thread.OutStorageThread;
import com.wap.model.Cargo;
import com.wap.model.OpcItemFinalString;
import com.wap.model.Storage;
import com.wap.model.TaskHistory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * wcs 控制
 *
 * @auther CalmLake
 * @create 2017/11/27  8:54
 */
@Controller
@RequestMapping("wcsCc")
public class WcsCc extends ControlCc {

//    private LoggerUtil loggerUtil = new LoggerUtil("WcsCc");

    /**
     * 获取货位信息
     *
     * @param request data-层
     * @return
     */
    @RequestMapping("getStorage")
    @ResponseBody
    public JSONObject getStorage(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            String y = request.getParameter("data");
            if (CalmLakeStringUtil.stringIsNull(y)) {
                y = "1";
            }
            Storage storage = new Storage();
            storage.setY((short) CalmLakeStringUtil.stringToInt(y));
            List<Storage> storageList = storageMapperImpl.selectByStorage(storage);
            for (Storage storage1 : storageList) {
                JSONArray jsonArray1 = new JSONArray();
                for (Storage storage2 : storageList) {
                    JSONObject jsonObject1 = new JSONObject();
                    if (storage1.getX() == storage2.getX()) {
                        jsonObject1.put("x", storage2.getX());
                        jsonObject1.put("status", storage2.getStatus());
                        jsonObject1.put("z", storage2.getZ());
                        jsonObject1.put("y", storage2.getY());
                        jsonObject1.put("id", storage2.getId());
                        jsonObject1.put("storageNo", storage2.getStorageno());
                        jsonArray1.add(jsonObject1);
                    }
                }
                jsonObject.put("x" + storage1.getX().toString(), jsonArray1.toJSONString());
            }
//            loggerUtil.getLoggerLevelInfo().info("获取货位结果jsonArray --> " + jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("String转Int出错" + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 出库
     *
     * @param request - data
     * @return
     */
    @RequestMapping("outStorageCargo")
    @ResponseBody
    public JSONObject outStorageCargo(HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            String json = request.getParameter("data");
            if (CalmLakeStringUtil.stringIsNull(json)) {
                jsonObject.put("msg", "数据错误");
                jsonObject.put("result", false);
                return jsonObject;
            }
            JSONObject jsonObjectData = JSON.parseObject(json);
            String storageNo = jsonObjectData.containsKey("cargoStorageNoOut") ? jsonObjectData.getString("cargoStorageNoOut") : "";
            Storage storage = new Storage();
            Cargo cargo = new Cargo();
            TaskHistory taskHistory = new TaskHistory();
            storage.setStorageno(storageNo);
            cargo.setStorageid(storageNo);
            List<Storage> storageList = storageMapperImpl.selectByStorage(storage);
            List<Cargo> cargoList = cargoMapperImpl.selectByCargo(cargo);
            //出库条件
            if (cargoList.size() > 0 && storageList.size() > 0 && storageList.get(0).getStatus() == 1 && cargoList.get(0).getStatus() == 2) {
                taskHistory.setCargoid(cargoList.get(0).getId());
                taskHistory.setX(storageList.get(0).getX());
                taskHistory.setY(storageList.get(0).getY());
                taskHistory.setZ(storageList.get(0).getZ());
                taskHistory.setStartpoint(storageList.get(0).getStorageno());
                taskHistory.setEndpoint("A175");
                taskHistory.setPriority((byte) 1);
                taskHistory.setCreatetime(new Date());
                taskHistory.setStarttime(new Date());
                taskHistory.setStatus((byte) 1);
                taskHistory.setWcstaskno(getRandomNum(10000));
                //堆垛机状态
                JSONObject jsonObjectDuiDuoJi = getDuiDuoJiStatus();
//                if (!jsonObjectDuiDuoJi.getBoolean("result")) {
//                    jsonObject.put("msg", "堆垛机状态不正确");
//                    jsonObject.put("result", false);
//                    return jsonObject;
//                }
                //子车状态
                JSONObject jsonObjectZiChe = getZiCheStatus(OpcItemFinalString.ERHAOZICHEGROUP);
//                if (!jsonObjectZiChe.getBoolean("result")) {
//                    jsonObject.put("msg", "子车状态不正确");
//                    jsonObject.put("result", false);
//                    return jsonObject;
//                }
                taskHistoryMapperImpl.insertSelective(taskHistory);
                //TODO  线程起始处 -  异步生成任务
                OutStorageThread outStorageThread = new OutStorageThread(taskHistory, jsonObjectDuiDuoJi.toJSONString(), jsonObjectZiChe.toJSONString());
                new Thread(outStorageThread).start();
                jsonObject.put("msg", "开始任务");
                jsonObject.put("result", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}

package com.wap.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kepware.opc.dto.OpcMonitor;
import com.kepware.opc.dto.command.*;
import com.kepware.opc.dto.status.*;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.enums.status.*;
import com.kepware.opc.service.enums.type.OrderTypeEnum;
import com.kepware.opc.service.enums.type.RouteTypeEnum;
import com.kepware.opc.service.operation.StationOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.kepware.opc.thread.monitor.MonitorThread;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.entity.Plc;
import com.thief.wcs.entity.Route;
import com.thief.wcs.entity.RouteSite;
import com.wap.model.Cargo;
import com.www.util.CalmLakeStringUtil;
import com.wap.model.Storage;
import com.www.util.LoggerUtil;
import com.www.util.SpringTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jinterop.dcom.common.JIException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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


    @RequestMapping("deleteRoute")
    @ResponseBody
    public JSONObject deleteRoute(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("msg", "数据为空");
            jsonObject.put("result", false);
            return jsonObject;
        }
        Route route = routeMapper.selectByPrimaryKey(Integer.valueOf(data));
        routeSiteMapper.deleteByRouteId(route.getId());
        routeMapper.deleteByPrimaryKey(route.getId());
        jsonObject.put("result", true);
        return jsonObject;
    }

    @RequestMapping("addRoute")
    @ResponseBody
    public JSONObject addRoute(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("msg", "数据为空");
            jsonObject.put("result", false);
            return jsonObject;
        }
        try {
            JSONObject jsonObjectData = JSONObject.parseObject(data);
            String startBlockNo = jsonObjectData.getString("startBlockNo");
            String endBlockNo = jsonObjectData.getString("endBlockNo");
            Byte routeType = jsonObjectData.getByte("routeType");
            Route route = new Route();
            route.setStartblockno(startBlockNo);
            route.setEndblockno(endBlockNo);
            route.setType(routeType);
            route.setStatus(Byte.valueOf("0"));
            routeMapper.insertSelective(route);
            jsonObject.put("msg", "成功");
            jsonObject.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "异常中断");
            jsonObject.put("result", false);
        }
        return jsonObject;
    }


    @RequestMapping("getRoute")
    @ResponseBody
    public JSONArray getRoute(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        List<Route> routeList = routeMapper.selectAll();
        for (Route route : routeList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", route.getId());
            jsonObject.put("startBlockNo", route.getStartblockno());
            jsonObject.put("endBlockNo", route.getEndblockno());
            jsonObject.put("type", RouteTypeEnum.getName(route.getType()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @RequestMapping("getRouteSite")
    @ResponseBody
    public JSONArray getRouteSite(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        String data = request.getParameter("routeId");
        if (StringUtils.isEmpty(data)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "数据为空");
            jsonObject.put("result", false);
            jsonArray.add(jsonObject);
            return jsonArray;
        }
        List<RouteSite> routeSiteList = routeSiteMapper.selectByRouteId(Integer.valueOf(data));
        for (RouteSite routeSite : routeSiteList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("routeSiteId", routeSite.getId());
            jsonObject.put("presentBlockNo", routeSite.getPresentblockno());
            jsonObject.put("nextBlockNo", routeSite.getNextblockno());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @RequestMapping("startMonitor")
    @ResponseBody
    public JSONObject startMonitor(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("msg", "数据为空");
            jsonObject.put("result", false);
            return jsonObject;
        }
        OpcBlock opcBlock = opcBlockMapper.selectByPrimaryKey(Integer.valueOf(data));
        Plc plc = plcMapper.selectByPlcName(opcBlock.getPlcname());
        if (plc.getStatus() == 0) {
            jsonObject.put("msg", "监控已开启");
            jsonObject.put("result", false);
            return jsonObject;
        }
        List<OpcItem> opcItemList = opcItemMapper.selectAll();
        //2.1 开启监控线程
        List<OpcItem> opcItems = new ArrayList<OpcItem>();
        for (OpcItem opcItem : opcItemList) {
            if (plc.getId() == opcItem.getMachineinfoid()) {
                opcItems.add(opcItem);
            }
        }
        new Thread(new MonitorThread.MachineThread(opcItems, plc.getPlcname())).start();
        //2.2 初始化写入item
        for (OpcItem opcItem : opcItemList) {
            if (plc.getId() == opcItem.getMachineinfoid()) {
                MonitorThread.OpcWriteItem(opcItem);
            }
        }

        plc.setStatus(Plc._STATUS_SUCCESS);
        opcBlock.setStatus(Plc._STATUS_SUCCESS);
        plcMapper.updateByPrimaryKeySelective(plc);
        opcBlockMapper.updateByPrimaryKeySelective(opcBlock);
        jsonObject.put("result", true);
        return jsonObject;
    }


    @RequestMapping("shutMonitor")
    @ResponseBody
    public JSONObject shutMonitor(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        OpcBlock opcBlock = opcBlockMapper.selectByPrimaryKey(Integer.valueOf(data));
        List<OpcMonitor> opcMonitorList = OpcDBDataCacheCenter.instance().getOpcMonitorList();
        try {
            for (OpcMonitor opcMonitor : opcMonitorList) {
                if (opcBlock.getPlcname().equals(opcMonitor.getPlcName())) {
                    opcMonitor.setMonitorStatus(false);
                    opcMonitor.getAccessBase().unbind();
                    opcMonitor.getServer().dispose();
                    opcBlock.setStatus(Plc._STATUS_DISABLED);
                    Plc plc = plcMapper.selectByPlcName(opcBlock.getPlcname());
                    plc.setStatus(Plc._STATUS_DISABLED);
                    plcMapper.updateByPrimaryKeySelective(plc);
                    opcBlockMapper.updateByPrimaryKeySelective(opcBlock);
//                    OpcDBDataCacheCenter.getMonitorBlockStatusMap().remove(plc.getPlcname());
                    jsonObject.put("result", true);
                }
            }
        } catch (JIException e) {
            e.printStackTrace();
            LoggerUtil.getLoggerByName("WcsCc").info("关闭监控异常：" + e.getMessage());
            jsonObject.put("result", false);
        }
        return jsonObject;
    }

    /**
     * 人工完成
     *
     * @param request
     * @return
     */
    @RequestMapping("finishOrder")
    @ResponseBody
    public JSONObject finishOrder(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        StationOperation stationOperation = new StationOperation();
        OpcOrder opcOrder = opcOrderMapper.selectByPrimaryKey(Integer.valueOf(data));
        try {
            if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                stationOperation.finishBePutInStorageByPeople(opcOrder.getOrderkey());
                BlockOperationDBUtil.getInstance().updateOpcBlockBykey(opcOrder.getOrderkey());
                jsonObject.put("result", true);
            } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
                stationOperation.finishOutPutWorkByPeople(opcOrder.getOrderkey());
                BlockOperationDBUtil.getInstance().updateOpcBlockBykey(opcOrder.getOrderkey());
                jsonObject.put("result", true);
            } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                stationOperation.finishBeMoveInStorageByPeople(opcOrder.getOrderkey());
                BlockOperationDBUtil.getInstance().updateOpcBlockBykey(opcOrder.getOrderkey());
                jsonObject.put("result", true);
            } else {
                LoggerUtil.getLoggerByName("WcsCc").info("人工完成,暂无该订单类型！");
                jsonObject.put("result", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.getLoggerByName("WcsCc").info("人工完成异常：" + e.getMessage());
            jsonObject.put("result", false);
        }
        return jsonObject;
    }

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    @RequestMapping("deleteOrder")
    @ResponseBody
    public JSONObject deleteOrder(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        int orderId = Integer.valueOf(data);
        OpcOrder opcOrder = opcOrderMapper.selectByPrimaryKey(orderId);
        if (opcOrder == null || opcOrder.getId() < 1) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            String storageNo = opcOrder.getTolocation();
            Storage storage = storageMapperImpl.selectByStorageNo(storageNo);
            if (storage != null || storage.getId() > 0) {
                storage.setStatus(Storage.STATUS_FREE);
                storageMapperImpl.updateByPrimaryKeySelective(storage);
            }
            Cargo cargo = cargoMapperImpl.selectByCargoStorageNo(storageNo);
            if (cargo != null || cargo.getId() > 0) {
                cargoMapperImpl.deleteByPrimaryKey(cargo.getId());
            }
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            String storageNo = opcOrder.getFromlocation();
            Storage storage = storageMapperImpl.selectByStorageNo(storageNo);
            if (storage != null || storage.getId() > 0) {
                storage.setStatus(Storage.STATUS_USEING);
                storageMapperImpl.updateByPrimaryKeySelective(storage);
            }
            Cargo cargo = cargoMapperImpl.selectByCargoStorageNo(storageNo);
            if (cargo != null || cargo.getId() > 0) {
                cargo.setStatus(Cargo.STATUS_INFISH_TWO);
                cargoMapperImpl.updateByPrimaryKey(cargo);
            }
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
            String storageNoTo = opcOrder.getTolocation();
            Storage storageTo = storageMapperImpl.selectByStorageNo(storageNoTo);
            if (storageTo != null || storageTo.getId() > 0) {
                storageTo.setStatus(Storage.STATUS_FREE);
                storageMapperImpl.updateByPrimaryKeySelective(storageTo);
            }
            String storageNoFrom = opcOrder.getFromlocation();
            Storage storageFrom = storageMapperImpl.selectByStorageNo(storageNoFrom);
            if (storageFrom != null || storageFrom.getId() > 0) {
                storageFrom.setStatus(Storage.STATUS_USEING);
                storageMapperImpl.updateByPrimaryKeySelective(storageFrom);
            }
            Cargo cargo = cargoMapperImpl.selectByCargoStorageNo(storageNoFrom);
            if (cargo != null || cargo.getId() > 0) {
                cargo.setStatus(Cargo.STATUS_INING_ONE);
                cargoMapperImpl.updateByPrimaryKey(cargo);
            }
        } else {
            //TODO  订单种类暂无
            jsonObject.put("result", false);
            return jsonObject;
        }
        String orderKey = opcOrder.getOrderkey();
        List<OpcBlock> opcBlockList = opcBlockMapper.selectAll();
        for (OpcBlock opcBlock : opcBlockList) {
            boolean result = false;
            if (orderKey.equals(opcBlock.getMckey())) {
                opcBlock.setMckey("");
                result = true;
            }
            if (orderKey.equals(opcBlock.getReservedmckey())) {
                opcBlock.setReservedmckey("");
                result = true;
            }
            if (result) {
                opcBlockMapper.updateByPrimaryKeySelective(opcBlock);
            }
        }
        opcOrder.setStatus(OpcOrder.STATUS_CANCEL);
        opcOrderMapper.updateByPrimaryKeySelective(opcOrder);
        opcBlockMapper.updateByKey(opcOrder.getOrderkey());
        jsonObject.put("result", true);
        return jsonObject;
    }

    @RequestMapping("getCargoDate")
    @ResponseBody
    public JSONArray getCargoDate() {
        JSONArray jsonArray = new JSONArray();

        return jsonArray;
    }

    @RequestMapping("deleteCargo")
    @ResponseBody
    public JSONObject deleteCargo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        Cargo cargo = cargoMapperImpl.selectByPrimaryKey(Integer.valueOf(data));
        if (StringUtils.isEmpty(cargo.getStorageid())) {

        } else {
            Storage storage = storageMapperImpl.selectByStorageNo(cargo.getStorageid());
            if (cargo == null || storage == null) {
                jsonObject.put("result", false);
                return jsonObject;
            }
            storage.setStatus(Storage.STATUS_FREE);
            storageMapperImpl.updateByPrimaryKeySelective(storage);
        }
        cargoMapperImpl.deleteByPrimaryKey(cargo.getId());
        jsonObject.put("result", true);
        return jsonObject;
    }


    @RequestMapping("addCargoDate")
    @ResponseBody
    public JSONObject addCargoDate(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        JSONObject jsonObjectDate = JSONObject.parseObject(data);
        String cargoPalletNO = jsonObjectDate.getString("cargoPalletNO");
        String cargoName = jsonObjectDate.getString("cargoName");
        String cargoNumber = jsonObjectDate.getString("cargoNumber");
        String cargoQuality = jsonObjectDate.getString("cargoQuality");
        String cargoBatchNo = jsonObjectDate.getString("cargoBatchNo");
        String cargoSpecific = jsonObjectDate.getString("cargoSpecifiction");
        Date cargoProductionDate = jsonObjectDate.getDate("cargoProductionDate");
        String cargoShelfLife = jsonObjectDate.getString("cargoShelfLife");
        Cargo cargo = new Cargo();
        cargo.setStatus(Cargo.STATUS_DEFAULT_ZERO);
        cargo.setBatchno(cargoBatchNo);
        cargo.setName(cargoName);
        cargo.setNumber(Short.valueOf(cargoNumber));
        cargo.setPalletno(cargoPalletNO);
        cargo.setQuality(cargoQuality);
        cargo.setProductiondate(cargoProductionDate);
        cargo.setSpecifiction(cargoSpecific);
        cargo.setShelflife(Short.valueOf(cargoShelfLife));
        cargoMapperImpl.insert(cargo);
        jsonObject.put("result", true);
        return jsonObject;
    }


    @RequestMapping("getCargoDetail")
    @ResponseBody
    public JSONArray getCargoDetail() {
        JSONArray jsonArray = new JSONArray();
        List<Cargo> cargoList = cargoMapperImpl.selectAll();
        for (Cargo cargo : cargoList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", cargo.getId());
            jsonObject.put("palletNo", cargo.getPalletno());
            jsonObject.put("name", cargo.getName());
            jsonObject.put("batchNo", cargo.getBatchno());
            jsonObject.put("productionDate", DateFormatUtils.format(cargo.getProductiondate(), "yyyy-MM-dd HH:mm:ss"));
            jsonObject.put("quality", cargo.getQuality());
            jsonObject.put("number", cargo.getNumber());
            jsonObject.put("shelfLife", cargo.getShelflife());
            jsonObject.put("specific", cargo.getSpecifiction());
            jsonObject.put("storageNo", cargo.getStorageid());
            jsonObject.put("status", CargoEnum.getName(cargo.getStatus()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

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
        } catch (Exception e) {
            e.printStackTrace();
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
            JSONObject jsonObjectData = JSON.parseObject(json);
            String cargoStorageNo = jsonObjectData.getString("cargoStorageNo").trim();
            Storage storage = storageMapperImpl.selectByStorageNo(cargoStorageNo);
            if (storage.getStatus() != Storage.STATUS_USEING) {
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！库位数据错误！");
                jsonObject.put("result", false);
                return jsonObject;
            }
            //TODO   校验当前货位是否可以出货
            Cargo cargo = cargoMapperImpl.selectByCargoStorageNo(cargoStorageNo);
            if (cargo == null || cargo.getStatus() != Cargo.STATUS_INFISH_TWO) {
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！货物状态错误！");
                jsonObject.put("result", false);
                return jsonObject;
            }
            OpcOrder opcOrder = new OpcOrder();
            opcOrder.setStatus(OpcOrder.STATUS_WAIT);
            opcOrder.setWmsmckey("WMS" + CalmLakeStringUtil.getRandomNum());
            opcOrder.setCreatetime(new Date());
            opcOrder.setRouteid(3);
            opcOrder.setOrderkey("WAP" + System.currentTimeMillis());
            opcOrder.setFromlocation(cargoStorageNo);
            opcOrder.setTostation("LF02");
            opcOrder.setOrdertype(OpcOrder.ORDERTYPE_OUT);
            opcOrder.setBarcode(cargo.getPalletno());
            BlockOperationDBUtil.getInstance().insertIntoOpcOrder(opcOrder);
            LoggerUtil.getLoggerByName("wcsCc").info(opcOrder.getOrderkey() + ",订单创建成功！");
            jsonObject.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
        }
        return jsonObject;
    }


    @RequestMapping("inStorageCargo")
    @ResponseBody
    public JSONObject inStorageCargo(HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            String json = request.getParameter("data");
            JSONObject jsonObjectData = JSON.parseObject(json);
            //            cargoStorageNo 库位码
            String cargoStorageNo = jsonObjectData.getString("cargoStorageNo").trim();
            Storage storage = storageMapperImpl.selectByStorageNo(cargoStorageNo);
            if (storage.getStatus() != Storage.STATUS_FREE) {
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！库位数据错误！");
                jsonObject.put("result", false);
                return jsonObject;
            }
            //            cargoPalletNO 托盘号
            String cargoPalletNO = jsonObjectData.getString("cargoPalletNO").trim();
            Cargo cargo = cargoMapperImpl.selectByCargoPalletNo(cargoPalletNO);
            if (cargo == null) {
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！货物数据错误！没有该托盘号!");
                jsonObject.put("result", false);
                return jsonObject;
            }
            OpcOrder opcOrder = new OpcOrder();
            opcOrder.setStatus(OpcOrder.STATUS_WAIT);
            opcOrder.setWmsmckey("WMS" + CalmLakeStringUtil.getRandomNum());
            opcOrder.setCreatetime(new Date());
            if (storage.getY() == 1) {
                opcOrder.setRouteid(9);
            } else if (storage.getY() == 2) {
                //TODO 二层子车 1号,6 或者 2号,8 ，现在未判断
                opcOrder.setRouteid(6);
            } else if (storage.getY() == 3) {
                //TODO  三层无设备暂不做处理
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！三层暂未处理！");
                jsonObject.put("result", false);
                return jsonObject;
            }
            opcOrder.setOrderkey("WAP" + System.currentTimeMillis());
            opcOrder.setTolocation(cargoStorageNo);
            opcOrder.setFromstation("LF02");
            opcOrder.setOrdertype(OpcOrder.ORDERTYPE_IN);
            opcOrder.setBarcode(cargoPalletNO);
            BlockOperationDBUtil.getInstance().insertIntoOpcOrder(opcOrder);
            LoggerUtil.getLoggerByName("wcsCc").info(opcOrder.getOrderkey() + ",订单创建成功！");
            jsonObject.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
        }
        return jsonObject;
    }


    @RequestMapping("moveStorageCargo")
    @ResponseBody
    public JSONObject moveStorageCargo(HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            String json = request.getParameter("data");
            JSONObject jsonObjectData = JSON.parseObject(json);
            String cargoStorageNoOut = jsonObjectData.getString("cargoStorageNoMoveOut").trim();
            String cargoStorageNoIn = jsonObjectData.getString("cargoStorageNoMoveIn").trim();
            Storage storageOut = storageMapperImpl.selectByStorageNo(cargoStorageNoOut);
            Storage storageIn = storageMapperImpl.selectByStorageNo(cargoStorageNoIn);
            if (storageOut.getStatus() != Storage.STATUS_USEING || storageIn.getStatus() != Storage.STATUS_FREE) {
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！库位数据错误！");
                jsonObject.put("result", false);
                return jsonObject;
            }
            Cargo cargo = cargoMapperImpl.selectByCargoStorageNo(cargoStorageNoOut);
            if (cargo == null || cargo.getStatus() != Cargo.STATUS_INFISH_TWO) {
                LoggerUtil.getLoggerByName("wcsCc").info("订单创建失败！货物状态错误！");
                jsonObject.put("result", false);
                return jsonObject;
            }
            OpcOrder opcOrder = new OpcOrder();
            opcOrder.setStatus(OpcOrder.STATUS_WAIT);
            opcOrder.setWmsmckey("WMS" + CalmLakeStringUtil.getRandomNum());
            opcOrder.setCreatetime(new Date());
            opcOrder.setRouteid(5);
            opcOrder.setOrderkey("WAP" + System.currentTimeMillis());
            opcOrder.setTolocation(cargoStorageNoIn);
            opcOrder.setFromlocation(cargoStorageNoOut);
            opcOrder.setOrdertype(OpcOrder.ORDERTYPE_MOVE);
            //托盘号
            opcOrder.setBarcode(cargo.getPalletno());
            BlockOperationDBUtil.getInstance().insertIntoOpcOrder(opcOrder);
            LoggerUtil.getLoggerByName("wcsCc").info(opcOrder.getOrderkey() + ",订单创建成功！");
            jsonObject.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
        }
        return jsonObject;
    }

    @RequestMapping("getMonitorData")
    @ResponseBody
    public JSONArray getMonitorData(HttpServletRequest request) {
        //1.子车 2.母车 3.堆垛机 4.升降机
        String type = request.getParameter("type");
        Plc plc = new Plc();
        List<Plc> plcList = ((PlcMapper) SpringTool.getBeanByClass(PlcMapper.class)).selectByPlc(plc);
        //2.开启线程
        JSONArray jsonArray = new JSONArray();
        try {
            for (Plc plc1 : plcList) {
                if (plc1.getStatus() != 9) {
                    if (type.equals("1") && plc1.getPlcname().contains("SC")) {
                        JSONObject jsonObject1 = getScJSON(plc1.getPlcname());
                        jsonArray.add(jsonObject1);
                    } else if (type.equals("2") && plc1.getPlcname().contains("MC")) {
                        JSONObject jsonObject1 = getMcJSON(plc1.getPlcname());
                        jsonArray.add(jsonObject1);
                    } else if (type.equals("3") && plc1.getPlcname().contains("ML")) {
                        JSONObject jsonObject1 = getMlJSON(plc1.getPlcname());
                        jsonArray.add(jsonObject1);
                    } else if (type.equals("4") && plc1.getPlcname().contains("EL")) {
                        JSONObject jsonObject1 = getElJSON(plc1.getPlcname());
                        jsonArray.add(jsonObject1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONObject getScJSON(String blockNo) throws Exception {
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", blockNo);
            jsonObject.put("auto", scBlockStatus.isAuto());
            jsonObject.put("onStandBy", scBlockStatus.isOnStandby());
            jsonObject.put("free", scBlockStatus.isFree());
            jsonObject.put("A", scBlockStatus.isAOrigin());
            jsonObject.put("B", scBlockStatus.isBOrigin());
            jsonObject.put("load", scBlockStatus.isLoad());
            jsonObject.put("error", scBlockStatus.isError());
            jsonObject.put("charging", scBlockStatus.isCharging());
            jsonObject.put("elevator", scBlockStatus.isElevator());
            jsonObject.put("RGV", scBlockStatus.isRGV());
            jsonObject.put("kWh", scBlockStatus.getkWh());
            jsonObject.put("plcTaskID", scBlockStatus.getPlcTaskID());
            jsonObject.put("line", scBlockStatus.getLine());
            jsonObject.put("row", scBlockStatus.getRow());
            jsonObject.put("tier", scBlockStatus.getTier());
            jsonObject.put("command", scBlockStatus.getCommand());
            jsonObject.put("taskID", scBlockStatus.getTaskID());
            jsonObject.put("lastTime", DateFormatUtils.format(scBlockStatus.getLastUpdateTime(), "yyyy-MM-dd HH:mm:ss:SSS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public JSONObject getMcJSON(String blockNo) throws Exception {
        McBlockStatus mcBlockStatus = (McBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", blockNo);
            jsonObject.put("auto", mcBlockStatus.isAuto());
            jsonObject.put("onStandBy", mcBlockStatus.isOnStandby());
            jsonObject.put("free", mcBlockStatus.isFree());
            jsonObject.put("load", mcBlockStatus.isLoad());
            jsonObject.put("theCar", mcBlockStatus.isTheCar());
            jsonObject.put("error", mcBlockStatus.isError());
            jsonObject.put("plcTaskID", mcBlockStatus.getPlcTaskID());
            jsonObject.put("line", mcBlockStatus.getLine());
            jsonObject.put("command", mcBlockStatus.getCommand());
            jsonObject.put("targetLine", mcBlockStatus.getTargetLine());
            jsonObject.put("taskID", mcBlockStatus.getTaskID());
            jsonObject.put("lastTime", DateFormatUtils.format(mcBlockStatus.getLastUpdateTime(), "yyyy-MM-dd HH:mm:ss:SSS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public JSONObject getMlJSON(String blockNo) throws Exception {
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", blockNo);
            jsonObject.put("auto", mlBlockStatus.isAuto());
            jsonObject.put("onStandBy", mlBlockStatus.isOnStandby());
            jsonObject.put("free", mlBlockStatus.isFree());
            jsonObject.put("load", mlBlockStatus.isLoad());
            jsonObject.put("theCar", mlBlockStatus.isTheCar());
            jsonObject.put("error", mlBlockStatus.isError());
            jsonObject.put("plcTaskID", mlBlockStatus.getPlcTaskID());
            jsonObject.put("line", mlBlockStatus.getLine());
            jsonObject.put("tier", mlBlockStatus.getTier());
            jsonObject.put("targetTier", mlBlockStatus.getTargetTier());
            jsonObject.put("command", mlBlockStatus.getCommand());
            jsonObject.put("targetLine", mlBlockStatus.getTargetLine());
            jsonObject.put("taskID", mlBlockStatus.getTaskID());
            jsonObject.put("lastTime", DateFormatUtils.format(mlBlockStatus.getLastUpdateTime(), "yyyy-MM-dd HH:mm:ss:SSS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getElJSON(String blockNo) throws Exception {
        ElBlockStatus elBlockStatus = (ElBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", blockNo);
            jsonObject.put("auto", elBlockStatus.isAuto());
            jsonObject.put("onStandBy", elBlockStatus.isOnStandby());
            jsonObject.put("free", elBlockStatus.isFree());
            jsonObject.put("load", elBlockStatus.isLoad());
            jsonObject.put("theCar", elBlockStatus.isTheCar());
            jsonObject.put("error", elBlockStatus.isError());
            jsonObject.put("plcTaskID", elBlockStatus.getPlcTaskID());
            jsonObject.put("tier", elBlockStatus.getTier());
            jsonObject.put("targetTier", elBlockStatus.getTargetTier());
            jsonObject.put("command", elBlockStatus.getCommand());
            jsonObject.put("taskID", elBlockStatus.getTaskID());
            jsonObject.put("lastTime", DateFormatUtils.format(elBlockStatus.getLastUpdateTime(), "yyyy-MM-dd HH:mm:ss:SSS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @RequestMapping("controlMsg")
    @ResponseBody
    public JSONObject controlMsg(HttpServletRequest request) {
        JSONObject jsonObject1 = new JSONObject();
        String msg = request.getParameter("msg");
        BlockCommand blockCommand = null;
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //操作类型
        String machineType = jsonObject.getString("machineType").trim();
        //设备blockNo
        String targetMachineBlockNo = jsonObject.getString("targetMachineId").trim();
        //层
        String tier = jsonObject.getString("machineCeng").trim();
        //列
        String line = jsonObject.getString("machineLie").trim();
        //排
        String row = jsonObject.getString("machinePai").trim();
        //指令
        String command = jsonObject.getString("machineCommand").trim();
        String taskID = CalmLakeStringUtil.getRandomNum();
        if (targetMachineBlockNo.contains("SC")) {
            blockCommand = ScBlockCommand.createMcBlockCommand(command, line, tier, row, taskID);
        } else if (targetMachineBlockNo.contains("MC")) {
            blockCommand = McBlockCommand.createMcBlockCommand(command, line, taskID);
        } else if (targetMachineBlockNo.contains("ML")) {
            blockCommand = MlBlockCommand.createMlBlockCommand(line, tier, command, taskID);
        } else if (targetMachineBlockNo.contains("EL")) {
            blockCommand = ElBlockCommand.createElBlockCommand(command, tier, taskID);
        }
        OpcWrite.instance().writeByBlockCommand(blockCommand, targetMachineBlockNo);
        jsonObject1.put("result", true);
        return jsonObject1;
    }

    @RequestMapping("getOrder")
    @ResponseBody
    public JSONArray getOrder() {
        JSONArray jsonArray = new JSONArray();
        List<OpcOrder> opcOrderList = BlockOperationDBUtil.getInstance().getOpcOrderListMaxTen();
        for (OpcOrder opcOrder : opcOrderList) {
            JSONObject jsonObject = new JSONObject();
            String startTime = "";
            String endTime = "";
            if (opcOrder.getStarttime() != null) {
                startTime = DateFormatUtils.format(opcOrder.getStarttime(), "yyyy-MM-dd HH:mm:ss");
            }
            if (opcOrder.getEndtime() != null) {
                endTime = DateFormatUtils.format(opcOrder.getEndtime(), "yyyy-MM-dd HH:mm:ss");
            }
            jsonObject.put("id", opcOrder.getId());
            jsonObject.put("orderKey", opcOrder.getOrderkey());
            jsonObject.put("barCode", opcOrder.getBarcode());
            jsonObject.put("wmsMcKey", opcOrder.getWmsmckey());
            jsonObject.put("createTime", DateFormatUtils.format(opcOrder.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("orderType", OrderTypeEnum.getName(opcOrder.getOrdertype()));
            jsonObject.put("routeId", opcOrder.getRouteid());
            jsonObject.put("fromStation", opcOrder.getFromstation());
            jsonObject.put("toStation", opcOrder.getTostation());
            jsonObject.put("fromLocation", opcOrder.getFromlocation());
            jsonObject.put("toLocation", opcOrder.getTolocation());
            jsonObject.put("status", OrderStatusEnum.getName(opcOrder.getStatus()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    @RequestMapping("getOpcWcsInfo")
    @ResponseBody
    public JSONArray getOpcWcsInfo(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        List<OpcWcsControlInfo> opcWcsControlInfoList = opcWcsControlInfoMapper.selectAll();
        for (OpcWcsControlInfo opcWcsControlInfo : opcWcsControlInfoList) {
            String startTime = "";
            String endTime = "";
            if (opcWcsControlInfo.getCreatetime() != null) {
                startTime = DateFormatUtils.format(opcWcsControlInfo.getCreatetime(), "yyyy-MM-dd HH:mm:ss:SSS");
            }
            if (opcWcsControlInfo.getEndtime() != null) {
                endTime = DateFormatUtils.format(opcWcsControlInfo.getEndtime(), "yyyy-MM-dd HH:mm:ss:SSS");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", opcWcsControlInfo.getId());
            jsonObject.put("blockNo", opcWcsControlInfo.getBlockno());
            jsonObject.put("orderKey", opcWcsControlInfo.getOrderkey());
            jsonObject.put("createTime", startTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("status", OpcWcsControlInfoEnum.getName(opcWcsControlInfo.getStatus()));
            jsonObject.put("taskID", opcWcsControlInfo.getWcstaskno());
            jsonObject.put("command", opcWcsControlInfo.getMovementid());
            jsonObject.put("x", opcWcsControlInfo.getX());
            jsonObject.put("y", opcWcsControlInfo.getY());
            jsonObject.put("z", opcWcsControlInfo.getZ());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @RequestMapping("getOpcBlock")
    @ResponseBody
    public JSONArray getOpcBlock(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        List<OpcBlock> opcBlockList = opcBlockMapper.selectAll();
        for (OpcBlock opcBlock : opcBlockList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", opcBlock.getId());
            jsonObject.put("blockNo", opcBlock.getBlockno());
            jsonObject.put("mcKey", opcBlock.getMckey());
            jsonObject.put("receivedMcKey", opcBlock.getReservedmckey());
            if (opcBlock.getStatus() == 1) {
                jsonObject.put("status", "启用");
            } else {
                jsonObject.put("status", "禁用");
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    @RequestMapping("sendBlockNo")
    @ResponseBody
    public JSONObject sendBlockNo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("data");
        if (StringUtils.isEmpty(id)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        String key = "";
        OpcBlock opcBlock = opcBlockMapper.selectByPrimaryKey(Integer.valueOf(id));
        if (StringUtils.isNotEmpty(opcBlock.getMckey())) {
            key = opcBlock.getMckey();
        } else if (StringUtils.isNotEmpty(opcBlock.getReservedmckey())) {
            key = opcBlock.getReservedmckey();
        }
        if (StringUtils.isEmpty(key)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        OpcDBDataCacheCenter.instance().addOrderKey(opcBlock.getBlockno(), key);
        jsonObject.put("result", true);
        return jsonObject;
    }
}

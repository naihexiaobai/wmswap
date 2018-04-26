package com.kepware.opc.thread.order;

import com.kepware.opc.dto.status.LBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.thief.wcs.entity.Route;
import com.wap.model.Cargo;
import com.wap.model.Storage;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 订单处理中心
 *
 * @auther CalmLake
 * @create 2018/4/3  14:28
 */

public class OrderCenterThread implements Runnable {
    private String loggerName = "OrderCenterThread";
    private String loggerNameException = "OrderCenterThreadException";

    @Override
    public void run() {
        long startTime = new Date().getTime();
        while (true) {
            try {
                OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getNewOpcOrder(1);
                if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_WAIT) {
                    //TODO
                    List<OpcOrder> opcOrders = BlockOperationDBUtil.getInstance().getOpcOrderListByStatus(OpcOrder.STATUS_EXEC);
                    for (OpcOrder opcOrder1 : opcOrders) {
                        if ((opcOrder1.getRouteid() == 1 && opcOrder.getRouteid() == 4) || (opcOrder.getRouteid() == 1 && opcOrder1.getRouteid() == 4)) {
                            LoggerUtil.getLoggerByName(loggerNameException).info("存在交叉路径，任务中，key:" + opcOrder1.getOrderkey() + ",待开始订单,key:" + opcOrder.getOrderkey());
                            Thread.sleep(2000);
                            continue;
                        }
                    }
                    Storage storage = new Storage();
                    String fromStorageNo;
                    String toStorageNo = null;
                    String storageNo;
                    String blockNo;
                    String key = opcOrder.getOrderkey();
                    if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                        storageNo = opcOrder.getTolocation();
                        blockNo = opcOrder.getFromstation();
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
                        storageNo = opcOrder.getFromlocation();
                        Route route = RouteSingleton.getInstance().getRouteHashMap().get(opcOrder.getRouteid());
                        blockNo = route.getStartblockno();
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                        fromStorageNo = opcOrder.getFromlocation();
                        toStorageNo = opcOrder.getTolocation();
                        storageNo = fromStorageNo;
                        Route route = RouteSingleton.getInstance().getRouteHashMap().get(opcOrder.getRouteid());
                        blockNo = route.getStartblockno();
                    } else {
                        //TODO   暂无
                        continue;
                    }
                    OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
                    String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                    OpcBlock nextOpcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
                    if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                        if (StringUtils.isNotEmpty(opcBlock.getMckey()) || StringUtils.isNotEmpty(opcBlock.getReservedmckey())) {
                            continue;
                        }
                        if (StringUtils.isNotEmpty(nextOpcBlock.getMckey()) || StringUtils.isNotEmpty(nextOpcBlock.getReservedmckey())) {
                            continue;
                        }
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT || opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                        if (StringUtils.isNotEmpty(opcBlock.getMckey()) || StringUtils.isNotEmpty(opcBlock.getReservedmckey())) {
                            continue;
                        }
                        if (StringUtils.isNotEmpty(nextOpcBlock.getReservedmckey())) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                    storage.setStorageno(storageNo);
                    List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
                    if (storageList.size() > 0) {
                        Storage storage1 = storageList.get(0);
                        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                            if (storage1.getStatus() == Storage.STATUS_FREE) {
                                storage1.setStatus(Storage.STATUS_INING);
                            } else {
                                continue;
                            }
                        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
                            if (storage1.getStatus() == Storage.STATUS_USEING) {
                                storage1.setStatus(Storage.STATUS_OUTING);
                            } else {
                                continue;
                            }
                        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                            Storage storage2 = BlockOperationDBUtil.getInstance().selectByStorageNo(toStorageNo);
                            if (storage1.getStatus() == Storage.STATUS_USEING && storage2.getStatus() == Storage.STATUS_FREE) {
                                storage1.setStatus(Storage.STATUS_OUTING);
                                storage2.setStatus(Storage.STATUS_INING);
                                BlockOperationDBUtil.getInstance().updateStorageById(storage2);
                            } else {
                                continue;
                            }
                        }
                        storage1.setUsetime(new Date());
                        BlockOperationDBUtil.getInstance().updateStorageById(storage1);

                        opcOrder.setStarttime(new Date());
                        opcOrder.setStatus(OpcOrder.STATUS_EXEC);
                        BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);

                        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {

                            Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                            cargo.setStatus(Cargo.STATUS_INING_ONE);
                            cargo.setStorageid(opcOrder.getTolocation());
                            BlockOperationDBUtil.getInstance().updateByCargo(cargo);


                            opcBlock.setMckey(key);
                            nextOpcBlock.setReservedmckey(key);
                            BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
                            BlockOperationDBUtil.getInstance().updateOpcBlock(nextOpcBlock);
                            OpcDBDataCacheCenter.instance().addOrderKey(blockNo, key);
                        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {

                            Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                            cargo.setStatus(Cargo.STATUS_OUTTING_FIVE);
                            BlockOperationDBUtil.getInstance().updateByCargo(cargo);

                            BlockOperationDBUtil.getInstance().updateSetBlockReservedMcKey(blockNo, nextBlockNo, key);
                            OpcDBDataCacheCenter.instance().addOrderKey(nextBlockNo, key);//母车或者堆垛机
                        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                            BlockOperationDBUtil.getInstance().updateSetBlockReservedMcKey(blockNo, nextBlockNo, key);
                            OpcDBDataCacheCenter.instance().addOrderKey(nextBlockNo, key);//母车或者堆垛机
                        } else {
                            continue;
                        }
                        LoggerUtil.getLoggerByName(loggerName).info("订单开始：" + opcOrder.toString());
                    } else {
                        LoggerUtil.getLoggerByName(loggerNameException).info("库位数据错误：" + opcOrder.getFromlocation());
                    }
                } else {
                    //TODO 当前无任务的设备回到初始位置待机
                }
                Thread.sleep(2000);
            } catch (NullPointerException e) {

            } catch (Exception e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(loggerNameException).warn("订单处理出现异常;" + e.getMessage());
            }
        }
    }
}

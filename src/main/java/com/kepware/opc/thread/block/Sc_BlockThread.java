package com.kepware.opc.thread.block;

import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.block.impl.Sc_BlockServiceImpl;
import com.kepware.opc.service.warehouse.in.Sc_Mc_BlockService_in;
import com.kepware.opc.service.warehouse.move.Sc_Mc_Sc_BlockService;
import com.kepware.opc.service.warehouse.out.Sc_Mc_BlockService;
import com.kepware.opc.service.warehouse.out.Sc_Ml_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 子车
 *
 * @auther CalmLake
 * @create 2018/4/3  16:23
 */
public class Sc_BlockThread extends BlockThread {

    private String logName = blockNo + "_Sc_BlockThread";

    public Sc_BlockThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String orderKey = OpcDBDataCacheCenter.instance().getOrderKey(blockNo);
                OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(orderKey);
                BlockService blockService = null;
                if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
                    OpcBlock opcBlock_SC = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
                    if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
                        try {
                            String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                            if (nextBlockNo.contains("MC")) {
                                blockService = new Sc_Mc_BlockService(blockNo, nextBlockNo, orderKey);
                            } else if (nextBlockNo.contains("ML")) {
                                blockService = new Sc_Ml_BlockService(blockNo, nextBlockNo, orderKey);
                            } else {
                                //TODO  暂无不处理
                                continue;
                            }
                            if (StringUtils.isNotEmpty(opcBlock_SC.getMckey()) && orderKey.equals(opcBlock_SC.getMckey())) {
                                OpcBlock opcBlockNext = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
                                if (StringUtils.isEmpty(opcBlockNext.getMckey()) && StringUtils.isNotEmpty(opcBlockNext.getReservedmckey()) && orderKey.equals(opcBlockNext.getReservedmckey())) {
                                    blockService.withKey();
                                } else {
                                    Thread.sleep(1000);
                                    OpcDBDataCacheCenter.instance().addOrderKey(blockNo, orderKey);
                                    continue;
                                }
                            } else if (StringUtils.isNotEmpty(opcBlock_SC.getReservedmckey()) && orderKey.equals(opcBlock_SC.getReservedmckey())) {
                                OpcBlock opcBlockNext = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
                                if (StringUtils.isNotEmpty(opcBlockNext.getReservedmckey()) && orderKey.equals(opcBlockNext.getReservedmckey())) {
                                    blockService.withReceivedMcKey();
                                } else {
                                    Thread.sleep(1000);
                                    OpcDBDataCacheCenter.instance().addOrderKey(blockNo, orderKey);
                                    continue;
                                }
                            } else {
                                LoggerUtil.getLoggerByName(logName).warn("block无key数据,orderKey:" + orderKey + ",block：" + blockNo + ",mcKey:" + opcBlock_SC.getMckey() + ",reservedMcKey:" + opcBlock_SC.getReservedmckey());
                                continue;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LoggerUtil.getLoggerByName(logName).warn(blockNo + ",出库异常：key:" + orderKey);
                            OpcDBDataCacheCenter.instance().addOrderKey(blockNo, orderKey);
                            Thread.sleep(20);
                            continue;
                        }
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                        //入库
                        String beforeBlockNo = RouteSingleton.getInstance().getBeforeBlockNo(opcOrder.getRouteid(), blockNo);
                        if (StringUtils.isNotEmpty(opcBlock_SC.getMckey())) {
                            blockService = new Sc_BlockServiceImpl(blockNo, beforeBlockNo, orderKey);
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlock_SC.getReservedmckey())) {
                            if (beforeBlockNo.contains("MC")) {
                                blockService = new Sc_Mc_BlockService_in(blockNo, beforeBlockNo, orderKey);
                            }
                            blockService.withReceivedMcKey();
                        } else {
                            LoggerUtil.getLoggerByName(logName).warn("block无key数据,orderKey:" + orderKey + ",block：" + blockNo + ",mcKey:" + opcBlock_SC.getMckey() + ",reservedMcKey:" + opcBlock_SC.getReservedmckey());
                            continue;
                        }
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                        //库内移动
                        String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                        if (StringUtils.isNotEmpty(opcBlock_SC.getMckey())) {
                            if (nextBlockNo.contains("MC")) {
                                blockService = new Sc_Mc_Sc_BlockService(blockNo, nextBlockNo, orderKey);
                            } else if (nextBlockNo.contains("ML")) {

                            }
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlock_SC.getReservedmckey())) {
                            if (nextBlockNo.contains("MC")) {
                                blockService = new Sc_Mc_Sc_BlockService(blockNo, nextBlockNo, orderKey);
                            } else if (nextBlockNo.contains("ML")) {

                            }
                            blockService.withReceivedMcKey();
                        } else {
                            LoggerUtil.getLoggerByName(logName).warn("block无key数据,orderKey:" + orderKey + ",block：" + blockNo + ",mcKey:" + opcBlock_SC.getMckey() + ",reservedMcKey:" + opcBlock_SC.getReservedmckey());
                            continue;
                        }
                    } else {
                        LoggerUtil.getLoggerByName(logName).warn("未知订单类型！");
                        continue;
                    }
                } else {
                    LoggerUtil.getLoggerByName(logName).warn("订单丢失：key，" + orderKey);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(logName + "InterruptedException").warn(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(logName + "Exception").warn(e.getLocalizedMessage());
            }
        }
    }
}

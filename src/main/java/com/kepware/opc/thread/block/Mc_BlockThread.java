package com.kepware.opc.thread.block;

import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.warehouse.in.Mc_Sc_BlockService_in;
import com.kepware.opc.service.warehouse.move.Mc_Sc_Mc_BlockService;
import com.kepware.opc.service.warehouse.Lf_Mc_Sc_BlockService;
import com.kepware.opc.service.warehouse.Mc_Lf_BlockService;
import com.kepware.opc.service.warehouse.out.Mc_Sc_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 母车
 *
 * @auther CalmLake
 * @create 2018/4/3  16:26
 */
public class Mc_BlockThread extends BlockThread {
    private String logName = blockNo + "_Mc_BlockThread";

    public Mc_BlockThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String orderKey = OpcDBDataCacheCenter.instance().getOrderKey(blockNo);
                Thread.sleep(150);
                OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(orderKey);
                BlockService blockService = null;
                if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
                    OpcBlock opcBlock_Mc = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
                    if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
                        //出库
                        if (StringUtils.isNotEmpty(opcBlock_Mc.getMckey())) {
                            if (StringUtils.isNotEmpty(opcBlock_Mc.getReservedmckey()) && !opcBlock_Mc.getMckey().equals(opcBlock_Mc.getReservedmckey())) {
                                OpcOrder opcOrder1 = BlockOperationDBUtil.getInstance().getOpcOrderByKey(opcBlock_Mc.getReservedmckey());
                                //当前有两个任务同时存在
                                String beforeBlockNo = RouteSingleton.getInstance().getBeforeBlockNo(opcOrder1.getRouteid(), blockNo);
                                if (beforeBlockNo.contains("SC")) {
                                    blockService = new Lf_Mc_Sc_BlockService(blockNo, beforeBlockNo, opcOrder1.getOrderkey());
                                }
                                blockService.withReceivedMcKey();
                            } else {
                                String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                                OpcBlock opcBlockNext = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
                                if (StringUtils.isNotEmpty(opcBlockNext.getMckey()) || StringUtils.isNotEmpty(opcBlockNext.getReservedmckey())) {
                                    //当之前有任务未完成时等待
                                    if (orderKey.equals(opcBlockNext.getReservedmckey())) {
                                        //未完成任务，继续
                                        if (nextBlockNo.contains("LF")) {
                                            blockService = new Mc_Lf_BlockService(blockNo, nextBlockNo, orderKey);
                                        }
                                        blockService.withKey();
                                    } else {
                                        Thread.sleep(1500);
                                        OpcDBDataCacheCenter.instance().addOrderKey(blockNo, orderKey);
                                        continue;
                                    }
                                } else {
                                    //新任务
                                    if (nextBlockNo.contains("LF")) {
                                        blockService = new Mc_Lf_BlockService(blockNo, nextBlockNo, orderKey);
                                    }
                                    blockService.withKey();
                                }
                            }
                        } else if (StringUtils.isNotEmpty(opcBlock_Mc.getReservedmckey())) {
                            String beforeBlockNo = RouteSingleton.getInstance().getBeforeBlockNo(opcOrder.getRouteid(), blockNo);
                            if (beforeBlockNo.contains("SC")) {
                                blockService = new Mc_Sc_BlockService(blockNo, beforeBlockNo, orderKey);
                            }
                            blockService.withReceivedMcKey();
                        } else {
                            LoggerUtil.getLoggerByName(logName).warn("block：无key，" + blockNo);
                            continue;
                        }
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                        //TODO  入库
                        String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                        if (StringUtils.isNotEmpty(opcBlock_Mc.getMckey())) {
                            if (nextBlockNo.contains("SC")) {
                                blockService = new Mc_Sc_BlockService_in(blockNo, nextBlockNo, orderKey);
                            }
                        } else if (StringUtils.isNotEmpty(opcBlock_Mc.getReservedmckey())) {

                        } else {

                        }
                        blockService.withKey();
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
                        //库内移动
                        String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                        if (StringUtils.isNotEmpty(opcBlock_Mc.getMckey())) {
                            if (nextBlockNo.contains("SC")) {
                                blockService = new Mc_Sc_Mc_BlockService(blockNo, nextBlockNo, orderKey);
                            }
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlock_Mc.getReservedmckey())) {
                            if (nextBlockNo.contains("SC")) {
                                blockService = new Mc_Sc_Mc_BlockService(blockNo, nextBlockNo, orderKey);
                            }
                            blockService.withReceivedMcKey();
                        } else {

                        }
                    } else {
                        //TODO  暂无不处理
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
                LoggerUtil.getLoggerByName(logName + "Exception").warn(e.getMessage());
            }
        }
    }
}

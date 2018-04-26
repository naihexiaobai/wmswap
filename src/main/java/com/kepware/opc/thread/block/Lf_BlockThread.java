package com.kepware.opc.thread.block;

import com.kepware.opc.dto.status.LBlockStatus;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.block.impl.Lf_BlockServiceImpl;
import com.kepware.opc.service.operation.StationOperation;
import com.kepware.opc.service.warehouse.Lf_El_BlockService;
import com.kepware.opc.service.warehouse.Lf_Lf_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 输送线
 *
 * @auther CalmLake
 * @create 2018/4/4  13:13
 */
public class Lf_BlockThread extends BlockThread {
    private String logName = blockNo + "_Lf_BlockThread";

    public Lf_BlockThread(String blockNo) {
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
                    OpcBlock opcBlockLf = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
                    String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                    if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
//                        if (blockNo.equals(opcOrder.getTostation())) {
//                            if (StringUtils.isNotEmpty(opcBlockLf.getMckey()) && orderKey.equals(opcBlockLf.getMckey())) {
//                                //出库完成
//                                StationOperation stationOperation = new StationOperation(blockNo, opcOrder);
//                                stationOperation.finishOutPutWork();
//                                LoggerUtil.getLoggerByName(logName + "OutPut").info("出库完成,blockNo:" + blockNo + ",orderKey:" + orderKey);
//                                continue;
//                            } else {
//                                LoggerUtil.getLoggerByName(logName).warn("block数据和订单数据不匹配：orderKey，" + orderKey + ",mcKey:" + opcBlockLf.getMckey());
//                            }
//                        }
                        if (StringUtils.isNotEmpty(opcBlockLf.getMckey())) {
                            blockService = new Lf_BlockServiceImpl(blockNo, nextBlockNo, orderKey);
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlockLf.getReservedmckey())) {

                        } else {

                        }
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                        if (StringUtils.isNotEmpty(opcBlockLf.getMckey())) {
                            if (RouteSingleton.getInstance().isStartStation(blockNo)) {
                                //入库货物检测.只有在检测到货物的情况下才进行工作
                                LBlockStatus lBlockStatus = (LBlockStatus) BlockOperationDBUtil.getInstance().getBlockStatusByBlockNo(blockNo);
                                boolean result = false;
                                while (!result) {
                                    if (lBlockStatus.isLoad()) {
                                        result = true;
                                    }
                                    Thread.sleep(100);
                                }
                            }
                            blockService = new Lf_BlockServiceImpl(blockNo, nextBlockNo, orderKey);
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlockLf.getReservedmckey())) {

                        } else {

                        }
                    } else {
                        //TODO   其它订单类型暂无处理
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

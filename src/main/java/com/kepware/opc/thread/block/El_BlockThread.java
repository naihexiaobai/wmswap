package com.kepware.opc.thread.block;

import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.RouteSingleton;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.block.impl.El_BlockServiceImpl;
import com.kepware.opc.service.warehouse.El_Lf_BlockService;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 固定提升机
 *
 * @auther CalmLake
 * @create 2018/4/4  14:24
 */
public class El_BlockThread extends BlockThread {
    private String logName = blockNo + "_El_BlockThread";

    public El_BlockThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String orderKey = OpcDBDataCacheCenter.instance().getOrderKey(blockNo);
                Thread.sleep(150);
                OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(orderKey);
                OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
                BlockService blockService;
                if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
                    String nextBlockNo = RouteSingleton.getInstance().getNextBlockNo(opcOrder.getRouteid(), blockNo);
                    OpcBlock opcBlockNext = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
                    if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
                        //入库
                        if (StringUtils.isNotEmpty(opcBlock.getMckey())) {
                            blockService = new El_BlockServiceImpl(blockNo, nextBlockNo, orderKey);
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlockNext.getReservedmckey())) {

                        } else {

                        }
                    } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
                        //出库
                        if (StringUtils.isNotEmpty(opcBlock.getMckey())) {
                            blockService = new El_BlockServiceImpl(blockNo, nextBlockNo, orderKey);
                            blockService.withKey();
                        } else if (StringUtils.isNotEmpty(opcBlockNext.getReservedmckey())) {

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

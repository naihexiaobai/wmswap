package com.kepware.opc.service.operation;

import com.kepware.opc.dto.command.BlockCommand;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;

/**
 * 订单表操作
 *
 * @auther CalmLake
 * @create 2018/3/28  11:07
 */
public class OpcOrderOperation {

    /**
     * 获取源货位
     *
     * @param key
     * @return
     */
    public static String getLocation(String key) throws Exception {
        String result;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_IN) {
            result = opcOrder.getTolocation();
        } else if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_OUT) {
            result = opcOrder.getFromlocation();
        } else {
            //TODO  获取货位其它订单类型未写
            throw new Exception();
        }
        return result;
    }

    public static String getTargetLine(String key) throws Exception {
        String location = getLocation(key);
        return BlockCommand.getX(location);
    }

    public static String getTargetTier(String key) throws Exception {
        String location = getLocation(key);
        return BlockCommand.getY(location);
    }

    public static String getFromLocation(String key) throws Exception {
        String result;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        result = opcOrder.getFromlocation();
        return result;
    }

    public static String getToLocation(String key) throws Exception {
        String result;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        result = opcOrder.getTolocation();
        return result;
    }

    public static String getFromLocationLine(String key) throws Exception {
        String result = null;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
            result = opcOrder.getFromlocation();
        }
        return BlockCommand.getX(result);
    }

    public static String getToLocationLine(String key) throws Exception {
        String result = null;
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder.getOrdertype() == OpcOrder.ORDERTYPE_MOVE) {
            result = opcOrder.getTolocation();
        }
        return BlockCommand.getX(result);
    }
}

package com.kepware.opc.thread.block.check;

import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.wap.model.Storage;
import com.www.util.CalmLakeStringUtil;

import java.util.List;

/**
 * 订单校验
 *
 * @auther CalmLake
 * @create 2018/3/22  13:22
 */
public class OrderCheck {

    /**
     * 货位是否可用
     *
     * @param opcOrder
     * @return
     */
    public static boolean isCanUseStorage(OpcOrder opcOrder) {
        boolean result = false;
        Storage storage = new Storage();
        storage.setStorageno(opcOrder.getTolocation());
        List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
        if (storageList.size() < 1) {

        } else {
            for (Storage storage1 : storageList) {
                if (storage1.getStatus() == Storage.STATUS_FREE) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 是否存在此符合开始任务的订单
     *
     * @return
     */
    public static boolean isHaveOrder(OpcOrder opcOrder) {
        boolean result = false;
        if (opcOrder.getId() > 0) {
            result = true;
        }
        return result;
    }


    /**
     * 订单状态
     *
     * @param opcOrder
     * @param bytes
     * @return
     */
    public static boolean isOpcOrderStatusOk(OpcOrder opcOrder, Byte bytes) {
        boolean result = false;
        if (opcOrder.getStatus() == bytes) {
            result = true;
        }
        return result;
    }


}

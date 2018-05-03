package com.kepware.opc.service.operation;

import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcOrder;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.wap.model.Cargo;
import com.wap.model.Storage;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 站台
 *
 * @auther CalmLake
 * @create 2018/3/28  8:47
 */
public class StationOperation {
    private String blockNo;
    private String key;

    public StationOperation() {

    }

    public StationOperation(String blockNo, String key) {
        this.blockNo = blockNo;
        this.key = key;
    }


    /**
     * 完成出库任务DB操作
     */
    @Transactional
    public void finishOutPutWork(String key) throws Exception {
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        Storage storage = new Storage();
        storage.setStorageno(opcOrder.getFromlocation());
        List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
        if (storageList.size() > 0) {
            Storage storage1 = storageList.get(0);
            if (storage1.getStatus() == Storage.STATUS_OUTING) {
                storage1.setStatus(Storage.STATUS_FREE);
                OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
                opcBlock.setMckey("");
                Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                cargo.setStatus(Cargo.STATUS_DEFAULT_ZERO);
                cargo.setStorageid("");
                opcOrder.setEndtime(new Date());
                opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                BlockOperationDBUtil.getInstance().updateStorageById(storage1);
                BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
                BlockOperationDBUtil.getInstance().updateByCargo(cargo);
                BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
            }
        }
    }

    /**
     * 完成出库任务DB操作,人工完成
     */
    @Transactional
    public void finishOutPutWorkByPeople(String key) throws Exception {
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        Storage storage = new Storage();
        storage.setStorageno(opcOrder.getFromlocation());
        List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
        if (storageList.size() > 0) {
            Storage storage1 = storageList.get(0);
            if (storage1.getStatus() == Storage.STATUS_OUTING) {
                storage1.setStatus(Storage.STATUS_FREE);
                Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                cargo.setStatus(Cargo.STATUS_DEFAULT_ZERO);
                cargo.setStorageid("");
                opcOrder.setEndtime(new Date());
                opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                BlockOperationDBUtil.getInstance().updateStorageById(storage1);
                BlockOperationDBUtil.getInstance().updateOpcBlockBykey(key);
                BlockOperationDBUtil.getInstance().updateByCargo(cargo);
                BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
            }
        }
    }

    /**
     * 入库完成,人工
     *
     * @param key-orderKey
     */
    @Transactional
    public void finishBePutInStorageByPeople(String key) {
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
            Storage storage = new Storage();
            storage.setStorageno(opcOrder.getTolocation());
            List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
            if (storageList.size() > 0) {
                Storage storage1 = storageList.get(0);
                if (storage1.getStatus() == Storage.STATUS_INING) {
                    storage1.setStatus(Storage.STATUS_USEING);
                    storage1.setUsetime(new Date());
                    BlockOperationDBUtil.getInstance().updateStorageById(storage1);
                    Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                    cargo.setStatus(Cargo.STATUS_INFISH_TWO);
                    BlockOperationDBUtil.getInstance().updateByCargo(cargo);
                    opcOrder.setEndtime(new Date());
                    opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                    BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
                    BlockOperationDBUtil.getInstance().updateOpcBlockBykey(key);
                }
            }
        }
    }

    /**
     * 移库完成，人工
     *
     * @param key-orderKey
     */
    @Transactional
    public void finishBeMoveInStorageByPeople(String key) {
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
            Storage storageFrom = BlockOperationDBUtil.getInstance().selectByStorageNo(opcOrder.getFromlocation());
            Storage storageTo = BlockOperationDBUtil.getInstance().selectByStorageNo(opcOrder.getTolocation());
            if (storageTo.getStatus() == Storage.STATUS_INING && storageFrom.getStatus() == Storage.STATUS_OUTING) {
                storageTo.setUsetime(new Date());
                storageTo.setStatus(Storage.STATUS_USEING);
                storageFrom.setStatus(Storage.STATUS_FREE);
                BlockOperationDBUtil.getInstance().updateStorageById(storageTo);
                BlockOperationDBUtil.getInstance().updateStorageById(storageFrom);

                Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                cargo.setStorageid(opcOrder.getTolocation());
                BlockOperationDBUtil.getInstance().updateByCargo(cargo);

                opcOrder.setEndtime(new Date());
                opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
                BlockOperationDBUtil.getInstance().updateOpcBlockBykey(key);
            }
        }
    }


    /**
     * 入库完成
     *
     * @param key-orderKey
     */
    @Transactional
    public void finishBePutInStorage(String key) {
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
            Storage storage = new Storage();
            storage.setStorageno(opcOrder.getTolocation());
            List<Storage> storageList = BlockOperationDBUtil.getInstance().getStorageList(storage);
            if (storageList.size() > 0) {
                Storage storage1 = storageList.get(0);
                if (storage1.getStatus() == Storage.STATUS_INING) {
                    storage1.setStatus(Storage.STATUS_USEING);
                    storage1.setUsetime(new Date());
                    BlockOperationDBUtil.getInstance().updateStorageById(storage1);

                    Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                    cargo.setStatus(Cargo.STATUS_INFISH_TWO);
                    BlockOperationDBUtil.getInstance().updateByCargo(cargo);

                    opcOrder.setEndtime(new Date());
                    opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                    BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
                }
            }
        }
    }

    /**
     * 移库完成
     *
     * @param key-orderKey
     */
    @Transactional
    public void finishBeMoveInStorage(String key) {
        OpcOrder opcOrder = BlockOperationDBUtil.getInstance().getOpcOrderByKey(key);
        if (opcOrder != null && opcOrder.getStatus() == OpcOrder.STATUS_EXEC) {
            Storage storageFrom = BlockOperationDBUtil.getInstance().selectByStorageNo(opcOrder.getFromlocation());
            Storage storageTo = BlockOperationDBUtil.getInstance().selectByStorageNo(opcOrder.getTolocation());
            if (storageTo.getStatus() == Storage.STATUS_INING && storageFrom.getStatus() == Storage.STATUS_OUTING) {
                storageTo.setUsetime(new Date());
                storageTo.setStatus(Storage.STATUS_USEING);
                storageFrom.setStatus(Storage.STATUS_FREE);
                BlockOperationDBUtil.getInstance().updateStorageById(storageTo);
                BlockOperationDBUtil.getInstance().updateStorageById(storageFrom);

                Cargo cargo = BlockOperationDBUtil.getInstance().selectByCargoPalletNo(opcOrder.getBarcode());
                cargo.setStorageid(opcOrder.getTolocation());
                BlockOperationDBUtil.getInstance().updateByCargo(cargo);

                opcOrder.setEndtime(new Date());
                opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
                BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
            }
        }
    }
}

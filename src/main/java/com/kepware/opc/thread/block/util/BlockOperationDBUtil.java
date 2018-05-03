package com.kepware.opc.thread.block.util;

import com.kepware.opc.dao.*;
import com.kepware.opc.dto.status.BlockStatus;
import com.kepware.opc.entity.*;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.wap.dao.CargoMapper;
import com.wap.dao.StorageMapper;
import com.wap.model.Cargo;
import com.wap.model.Storage;
import com.www.util.LoggerUtil;
import com.www.util.SpringTool;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备工作业务处理
 *
 * @auther CalmLake
 * @create 2018/3/22  10:30
 */
public class BlockOperationDBUtil {

    private static BlockOperationDBUtil ourInstance = new BlockOperationDBUtil();

    public static BlockOperationDBUtil getInstance() {
        return ourInstance;
    }

    public Cargo selectByCargoPalletNo(String palletNo) {
        return ((CargoMapper) SpringTool.getBeanByClass(CargoMapper.class)).selectByCargoPalletNo(palletNo);
    }

    public int updateByCargo(Cargo cargo) {
        return ((CargoMapper) SpringTool.getBeanByClass(CargoMapper.class)).updateByPrimaryKeySelective(cargo);
    }

    public int deleteCargoById(int id) {
        return ((CargoMapper) SpringTool.getBeanByClass(CargoMapper.class)).deleteByPrimaryKey(id);
    }

    public OpcWcsControlInfo getOpcWcsInfoByKey(int id) {
        return ((OpcWcsControlInfoMapper) SpringTool.getBeanByClass(OpcWcsControlInfoMapper.class)).selectByPrimaryKey(id);
    }

    public BlockStatus getBlockStatusByBlockNo(String blockNo) {
        return OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
    }


    public OpcOrder getOpcOrderByOrderKey(String orderKey) {
        return ((OpcOrderMapper) SpringTool.getBean("opcOrderMapper")).selectByOrderKey(orderKey);
    }

    public List<OpcOrder> getOpcOrderListByStatus(int status) {
        return ((OpcOrderMapper) SpringTool.getBean("opcOrderMapper")).selectOpcOrderListByStatus(status);
    }

    public List<OpcOrder> getOpcOrderListMaxTen() {
        return ((OpcOrderMapper) SpringTool.getBean("opcOrderMapper")).selectOpcOrderListAll();
    }

    public void insertIntoOpcOrder(OpcOrder opcOrder) {
        ((OpcOrderMapper) SpringTool.getBean("opcOrderMapper")).insertSelective(opcOrder);
    }

    public int insertIntoOpcWcsInfo(OpcWcsControlInfo opcWcsControlInfo) {
        return ((OpcWcsControlInfoMapper) SpringTool.getBeanByClass(OpcWcsControlInfoMapper.class)).insertSelective(opcWcsControlInfo);
    }


    public int updateByWcsTaskNoAndBlockNo(OpcWcsControlInfo opcWcsControlInfo) {
        return ((OpcWcsControlInfoMapper) SpringTool.getBeanByClass(OpcWcsControlInfoMapper.class)).updateByWcsTaskNoAndBlockNo(opcWcsControlInfo);
    }


    public int updateStorageById(Storage storage) {
        return ((StorageMapper) SpringTool.getBeanByClass(StorageMapper.class)).updateByPrimaryKey(storage);
    }

    public Storage selectByStorageNo(String storageNo) {
        return ((StorageMapper) SpringTool.getBeanByClass(StorageMapper.class)).selectByStorageNo(storageNo);
    }

    public OpcBlock getOpcBlockByBlockNo(String blockNo) {
        return ((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).selectByBlockNo(blockNo);
    }

    public OpcBlock selectByMcKey(String mcKey) {
        return ((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).selectByMcKey(mcKey);
    }

    public int updateOpcBlockBykey(String key) {
        return ((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).updateByKey(key);
    }

    public int updateOpcBlock(OpcBlock opcBlock) {
        return ((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).updateByPrimaryKeySelective(opcBlock);
    }

    public int updateOpcOrder(OpcOrder opcOrder) {
        return ((OpcOrderMapper) SpringTool.getBean("opcOrderMapper")).updateByPrimaryKeySelective(opcOrder);
    }

    public int clearBlockStatusByKey(String mcKey) {
        return ((OpcBlockMapper) SpringTool.getBeanByClass(OpcBlockMapper.class)).clearKeyByKey(mcKey);
    }


    public OpcWharf selectByBlockNo(String blockNo) {
        return ((OpcWharfMapper) SpringTool.getBeanByClass(OpcWharfMapper.class)).selectByBlockNo(blockNo);
    }

    public List<OpcStation> selectAll() {
        return ((OpcStationMapper) SpringTool.getBeanByClass(OpcStationMapper.class)).selectAll();
    }

    /**
     * mapper 中已加入限制条件，每次只取一条记录
     *
     * @param status
     * @return
     */
    public OpcOrder getNewOpcOrder(int status) {
        return ((OpcOrderMapper) SpringTool.getBeanByClass(OpcOrderMapper.class)).selectByStatus(status);
    }

    public OpcOrder getNewOpcOrderIn(int status) {
        return ((OpcOrderMapper) SpringTool.getBeanByClass(OpcOrderMapper.class)).selectOrderInByStatus(status);
    }

    public OpcOrder getNewOpcOrderOut(int status) {
        return ((OpcOrderMapper) SpringTool.getBeanByClass(OpcOrderMapper.class)).selectOrderOutByStatus(status);
    }

    public OpcOrder getNewOpcOrderMove(int status) {
        return ((OpcOrderMapper) SpringTool.getBeanByClass(OpcOrderMapper.class)).selectOrderMoveByStatus(status);
    }

    public List<OpcOrder> selectOpcOrderListByStatus(int status) {
        return ((OpcOrderMapper) SpringTool.getBeanByClass(OpcOrderMapper.class)).selectOpcOrderListByStatus(status);
    }

    public List<Storage> getStorageList(Storage storage) {
        return ((StorageMapper) SpringTool.getBeanByClass(StorageMapper.class)).selectByStorage(storage);
    }


    public int updateOpcWcsControlInfo(OpcWcsControlInfo opcWcsControlInfo) {
        return ((OpcWcsControlInfoMapper) SpringTool.getBeanByClass(OpcWcsControlInfoMapper.class)).updateByPrimaryKeySelective(opcWcsControlInfo);
    }

    public OpcOrder getOpcOrderByKey(String key) {
        return BlockOperationDBUtil.getInstance().getOpcOrderByOrderKey(key);
    }

    public int failOrderUpdate(OpcOrder opcOrder, String msg, String key, String logName) {
        opcOrder.setStatus(OpcOrder.STATUS_FAIL);
        opcOrder.setErrormsg(msg);
        LoggerUtil.getLoggerByName(logName).info(msg);
        return BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
    }

    public int orderSuccessUpdate(OpcOrder opcOrder) {
        opcOrder.setEndtime(new Date());
        opcOrder.setStatus(OpcOrder.STATUS_SUCCESS);
        return BlockOperationDBUtil.getInstance().updateOpcOrder(opcOrder);
    }

    public int wcsInfoFailUpdate(OpcWcsControlInfo opcWcsControlInfo) {
        opcWcsControlInfo.setEndtime(new Date());
        opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
        return BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
    }

    public void updateTwoBlock(String blockNo, String nextBlockNo, String key) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock opcNextBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        opcBlock.setMckey("");
        opcNextBlock.setMckey(key);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcNextBlock);
    }

    @Transactional
    public void updateBlockMcKeyAndReservedMcKey(String blockNo, String nextBlockNo, String key) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock opcBlock1 = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        opcBlock.setReservedmckey("");
        opcBlock.setMckey("");
        opcBlock1.setMckey(key);
        opcBlock1.setReservedmckey("");
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock1);
    }

    /**
     * 子车载货下车，block修改
     *
     * @param blockNo，子车
     * @param nextBlockNo，母车/堆垛机
     * @param key
     */
    @Transactional
    public void updateBlockScGetOffCar(String blockNo, String nextBlockNo, String key) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock opcBlock1 = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        opcBlock1.setMckey("");
        opcBlock.setMckey(key);
        opcBlock.setReservedmckey("");
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock1);
    }


    @Transactional
    public void updateSetBlockReservedMcKey(String blockNo, String nextBlockNo, String key) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        OpcBlock opcBlock1 = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(nextBlockNo);
        opcBlock.setReservedmckey(key);
        opcBlock1.setReservedmckey(key);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock1);
    }

    public void clearBlockMcKey(String blockNo) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(blockNo);
        opcBlock.setMckey("");
        BlockOperationDBUtil.getInstance().updateOpcBlock(opcBlock);
    }

}

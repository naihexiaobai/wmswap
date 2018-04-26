package com.kepware.opc.service.block.impl;

import com.kepware.opc.dto.command.ScBlockCommand;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.BlockService;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.service.operation.ScOperation;
import com.kepware.opc.service.operation.StationOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;
import com.www.util.LoggerUtil;

import java.util.Date;

/**
 * 子车
 *
 * @auther CalmLake
 * @create 2018/4/3  16:29
 */
public class Sc_BlockServiceImpl implements BlockService {

    public String blockNo_sc;
    public String blockNo_other;
    public String key;

    public Sc_BlockServiceImpl(String blockNo_sc, String blockNo_other, String key) {
        this.blockNo_sc = blockNo_sc;
        this.blockNo_other = blockNo_other;
        this.key = key;
    }

    @Override
    public void withKey() throws Exception {
        StationOperation stationOperation = new StationOperation();
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_sc);
        ScOperation scOperation = new ScOperation(blockNo_sc, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        String line = OpcOrderOperation.getTargetLine(key);
        ScBlockCommand scBlockCommand = null;
        if (scBlockStatus.isLoad() && line.equals(scBlockStatus.getLine()) && BlockStatusOperationUtil.isScBlockCanWork(scBlockStatus)) {
            //载货入库
            scBlockCommand = scOperation.cargoWarehousing();
        } else {
            withKey();
        }
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(scBlockCommand, blockNo_sc, key);
        OpcWrite.instance().writeByBlockCommand(scBlockCommand, blockNo_sc);
        boolean result = scOperation.isFinishWork(scBlockCommand, blockNo_sc);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            BlockOperationDBUtil.getInstance().clearBlockMcKey(blockNo_sc);
            stationOperation.finishBePutInStorage(key);
            LoggerUtil.getLoggerByName("warehouse").info("in,入库任务完成,blockNo" + blockNo_sc + ",orderKey:" + key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, false);
            BlockOperationDBUtil.getInstance().wcsInfoFailUpdate(opcWcsControlInfo);
            LoggerUtil.getLoggerByName("warehouse").info("in,入库任务失败,blockNo" + blockNo_sc + ",orderKey:" + key);
        }
    }

    @Override
    public void withReceivedMcKey() throws Exception {

    }


}

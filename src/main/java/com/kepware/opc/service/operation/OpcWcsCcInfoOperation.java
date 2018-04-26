package com.kepware.opc.service.operation;

import com.kepware.opc.dao.OpcWcsControlInfoMapper;
import com.kepware.opc.dto.command.*;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.www.util.CalmLakeStringUtil;
import com.www.util.SpringTool;

import java.util.Date;

/**
 * 消息命令表信息操作
 *
 * @auther CalmLake
 * @create 2018/3/27  15:25
 */
public class OpcWcsCcInfoOperation {

    public OpcWcsCcInfoOperation() {
    }

    /**
     * 新建一条记录
     *
     * @param presentBlockNo
     * @param key
     * @return
     */
    public OpcWcsControlInfo createOpcWcsInfo(BlockCommand blockCommand, String presentBlockNo, String key) {
        OpcBlock opcBlock = BlockOperationDBUtil.getInstance().getOpcBlockByBlockNo(presentBlockNo);
        OpcWcsControlInfo opcWcsControlInfo = new OpcWcsControlInfo();
        if (presentBlockNo.contains("SC")) {
            ScBlockCommand scBlockCommand = (ScBlockCommand) blockCommand;
            opcWcsControlInfo.setY(Short.valueOf(scBlockCommand.getTargetTier()));
            opcWcsControlInfo.setX(Short.valueOf(scBlockCommand.getTargetLine()));
            opcWcsControlInfo.setZ(Short.valueOf(scBlockCommand.getTargetRow()));
        } else if (presentBlockNo.contains("MC")) {
            McBlockCommand mcBlockCommand = (McBlockCommand) blockCommand;
            opcWcsControlInfo.setX(Short.valueOf(mcBlockCommand.getTargetLine()));
            opcWcsControlInfo.setY(Short.valueOf(opcBlock.getTier()));
            opcWcsControlInfo.setZ(Short.valueOf(opcBlock.getRow()));
        } else if (presentBlockNo.contains("ML")) {
            MlBlockCommand mlBlockCommand = (MlBlockCommand) blockCommand;
            opcWcsControlInfo.setY(Short.valueOf(mlBlockCommand.getTargetTier()));
            opcWcsControlInfo.setX(Short.valueOf(mlBlockCommand.getTargetLine()));
            opcWcsControlInfo.setZ(Short.valueOf(opcBlock.getRow()));
        } else if (presentBlockNo.contains("EL")) {
            ElBlockCommand elBlockCommand = (ElBlockCommand) blockCommand;
            opcWcsControlInfo.setY(Short.valueOf(elBlockCommand.getTargetTier()));
            opcWcsControlInfo.setX(Short.valueOf(opcBlock.getLine()));
            opcWcsControlInfo.setZ(Short.valueOf(opcBlock.getRow()));
        } else {
            opcWcsControlInfo.setX(Short.valueOf(opcBlock.getLine()));
            opcWcsControlInfo.setY(Short.valueOf(opcBlock.getTier()));
            opcWcsControlInfo.setZ(Short.valueOf(opcBlock.getRow()));
        }
        opcWcsControlInfo.setCreatetime(new Date());
        opcWcsControlInfo.setBlockno(opcBlock.getBlockno());
        opcWcsControlInfo.setMovementid(CalmLakeStringUtil.StringToByte(blockCommand.getCommand()));
        opcWcsControlInfo.setOrderkey(key);
        opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_NO_WRITE);
        opcWcsControlInfo.setWcstaskno(blockCommand.getTaskID());
        BlockOperationDBUtil.getInstance().insertIntoOpcWcsInfo(opcWcsControlInfo);
        return opcWcsControlInfo;
    }

    /**
     * 执行成功
     *
     * @param opcWcsControlInfo
     */
    public void updateOpcWcsInfoSuccess(OpcWcsControlInfo opcWcsControlInfo) {
        opcWcsControlInfo.setEndtime(new Date());
        opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
        BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
    }

    /**
     * 执行失败
     *
     * @param opcWcsControlInfo
     */
    public void updateOpcWcsInfoFail(OpcWcsControlInfo opcWcsControlInfo) {
        opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
        BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
    }

    /**
     * 写入失败
     *
     * @param opcWcsControlInfo
     */
    public void updateOpcWcsInfoWriteFail(OpcWcsControlInfo opcWcsControlInfo) {
        opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
        BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
    }

    /**
     * 写入成功
     *
     * @param opcWcsControlInfo
     */
    public void updateOpcWcsInfoWriteSuccess(OpcWcsControlInfo opcWcsControlInfo) {
        opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_WRITE);
        BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
    }
}

package com.kepware.opc.service.warehouse.out;

import com.kepware.opc.dto.command.MlBlockCommand;
import com.kepware.opc.dto.status.MlBlockStatus;
import com.kepware.opc.dto.status.ScBlockStatus;
import com.kepware.opc.entity.OpcWcsControlInfo;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcWrite;
import com.kepware.opc.service.block.impl.Ml_BlockServiceImpl;
import com.kepware.opc.service.operation.MlOperation;
import com.kepware.opc.service.operation.OpcOrderOperation;
import com.kepware.opc.service.operation.OpcWcsCcInfoOperation;
import com.kepware.opc.thread.block.util.BlockOperationDBUtil;
import com.kepware.opc.thread.block.util.BlockStatusOperationUtil;

import java.util.Date;

/**
 * 堆垛机——子车
 *
 * @auther CalmLake
 * @create 2018/4/9  11:41
 */
public class Ml_Sc_BlockService extends Ml_BlockServiceImpl {


    public Ml_Sc_BlockService(String blockNo_ml, String blockNo_other, String key) {
        super(blockNo_ml, blockNo_other, key);
    }

    @Override
    public void withReceivedMcKey() throws Exception {
        Thread.sleep(110);
        MlOperation mlOperation = new MlOperation(blockNo_ml, blockNo_other, key);
        OpcWcsCcInfoOperation opcWcsCcInfoOperation = new OpcWcsCcInfoOperation();
        MlBlockStatus mlBlockStatus = (MlBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_ml);
        ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus(blockNo_other);
        String line = OpcOrderOperation.getTargetLine(key);
        String tier = OpcOrderOperation.getTargetTier(key);
        MlBlockCommand mlBlockCommand;
        if (mlBlockStatus.isTheCar()) {
            if (tier.equals(mlBlockStatus.getTier()) && line.equals(mlBlockStatus.getLine())) {
                //子车在母车上，且母车在目标列，子车空车下车
                OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, true);
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
                return;
            } else {
                mlBlockCommand = mlOperation.moveToTargetLine(key);
            }
        } else if (mlBlockStatus.getTier().equals(scBlockStatus.getTier()) && mlBlockStatus.getLine().equals(scBlockStatus.getLine())) {
            if (!line.equals(mlBlockStatus.getLine()) || !tier.equals(mlBlockStatus.getTier())) {
                //子车不在母车上，子母车在同一层列，且不在目标列，子车空车上车
                OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, true);
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
                return;
            } else {
                //子车在目标列，子车出库 取得货物，mcKey 赋值 ，receivedMcKey 清除
                OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
                return;
            }
        } else {
            //母车上没有子车且子母车不同层列
            mlBlockCommand = mlOperation.moveToNextBlockScLine();
        }
//        if ((!mlBlockStatus.getTier().equals(tier) || !mlBlockStatus.getLine().equals(line)) && mlBlockStatus.isTheCar()) {
//            mlBlockCommand = mlOperation.moveToTargetLine(key);
//        } else if (!mlBlockStatus.isTheCar() && (!scBlockStatus.getLine().equals(line) || !scBlockStatus.getTier().equals(tier))
//                && (!scBlockStatus.getLine().equals(mlBlockStatus.getLine()) || !scBlockStatus.getTier().equals(mlBlockStatus.getTier()))) {
//            mlBlockCommand = mlOperation.moveToNextBlockScLine();
//        } else if (!mlBlockStatus.isTheCar() && scBlockStatus.isLoad() && !scBlockStatus.getLine().equals(mlBlockStatus.getLine())) {
//            mlBlockCommand = mlOperation.moveToNextBlockScLine();
//        } else if (scBlockStatus.isRGV() && line.equals(mlBlockStatus.getLine()) && !scBlockStatus.isLoad()) {
//            //子车在母车上，且母车在目标列，子车空车下车
//            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, true);
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
//            return;
//        } else if (!scBlockStatus.isRGV() && line.equals(scBlockStatus.getLine()) && !scBlockStatus.isJacking()) {
//            //子车在目标列，子车出库 取得货物，mcKey 赋值 ，receivedMcKey 清除
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
//            return;
//        } else if (!scBlockStatus.isRGV() && mlBlockStatus.getLine().equals(scBlockStatus.getLine()) && !line.equals(scBlockStatus.getLine())) {
//            //子车不在母车上，子母车在同一列，且不在目标列，子车空车上车
//            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, true);
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
//            return;
//        } else if (line.equals(mlBlockStatus.getLine()) && scBlockStatus.isLoad() && !scBlockStatus.isRGV() && mlBlockStatus.getLine().equals(scBlockStatus.getLine())) {
//            //子车不在母车上，子母车同列,子车载货， 子车载货上车
//            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_other, true);
//            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
//            return;
//        } else {
//            LoggerUtil.getLoggerByName("Ml_Sc_BlockService_out").info("状态不正确：" + key);
//            return;
//        }
        if (OpcDBDataCacheCenter.instance().getBlockIsDock(blockNo_ml)) {
            withReceivedMcKey();
        }
        OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, true);
        OpcWcsControlInfo opcWcsControlInfo = opcWcsCcInfoOperation.createOpcWcsInfo(mlBlockCommand, blockNo_ml, key);
        OpcWrite.instance().writeByBlockCommand(mlBlockCommand, blockNo_ml);
        boolean result = mlOperation.isFinishWork(mlBlockCommand, blockNo_ml);
        if (result) {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_SUCCESS);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_other, key);
        } else {
            OpcDBDataCacheCenter.instance().setBlockDock(blockNo_ml, false);
            opcWcsControlInfo.setStatus(OpcWcsControlInfo.STATUS_FAIL);
            opcWcsControlInfo.setEndtime(new Date());
            BlockOperationDBUtil.getInstance().updateOpcWcsControlInfo(opcWcsControlInfo);
            OpcDBDataCacheCenter.instance().addOrderKey(blockNo_ml, key);
        }
    }
}

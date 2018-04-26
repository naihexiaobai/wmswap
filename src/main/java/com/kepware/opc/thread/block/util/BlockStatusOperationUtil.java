package com.kepware.opc.thread.block.util;

import com.kepware.opc.dto.status.*;
import com.kepware.opc.entity.OpcBlock;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.www.util.CalmLakeStringUtil;

/**
 * 各设备状态操作
 *
 * @auther CalmLake
 * @create 2018/3/22  11:15
 */
public class BlockStatusOperationUtil {
    /**
     * 升降机是否可以工作
     *
     * @param elBlockStatus
     * @return
     */
    public static boolean isElBlockCanWork(ElBlockStatus elBlockStatus) {
        return elBlockStatus.isFree() && elBlockStatus.isAuto() && !elBlockStatus.isOnStandby() && !elBlockStatus.isError();
    }

    public static boolean isMcBlockCanWork(McBlockStatus mcBlockStatus) {
        return mcBlockStatus.isFree() && mcBlockStatus.isAuto() && !mcBlockStatus.isOnStandby() && !mcBlockStatus.isError();
    }

    public static boolean isScBlockCanWork(ScBlockStatus scBlockStatus) {
        return scBlockStatus.isFree() && scBlockStatus.isAuto() && !scBlockStatus.isOnStandby() && !scBlockStatus.isError();
    }

    public static boolean isMlBlockCanWork(MlBlockStatus mlBlockStatus) {
        return mlBlockStatus.isFree() && mlBlockStatus.isAuto() && !mlBlockStatus.isOnStandby() && !mlBlockStatus.isError();
    }

    public static boolean isBlockNoTDock(String blockNo) {
        return OpcDBDataCacheCenter.getBlockDock().get(blockNo);
    }

    /**
     * 设备锁定/设备解锁
     *
     * @param blockNo
     * @param dock
     */
    public static void blockNoDock(String blockNo, boolean dock) {
        OpcDBDataCacheCenter.getBlockDock().put(blockNo, dock);
    }

    /**
     * 当前blockNo是否载物
     *
     * @param blockNo
     * @return
     */
    public static boolean blockISLoad(String blockNo) throws Exception {
        if (blockNo.contains("LF")) {
            LBlockStatus lBlockStatus = (LBlockStatus) getBlockStatus(blockNo);
            return lBlockStatus.isLoad();
        } else if (blockNo.contains("EL")) {
            ElBlockStatus lBlockStatus = (ElBlockStatus) getBlockStatus(blockNo);
            return lBlockStatus.isLoad();
        } else if (blockNo.contains("ML")) {
            MlBlockStatus lBlockStatus = (MlBlockStatus) getBlockStatus(blockNo);
            return lBlockStatus.isLoad();
        } else if (blockNo.contains("SC")) {
            ScBlockStatus lBlockStatus = (ScBlockStatus) getBlockStatus(blockNo);
            return lBlockStatus.isLoad();
        } else if (blockNo.contains("MC")) {
            McBlockStatus lBlockStatus = (McBlockStatus) getBlockStatus(blockNo);
            return lBlockStatus.isLoad();
        } else {
            throw new Exception();
        }
    }

    public static BlockStatus getBlockStatus(String blockNo) throws Exception {
        if (blockNo.contains("LF")) {
            LBlockStatus lBlockStatus = (LBlockStatus) OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
            return lBlockStatus;
        } else if (blockNo.contains("EL")) {
            ElBlockStatus lBlockStatus = (ElBlockStatus) OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
            return lBlockStatus;
        } else if (blockNo.contains("ML")) {
            MlBlockStatus lBlockStatus = (MlBlockStatus) OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
            return lBlockStatus;
        } else if (blockNo.contains("SC")) {
            ScBlockStatus lBlockStatus = (ScBlockStatus) OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
            return lBlockStatus;
        } else if (blockNo.contains("MC")) {
            McBlockStatus lBlockStatus = (McBlockStatus) OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
            return lBlockStatus;
        } else {
            throw new Exception();
        }
    }

    /**
     * 任务是否完成
     *
     * @param blockStatus
     * @return
     */
    public static boolean isFinishedWork(BlockStatus blockStatus) {
        if (blockStatus.getTaskID().equals(blockStatus.getPlcTaskID()) && !CalmLakeStringUtil.stringIsNull(blockStatus.getTaskID())) {
            return true;
        } else {
            return false;
        }
    }
}

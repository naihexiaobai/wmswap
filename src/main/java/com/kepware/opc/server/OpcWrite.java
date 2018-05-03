package com.kepware.opc.server;

import com.kepware.opc.dto.command.*;
import com.www.util.LoggerUtil;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.da.Item;

/**
 * opc写入
 *
 * @auther CalmLake
 * @create 2018/3/20  12:59
 */
public class OpcWrite {


    public static OpcWrite instance() {
        return instance;
    }

    private static OpcWrite instance;

    static {
        instance = new OpcWrite();
    }

    private OpcWrite() {
    }

    public void writeByBlockCommand(BlockCommand blockCommand, String blockNo) {
        if (blockCommand instanceof ElBlockCommand) {
            LoggerUtil.getLoggerByName("OpcWrite").info(blockNo + "," + ((ElBlockCommand) blockCommand).toString());
            writeEl(blockNo, (ElBlockCommand) blockCommand);
        } else if (blockCommand instanceof LBlockCommand) {
            LoggerUtil.getLoggerByName("OpcWrite").info(blockNo + "," + ((LBlockCommand) blockCommand).toString());
            writeL(blockNo, (LBlockCommand) blockCommand);
        } else if (blockCommand instanceof MlBlockCommand) {
            LoggerUtil.getLoggerByName("OpcWrite").info(blockNo + "," + ((MlBlockCommand) blockCommand).toString());
            writeMl(blockNo, (MlBlockCommand) blockCommand);
        } else if (blockCommand instanceof McBlockCommand) {
            LoggerUtil.getLoggerByName("OpcWrite").info(blockNo + "," + ((McBlockCommand) blockCommand).toString());
            writeMc(blockNo, (McBlockCommand) blockCommand);
        } else if (blockCommand instanceof ScBlockCommand) {
            LoggerUtil.getLoggerByName("OpcWrite").info(blockNo + "," + ((ScBlockCommand) blockCommand).toString());
            writeSc(blockNo, (ScBlockCommand) blockCommand);
        } else {

        }
    }

    /**
     * 写入指令至固定提升机
     *
     * @param blockNo
     * @param elBlockCommand
     */
    void writeEl(String blockNo, ElBlockCommand elBlockCommand) {
        String key_1 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + ElBlockCommand.ITEM_TARGET_TIER;
        String key_2 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + ElBlockCommand.ITEM_COMMAND;
        String key_3 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + ElBlockCommand.ITEM_TASK_ID;
        write(key_1, new JIVariant(elBlockCommand.getTargetTier()), elBlockCommand.getTargetTier());
        write(key_2, new JIVariant(elBlockCommand.getCommand()), elBlockCommand.getCommand());
        write(key_3, new JIVariant(elBlockCommand.getTaskID()), elBlockCommand.getTaskID());
    }

    /**
     * 写入指令至输送线
     *
     * @param blockNo
     * @param lBlockCommand
     */
    void writeL(String blockNo, LBlockCommand lBlockCommand) {
    }

    /**
     * 写入指令至母车
     *
     * @param blockNo
     * @param mcBlockCommand
     */
    void writeMc(String blockNo, McBlockCommand mcBlockCommand) {
        String key_1 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mcBlockCommand.ITEM_TARGET_LINE;
        String key_2 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mcBlockCommand.ITEM_COMMAND;
        String key_3 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mcBlockCommand.ITEM_TASK_ID;
        write(key_1, new JIVariant(mcBlockCommand.getTargetLine()), mcBlockCommand.getTargetLine());
        write(key_2, new JIVariant(mcBlockCommand.getCommand()), mcBlockCommand.getCommand());
        write(key_3, new JIVariant(mcBlockCommand.getTaskID()), mcBlockCommand.getTaskID());
    }

    /**
     * 写入指令至堆垛机
     *
     * @param blockNo
     * @param mlBlockCommand
     */
    void writeMl(String blockNo, MlBlockCommand mlBlockCommand) {
        String key_1 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mlBlockCommand.ITEM_TARGET_LINE;
        String key_2 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mlBlockCommand.ITEM_COMMAND;
        String key_3 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mlBlockCommand.ITEM_TASK_ID;
        String key_4 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + mlBlockCommand.ITEM_TARGET_TIER;
        write(key_1, new JIVariant(mlBlockCommand.getTargetLine()), mlBlockCommand.getTargetLine());
        write(key_2, new JIVariant(mlBlockCommand.getCommand()), mlBlockCommand.getCommand());
        write(key_4, new JIVariant(mlBlockCommand.getTargetTier()), mlBlockCommand.getTargetTier());
        write(key_3, new JIVariant(mlBlockCommand.getTaskID()), mlBlockCommand.getTaskID());
    }

    /**
     * 写入指令至子车
     *
     * @param blockNo
     * @param scBlockCommand
     */
    void writeSc(String blockNo, ScBlockCommand scBlockCommand) {
        String key_1 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + scBlockCommand.ITEM_TARGET_LINE;
        String key_2 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + scBlockCommand.ITEM_COMMAND;
        String key_3 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + scBlockCommand.ITEM_TASK_ID;
        String key_4 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + scBlockCommand.ITEM_TARGET_TIER;
        String key_5 = BlockCommand.CHANEL_ALL + "." + blockNo + "." + scBlockCommand.ITEM_TARGET_ROW;
        write(key_1, new JIVariant(scBlockCommand.getTargetLine()), scBlockCommand.getTargetLine());
        write(key_2, new JIVariant(scBlockCommand.getCommand()), scBlockCommand.getCommand());
        write(key_4, new JIVariant(scBlockCommand.getTargetTier()), scBlockCommand.getTargetTier());
        write(key_5, new JIVariant(scBlockCommand.getTargetRow()), scBlockCommand.getTargetRow());
        write(key_3, new JIVariant(scBlockCommand.getTaskID()), scBlockCommand.getTaskID());
    }

    /**
     * 写入opc
     *
     * @param key
     * @param jiVariant
     * @param writeValue
     * @return
     */
    private boolean write(String key, JIVariant jiVariant, String writeValue) {
        boolean writeResult = false;
        for (int i = 0; i < 5; i++) {
            try {
                if (OpcDBDataCacheCenter.instance().getItemWriteMap().containsKey(key)) {
                    Item item = OpcDBDataCacheCenter.instance().getItemWriteMap().get(key);
                    item.write(jiVariant);
                    writeResult = true;
                    break;
                }
            } catch (JIException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName("OpcWriteException").warn("key:" + key + ",第" + i + "次，数据写入失败！" + e.toString());
                Item item = OpcDBDataCacheCenter.instance().getItemWriteMap().get(key);
                try {
                    item.write(jiVariant);
                    writeResult = true;
                    break;
                } catch (JIException e1) {
                    e1.printStackTrace();
                    LoggerUtil.getLoggerByName("OpcWriteException").warn("key:" + key + ",第" + i + "次，数据再次写入失败！" + e.toString());
                }
            }
        }
        return writeResult;
    }

}

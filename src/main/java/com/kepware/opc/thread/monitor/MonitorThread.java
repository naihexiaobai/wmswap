package com.kepware.opc.thread.monitor;

import com.kepware.opc.dao.OpcItemMapper;
import com.kepware.opc.dto.OpcMonitor;
import com.kepware.opc.dto.status.*;
import com.kepware.opc.entity.OpcItem;
import com.kepware.opc.interfaces.BlockStatusOperation;
import com.kepware.opc.interfaces.impl.*;
import com.kepware.opc.server.OpcDBDataCacheCenter;
import com.kepware.opc.server.OpcServerIns;
import com.thief.wcs.dao.PlcMapper;
import com.thief.wcs.entity.Plc;
import com.www.util.LoggerUtil;
import com.www.util.SpringTool;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 监控
 *
 * @auther CalmLake
 * @create 2018/3/19  11:31
 */
public class MonitorThread implements Runnable {
    /**
     * 间隔时间
     */
    private static final int PERIOD = 100;

    @Override
    public void run() {
        //1.获取数据
        Plc plc = new Plc();
        List<Plc> plcList = ((PlcMapper) SpringTool.getBeanByClass(PlcMapper.class)).selectByPlc(plc);
        List<OpcItem> opcItemList = ((OpcItemMapper) SpringTool.getBeanByClass(OpcItemMapper.class)).selectAll();
        //2.1 开启监控线程
        for (Plc plc1 : plcList) {
            if (plc1.getStatus() != 9) {
                List<OpcItem> opcItems = new ArrayList<OpcItem>();
                for (OpcItem opcItem : opcItemList) {
                    if (plc1.getId() == opcItem.getMachineinfoid()) {
                        opcItems.add(opcItem);
                    }
                }
                new Thread(new MachineThread(opcItems, plc1.getPlcname())).start();
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //2.2 初始化写入item
        for (Plc plc1 : plcList) {
            if (plc1.getStatus() != 9) {
                for (OpcItem opcItem : opcItemList) {
                    if (plc1.getId() == opcItem.getMachineinfoid()) {
                        OpcWriteItem(opcItem);
                    }
                }
            }
        }
        //
//        new Thread(new ServerLifeThread()).start();
    }

    public static void initWriteItemMap(Item item, String key, OpcItem opcItem) {
        if (OpcDBDataCacheCenter.instance().getItemWriteMap().containsKey(key)) {
        } else {
            OpcDBDataCacheCenter.instance().getItemWriteMap().put(key, item);
            LoggerUtil.getLoggerByName("initWriteTItem").info("key:" + key + ",item:" + item.getId());
        }
    }

    public static void OpcWriteItem(OpcItem opcItem) {
        if (OpcItem.ITEMTYPE_WRITE != opcItem.getItemtype()) {

        } else {
            String groupString = opcItem.getChanels() + "." + opcItem.getGroups();
            String key = opcItem.getChanels() + "." + opcItem.getGroups() + "." + opcItem.getItem();
            Server server = OpcServerIns.instance().getServer();
            Group group = null;
            try {
                group = server.findGroup(groupString);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    group = server.addGroup(groupString);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    group.setActive(true);
                    Item item = group.addItem(key);
                    initWriteItemMap(item, key, opcItem);
                } catch (JIException e) {
                    e.printStackTrace();
                } catch (AddFailedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ServerLifeThread implements Runnable {

        @Override
        public void run() {
            boolean result = true;
            long date = new Date().getTime();
            while (result) {
                //TODO   检测数据读取是否正常
                try {
//                    ScBlockStatus scBlockStatus = (ScBlockStatus) BlockStatusOperationUtil.getBlockStatus("SC03");
                    long nowDate = new Date().getTime();
                    if (((nowDate - date) / 1000 / 60) > 1) {
                        List<OpcMonitor> opcMonitorList = OpcDBDataCacheCenter.instance().getOpcMonitorList();
                        for (OpcMonitor opcMonitor : opcMonitorList) {
                            opcMonitor.getAccessBase().unbind();
                            opcMonitor.getAccessBase().bind();
                            LoggerUtil.getLoggerByName("ServerLifeThread").info(opcMonitor.getPlcName() + ",重新监听");
                        }
                        date = new Date().getTime();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class MachineThread implements Runnable {

        private List<OpcItem> opcItemList;

        private Group group;

        private String plcName;

        private HashMap<String, Monitor> monitorHashMap = new HashMap<String, Monitor>();

        public MachineThread(List<OpcItem> opcItemList, String plcName) {
            this.opcItemList = opcItemList;
            this.plcName = plcName;
        }

        @Override
        public void run() {
            initMonitorV3();
        }

        /**
         * 监控方法一，同步通过item读取
         *
         * @param server
         */
        private void initMonitorV1(Server server) {
            boolean result = true;
            initGroupItem(server);
            while (result) {
                for (Monitor monitor : monitorHashMap.values()) {
                    readItem(monitor.getKey());
                }
            }
        }

        /**
         * 监控方法二，异步读取
         */
        private void initMonitorV2(Server server) {
            try {
                final AccessBase access = new Async20Access(server, 100, false);
//                AccessBase access = new SyncAccess(server, 100);
                for (final OpcItem opcItem : opcItemList) {
                    final String key = opcItem.getChanels() + "." + opcItem.getGroups() + "." + opcItem.getItem();
                    if (opcItem.getDatatype() == OpcItem.DATATYPE_BOOLEAN) {
                        access.addItem(key, new DataCallback() {
                            public void changed(Item item, ItemState itemState) {
                                initWriteItemMap(item, key, opcItem);
                                try {
                                    String value = itemState.getValue() + "";

                                    value = value.contains("true") ? "true" : "false";
                                    LoggerUtil.getLoggerByName(plcName + "Monitor").info("key-" + key + ",value-" + value);
                                    initMonitorBlockStatus(key, value, opcItem);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
                                }
                            }
                        });
                    } else if (opcItem.getDatatype() == OpcItem.DATATYPE_WORD) {
                        access.addItem(key, new DataCallback() {
                            public void changed(Item item, ItemState itemState) {
                                initWriteItemMap(item, key, opcItem);
                                String value = "999";
                                try {
                                    value = itemState.getValue().getObjectAsUnsigned().getValue().toString();
                                    initMonitorBlockStatus(key, value, opcItem);

                                } catch (JIException e) {
                                    LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
                                    e.printStackTrace();
                                }
                                LoggerUtil.getLoggerByName(plcName + "Monitor").info("key-" + key + ",value-" + value);
                            }
                        });
                    }
                }
                access.bind();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
            } catch (NotConnectedException e) {
                LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
                e.printStackTrace();
            } catch (JIException e) {
                LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
                e.printStackTrace();
            } catch (DuplicateGroupException e) {
                LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
                e.printStackTrace();
            } catch (AddFailedException e) {
                LoggerUtil.getLoggerByName(plcName + "Exception").info(e.toString());
                e.printStackTrace();
            }
        }

        private void initMonitorV3() {
            Server server = null;
            AccessBase accessBase = null;
            try {
                server = OpcServerIns.instance().createServer();
                LoggerUtil.getLoggerByName("opcMonitor").info(plcName + ",获取server," + server.isDefaultActive());
                accessBase = new Async20Access(server, PERIOD, true);
                accessBase.connectionStateChanged(true);
                for (final OpcItem opcItem1 : opcItemList) {
                    final String key = opcItem1.getChanels() + "." + opcItem1.getGroups() + "." + opcItem1.getItem();
                    accessBase.addItem(key, new DataCallback() {
                        @Override
                        public void changed(Item item, ItemState itemState) {
                            try {
                                String value = VariantDumper.dumpValue("\t", itemState.getValue().getObject());
                                LoggerUtil.getLoggerByName("opcMonitor").info("plcName:" + plcName + ",item:" + item.getId() + ",value:" + value);
                                initMonitorBlockStatus(key, value, opcItem1);
                            } catch (final JIException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
//                accessBase.bind();
                List<OpcMonitor> opcMonitorList = OpcDBDataCacheCenter.instance().getOpcMonitorList();
                for (OpcMonitor opcMonitor : opcMonitorList) {
                    if (plcName.equals(opcMonitor.getPlcName())) {
                        opcMonitorList.remove(opcMonitor);
                    }
                }
                OpcMonitor opcMonitor = new OpcMonitor();
                opcMonitor.setPlcName(plcName);
                opcMonitor.setServer(server);
                opcMonitor.setAccessBase(accessBase);
                opcMonitor.setMonitorStatus(true);
                OpcDBDataCacheCenter.instance().getOpcMonitorList().add(opcMonitor);
                boolean whileResultStatus = true;
                while (true && whileResultStatus) {
                    try {
                        int count = 0;
                        List<OpcMonitor> opcMonitorLists = OpcDBDataCacheCenter.instance().getOpcMonitorList();
                        for (OpcMonitor opcMonitorWhile : opcMonitorLists) {
                            if (plcName.equals(opcMonitorWhile.getPlcName()) && opcMonitorWhile.isMonitorStatus()) {
                                count = 1;
                                opcMonitor = opcMonitorWhile;
                            } else if (plcName.equals(opcMonitorWhile.getPlcName()) && !opcMonitorWhile.isMonitorStatus()) {
                                count = 2;
                            } else {

                            }
                        }
                        if (count == 1) {
                            accessBase.unbind();
                            accessBase.bind();
                            LoggerUtil.getLoggerByName("opcMonitor").info(plcName + ",重新监听," + accessBase.isBound());
                            long startTime = new Date().getTime();
                            while (true) {
                                long nowTime = new Date().getTime();
                                long second = (nowTime - startTime) / 1000;
                                long minute = second / 60;
                                Thread.sleep(5 * 1000);
                                if (minute > 5) {
                                    break;
                                }
                                if (!opcMonitor.isMonitorStatus()) {
                                    whileResultStatus = false;
                                    break;
                                }
                            }
                        } else if (count == 2) {
                            accessBase.unbind();
                            Thread.sleep(3 * 1000);
                        } else {
                            LoggerUtil.getLoggerByName("opcMonitorException").info(plcName + ",找不到对应的OpcMonitor");
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LoggerUtil.getLoggerByName("opcMonitorException").info(e.getMessage());
                        break;
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName("opcMonitorException").info(e.getMessage());
            } catch (NotConnectedException e) {
                LoggerUtil.getLoggerByName("opcMonitorException").info(e.getMessage());
                e.printStackTrace();
            } catch (JIException e) {
                LoggerUtil.getLoggerByName("opcMonitorException").info(e.getMessage());
                e.printStackTrace();
            } catch (DuplicateGroupException e) {
                LoggerUtil.getLoggerByName("opcMonitorException").info(e.getMessage());
                e.printStackTrace();
            } catch (AddFailedException e) {
                LoggerUtil.getLoggerByName("opcMonitorException").info(e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    accessBase.clear();
                    accessBase.unbind();
                    server.dispose();
                } catch (JIException e) {
                    e.printStackTrace();
                }
                LoggerUtil.getLoggerByName("opcMonitorException").info(plcName + ",循环监听跳出！");
            }

        }


        private void initGroupItem(Server server) {
            if (server == null || !server.isDefaultActive()) {
                OpcServerIns.instance().createServer();
                server = OpcServerIns.instance().getServer();
                LoggerUtil.getLoggerByName(plcName + "monitor").info("server为空，重新创建" + server.getDefaultLocaleID());
            }
            try {
                try {
                    group = server.findGroup(opcItemList.get(0).getChanels() + "." + opcItemList.get(0).getGroups());
                } catch (UnknownGroupException e) {
                    e.printStackTrace();
                    group = server.addGroup(opcItemList.get(0).getChanels() + "." + opcItemList.get(0).getGroups());
                    LoggerUtil.getLoggerByName(plcName + "monitor").info("获取group失败，新增group" + group.getName());
                }
                for (OpcItem opcItem : opcItemList) {
                    String key = opcItem.getChanels() + "." + opcItem.getGroups() + "." + opcItem.getItem();
                    if (monitorHashMap.containsKey(key)) {
                    } else {
                        Monitor monitor = new Monitor();
                        Item item = group.addItem(key);
                        monitor.setItem(item);
                        monitor.setKey(key);
                        monitor.setOpcItem(opcItem);
                        monitorHashMap.put(key, monitor);
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(plcName + "monitorException").info("异常：" + e.getMessage());
            } catch (JIException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(plcName + "monitorException").info("异常：" + e.getMessage());
            } catch (NotConnectedException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(plcName + "monitorException").info("异常：" + e.getMessage());
            } catch (DuplicateGroupException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(plcName + "monitorException").info("异常：" + e.getMessage());
            } catch (AddFailedException e) {
                LoggerUtil.getLoggerByName(plcName + "monitorException").info("异常：" + e.getMessage());
                e.printStackTrace();
            }
        }

        private void readItem(String key) {
            try {
                ItemState itemState = monitorHashMap.get(key).getItem().read(true);
                Byte dataType = monitorHashMap.get(key).getOpcItem().getDatatype();
                if (OpcItem.DATATYPE_BOOLEAN == dataType) {
                    String value = itemState.getValue().toString();
                    value = value.contains("true") ? "true" : "false";
                    LoggerUtil.getLoggerByName(plcName + "monitor").info("读取信息：key :" + key + ",value :" + value);
                    OpcDBDataCacheCenter.instance().getMonitorData().put(key, value);
                } else if (OpcItem.DATATYPE_WORD == dataType) {
                    String value = "10001";
                    try {
                        value = itemState.getValue().getObjectAsUnsigned().getValue().toString();
                    } catch (JIException e) {
                        e.printStackTrace();
                        LoggerUtil.getLoggerByName(plcName + "monitorJIException").warn("读取信息：key :" + key + ",异常信息" + e.getMessage());
                    }
                    OpcDBDataCacheCenter.instance().getMonitorData().put(key, value);
                    LoggerUtil.getLoggerByName(plcName + "monitor").info("读取信息：key :" + key + ",value :" + value);
                } else {
                    String value = itemState.getValue().toString();
                    OpcDBDataCacheCenter.instance().getMonitorData().put(key, value);
                    LoggerUtil.getLoggerByName(plcName + "monitor").info("读取信息：key :" + key + ",value :" + value);
                }
            } catch (JIException e) {
                e.printStackTrace();
                LoggerUtil.getLoggerByName(plcName + "monitorJIException").info("读取信息：key :" + key + ",异常信息" + e.getMessage());
            }
        }
    }


    public static void initMonitorBlockStatus(String key, String value, OpcItem opcItem) {
        if (value == null) {
            return;
        }
        String blockNo = opcItem.getGroups();
        String _key = opcItem.getChanels() + "." + opcItem.getGroups() + "." + opcItem.getItem();
        if (key.equals(_key)) {
            if (OpcDBDataCacheCenter.instance().getMonitorBlockStatusMap().containsKey(blockNo)) {
                BlockStatus blockStatus = OpcDBDataCacheCenter.getMonitorBlockStatusMap().get(blockNo);
                updateMonitorBlockStatus(key, value, opcItem, blockStatus);
            } else {
                addMonitorBlockStatus(key, value, opcItem);
            }
        } else {
            LoggerUtil.getLoggerByName(blockNo + "updateBlockStatusWarn").info("blockNo:" + opcItem.getGroups() + "key:" + key + ",_key:" + _key);
        }
    }


    private static void addMonitorBlockStatus(String key, String value, OpcItem opcItem) {
        BlockStatusOperation blockStatusOperation = null;
        if (opcItem.getGroups().contains(OpcItem.MACHINE_TYPE_EL)) {
            blockStatusOperation = new ELBlockStatusOperation();
        } else if (opcItem.getGroups().contains(OpcItem.MACHINE_TYPE_LF)) {
            blockStatusOperation = new LBlockStatusOperation();
        } else if (opcItem.getGroups().contains(OpcItem.MACHINE_TYPE_MC)) {
            blockStatusOperation = new McBlockStatusOperation();
        } else if (opcItem.getGroups().contains(OpcItem.MACHINE_TYPE_ML)) {
            blockStatusOperation = new MlBlockStatusOperation();
        } else if (opcItem.getGroups().contains(OpcItem.MACHINE_TYPE_SC)) {
            blockStatusOperation = new ScBlockStatusOperation();
        } else {
            new UnKnowBlockStatusException("数据匹配失败" + opcItem.getGroups() + ",key-" + key);
        }
        blockStatusOperation.addMonitorData(key, value, opcItem);
    }

    private static void updateMonitorBlockStatus(String key, String value, OpcItem opcItem, BlockStatus blockStatus) {
        BlockStatusOperation blockStatusOperation = null;
        blockStatus.setLastUpdateTime(new Date());
        if (blockStatus instanceof ElBlockStatus) {
            blockStatusOperation = new ELBlockStatusOperation();
        } else if (blockStatus instanceof LBlockStatus) {
            blockStatusOperation = new LBlockStatusOperation();
        } else if (blockStatus instanceof McBlockStatus) {
            blockStatusOperation = new McBlockStatusOperation();
        } else if (blockStatus instanceof MlBlockStatus) {
            blockStatusOperation = new MlBlockStatusOperation();
        } else if (blockStatus instanceof ScBlockStatus) {
            blockStatusOperation = new ScBlockStatusOperation();
        } else {
            new UnKnowBlockStatusException("数据匹配失败" + opcItem.getGroups() + ",key-" + key);
        }
        blockStatusOperation.updateMonitorData(key, value, opcItem, blockStatus);
    }
}

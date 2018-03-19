package com.kepware.opc;

import com.www.util.LoggerUtil;
import com.wap.model.OpcItemFinalString;
import com.wap.model.OpcItems;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @auther CalmLake
 * @create 2017/11/21  09:40
 */
public class OpcServer {

//    private static LoggerUtil loggerUtil = new LoggerUtil("OpcServer");
    private static final String HOST = "localhost";
    private static final String DOMAIN = "localhost";
    private static final String USER = "admin";
    private static final String PASSWORD = "ren130303.";
    private static final String PROGID = "Kepware.KEPServerEX 6.3";
    private static final String CLSID = "7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729";
    private static final int POOLSIZE = 2;
    public static final String KEYREAD = "readItemKey";
    public static final String KEYWRITE = "writeItemKey";

    private Server server;

    public static List<OpcServerModel> opcServerModels = new ArrayList<OpcServerModel>();

    /**
     * 监控数据
     */
    public static Map<String, String> monitoringMap = new HashMap<String, String>();

    private static OpcServer ourInstance = new OpcServer();

    public static OpcServer getInstance() {
        return ourInstance;
    }

    private OpcServer() {
        if (OpcServer.getInstance().opcServerModels.size() < 1) {
            for (int i = 0; i < POOLSIZE; i++) {
                tryAgain();
            }
            initMap();
        } else {
//            loggerUtil.getLoggerLevelInfo().info("opcServer - serverList已创建");
        }
    }

    /**
     * 初始化map中设备数据
     */
    private void initMap() {
        OpcServer.monitoringMap.put(OpcItemFinalString.MUCHEYILOCK, "false");
        OpcServer.monitoringMap.put(OpcItemFinalString.MUCHEERLOCK, "false");
        OpcServer.monitoringMap.put(OpcItemFinalString.MUCHESANLOCK, "false");
        OpcServer.monitoringMap.put(OpcItemFinalString.DDJLOCK, "false");
    }

    private Server createServer() {
        Server server = null;
        try {
            ConnectionInformation ci = new ConnectionInformation();
            ci.setHost(HOST);
            ci.setUser(USER);
            ci.setPassword(PASSWORD);
            ci.setDomain(DOMAIN);
            ci.setProgId(PROGID);
            ci.setClsid(CLSID);
            server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
            server.connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - server创建连接失败-" + e.getMessage());
        } catch (JIException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - server创建连接失败-" + e.getMessage());
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - server创建连接失败-" + e.getMessage());
        } finally {

        }
        return server;
    }

    /**
     * 创建连接且重试
     */
    private void tryAgain() {
        int i = 0;
        while (i < 5) {
            server = createServer();
            if (server != null) {
                OpcServerModel opcServerModel = new OpcServerModel();
                opcServerModel.setServer(server);
                opcServerModel.setStatus(0);
                OpcServer.getInstance().opcServerModels.add(opcServerModel);
//                loggerUtil.getLoggerLevelInfo().info("opcServer - server创建连接成功");
                break;
            }
            i++;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void read(List<OpcItems> opcItemsList) {
        try {
//            for (OpcServerModel opcServerModel : OpcServer.getInstance().opcServerModels) {
//                if (!result && opcServerModel.getStatus() == 0) {
            Server server = createServer();
//            AccessBase access = new SyncAccess(server, 500);
//            AccessBase access = new Async20Access(server, 500, true);
//            for (OpcItems opcItems : opcItemsList) {
//                try {
//                    if (opcItems.getDatatype() == 1) {
//                        readItemBoolean(access, opcItems.getItem());
//                    } else if (opcItems.getDatatype() == 2) {
//                        readItemWord(access, opcItems.getItem());
//                    }
//                } catch (JIException e) {
//                    e.printStackTrace();
//                } catch (AddFailedException e) {
//                    e.printStackTrace();
//                }
//            }
//            access.bind();
            for (int machineId = 4; machineId < 12; machineId++) {
                List<OpcItems> opcItemsList1 = new ArrayList<OpcItems>();
                for (OpcItems opcItems : opcItemsList) {
                    if (machineId == opcItems.getMachineinfoid()) {
                        opcItemsList1.add(opcItems);
                    }
                }
                final AccessBase access = new Async20Access(server, 500, true);
                ReadThread readThread = new ReadThread(access, opcItemsList1);
                new Thread(readThread).start();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (NotConnectedException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (JIException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (DuplicateGroupException e) {
            e.printStackTrace();
//            loggerUtil.getLoggerLevelWarn().info("opcServer - 读取数据异常-" + e.getMessage());
        }
    }

    public static class ReadThread implements Runnable {

        private AccessBase access;
        private List<OpcItems> opcItemsList = new ArrayList<OpcItems>();

        public ReadThread(AccessBase access, List<OpcItems> opcItemsList) {
            this.access = access;
            this.opcItemsList = opcItemsList;
        }

        public void run() {
            for (OpcItems opcItems : opcItemsList) {
                try {
                    if (opcItems.getDatatype() == 1) {
                        readItemBoolean(access, opcItems.getItem());
                    } else if (opcItems.getDatatype() == 2) {
                        readItemWord(access, opcItems.getItem());
                    }
                } catch (JIException e) {
                    e.printStackTrace();
                } catch (AddFailedException e) {
                    e.printStackTrace();
                }
            }
            access.bind();
        }


    }

    private static void readItemBoolean(AccessBase access, final String itemId) throws JIException, AddFailedException {
        try {
            access.addItem(itemId, new DataCallback() {
                public void changed(Item item, ItemState itemState) {
                    String value = itemState.getValue() + "";
                    value = getCleanValue(value);
                    monitoringMap.put(itemId, value);
                }
            });
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        }
    }

    private static void readItemWord(AccessBase access, final String itemId) throws JIException, AddFailedException {
        try {
            access.addItem(itemId, new DataCallback() {
                public void changed(Item item, ItemState itemState) {
                    String value = "";
                    try {
                        value = itemState.getValue().getObjectAsUnsigned().getValue().toString();
                        monitoringMap.put(itemId, value);
                    } catch (JIException e) {
                        e.printStackTrace();
                        monitoringMap.put(itemId, "9999");
//                        loggerUtil.getLoggerLevelWarn().info("opcServer - word数据转换异常-" + e.getMessage());
                    }
                }
            });
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        }
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    private static String getCleanValue(String result) {
        if (result.contains("true")) {
            return "true";
        } else if (result.contains("false")) {
            return "false";
        } else {
            return "999";
        }
    }


    public void serverAddGroups() {
        boolean result = false;
        for (OpcServerModel opcServerModel : OpcServer.getInstance().opcServerModels) {
            if (opcServerModel.getKey() != null && !"".equals(opcServerModel.getKey()) && opcServerModel.getKey().equals(OpcServer.KEYWRITE)) {
                result = true;
                break;
            }
        }
        if (!result) {
            boolean resultWrite = false;
            for (OpcServerModel opcServerModel : OpcServer.getInstance().opcServerModels) {
                if (!resultWrite && opcServerModel.getStatus() == 0 && opcServerModel.getServer().isDefaultActive()) {
                    opcServerModel.setStatus(1);
                    opcServerModel.setKey(OpcServer.KEYWRITE);
                    try {
                        opcServerModel.getServer().addGroup("machine.duiDuoJi");
                        opcServerModel.getServer().addGroup("machine.erHaoMuChe");
                        opcServerModel.getServer().addGroup("machine.erHaoZiChe");
                        opcServerModel.getServer().addGroup("machine.sanHaoMuChe");
                        opcServerModel.getServer().addGroup("machine.sanHaoZiChe");
                        opcServerModel.getServer().addGroup("machine.shengJiangJi");
                        opcServerModel.getServer().addGroup("machine.yiHaoMuChe");
                        opcServerModel.getServer().addGroup("machine.yiHaoZiChe");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                    } catch (JIException e) {
                        e.printStackTrace();
                    } catch (DuplicateGroupException e) {
                        e.printStackTrace();
                    }
                    resultWrite = true;
                }
            }
        }
    }


    public boolean write(MsgModel msgModel) {
        boolean writeResult = false;
        serverAddGroups();
        for (OpcServerModel opcServerModel : OpcServer.getInstance().opcServerModels) {
            if (opcServerModel.getStatus() == 1 && opcServerModel.getServer().isDefaultActive() && opcServerModel.getKey().equals(OpcServer.KEYWRITE)) {
                try {
                    if (msgModel instanceof ZiCheMsgModel) {
                        ZiCheMsgModel ziCheMsgModel = (ZiCheMsgModel) msgModel;
                        String groupName = "";
                        if (ziCheMsgModel.getNO() == 4) {
                            groupName = "machine.yiHaoZiChe";
                        } else if (ziCheMsgModel.getNO() == 5) {
                            groupName = "machine.erHaoZiChe";
                        } else if (ziCheMsgModel.getNO() == 6) {
                            groupName = "machine.sanHaoZiChe";
                        }
                        Group group = opcServerModel.getServer().findGroup(groupName);
                        if (group != null) {
                            Item itemRow = group.addItem(groupName + ".muBiaoPai");
                            Item itemLine = group.addItem(groupName + ".muBiaoLie");
                            Item itemStorey = group.addItem(groupName + ".muBiaoCeng");
                            Item itemCommand = group.addItem(groupName + ".dongZuoZhiLing");
                            Item itemOrderNum = group.addItem(groupName + ".dongZuoRenWuHao");

                            itemRow.write(new JIVariant(ziCheMsgModel.getTargetRow()));
                            itemLine.write(new JIVariant(ziCheMsgModel.getTargetLine()));
                            itemStorey.write(new JIVariant(ziCheMsgModel.getTargetStorey()));
                            itemCommand.write(new JIVariant(ziCheMsgModel.getCommandNum()));
                            itemOrderNum.write(new JIVariant(ziCheMsgModel.getOrderNum()));
                            writeResult = true;
//                            loggerUtil.getLoggerLevelInfo().info("成功---" + ziCheMsgModel.toString());
                        } else {
                            writeResult = false;
                        }
                    }
                    if (msgModel instanceof MuCheMsgModel) {
                        MuCheMsgModel muCheMsgModel = (MuCheMsgModel) msgModel;
                        String groupName = "";
                        if (muCheMsgModel.getNO() == 7) {
                            groupName = "machine.yiHaoMuChe";
                        } else if (muCheMsgModel.getNO() == 8) {
                            groupName = "machine.erHaoMuChe";
                        } else if (muCheMsgModel.getNO() == 9) {
                            groupName = "machine.sanHaoMuChe";
                        }
                        Group group = opcServerModel.getServer().findGroup(groupName);
                        if (group != null) {
                            Item itemLine = group.addItem(groupName + ".muBiaoLie");
                            Item itemCommand = group.addItem(groupName + ".dongZuoZhiLing");
                            Item itemOrderNum = group.addItem(groupName + ".dongZuoRenWuHao");

                            itemLine.write(new JIVariant(muCheMsgModel.getTargetLine()));
                            itemCommand.write(new JIVariant(muCheMsgModel.getCommandNum()));
                            itemOrderNum.write(new JIVariant(muCheMsgModel.getOrderNum()));
                            writeResult = true;
//                            loggerUtil.getLoggerLevelInfo().info("成功---" + muCheMsgModel.toString());
                        } else {
                            writeResult = false;
                        }
                    }
                    if (msgModel instanceof DuiDuoJiMsgModel) {
                        DuiDuoJiMsgModel duiDuoJiMsgModel = (DuiDuoJiMsgModel) msgModel;
                        String groupName = "";
                        if (duiDuoJiMsgModel.getNO() == 10) {
                            groupName = "machine.duiDuoJi";
                        }
                        Group group = opcServerModel.getServer().findGroup(groupName);
                        if (group != null) {
                            Item itemLine = group.addItem(groupName + ".muBiaoLie");
                            Item itemStorey = group.addItem(groupName + ".muBiaoCeng");
                            Item itemCommand = group.addItem(groupName + ".dongZuoZhiLing");
                            Item itemOrderNum = group.addItem(groupName + ".dongZuoRenWuHao");

                            itemLine.write(new JIVariant(duiDuoJiMsgModel.getTargetLine()));
                            itemStorey.write(new JIVariant(duiDuoJiMsgModel.getTargetStorey()));
                            itemCommand.write(new JIVariant(duiDuoJiMsgModel.getCommandNum()));
                            itemOrderNum.write(new JIVariant(duiDuoJiMsgModel.getOrderNum()));
                            writeResult = true;
//                            loggerUtil.getLoggerLevelInfo().info("成功---" + duiDuoJiMsgModel.toString());
                        } else {
                            writeResult = false;
                        }
                    }
                    if (msgModel instanceof ShengJiangJiMsgModel) {
                        ShengJiangJiMsgModel shengJiangJiMsgModel = (ShengJiangJiMsgModel) msgModel;
                        String groupName = "";
                        if (shengJiangJiMsgModel.getNO() == 11) {
                            groupName = "machine.shengJiangJi";
                        }
                        Group group = opcServerModel.getServer().findGroup(groupName);
                        if (group != null) {
                            Item itemStorey = group.addItem(groupName + ".muBiaoCeng");
                            Item itemCommand = group.addItem(groupName + ".dongZuoZhiLing");
                            Item itemOrderNum = group.addItem(groupName + ".dongZuoRenWuHao");

                            itemStorey.write(new JIVariant(shengJiangJiMsgModel.getTargetStorey()));
                            itemCommand.write(new JIVariant(shengJiangJiMsgModel.getCommandNum()));
                            itemOrderNum.write(new JIVariant(shengJiangJiMsgModel.getOrderNum()));
                            writeResult = true;
//                            loggerUtil.getLoggerLevelInfo().info("成功---" + shengJiangJiMsgModel.toString());
                        } else {
                            writeResult = false;
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    writeResult = false;
                } catch (JIException e) {
                    e.printStackTrace();
                    writeResult = false;
                } catch (UnknownGroupException e) {
                    e.printStackTrace();
                    writeResult = false;
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                    writeResult = false;
                } catch (AddFailedException e) {
                    e.printStackTrace();
                    writeResult = false;
                }
                break;
            }
        }
        return writeResult;
    }


    public static void main(String[] aeg) {
        try {
            ConnectionInformation ci = new ConnectionInformation();
            ci.setHost(HOST);
            ci.setUser(USER);
            ci.setPassword(PASSWORD);
            ci.setDomain(DOMAIN);
            ci.setProgId(PROGID);
            ci.setClsid(CLSID);
            Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
            server.connect();
            final AccessBase access = new SyncAccess(server, 600);
            String opcItems = "machine.yiHaoZiChe.ziShouDong&1@machine.yiHaoZiChe.daiMing&1@machine.yiHaoZiChe.kongXian&1@machine.yiHaoZiChe.tiShengZhong&1@machine.yiHaoZiChe.xiaJiangZhong&1@machine.yiHaoZiChe.qianJinZhong&1@machine.yiHaoZiChe.houTuiZhong&1@machine.yiHaoZiChe.AYuanDianDaiJi&1@machine.yiHaoZiChe.BYuanDianDaiJi&1@machine.yiHaoZiChe.zaiWu&1@machine.yiHaoZiChe.shiFouGuZhang&1@machine.yiHaoZiChe.beiYongZhuangTai&1@machine.yiHaoZiChe.jingGao&1@machine.yiHaoZiChe.chongDian&1@machine.yiHaoZiChe.tiShengJiShangDaiJI&1@machine.yiHaoZiChe.muCheShangDaiJi&1@machine.yiHaoZiChe.dianLiang&2@machine.yiHaoZiChe.renWuMa&2@machine.yiHaoZiChe.lie&2@machine.yiHaoZiChe.pai&2@machine.yiHaoZiChe.ceng&2@machine.yiHaoZiChe.wcsRenWuMa&2@machine.yiHaoZiChe.yaoKongQiRenWuMa&2@machine.yiHaoZiChe.panDianTuoPanShu&2@machine.yiHaoZiChe.guZhangXinXi&2@machine.yiHaoZiChe.dongZuoZhiLing&2@machine.yiHaoZiChe.dongZuoRenWuHao&2@machine.yiHaoZiChe.muBiaoCeng&2@machine.yiHaoZiChe.muBiaoLie&2@machine.yiHaoZiChe.muBiaoPai&2@machine.sanHaoZiChe.ziShouDong&1@machine.sanHaoZiChe.daiMing&1@machine.sanHaoZiChe.kongXian&1@machine.sanHaoZiChe.tiShengZhong&1@machine.sanHaoZiChe.xiaJiangZhong&1@machine.sanHaoZiChe.qianJinZhong&1@machine.sanHaoZiChe.houTuiZhong&1@machine.sanHaoZiChe.AYuanDianDaiJi&1@machine.sanHaoZiChe.BYuanDianDaiJi&1@machine.sanHaoZiChe.zaiWu&1@machine.sanHaoZiChe.shiFouGuZhang&1@machine.sanHaoZiChe.beiYongZhuangTai&1@machine.sanHaoZiChe.jingGao&1@machine.sanHaoZiChe.chongDian&1@machine.sanHaoZiChe.tiShengJiShangDaiJI&1@machine.sanHaoZiChe.muCheShangDaiJi&1@machine.sanHaoZiChe.dianLiang&2@machine.sanHaoZiChe.renWuMa&2@machine.sanHaoZiChe.lie&2@machine.sanHaoZiChe.pai&2@machine.sanHaoZiChe.ceng&2@machine.sanHaoZiChe.wcsRenWuMa&2@machine.sanHaoZiChe.yaoKongQiRenWuMa&2@machine.sanHaoZiChe.panDianTuoPanShu&2@machine.sanHaoZiChe.dongZuoZhiLing&2@machine.sanHaoZiChe.dongZuoRenWuHao&2@machine.sanHaoZiChe.muBiaoCeng&2@machine.sanHaoZiChe.muBiaoLie&2@machine.sanHaoZiChe.muBiaoPai&2@machine.erHaoZiChe.ziShouDong&1@machine.erHaoZiChe.daiMing&1@machine.erHaoZiChe.kongXian&1@machine.erHaoZiChe.tiShengZhong&1@machine.erHaoZiChe.xiaJiangZhong&1@machine.erHaoZiChe.qianJinZhong&1@machine.erHaoZiChe.houTuiZhong&1@machine.erHaoZiChe.AYuanDianDaiJi&1@machine.erHaoZiChe.BYuanDianDaiJi&1@machine.erHaoZiChe.zaiWu&1@machine.erHaoZiChe.shiFouGuZhang&1@machine.erHaoZiChe.beiYongZhuangTai&1@machine.erHaoZiChe.jingGao&1@machine.erHaoZiChe.chongDian&1@machine.erHaoZiChe.tiShengJiShangDaiJI&1@machine.erHaoZiChe.muCheShangDaiJi&1@machine.erHaoZiChe.dianLiang&2@machine.erHaoZiChe.renWuMa&2@machine.erHaoZiChe.lie&2@machine.erHaoZiChe.pai&2@machine.erHaoZiChe.ceng&2@machine.erHaoZiChe.wcsRenWuMa&2@machine.erHaoZiChe.yaoKongQiRenWuMa&2@machine.erHaoZiChe.panDianTuoPanShu&2@machine.erHaoZiChe.dongZuoZhiLing&2@machine.erHaoZiChe.dongZuoRenWuHao&2@machine.erHaoZiChe.muBiaoCeng&2@machine.erHaoZiChe.muBiaoLie&2@machine.erHaoZiChe.muBiaoPai&2@machine.yiHaoMuChe.ziShouDong&1@machine.yiHaoMuChe.daiMing&1@machine.yiHaoMuChe.kongXian&1@machine.yiHaoMuChe.qianJinZhong&1@machine.yiHaoMuChe.houTuiZhong&1@machine.yiHaoMuChe.huoChaZuoShen&1@machine.yiHaoMuChe.huoChaZuoHui&1@machine.yiHaoMuChe.huoChaYouShen&1@machine.yiHaoMuChe.huoChaYouHui&1@machine.yiHaoMuChe.huoChaZhongWei&1@machine.yiHaoMuChe.zaiWu&1@machine.yiHaoMuChe.zaiChe&1@machine.yiHaoMuChe.shiFouGuZhang&1@machine.yiHaoMuChe.fuWei&1@machine.yiHaoMuChe.wcsRenWuMa&2@machine.yiHaoMuChe.renWuMa&2@machine.yiHaoMuChe.lie&2@machine.yiHaoMuChe.guZhangXinXi&2@machine.yiHaoMuChe.muBiaoLie&2@machine.yiHaoMuChe.dongZuoZhiLing&2@machine.yiHaoMuChe.dongZuoRenWuHao&2@machine.erHaoMuChe.ziShouDong&1@machine.erHaoMuChe.daiMing&1@machine.erHaoMuChe.kongXian&1@machine.erHaoMuChe.qianJinZhong&1@machine.erHaoMuChe.houTuiZhong&1@machine.erHaoMuChe.huoChaZuoShen&1@machine.erHaoMuChe.huoChaZuoHui&1@machine.erHaoMuChe.huoChaYouShen&1@machine.erHaoMuChe.huoChaYouHui&1@machine.erHaoMuChe.huoChaZhongWei&1@machine.erHaoMuChe.zaiWu&1@machine.erHaoMuChe.zaiChe&1@machine.erHaoMuChe.shiFouGuZhang&1@machine.erHaoMuChe.fuWei&1@machine.erHaoMuChe.wcsRenWuMa&2@machine.erHaoMuChe.renWuMa&2@machine.erHaoMuChe.lie&2@machine.erHaoMuChe.muBiaoLie&2@machine.erHaoMuChe.dongZuoZhiLing&2@machine.erHaoMuChe.dongZuoRenWuHao&2@machine.duiDuoJi.ziShouDong&1@machine.duiDuoJi.daiMing&1@machine.duiDuoJi.kongXian&1@machine.duiDuoJi.tiSheng&1@machine.duiDuoJi.xiaJiang&1@machine.duiDuoJi.qianJinZhong&1@machine.duiDuoJi.houTuiZhong&1@machine.duiDuoJi.huoChaZuoShen&1@machine.duiDuoJi.huoChaZuoHui&1@machine.duiDuoJi.huoChaYouShen&1@machine.duiDuoJi.huoChaYouHui&1@machine.duiDuoJi.huoChaZhongWei&1@machine.duiDuoJi.zaiWu&1@machine.duiDuoJi.zaiChe&1@machine.duiDuoJi.shiFouGuZhang&1@machine.duiDuoJi.beiYongZhuangTai&1@machine.duiDuoJi.renWuMa&2@machine.duiDuoJi.lie&2@machine.duiDuoJi.ceng&2@machine.duiDuoJi.guZhangXinXi&2@machine.duiDuoJi.muBiaoLie&2@machine.duiDuoJi.muBiaoCeng&2@machine.duiDuoJi.dongZuoZhiLing&2@machine.duiDuoJi.dongZuoRenWuHao&2@machine.shengJiangJi.ziShouDong&1@machine.shengJiangJi.daiMing&1@machine.shengJiangJi.kongXian&1@machine.shengJiangJi.tiSheng&1@machine.shengJiangJi.xiaJiang&1@machine.shengJiangJi.huoChaZuoShen&1@machine.shengJiangJi.huoChaZuoHui&1@machine.shengJiangJi.huoChaYouShen&1@machine.shengJiangJi.huoChaYouHui&1@machine.shengJiangJi.huoChaZhongWei&1@machine.shengJiangJi.zaiWu&1@machine.shengJiangJi.zaiChe&1@machine.shengJiangJi.shiFouGuZhang&1@machine.shengJiangJi.beiYongZhuangTai&1@machine.shengJiangJi.fuWei&1@machine.shengJiangJi.renWuMa&2@machine.shengJiangJi.ceng&2@machine.shengJiangJi.guZhangXinXi&2@machine.shengJiangJi.muBiaoCeng&2@machine.shengJiangJi.dongZuoZhiLing&2@machine.shengJiangJi.dongZuoRenWuHao&2@machine.sanHaoMuChe.ziShouDong&1@machine.sanHaoMuChe.daiMing&1@machine.sanHaoMuChe.kongXian&1@machine.sanHaoMuChe.qianJinZhong&1@machine.sanHaoMuChe.houTuiZhong&1@machine.sanHaoMuChe.huoChaZuoShen&1@machine.sanHaoMuChe.huoChaZuoHui&1@machine.sanHaoMuChe.huoChaYouShen&1@machine.sanHaoMuChe.huoChaYouHui&1@machine.sanHaoMuChe.huoChaZhongWei&1@machine.sanHaoMuChe.zaiWu&1@machine.sanHaoMuChe.zaiChe&1@machine.sanHaoMuChe.shiFouGuZhang&1@machine.sanHaoMuChe.fuWei&1@machine.sanHaoMuChe.wcsRenWuMa&2@machine.sanHaoMuChe.renWuMa&2@machine.sanHaoMuChe.lie&2@machine.sanHaoMuChe.muBiaoLie&2@machine.sanHaoMuChe.dongZuoZhiLing&2@machine.sanHaoMuChe.dongZuoRenWuHao&2@machine.shengJiangJi.ZhuangTaiYiHaoWei&1@machine.shengJiangJi.daiMingYiHaoWei&1@machine.shengJiangJi.kongXianYiHaoWei&1@machine.shengJiangJi.waiWeiZhangAiYiHaoWei&1@machine.shengJiangJi.qiDongYiHaoWei&1@machine.shengJiangJi.zaiWuYiHaoWei&1@machine.shengJiangJi.renWuMaYiHaoWei&2@machine.shengJiangJi.dongZuoZhiLingYiHaoWei&2@machine.shengJiangJi.wcsRenWuMaYiHaoWei&2@machine.shengJiangJi.ZhuangTaiWuHaoWei&1@machine.shengJiangJi.daiMingWuHaoWei&1@machine.shengJiangJi.kongXianWuHaoWei&1@machine.shengJiangJi.waiWeiZhangAiWuHaoWei&1@machine.shengJiangJi.qiDongWuHaoWei&1@machine.shengJiangJi.zaiWuWuHaoWei&1@machine.shengJiangJi.renWuMaWuHaoWei&2@machine.shengJiangJi.dongZuoZhiLingWuHaoWei&2@machine.shengJiangJi.wcsRenWuMaWuHaoWei&2@machine.shengJiangJi.ZhuangTaiLiuHaoWei&1@machine.shengJiangJi.daiMingLiuHaoWei&1@machine.shengJiangJi.kongXianLiuHaoWei&1@machine.shengJiangJi.waiWeiZhangAiLiuHaoWei&1@machine.shengJiangJi.qiDongLiuHaoWei&1@machine.shengJiangJi.zaiWuLiuHaoWei&1@machine.shengJiangJi.renWuMaLiuHaoWei&2@machine.shengJiangJi.dongZuoZhiLingLiuHaoWei&2@machine.shengJiangJi.wcsRenWuMaLiuHaoWei&2@machine.shengJiangJi.ZhuangTaiErHaoWei&1@machine.shengJiangJi.daiMingErHaoWei&1@machine.shengJiangJi.kongXianErHaoWei&1@machine.shengJiangJi.zaiWuErHaoWei&1@machine.shengJiangJi.renWuMaErHaoWei&2@machine.shengJiangJi.dongZuoZhiLingErHaoWei&2@machine.shengJiangJi.wcsRenWuMaErHaoWei&2@machine.shengJiangJi.ZhuangTaiSanHaoWei&1@machine.shengJiangJi.daiMingSanHaoWei&1@machine.shengJiangJi.kongXianSanHaoWei&1@machine.shengJiangJi.zaiWuSanHaoWei&1@machine.shengJiangJi.renWuMaSanHaoWei&2@machine.shengJiangJi.dongZuoZhiLingSanHaoWei&2@machine.shengJiangJi.wcsRenWuMaSanHaoWei&2@machine.shengJiangJi.ZhuangTaiSiHaoWei&1@machine.shengJiangJi.daiMingSiHaoWei&1@machine.shengJiangJi.kongXianSiHaoWei&1@machine.shengJiangJi.zaiWuSiHaoWei&1@machine.shengJiangJi.renWuMaSiHaoWei&2@machine.shengJiangJi.dongZuoZhiLingSiHaoWei&2@machine.shengJiangJi.wcsRenWuMaSiHaoWei&2";
            String[] aa = new String[opcItems.split("@").length];
            for (int i = 0; i < aa.length; i++) {
                aa[i] = opcItems.split("@")[i];
            }
            List<OpcItems> opcItemsList = new ArrayList<OpcItems>();
            for (String string : aa) {
                OpcItems opcItems1 = new OpcItems();
                opcItems1.setItem(string.split("&")[0]);
                String ssssss = string.split("&")[1];
                opcItems1.setDatatype(Integer.valueOf(ssssss).byteValue());
                opcItemsList.add(opcItems1);
            }
//            ReadThread readThread = new ReadThread(access, opcItemsList);
//            new Thread(readThread).start();
//            access.bind();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (JIException e) {
            e.printStackTrace();
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (DuplicateGroupException e) {
            e.printStackTrace();
        }
    }

}

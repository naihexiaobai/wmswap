package com.kepware.opc;

import com.ren.util.LoggerUtil;
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

    private LoggerUtil loggerUtil = new LoggerUtil("OpcServer");
    private static final String HOST = "localhost";
    private static final String DOMAIN = "localhost";
    private static final String USER = "admin";
    private static final String PASSWORD = "ren130303.";
    private static final String PROGID = "Kepware.KEPServerEX 6.3";
    private static final String CLSID = "7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729";
    private static final int POOLSIZE = 5;
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
        } else {
            loggerUtil.getLogger().info("opcServer - serverList已创建");
        }
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
            loggerUtil.getLogger().info("opcServer - server创建连接失败-" + e.getMessage());
        } catch (JIException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - server创建连接失败-" + e.getMessage());
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - server创建连接失败-" + e.getMessage());
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
                loggerUtil.getLogger().info("opcServer - server创建连接成功");
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
            boolean result = false;
            for (OpcServerModel opcServerModel : OpcServer.getInstance().opcServerModels) {
                if (!result && opcServerModel.getStatus() == 0) {
                    server = opcServerModel.getServer();
                    opcServerModel.getServer().setDefaultActive(true);
                    opcServerModel.setStatus(1);
                    opcServerModel.setKey(KEYREAD);
                    AccessBase access = new Async20Access(server, 1000, true);
                    for (OpcItems opcItems : opcItemsList) {
                        if (opcItems.getDatatype() == 1) {
                            readItemBoolean(access, opcItems.getItem());
                        } else if (opcItems.getDatatype() == 2) {
                            readItemWord(access, opcItems.getItem());
                        }
                    }
                    access.bind();
                    opcServerModel.setAccessBase(access);
                    result = true;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (NotConnectedException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (JIException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (DuplicateGroupException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - 读取数据异常-" + e.getMessage());
        } catch (AddFailedException e) {
            e.printStackTrace();
            loggerUtil.getLogger().info("opcServer - 读取数据异常-" + e.getMessage());
        }
    }

    private void readItemBoolean(AccessBase access, final String itemId) throws JIException, AddFailedException {
        access.addItem(itemId, new DataCallback() {
            public void changed(Item item, ItemState itemState) {
                String value = itemState.getValue() + "";
                value = getCleanValue(value);
                monitoringMap.put(itemId, value);
            }
        });
    }

    private void readItemWord(AccessBase access, final String itemId) throws JIException, AddFailedException {
        access.addItem(itemId, new DataCallback() {
            public void changed(Item item, ItemState itemState) {
                String value = "";
                try {
                    value = itemState.getValue().getObjectAsUnsigned().getValue().toString();
                } catch (JIException e) {
                    e.printStackTrace();
                    loggerUtil.getLogger().info("opcServer - word数据转换异常-" + e.getMessage());
                }
                monitoringMap.put(itemId, value);
            }
        });
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    private String getCleanValue(String result) {
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
                        loggerUtil.getLogger().warn("添加group出现异常" + e.getMessage());
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                        loggerUtil.getLogger().warn("添加group出现异常" + e.getMessage());
                    } catch (JIException e) {
                        e.printStackTrace();
                        loggerUtil.getLogger().warn("添加group出现异常" + e.getMessage());
                    } catch (DuplicateGroupException e) {
                        e.printStackTrace();
                        loggerUtil.getLogger().warn("添加group出现异常" + e.getMessage());
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
                            loggerUtil.getLogger().info("成功---" + ziCheMsgModel.toString());
                        } else {
                            writeResult = false;
                            loggerUtil.getLogger().warn("没有找到group");
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
                            loggerUtil.getLogger().info("成功---" + muCheMsgModel.toString());
                        } else {
                            writeResult = false;
                            loggerUtil.getLogger().warn("没有找到group");
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
                            loggerUtil.getLogger().info("成功---" + duiDuoJiMsgModel.toString());
                        } else {
                            writeResult = false;
                            loggerUtil.getLogger().warn("没有找到group");
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
                            loggerUtil.getLogger().info("成功---" + shengJiangJiMsgModel.toString());
                        } else {
                            writeResult = false;
                            loggerUtil.getLogger().warn("没有找到group");
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    loggerUtil.getLogger().error("opc写入异常" + e.getMessage());
                    writeResult = false;
                } catch (JIException e) {
                    e.printStackTrace();
                    loggerUtil.getLogger().error("opc写入异常" + e.getMessage());
                    writeResult = false;
                } catch (UnknownGroupException e) {
                    e.printStackTrace();
                    loggerUtil.getLogger().error("opc写入异常" + e.getMessage());
                    writeResult = false;
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                    loggerUtil.getLogger().error("opc写入异常" + e.getMessage());
                    writeResult = false;
                } catch (AddFailedException e) {
                    e.printStackTrace();
                    loggerUtil.getLogger().error("opc写入异常" + e.getMessage());
                    writeResult = false;
                }
                break;
            }
        }
        return writeResult;
    }

}

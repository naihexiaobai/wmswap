package com.kepware.opc.server;

import com.www.util.LoggerUtil;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * opcServer
 *
 * @auther CalmLake
 * @create 2018/3/19  10:51
 */
public class OpcServerIns {

    private static final String HOST = "localhost";
    private static final String DOMAIN = "localhost";
    private static final String KEP = "Kepware.KEPServerEX 6.3";
    private static final String KEP_ID = "7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729";
    private static final String USER = "admin";
    private static final String PASSWORD = "ren130303.";

//    private static final String HOST = "localhost";
//    private static final String DOMAIN = "localhost";
//    private static final String KEP = "Kepware.KEPServerEX 6.3";
//    private static final String KEP_ID = "7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729";
//    private static final String USER = "Administrators";
//    private static final String PASSWORD = "3EDC5tgb";


    //    private static final String KEP_ID = "13486D44-4821-11D2-A494-3CB306C10000";
    private Server server = null;

    public static OpcServerIns instance() {
        return instance;
    }

    private static OpcServerIns instance;

    static {
        instance = new OpcServerIns();
    }

    private OpcServerIns() {
        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(HOST);
        ci.setUser(USER);
        ci.setPassword(PASSWORD);
        ci.setDomain(DOMAIN);
        ci.setProgId(KEP);
        ci.setClsid(KEP_ID);
        server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        AutoReconnectController controller = new AutoReconnectController(server);
        controller.connect();
        server.setDefaultActive(true);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server createServer() {
        Server server1 = null;
        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(HOST);
        ci.setUser(USER);
        ci.setPassword(PASSWORD);
        ci.setDomain(DOMAIN);
        ci.setProgId(KEP);
        ci.setClsid(KEP_ID);
        server1 = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        AutoReconnectController controller = new AutoReconnectController(server1);
        controller.connect();
        server1.setDefaultActive(true);
        return server1;
    }

}

package com.wap.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.communication.socket.client.CreateClientConnect;
import com.communication.socket.connect.CreateConnectAbs;
import com.communication.socket.data.model.SocketInfo;
import com.communication.socket.data.model.SocketInfoListsSingleton;
import com.communication.socket.data.model.SocketInitLinkInfo;
import com.communication.socket.thread.IPCThread;
import com.communication.socket.thread.ServerThread;
import com.communication.socket.thread.TestIPCThread;
import com.ren.util.CalmLakeStringUtil;
import com.ren.util.LoggerUtil;
import com.wap.control.dao.daoImpl.*;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * socket 控制层
 *
 * @auther CalmLake
 * @create 2017/11/3  14:16
 */
@Controller
@RequestMapping("socketCc")
public class SocketCc   extends ControlCc  {

    private LoggerUtil loggerUtil = new LoggerUtil("SocketCc");

    @RequestMapping("createSocket")
    @ResponseBody
    public JSONObject createSocket(HttpServletRequest request) {
        JSONObject jsonObject = null;
        SocketInfo socketInfo = new SocketInfo();
        SocketInitLinkInfo socketInitLinkInfo = new SocketInitLinkInfo();
        try {
            String string = request.getParameter("submitData");
            loggerUtil.getLogger().info(string);
            jsonObject = new JSONObject();
            jsonObject.put("time", new Date());
            if (StringUtil.isNullOrEmpty(string)) {
                jsonObject.put("result", false);
                jsonObject.put("msg", "表单数据为空！");
                return jsonObject;
            }
            //TODO    数据解析后续需处理改为json
            String socketInfo_idNum = string.split("&")[0].split("=")[1];
            String socketInfo_id = string.split("&")[1].split("=")[1];
            String socketInfo_name = string.split("&")[2].split("=")[1];
            String socketInfo_description = string.split("&")[3].split("=")[1];
            String socketInfo_ip = string.split("&")[4].split("=")[1];
            String socketInfo_port = string.split("&")[5].split("=")[1];
            String socketInfo_type = string.split("&")[6].split("=")[1];
            String socketInfo_status = string.split("&")[7].split("=")[1];

            socketInfo.setId(socketInfo_id);
            socketInfo.setStatus(Integer.valueOf(socketInfo_status));
            socketInfo.setName(socketInfo_name);
            socketInfo.setIdNum(Integer.valueOf(socketInfo_idNum));
            socketInfo.setType(Integer.valueOf(socketInfo_type));
            socketInfo.setDescription(socketInfo_description);

            socketInitLinkInfo.setPort(Integer.valueOf(socketInfo_port));
            socketInitLinkInfo.setUrl(socketInfo_ip);
            if (CalmLakeStringUtil.stringToInt(socketInfo_type) == 1) {
                //TODO  创建服务器连接
                ServerThread server = new ServerThread(socketInfo, socketInitLinkInfo);
                new Thread(server).start();
                jsonObject.put("result", true);
                jsonObject.put("msg", "成功！");
            } else if (CalmLakeStringUtil.stringToInt(socketInfo_type) == 0) {
                CreateConnectAbs createConnectClient = new CreateClientConnect(socketInitLinkInfo.getUrl(), socketInitLinkInfo.getPort());
                Socket socket = createConnectClient.getSocket();
                //TODO    客户端线程起始处
                IPCThread ipcThread = new TestIPCThread(socketInfo);
                new Thread(ipcThread).start();
                socketInfo.setStatus(1);
                createConnectClient.setSocketInfo(socket, socketInfo);
                jsonObject.put("result", true);
                jsonObject.put("msg", "成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerUtil.getLogger().error("创建socket连接出错：" + e.getMessage());
            jsonObject.put("result", false);
            jsonObject.put("msg", "服务器繁忙！");
        }
        return jsonObject;
    }

    /**
     * 初始化新增socket页面数据
     *
     * @return
     */
    @RequestMapping("initData")
    @ResponseBody
    public JSONObject initData() {
        JSONObject object = new JSONObject();
        int num = SocketInfoListsSingleton.getInstance().getSocketInfoList().size();
        object.put("idNum", ++num);
        return object;
    }

    @RequestMapping("getSocketInfoJSON")
    @ResponseBody
    public JSONArray getSocketInfoJSON(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
        for (SocketInfo socketInfo : socketInfoList) {
            JSONObject object = new JSONObject();
            object.put("idNumSocket", socketInfo.getIdNum());
            object.put("idSocket", socketInfo.getId());
            object.put("DescSocket", socketInfo.getDescription());
            object.put("ipSocket", socketInfo.getSocket().getInetAddress().getHostAddress());
            object.put("ipSocketLocal", socketInfo.getSocket().getLocalAddress());
            object.put("nameSocket", socketInfo.getName());
            object.put("portSocket", socketInfo.getSocket().getPort());
            object.put("portSocketLocal", socketInfo.getSocket().getLocalPort());
            object.put("typeSocket", socketInfo.getType());
            object.put("statusSocket", socketInfo.getStatus());
            jsonArray.add(object);
        }
        return jsonArray;
    }

    /**
     * 发送消息
     *
     * @param request "msgIdNum":"1","msgTime":"20171107171306","msgOrderNum":"1","msgCommandType":"1","msgCommandData":"1","msgCountNum":"1"
     */
    @RequestMapping("sendMsg")
    @ResponseBody
    public JSONObject sendMsg(HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        String requestString = request.getParameter("submitData");
        if (CalmLakeStringUtil.stringIsNull(requestString)) {
            jsonObject.put("result", false);
            return jsonObject;
        }
        JSONObject jsonObjectReqData = JSONObject.parseObject(requestString);
        int msgIdNum = jsonObjectReqData.getIntValue("msgIdNum");
        String msgTime = jsonObjectReqData.getString("msgTime");
        short msgOrderNum = jsonObjectReqData.getShort("msgOrderNum");
        short msgCommandType = jsonObjectReqData.getShort("msgCommandType");
        short msgCommandData = jsonObjectReqData.getShort("msgCommandData");
        short msgCountNum = jsonObjectReqData.getShort("msgCountNum");

        List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
        for (SocketInfo socketInfo : socketInfoList) {
            if (msgIdNum == socketInfo.getIdNum()) {
                Socket socket = socketInfo.getSocket();
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeShort(22);
                outputStream.write(msgTime.getBytes());
                outputStream.writeShort(msgCountNum);
                outputStream.writeShort(msgOrderNum);
                outputStream.writeShort(msgCommandType);
                outputStream.writeShort(msgCommandData);
                outputStream.flush();
                jsonObject.put("result", true);
                loggerUtil.getLogger().info(socketInfo.getName() + "发送消息:时间-->" + msgTime + "任务号-->" + msgOrderNum + "指令类型-->" + msgCommandType + "指令内容-->" + msgCommandData + "" + msgCountNum);
                return jsonObject;
            }
        }
        return jsonObject;
    }

    /**
     * 关闭连接
     *
     * @param request
     * @return
     */
    @RequestMapping("closeSocket")
    @ResponseBody
    public JSONObject closeSocket(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            String idNum = request.getParameter("idNum");
            if (idNum == null || idNum.equals("")) {
                jsonObject.put("result", false);
            } else {
                int num = CalmLakeStringUtil.stringToInt(idNum);
                List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
                for (SocketInfo socketInfo : socketInfoList) {
                    if (num == socketInfo.getIdNum()) {
                        closeSocket(socketInfo);
                        socketInfoList.remove(socketInfo);
                        jsonObject.put("result", true);
                        loggerUtil.getLogger().info("socket正常关闭,连接序号-->" + num);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("result", false);
            loggerUtil.getLogger().info("socket正常关闭异常" + e.getMessage());
        }
        return jsonObject;
    }

    public void closeSocket(SocketInfo socketInfo) throws IOException {
        socketInfo.getSocket().close();
    }

    /**
     * 获取设备信息
     *
     * @param request
     * @return
     */
    @RequestMapping("getAutoMachineData")
    @ResponseBody
    public JSONArray getAutoMachineData(HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        List<SocketInfo> socketInfoList = SocketInfoListsSingleton.getInstance().getSocketInfoList();
        for (SocketInfo socketInfo : socketInfoList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idNum", socketInfo.getIdNum());
            jsonObject.put("name", socketInfo.getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static void main(String[] args) {
        System.out.println("20171107171306".getBytes().length);
    }
}

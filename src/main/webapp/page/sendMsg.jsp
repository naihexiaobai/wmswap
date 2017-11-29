<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/6
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>Title</title>
    <!-- 引入 Bootstrap -->
    <meta name="viewport" content="width=device-width, maximum-scale=1.0,initial-scale=1.0,user-scalable=no">
    <link href="<%=basePath %>bootStrap/css/bootstrap.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-select.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrapValidator.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="<%=basePath %>css/index.css" rel="stylesheet">
</head>
<body>
<div id="main_index" class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>Home</li>
            <li>Socket</li>
            <li class="active">发送消息</li>
        </ol>
    </div>
    <div class="row">
        <form id="from_sendMsg" class="form-horizontal" role="form">
            <div class="form-group">
                <label for="msgIdNum" class="col-sm-1 control-label">设备名称:</label>
                <div class="col-sm-2">
                    <select id="msgIdNum" name="msgIdNum" placeholder="请选择设备"
                            class="selectpicker show-tick input-sm form-control"
                            data-live-search="true">
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="msgTime" class="col-sm-1 control-label">发送时间:</label>
                <div class="col-sm-2">
                    <input id="msgTime" name="msgTime" class="form-control input-sm form_datetime" type="text"
                           placeholder="请输入发送时间:yyyyMMddHHmmss"/><span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-th"></i></span>
                </div>
            </div>
            <div class="form-group">
                <label for="msgOrderNum" class="col-sm-1 control-label">任务序号:</label>
                <div class="col-sm-2">
                    <input id="msgOrderNum" name="msgOrderNum" class="form-control input-sm " type="text"
                           placeholder="请输入任务序号：0-9999"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgCommandType" class="col-sm-1 control-label">指令类型:</label>
                <div class="col-sm-2">
                    <select id="msgCommandType" name="msgCommandType" placeholder="请选择指令类型"
                            class="selectpicker show-tick input-sm form-control"
                            data-live-search="true">
                        <option selected="selected" value="0">请选择指令类型</option>
                        <option value="1">握手</option>
                        <option value="2">心跳</option>
                        <option value="3">动作</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="msgCommandData" class="col-sm-1 control-label">指令内容:</label>
                <div class="col-sm-2">
                    <select id="msgCommandData" name="msgCommandData" placeholder="请输入指令内容"
                            class="selectpicker show-tick input-sm form-control"
                            data-live-search="true">
                        <option selected="selected" value="0">请选择指令内容</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="msgCountNum" class="col-sm-1 control-label">发送次数:</label>
                <div class="col-sm-2">
                    <input id="msgCountNum" name="msgCountNum" class="form-control input-sm " type="text"
                           placeholder="这是第几次发送"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-1"></div>
                <div class="col-sm-2">
                    <button type="button" class="btn btn-primary" id="btnSendMsg"
                            data-loading-text="发送中" data-complete-text="Loading finished"
                            onclick="sendMsg('btnSendMsg')"><i class="glyphicon glyphicon-send">&nbsp;</i>发送
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="<%=basePath %>bootStrap/js/jquery-3.2.1.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="<%=basePath %>bootStrap/js/bootstrap.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-select.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrapValidator.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="<%=basePath %>bootStrap/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>

<script src="<%=basePath %>page/js/sendMsg.js"></script>
<script src="<%=basePath %>page/js/publicMethod.js"></script>
</body>
</html>

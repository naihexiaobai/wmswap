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
            <li class="active">WCS发送消息</li>
        </ol>
    </div>
    <div class="row">
        <div class="container-fluid">

        </div>
    </div>
    <div class="row">
        <form id="from_sendMsgWCS" class="form-horizontal" role="form">
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
                <label for="msgNO" class="col-sm-1 control-label">消息序号:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgNO" name="msgNO" type="text" placeholder="请输入消息序号(4)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgCommand" class="col-sm-1 control-label">命令:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgCommand" name="msgCommand" type="text"
                           placeholder="请输入命令(2)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgSendScend" class="col-sm-1 control-label">重发标识:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgSendScend" name="msgSendScend" type="text"
                           placeholder="重发标识(1)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgSendTime" class="col-sm-1 control-label">送信時間:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgSendTime" name="msgSendTime" type="text"
                           placeholder="请输入送信時間HHmmss(6)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgMCkey" class="col-sm-1 control-label">MCkey:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgMCkey" name="msgMCkey" type="text"
                           placeholder="请输入MCkey(4)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgMachineName" class="col-sm-1 control-label">机器号:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgMachineName" name="msgMachineName" type="text"
                           placeholder="请输入机器号(4)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgCycleCommand" class="col-sm-1 control-label">Cycle命令:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgCycleCommand" name="msgCycleCommand" type="text"
                           placeholder="请输入Cycle命令(2)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgJobType" class="col-sm-1 control-label">作业区分:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgJobType" name="msgJobType" type="text"
                           placeholder="请输入作业区分(2)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgHeight" class="col-sm-1 control-label">货形(高度):</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgHeight" name="msgHeight" type="text"
                           placeholder="货形(高度)(1)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgWidth" class="col-sm-1 control-label">货形(宽度):</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgWidth" name="msgWidth" type="text"
                           placeholder="请输入货形(宽度)(1)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgZ" class="col-sm-1 control-label">货架排:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgZ" name="msgZ" type="text" placeholder="请输入货架排(2)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgX" class="col-sm-1 control-label">货架列:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgX" name="msgX" type="text" placeholder="请输入货架列(2)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgY" class="col-sm-1 control-label">货架层:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgY" name="msgY" type="text" placeholder="请输入货架层(2)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgStation" class="col-sm-1 control-label">站台:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgStation" name="msgStation" type="text"
                           placeholder="请输入站台(4)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgWharf" class="col-sm-1 control-label">码头:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgWharf" name="msgWharf" type="text" placeholder="码头(4)"/>
                </div>
            </div>
            <div class="form-group">
                <label for="msgBCC" class="col-sm-1 control-label">BCC:</label>
                <div class="col-sm-2">
                    <input class="form-control input-md " id="msgBCC" name="msgBCC" type="text" placeholder="请输入BCC(2)"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-1"></div>
                <div class="col-sm-2">
                    <button type="button" class="btn btn-primary" id="btnSendMsgWCS"
                            data-loading-text="发送中" data-complete-text="Loading finished"
                            onclick="sendMsgWCS('btnSendMsgWCS')"><i class="glyphicon glyphicon-send">&nbsp;</i>发送
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


<script src="<%=basePath %>page/js/publicMethod.js"></script>
<script src="<%=basePath %>page/js/sendMsgWCS.js"></script>
</body>
</html>

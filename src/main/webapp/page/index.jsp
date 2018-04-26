<%--
  Created by IntelliJ IDEA.
  User: CalmLake
  Date: 2017/11/1
  Time: 15:03
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
    <title>welcome</title>
    <!-- 引入 Bootstrap -->
    <meta name="viewport" content="width=device-width, maximum-scale=1.0,initial-scale=1.0,user-scalable=no">
    <link href="<%=basePath %>bootStrap/css/bootstrap.css" rel="stylesheet">
    <link href="<%=basePath %>css/index.css" rel="stylesheet">
</head>
<body>
<div id="div-head" class="container-fluid">
    <div id="div-head-nav" class="row">
        <nav class="navbar navbar-default navbar-static-top navbar-inverse" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <h2><p>WMS自动化系统</p></h2>
                </div>
            </div>
        </nav>
    </div>
</div>
<div id="container-fluid-body" class="container-fluid">
    <div id="sidebar" class="col-md-2">
        <%--导航栏--%>
        <nav class="navbar" role="navigation">
            <div class="collapse navbar-collapse ">
                <ul class="nav nav-pills nav-stacked panel-group" id="example-navbar-collapse">
                    <li class="panel">
                        <a data-toggle="collapse" data-parent="#example-navbar-collapse"
                           href="#collapseTwo"><i class="glyphicon glyphicon-cog">&nbsp;</i> 春夜洛城闻笛</a>
                        <ul id="collapseTwo" class="panel-collapse collapse nav nav-pills nav-stacked">
                            <li><a>谁家玉笛暗飞声</a></li>
                            <li><a>飞入寻常百姓家</a></li>
                            <li><a>此夜曲中闻折柳</a></li>
                            <li><a>何人不起故乡情</a></li>
                        </ul>
                    </li>
                    <li class="panel">
                        <a data-toggle="collapse" data-parent="#example-navbar-collapse"
                           href="#collapseTwo2"><i class="glyphicon glyphicon-th-large">&nbsp;</i> Socket</a>
                        <ul id="collapseTwo2" class="panel-collapse collapse nav nav-pills nav-stacked">
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwo21" data-parent="#collapseTwo2">socket创建</a>
                                <ul id="collapseTwo21" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/createSocketClient.jsp')">创建SocketClient</a>
                                    </li>
                                    <li><a onclick="goToPage('<%=basePath %>page/createSocketServer.jsp')">创建SocketServer</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwo22" data-parent="#collapseTwo2">Socket属性</a>
                                <ul id="collapseTwo22" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/socketInfo.jsp')">Socket信息</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwo24" data-parent="#collapseTwo2">Socket消息</a>
                                <ul id="collapseTwo24" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/sendMsg.jsp')">发送消息</a>
                                    </li>
                                    <li><a onclick="goToPage('<%=basePath %>page/sendMsgWCS.jsp')">WCS消息</a>
                                    </li>
                                    <li><a>历史消息</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="panel">
                        <a data-toggle="collapse" data-parent="#example-navbar-collapse"
                           href="#collapseTwo211"><i class="glyphicon glyphicon-eye-open">&nbsp;</i> 设备管理</a>
                        <ul id="collapseTwo211" class="panel-collapse collapse nav nav-pills nav-stacked">
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwo121" data-parent="#collapseTwo211">设备状态</a>
                                <ul id="collapseTwo121" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/monitor.jsp')">实时数据</a>
                                    </li>
                                    <li><a onclick="goToPage('<%=basePath %>page/wcsMessage.jsp')">动作查询</a>
                                    </li>
                                    <li><a onclick="goToPage('<%=basePath %>page/block.jsp')">设备查看</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwo33"
                                   data-parent="#collapseTwo211">设备控制</a>
                                <ul id="collapseTwo33" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/machineControl.jsp')">单步操作</a>
                                    </li>

                                </ul>
                            </li>
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwo34"
                                   data-parent="#collapseTwo211">Socket消息</a>
                                <ul id="collapseTwo34" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/sendMsg.jsp')">发送消息</a>
                                    </li>
                                    <li><a>历史消息</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="panel">
                        <a data-toggle="collapse" data-parent="#example-navbar-collapse"
                           href="#collapseTwoCW"><i class="glyphicon glyphicon-folder-close">&nbsp;</i> 仓储管理</a>
                        <ul id="collapseTwoCW" class="panel-collapse collapse nav nav-pills nav-stacked">
                            <%--<li class="panel">--%>
                            <%--<a data-toggle="collapse" href="#collapseTwoHW" data-parent="#collapseTwoCW">货位管理</a>--%>
                            <%--<ul id="collapseTwoHW" class="panel-collapse collapse nav nav-pills nav-stacked">--%>
                            <%--<li><a onclick="goToPage('<%=basePath %>page/monitor.jsp')">实时数据</a>--%>
                            <%--</li>--%>
                            <%--</ul>--%>
                            <%--</li>--%>
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwoHWW"
                                   data-parent="#collapseTwoCW">货物管理</a>
                                <ul id="collapseTwoHWW" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/cargo.jsp')">货物存取</a>
                                    </li>
                                    <li><a onclick="goToPage('<%=basePath %>page/cargoDetail.jsp')">货物库存</a>
                                    </li>
                                    <li><a onclick="goToPage('<%=basePath %>page/cargoData.jsp')">货物数据</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="panel">
                                <a data-toggle="collapse" href="#collapseTwoHWWW"
                                   data-parent="#collapseTwoCW">订单管理</a>
                                <ul id="collapseTwoHWWW" class="panel-collapse collapse nav nav-pills nav-stacked">
                                    <li><a onclick="goToPage('<%=basePath %>page/order.jsp')">订单查询</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
    <div class="col-md-10">
        <iframe src="<%=basePath %>page/welcome.jsp" id="div-iframe" scrolling="no"
                onload="changeFrameHeight()"
                frameborder="0"></iframe>
    </div>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="model_success" tabindex="-1" role="dialog" aria-labelledby="model_success_label"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body alert alert-success">
                成功！很好地完成了提交!
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="model_fail" tabindex="-1" role="dialog" aria-labelledby="model_fail_label"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body alert alert alert-danger">
                失败！请仔细检查数据!
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="model_info" tabindex="-1" role="dialog" aria-labelledby="model_info_label"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body alert alert alert-info">
                This is a message!
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="<%=basePath %>bootStrap/js/jquery-3.2.1.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="<%=basePath %>bootStrap/js/bootstrap.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-select.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrapValidator.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="<%=basePath %>page/js/index.js"></script>
<script src="<%=basePath %>page/js/publicMethod.js"></script>
</body>
</html>

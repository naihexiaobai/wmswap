<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/2
  Time: 16:14
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
            <li>设备管理</li>
            <li>设备监控</li>
            <li class="active">实时数据</li>
        </ol>
    </div>
    <div class="row form-group">
        <div class="container">
            <div class="col-md-2">

            </div>
            <div class="col-md-8">
                <%--<button type="button" id="startMonitor_btn" class="btn btn-primary" onclick="monitoring(1)"--%>
                <%--data-loading-text="监控中">开始监控--%>
                <%--</button> &nbsp;--%>
                <%--<button type="button"  id="stopMonitor_btn" class="btn btn-primary" data-loading-text="关闭监控" onclick="monitoring(0)">关闭监控</button> &nbsp;--%>
                <button type="button" class="btn btn-primary" onclick="showMonitorData(1)">子车</button> &nbsp;
                <button type="button" class="btn btn-primary" onclick="showMonitorData(2)">母车</button> &nbsp;
                <button type="button" class="btn btn-primary" onclick="showMonitorData(3)">堆垛机</button> &nbsp;
                <button type="button" class="btn btn-primary" onclick="showMonitorData(4)">升降机</button> &nbsp;
                <button type="button" class="btn btn-primary" onclick="showMonitorData(5)">输送线</button>
            </div>
            <div class="col-md-2"></div>
        </div>
    </div>
    <div class="row">
        <table id="table_monitor_info" class="table table-striped  table-bordered">
        </table>
    </div>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="model_success" tabindex="-1" role="dialog" aria-labelledby="model_success_label"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body alert alert-success">
                成功！
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="model_fail" tabindex="-1" role="dialog" aria-labelledby="model_fail_label"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body alert alert alert-danger">
                失败！
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
<script src="<%=basePath %>bootStrap/js/bootstrap-datetimepicker.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-table.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-table-zh-CN.js"></script>

<script src="<%=basePath %>page/js/publicMethod.js"></script>
<script src="<%=basePath %>page/js/monitor.js"></script>
</body>
</html>

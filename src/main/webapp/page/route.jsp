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
    <title>路径详情</title>
    <!-- 引入 Bootstrap -->
    <meta name="viewport" content="width=device-width, maximum-scale=1.0,initial-scale=1.0,user-scalable=no">
    <link href="<%=basePath %>bootStrap/css/bootstrap.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-select.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrapValidator.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-table.css" rel="stylesheet">
    <link href="<%=basePath %>css/index.css" rel="stylesheet">
</head>
<body>
<div id="main_index" class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>Home</li>
            <li>系统管理</li>
            <li>路径数据</li>
            <li class="active">路径详情</li>
        </ol>
    </div>
    <div class="row">
        <div id="toolbar" class="btn-group">
            <button id="btn_add" type="button" class="btn btn-primary" onclick="addRouteShow();">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
            </button>
        </div>
        <table id="table_route_info" class="table table-striped  table-bordered">
        </table>
    </div>
</div>

<div class="modal fade" id="divRouteAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <H3>新增路径</H3>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <form id="addRouteData" class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="startBlockNo" class="col-sm-2 control-label">起始设备:</label>
                            <div class="col-lg-3">
                                <input id="startBlockNo" name="startBlockNo"
                                       class="selectpicker show-tick input-sm form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="endBlockNo" class="col-sm-2 control-label">截止设备:</label>
                            <div class="col-lg-3">
                                <input id="endBlockNo" name="endBlockNo"
                                       class="selectpicker show-tick input-sm form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="routeType" class="col-sm-2 control-label">路径类型:</label>
                            <div class="col-lg-3">
                                <select id="routeType" name="routeType"
                                        class="selectpicker show-tick input-sm form-control"
                                        data-live-search="true">
                                    <option selected="selected" value="2">出库</option>
                                    <option value="1">入库</option>
                                    <option value="3">移库</option>
                                    <option value="4">归港</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-1">
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" id="btnSendMsg"
                                        data-loading-text="Loading..." onclick="addRoute()">
                                    <i class="glyphicon glyphicon-send">&nbsp;</i>提交
                                </button>
                            </div>
                            <div class="col-lg-10">
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <%--<div class="modal-footer">--%>
            <%--<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>--%>
            <%--</div>--%>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
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
<script src="<%=basePath %>page/js/route.js"></script>
</body>
</html>

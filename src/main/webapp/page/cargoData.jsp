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
    <title>货物数据</title>
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
            <li>仓储管理</li>
            <li>货物管理</li>
            <li class="active">货物数据</li>
        </ol>
    </div>
    <div class="row">
        <div class="container-fluid">
            <div class="col-md-3">
                <button onclick="showDivModel()" data-loading-text="操作中" id="outStorage" class="btn btn-primary">
                    <i class="glyphicon glyphicon-plus">&nbsp;</i>新增货物
                </button>
            </div>
        </div>
    </div>
    <div class="row">
        &nbsp;
    </div>
    <div class="row">
        <div class="container-fluid">
            <div class="col-md-6">
                <form id="cargoAdd" style="display: none" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="cargoPalletNO" class="col-sm-2 control-label">托盘编号:</label>
                        <div class="col-lg-3">
                            <input id="cargoPalletNO" name="cargoPalletNO" value=""
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cargoName" class="col-sm-2 control-label">货物名称:</label>
                        <div class="col-lg-3">
                            <input id="cargoName" name="cargoName" value="薯片"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoNameId" class="col-sm-2 control-label">货物名称编码:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoNameId" name="cargoNameId"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <label for="cargoNumber" class="col-sm-2 control-label">数量:</label>
                        <div class="col-lg-3">
                            <input id="cargoNumber" name="cargoNumber" value="40"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cargoQuality" class="col-sm-2 control-label">品质:</label>
                        <div class="col-lg-3">
                            <input id="cargoQuality" name="cargoQuality" value="优"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cargoBatchNo" class="col-sm-2 control-label">批次号:</label>
                        <div class="col-lg-3">
                            <input id="cargoBatchNo" name="cargoBatchNo" value="20180420"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cargoSpecifiction" class="col-sm-2 control-label">规格:</label>
                        <div class="col-lg-3">
                            <input id="cargoSpecifiction" name="cargoSpecifiction" value="箱"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cargoProductionDate" class="col-sm-2 control-label">生产日期:</label>
                        <div class="col-lg-3">
                            <input id="cargoProductionDate" name="cargoProductionDate"
                                   class="form-control input-sm" type="date"
                                   placeholder="请输入发送时间:yyyyMMddHHmmss"/>
                            <%--<span class="add-on"><i--%>
                            <%--class="icon-remove"></i></span>--%>
                            <%--<span class="add-on"><i class="icon-th"></i></span>--%>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cargoShelfLife" class="col-sm-2 control-label">保质期:</label>
                        <div class="col-lg-3">
                            <input id="cargoShelfLife" name="cargoShelfLife" value="180"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-1">
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" id="btnSendMsg"
                                    data-loading-text="Loading..." onclick="addCargoDate()">
                                <i class="glyphicon glyphicon-send">&nbsp;</i>新增
                            </button>
                        </div>
                        <div class="col-lg-10">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
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
<script src="<%=basePath %>bootStrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="<%=basePath %>bootStrap/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-table.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-table-zh-CN.js"></script>

<script src="<%=basePath %>page/js/publicMethod.js"></script>
<script src="<%=basePath %>page/js/cargoData.js"></script>
</body>
</html>

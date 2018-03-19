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
            <li>设备控制</li>
            <li class="active">单步操作</li>
        </ol>
    </div>

    <div class="row">
        <div class="container-fluid">
            <div class="col-md-9">
                <form id="machine_msg" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="machineType" class="col-sm-1 control-label">操作类型:</label>
                        <div class="col-lg-3">
                            <select id="machineType" name="machineType"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="0">手动</option>
                                <option value="1">自动</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="machineId" class="col-sm-1 control-label">设备序号:</label>
                        <div class="col-lg-3">
                            <select id="machineId" name="machineId"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="4">子车A1</option>
                                <option value="5">子车A2</option>
                                <option value="6">子车A3</option>
                                <option value="7">母车A1</option>
                                <option value="8">母车A2</option>
                                <option value="9">母车A3</option>
                                <option value="10">堆垛机A1</option>
                                <option value="11">升降机A1</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="targetMachineId" class="col-sm-1 control-label">目标设备:</label>
                        <div class="col-lg-3">
                            <select id="targetMachineId" name="targetMachineId"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="4">子车A1</option>
                                <option value="5">子车A2</option>
                                <option value="6">子车A3</option>
                                <option value="7">母车A1</option>
                                <option value="8">母车A2</option>
                                <option value="9">母车A3</option>
                                <option value="10">堆垛机A1</option>
                                <option value="11">升降机A1</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="machineCeng" class="col-sm-1 control-label">目标层:</label>
                        <div class="col-lg-3">
                            <select id="machineCeng" name="machineCeng"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="machineLie" class="col-sm-1 control-label">目标列:</label>
                        <div class="col-lg-3">
                            <select id="machineLie" name="machineLie"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="machinePai" class="col-sm-1 control-label">目标排:</label>
                        <div class="col-lg-3">
                            <select id="machinePai" name="machinePai"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="machineCommand" class="col-sm-1 control-label">动作指令:</label>
                        <div class="col-lg-3">
                            <select id="machineCommand" name="machineCommand"
                                    class="selectpicker show-tick input-sm form-control"
                                    data-live-search="true">
                                <option selected="selected" value="0">0</option>
                                <option value="1">子车A面入货</option>
                                <option value="2">子车A面出货</option>
                                <option value="3">子车B面入货</option>
                                <option value="4">子车B面出货</option>
                                <option value="5">子车A面整仓</option>
                                <option value="6">子车B面整仓</option>
                                <option value="7">子车出母车到输送线B</option>
                                <option value="8">子车上提升机</option>
                                <option value="9">子车出输送机到输送线A</option>
                                <option value="11">子车A面上RGV</option>
                                <option value="12">子车A面下RGV</option>
                                <option value="13">子车B面上RGV</option>
                                <option value="14">子车B面下RGV</option>
                                <option value="15">子车A面盘点</option>
                                <option value="16">子车B面盘点</option>
                                <option value="17">子车A-B面切换</option>
                                <option value="18">子车B-A面切换</option>
                                <option value="19">子车小车充电</option>
                                <option value="21">子车A面载货上RGV</option>
                                <option value="22">子车A面载货下RGV</option>
                                <option value="23">子车B面载货上RGV</option>
                                <option value="24">子车B面载货下RGV</option>
                                <option value="0">RGV/RS链条静止</option>
                                <option value="11">RGV/RS左侧取货</option>
                                <option value="12">RGV/RS右侧取货</option>
                                <option value="21">RGV/RS左侧出货</option>
                                <option value="22">RGV/RS右侧出货</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-1">
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" id="btnSendMsg"
                                    data-loading-text="Loading..." onclick="machineSendMsg()">
                                <i class="glyphicon glyphicon-send">&nbsp;</i>发送
                            </button>
                        </div>
                        <div class="col-lg-1">
                        </div>
                        <div class="col-lg-3">
                        </div>
                        <div class="col-lg-3"></div>
                        <div class="col-lg-3"></div>
                    </div>
                </form>
            </div>
        </div>
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
<script src="<%=basePath %>page/js/machineControl.js"></script>
</body>
</html>

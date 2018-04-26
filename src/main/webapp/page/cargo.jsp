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
    <link href="<%=basePath %>bootStrap/css/bootstrap.css.map" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-select.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-select.css.map" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrapValidator.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-table.css" rel="stylesheet">

    <link href="<%=basePath %>css/index.css" rel="stylesheet">

    <link href="<%=basePath %>css/cargo.css" rel="stylesheet">
</head>
<body>
<div id="main_index" class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>Home</li>
            <li>仓储管理</li>
            <li>货物管理</li>
            <li class="active">货物存取</li>
        </ol>
    </div>
    <div class="row">
        <div class="container-fluid">
            <div class="col-md-3">
                <button onclick="onclickA(1)" data-loading-text="操作中" id="outStorage" class="btn btn-primary">
                    <i class="glyphicon glyphicon-circle-arrow-up">&nbsp;</i>出库
                </button>&nbsp;
                <button onclick="onclickA(2)" data-loading-text="操作中" id="inStorage" class="btn btn-primary">
                    <i class="glyphicon  glyphicon-circle-arrow-down">&nbsp;</i>入库
                </button>
                <button onclick="onclickA(3)" data-loading-text="操作中" id="moveStorage" class="btn btn-primary">
                    <i class="glyphicon  glyphicon-circle-arrow-down">&nbsp;</i>移库
                </button>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container">
            &nbsp;
        </div>
    </div>
    <div class="row">
        <div class="container-fluid">
            <div class="col-md-6">
                <form id="outCargoInfo" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="cargoStorageNoOut" class="col-sm-2 control-label">库位编码:</label>
                        <div class="col-lg-3">
                            <input id="cargoStorageNoOut" name="cargoStorageNo"
                                   class="selectpicker show-tick input-sm form-control" readonly="readonly"/>
                        </div>
                        <button type="button" class="btn btn-primary " data-toggle="modal" data-target="#divStorageMap">
                            <i
                                    class="glyphicon glyphicon-screenshot">&nbsp;</i>库位
                        </button>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-1">
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" id="outCargo"
                                    data-loading-text="Loading..." onclick="CargoBtn(1)">
                                <i class="glyphicon glyphicon-send">&nbsp;</i>GO
                            </button>
                        </div>
                        <div class="col-lg-10">
                        </div>
                    </div>
                </form>
                <form id="inCargoInfo" style="display: none" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="cargoStorageNo" class="col-sm-2 control-label">库位编码:</label>
                        <div class="col-lg-3">
                            <input id="cargoStorageNo" name="cargoStorageNo" readonly="readonly"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                        <button type="button" class="btn btn-primary " data-toggle="modal" data-target="#divStorageMap">
                            <i class="glyphicon glyphicon-screenshot">&nbsp;</i>库位
                        </button>
                    </div>
                    <div class="form-group">
                        <label for="cargoPalletNO" class="col-sm-2 control-label">托盘编号:</label>
                        <div class="col-lg-3">
                            <input id="cargoPalletNO" name="cargoPalletNO"
                                   class="selectpicker show-tick input-sm form-control"/>
                        </div>
                    </div>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoName" class="col-sm-2 control-label">货物名称:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoName" name="cargoName"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoNameId" class="col-sm-2 control-label">货物名称编码:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoNameId" name="cargoNameId"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoNumber" class="col-sm-2 control-label">数量:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoNumber" name="cargoNumber"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoQuality" class="col-sm-2 control-label">品质:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoQuality" name="cargoQuality"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoBatchNo" class="col-sm-2 control-label">批次号:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoBatchNo" name="cargoBatchNo"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoSpecifiction" class="col-sm-2 control-label">规格:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoSpecifiction" name="cargoSpecifiction"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoProductionDate" class="col-sm-2 control-label">生产日期:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoProductionDate" name="cargoProductionDate"--%>
                    <%--class="form-control input-sm form_datetime" type="text"--%>
                    <%--placeholder="请输入发送时间:yyyyMMddHHmmss"/><span class="add-on"><i--%>
                    <%--class="icon-remove"></i></span>--%>
                    <%--<span class="add-on"><i class="icon-th"></i></span>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="cargoShelfLife" class="col-sm-2 control-label">保质期:</label>--%>
                    <%--<div class="col-lg-3">--%>
                    <%--<input id="cargoShelfLife" name="cargoShelfLife"--%>
                    <%--class="selectpicker show-tick input-sm form-control"/>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <div class="col-lg-1">
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" id="btnSendMsg"
                                    data-loading-text="Loading..." onclick="CargoBtn(2)">
                                <i class="glyphicon glyphicon-send">&nbsp;</i>GO
                            </button>
                        </div>
                        <div class="col-lg-10">
                        </div>
                    </div>
                </form>
                <form id="moveCargoInfo" style="display: none" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="cargoStorageNoOut" class="col-sm-2 control-label">出库位编码:</label>
                        <div class="col-lg-3">
                            <input id="cargoStorageNoMoveOut" name="cargoStorageNoMoveOut"
                                   class="selectpicker show-tick input-sm form-control" readonly="readonly"/>
                        </div>
                        <button type="button" class="btn btn-primary " data-toggle="modal" onclick="valueTo(1)"
                                data-target="#divStorageMap">
                            <i
                                    class="glyphicon glyphicon-screenshot">&nbsp;</i>库位
                        </button>
                    </div>
                    <div class="form-group">
                        <label for="cargoStorageNoOut" class="col-sm-2 control-label">入库位编码:</label>
                        <div class="col-lg-3">
                            <input id="cargoStorageNoMoveIn" name="cargoStorageNoMoveIn"
                                   class="selectpicker show-tick input-sm form-control" readonly="readonly"/>
                        </div>
                        <button type="button" class="btn btn-primary " data-toggle="modal" onclick="valueTo(2)"
                                data-target="#divStorageMap">
                            <i
                                    class="glyphicon glyphicon-screenshot">&nbsp;</i>库位
                        </button>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-1">
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" id="moveCargo"
                                    data-loading-text="Loading..." onclick="CargoBtn(3)">
                                <i class="glyphicon glyphicon-send">&nbsp;</i>GO
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
</div>


<!-- 模态框（Modal） -->
<div class="modal fade" id="divStorageMap" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">库位地图详情</h4>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <button class="btn btn-primary btn-success active" onclick="postCc(1)" id="btnY1">
                            <i class="glyphicon glyphicon-fire"></i>&nbsp;一层
                        </button>&nbsp;
                        <button class="btn btn-primary" onclick="postCc(2)" id="btnY2">
                            <i class="glyphicon glyphicon-fire"></i>&nbsp;二层
                        </button>&nbsp;
                        <button class="btn btn-primary" onclick="postCc(3)" id="btnY3">
                            <i class="glyphicon glyphicon-fire"></i>&nbsp;三层
                        </button>&nbsp;
                    </div>
                    <div class="row">&nbsp;</div>
                    <div class="row">
                        <table id="tableStorageMap" style="border: 1px solid #000000">
                        </table>
                    </div>
                    <div class="row">&nbsp;</div>
                    <div class="row">
                        <div class="col-lg-2">
                            <div style="background-color:chartreuse;height: 30px;width: 30px "></div>
                            <label class=" control-label"> 空闲</label>
                        </div>
                        <div class="col-lg-2">
                            <div style="background-color:yellow;height: 30px;width: 30px "></div>
                            <label class=" control-label"> 有货</label>
                        </div>
                        <div class="col-lg-2">
                            <div style="background-color:red;height: 30px;width: 30px "></div>
                            <label class=" control-label">禁用</label>
                        </div>
                        <div class="col-lg-2">
                            <div style="background-color:#5e5e5e;height: 30px;width: 30px "></div>
                            <label class=" control-label">障碍物</label>
                        </div>
                        <div class="col-lg-2">
                            <div style="background-color:#122b40;height: 30px;width: 30px "></div>
                            <label class=" control-label">母车巷道</label>
                        </div>
                        <div class="col-lg-2">
                            <div style="background-color:#31b0d5;height: 30px;width: 30px "></div>
                            <label class=" control-label">
                                输送线</label>
                        </div>
                        <div class="col-lg-2">
                            <div style="background-color:white;height: 30px;width: 30px;border: 1px solid black;"></div>
                            <label class=" control-label">
                                空白区域</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
            </div>
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
<script src="<%=basePath %>bootStrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="<%=basePath %>bootStrap/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-table.js"></script>
<script src="<%=basePath %>bootStrap/js/bootstrap-table-zh-CN.js"></script>


<script src="<%=basePath %>page/js/publicMethod.js"></script>
<script src="<%=basePath %>page/js/cargo.js"></script>
</body>
</html>

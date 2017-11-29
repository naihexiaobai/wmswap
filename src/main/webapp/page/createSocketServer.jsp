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
    <title>创建socket</title>
    <!-- 引入 Bootstrap -->
    <meta name="viewport" content="width=device-width, maximum-scale=1.0,initial-scale=1.0,user-scalable=no">
    <link href="<%=basePath %>bootStrap/css/bootstrap.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-select.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrapValidator.css" rel="stylesheet">
    <link href="<%=basePath %>css/index.css" rel="stylesheet">
</head>
<body>
<div id="main_index" class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>Home</li>
            <li>Socket</li>
            <li class="active">创建socket</li>
        </ol>
    </div>
    <div class="row">
        <form id="from_socketInfo" class="form-horizontal" role="form">
            <div class="form-group">
                <label for="socketInfo_idNum" class="col-sm-1 control-label">序号:</label>
                <div class="col-sm-2">
                    <input id="socketInfo_idNum" name="socketInfo_idNum" class="form-control input-sm " type="text"
                           readonly="readonly"  placeholder="请输入序号:1,2,3"/>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_id" class="col-sm-1 control-label">编号:</label>
                <div class="col-sm-2">
                    <input id="socketInfo_id" name="socketInfo_id" class="form-control input-sm" type="text"
                           placeholder="请输入编号:id1,id2"/>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_name" class="col-sm-1 control-label">名称:</label>
                <div class="col-sm-2">
                    <input id="socketInfo_name" name="socketInfo_name" class="form-control input-sm" type="text"
                           placeholder="请输入名称：子车A"/>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_description" class="col-sm-1 control-label">描述:</label>
                <div class="col-sm-2">
                    <input id="socketInfo_description" name="socketInfo_description" class="form-control input-sm"
                           type="text"
                           placeholder="请输入该连接描述信息"/>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_ip" class="col-sm-1 control-label">IP:</label>
                <div class="col-sm-2">
                    <input id="socketInfo_ip" name="socketInfo_ip" class="form-control input-sm" type="text"
                           value="localhost" disabled="disabled" placeholder="请输入IP地址"/>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_port" class="col-sm-1 control-label">端口:</label>
                <div class="col-sm-2">
                    <input id="socketInfo_port" name="socketInfo_port" class="form-control input-sm" type="text"
                           placeholder="请输入端口号"/>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_type" class="col-sm-1 control-label">类型:</label>
                <div class="col-sm-2">
                    <select id="socketInfo_type" name="socketInfo_type"
                            class="selectpicker show-tick input-sm form-control"
                            data-live-search="true">
                        <option  value="0">客户端</option>
                        <option selected="selected" value="1">服务器</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="socketInfo_status" class="col-sm-1 control-label">状态:</label>
                <div class="col-sm-2">
                    <select id="socketInfo_status" name="socketInfo_status"
                            class="selectpicker show-tick input-sm form-control"
                            data-live-search="true">
                        <option selected="selected" value="0">未使用</option>
                        <option value="1">使用中</option>
                        <option value="2">待关闭</option>
                        <option value="3">已关闭</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-1"></div>
                <div class="col-sm-2">
                    <button type="button" class="btn btn-primary" id="btnCreateServer"
                            onclick="submitForm('btnCreateServer')"
                            data-loading-text="Loading..." data-complete-text="Loading finished">创建Server
                    </button>
                </div>
            </div>
        </form>
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
<script src="<%=basePath %>page/js/createSocket.js"></script>
<script>
</script>
</body>
</html>


<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/6
  Time: 9:47
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
    <title>socket信息</title>
    <!-- 引入 Bootstrap -->
    <meta name="viewport" content="width=device-width, maximum-scale=1.0,initial-scale=1.0,user-scalable=no">
    <link href="<%=basePath %>bootStrap/css/bootstrap.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-select.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrapValidator.css" rel="stylesheet">
    <link href="<%=basePath %>bootStrap/css/bootstrap-table.css" rel="stylesheet">
    <!-- 引入 自定义css -->
    <link href="<%=basePath %>css/index.css" rel="stylesheet">
</head>
<body>
<div id="main_index" class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>Home</li>
            <li>Socket</li>
            <li>Socket属性</li>
            <li class="active">Socket信息</li>
        </ol>
    </div>
    <div class="row">
        <div class="container">
            <table id="table_socket_info">
            </table>
        </div>
    </div>
    <div class="modal fade" id="modal_sendMsg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title text-primary" id="myModalLabel">发送消息</h4>
                </div>
                <div class="modal-body">
                    <textarea class="form-control" rows="3"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary"><i class="glyphicon glyphicon-send">&nbsp;</i>发送
                    </button>
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
    <script src="<%=basePath %>bootStrap/js/bootstrap-table.js"></script>
    <script src="<%=basePath %>bootStrap/js/bootstrap-table-zh-CN.js"></script>
    <!-- 自定义 -->
    <script src="<%=basePath %>page/js/socket.js"></script>
    <script src="<%=basePath %>page/js/publicMethod.js"></script>
</body>
</html>

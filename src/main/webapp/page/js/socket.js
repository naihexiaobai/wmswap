$(function () {
    var oTable = new TableInit();
    oTable.init();
});

/**
 * 初始化table
 * @returns {Object}
 * @constructor
 */
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.init = function () {
        $('#table_socket_info').bootstrapTable({
            url: 'http://localhost:8080/wmsWap/socketCc/getSocketInfoJSON',         //请求后台的URL（*）
            method: 'post',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            dataType: "json",                  //服务器返回数据类型
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            showPaginationSwitch: true,          //是否显示 数据条数选择框
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 0,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            // height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                field: 'idNumSocket',
                title: '序号',
            }, {
                field: 'idSocket',
                title: '编号'
            }, {
                field: 'nameSocket',
                title: '名称'
            }, {
                field: 'DescSocket',
                title: '描述'
            }, {
                field: 'ipSocket',
                title: 'IP'
            }, {
                field: 'portSocket',
                title: '端口'
            }, {
                field: 'ipSocketLocal',
                title: 'LocalIP'
            }, {
                field: 'portSocketLocal',
                title: 'Local端口'
            }, {
                field: 'typeSocket',
                title: '类型'
            }, {
                field: 'statusSocket',
                title: '状态'
            }, {
                field: 'operate',
                title: '操作',
                formatter: operateFormatter //自定义方法，添加操作按钮
            }],
            rowStyle: function (row, index) {
                var classesArr = ['success', 'info'];
                var strclass = "";
                if (index % 2 === 0) {//偶数行
                    strclass = classesArr[0];
                } else {//奇数行
                    strclass = classesArr[1];
                }
                return {classes: strclass};
            }
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
        };
        return temp;
    };

    function operateFormatter(value, row, index) {//赋予的参数
        return [
            // '<a href="#"  onclick="showModalSendMsg();" title="发送消息" ><i class="glyphicon glyphicon-send"></i></a>&nbsp;&nbsp;',
            // '<a  href="#" title="查看消息记录" ><i class="glyphicon glyphicon-list-alt"></i></a>&nbsp;&nbsp;',
            '<a  href="#" onclick="closeSocket(\'' + row.idNumSocket + '\');" title="删除" ><i class="glyphicon  glyphicon-remove"></i></a>'
        ].join('');
    }

    return oTableInit;
};

/**
 * 显示发送消息页面
 */
function showModalSendMsg() {
    //调用父窗口方法  parent
    parent.goToPage('http://localhost:8080/wmsWap/page/sendMsg.jsp');
}

/**
 * 关闭socket
 * @param row
 */
function closeSocket(idNumSocket) {
    $.ajax({
        url: "http://localhost:8080/wmsWap/socketCc/closeSocket",
        data: {"idNum": idNumSocket},
        dataType: "json",
        type: "post",
        success: function (resultData) {
            if (resultData.result) {
                modelDiv(2);
            } else {
                modelDiv(3);
            }
        },
        error: function () {
            modelDiv(3);
        }
    });
}
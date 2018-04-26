/**
 * 初始化表格
 */
$(function () {
    var oTableZ = new TableInitZ();
    oTableZ.init();
});


/**
 * 初始化table
 * @returns {Object}
 * @constructor
 */
var TableInitZ = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.init = function () {
        $('#table_cargo_info').bootstrapTable({
            //请求后台的URL（*）
            url: "http://localhost:8080/wmsWap/wcsCc/getCargoDetail",
            method: 'post',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            dataType: "json",                  //服务器返回数据类型
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            showPaginationSwitch: true,          //是否显示 数据条数选择框
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            searchAlign: "left",                 //指定 搜索框 水平方向的位置。'left' or 'right'
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 20, 30, 40],        //可供选择的每页的行数（*）
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
            columns: [
                {field: 'id', title: '序号'},
                {field: 'name', title: '名称'},
                {field: 'palletNo', title: '托盘号'},
                {field: 'batchNo', title: '批次号'},
                {field: 'storageNo', title: '库位'},
                {field: 'status', title: '状态'},
                {field: 'quality', title: '品质'},
                {field: 'specific', title: '规格'},
                {field: 'number', title: '数量'},
                {field: 'productionDate', title: '生产日期'},
                {field: 'shelfLife', title: '保质期'},
                {
                    title: '操作',
                    field: 'id',
                    align: 'center',
                    formatter: function (value, row, index) {
                        var e = '<button  class="glyphicon glyphicon-arrow-up" data-toggle="tooltip"  title="出库"></button>' +
                            '<button  class="glyphicon glyphicon-arrow-down" data-toggle="tooltip"  title="入库"></button>' +
                            ' <button  class="glyphicon glyphicon-edit" data-toggle="tooltip"   title="修改"></button>' +
                            '<button  class="glyphicon glyphicon-remove" data-toggle="tooltip"  onclick="deleteCargo(\'' + row.id + '\')"   title="删除"></button>';  //row.id为每行的id
                        return e;
                    }
                }
            ]
            ,
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
    return oTableInit;
};


function deleteCargo(id) {
    $.ajax({
        url: "http://localhost:8080/wmsWap/wcsCc/deleteCargo",
        type: "post",
        data: {"data": id},
        dataType: "json",
        success: function (resultData) {
            if (resultData.result) {
                modelDiv(2);
            } else {
                modelDiv(3);
            }
        }, error: function () {
            modelDiv(3);
        }
    });
}
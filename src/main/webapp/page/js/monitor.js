/**
 * 设备类型
 * @type {number}
 */
var typeAll = 1;
/**
 * 数据刷新时间 /毫秒
 * @type {number}
 */
var freshTime = 200;
/**
 * 定时刷新变量
 * @type {null}
 */
var intAPF = null;

/**
 * 初始化表格
 */
$(function () {
    var oTableZ = new TableInitZ();
    oTableZ.init();
    // $('#table_monitor_info').bootstrapTable("removeAll");
    initMonitorStatus();
});

/**
 * 初始化监控状态
 */
function initMonitorStatus() {
    $.ajax({
        url: "http://localhost:8080/wmsWap/monitoringCc/checkMonitorStatus",
        type: "post",
        dataType: "json",
        success: function (resultData) {
            if (resultData.result) {
                $("#startMonitor_btn").button('loading');
                $("#stopMonitor_btn").button('reset');
            }else {
                $("#startMonitor_btn").button('reset');
                $("#stopMonitor_btn").button('loading');
            }
        }, error: function () {
        }
    });
}

/**
 * 查看数据,定时刷新
 * @param type
 */
function showMonitorData(type) {
    typeAll = type;
    clearInterval(intAPF);
    intAPF = null;
    $('#table_monitor_info').bootstrapTable("removeAll");
    if (typeAll == 1) {
        intAPF = setInterval(monitoeZiChe, freshTime);
    } else if (typeAll == 2) {
        intAPF = setInterval(monitorMuChe, freshTime);
    } else if (typeAll == 3) {
        intAPF = setInterval(monitorDuiDuoJi, freshTime);
    } else if (typeAll == 4) {
        intAPF = setInterval(monitorShengJiangJi, freshTime);
    } else if (typeAll == 5) {
        intAPF = setInterval(monitorShuSongXian, freshTime);
    }

}

/**
 * 初始化table
 * @returns {Object}
 * @constructor
 */
var TableInitZ = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.init = function () {
        $('#table_monitor_info').bootstrapTable({
            url: 'http://localhost:8080/wmsWap/monitoringCc/getMonitorData?type=' + typeAll,         //请求后台的URL（*）
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
            columns: [
                {field: 'name', title: '名称'},
                {field: 'ziShouDong', title: '自/手动'},
                {field: 'daiMing', title: '待命'},
                {field: 'kongXian', title: '空闲'},
                {field: 'tiShengZhong', title: '顶升'},
                {field: 'xiaJiangZhong', title: '下降'},
                {field: 'qianJinZhong', title: '前进'},
                {field: 'houTuiZhong', title: '后退'},
                {field: 'AYuanDianDaiJi', title: 'A点'},
                {field: 'BYuanDianDaiJi', title: 'B点'},
                {field: 'zaiWu', title: '载荷'},
                {field: 'shiFouGuZhang', title: '故障'},
                {field: 'beiYongZhuangTai', title: '备用'},
                {field: 'jingGao', title: '警告'},
                {field: 'chongDian', title: '充电'},
                {field: 'tiShengJiShangDaiJI', title: 'RS待机'},
                {field: 'muCheShangDaiJi', title: 'RGV待机'},
                {field: 'dianLiang', title: '电量'},
                {field: 'renWuMa', title: '任务码'},
                {field: 'lie', title: '列'},
                {field: 'pai', title: '排'},
                {field: 'ceng', title: '层'},
                {field: 'wcsRenWuMa', title: 'wcs任务码'},
                // {field: 'yaoKongQiRenWuMa', title: '遥控器任务码'},
                // {field: 'panDianTuoPanShu', title: '盘点数'},
                {field: 'guZhangXinXi', title: '故障码'},
                // {field: 'dangQIanBianMaQiZhi', title: '当前编码器值'},
                // {field: 'shangCiBianMaQiZhi', title: '上次编码器值'},
                // {field: 'dongZuoZhiLing', title: '动作指令'},
                // {field: 'dongZuoRenWuHao', title: '动作任务号'},
                // {field: 'muBiaoCeng', title: '目标层'},
                // {field: 'muBiaoLie', title: '目标列'},
                // {field: 'muBiaoPai', title: '目标排'}
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

    // function operateFormatter(value, row, index) {//赋予的参数
    //     return [
    //         '<a  href="#" onclick="closeSocket(\'' + row.idNumSocket + '\');" title="删除" ><i class="glyphicon  glyphicon-remove"></i></a>'
    //     ].join('');
    // }

    return oTableInit;
};

/**
 * 子车数据刷新
 */
function monitoeZiChe() {
    $('#table_monitor_info').bootstrapTable(
        "refreshOptions",
        {
            url: 'http://localhost:8080/wmsWap/monitoringCc/getMonitorData?type=' + typeAll, // 获取数据的地址
            columns: [
                {field: 'name', title: '名称'},
                {field: 'ziShouDong', title: '自/手动'},
                {field: 'daiMing', title: '待命'},
                {field: 'kongXian', title: '空闲'},
                {field: 'tiShengZhong', title: '顶升'},
                {field: 'xiaJiangZhong', title: '下降'},
                {field: 'qianJinZhong', title: '前进'},
                {field: 'houTuiZhong', title: '后退'},
                {field: 'AYuanDianDaiJi', title: 'A点'},
                {field: 'BYuanDianDaiJi', title: 'B点'},
                {field: 'zaiWu', title: '载荷'},
                {field: 'shiFouGuZhang', title: '故障'},
                {field: 'beiYongZhuangTai', title: '备用'},
                {field: 'jingGao', title: '警告'},
                {field: 'chongDian', title: '充电'},
                {field: 'tiShengJiShangDaiJI', title: 'RS待机'},
                {field: 'muCheShangDaiJi', title: 'RGV待机'},
                {field: 'dianLiang', title: '电量'},
                {field: 'renWuMa', title: '任务码'},
                {field: 'lie', title: '列'},
                {field: 'pai', title: '排'},
                {field: 'ceng', title: '层'},
                {field: 'wcsRenWuMa', title: 'wcs任务码'},
                {field: 'guZhangXinXi', title: '故障码'},
                {field: 'dongZuoZhiLing', title: '动作指令'}
            ],
        }
    );
}

/**
 * 母车数据刷新
 */
function monitorMuChe() {
    $('#table_monitor_info').bootstrapTable(
        "refreshOptions",
        {
            url: 'http://localhost:8080/wmsWap/monitoringCc/getMonitorData?type=' + typeAll, // 获取数据的地址
            columns: [
                {field: 'name', title: '名称'},
                {field: 'ziShouDong', title: '自/手动'},
                {field: 'daiMing', title: '待命'},
                {field: 'kongXian', title: '空闲'},
                {field: 'qianJinZhong', title: '前进'},
                {field: 'houTuiZhong', title: '后退'},
                {field: 'huoChaZuoShen', title: '链条反转'},
                {field: 'huoChaZuoHui', title: '链条正转'},
                // {field: 'huoChaYouShen', title: '右伸'},
                // {field: 'huoChaYouHui', title: '右回'},
                // {field: 'huoChaZhongWei', title: '中位'},
                {field: 'zaiWu', title: '载荷'},
                {field: 'zaiChe', title: '载车'},
                {field: 'shiFouGuZhang', title: '故障'},
                {field: 'fuWei', title: '复位'},
                {field: 'renWuMa', title: '任务码'},
                // {field: 'wcsRenWuMa', title: 'wcs任务码'},
                {field: 'lie', title: '列'},
                {field: 'guZhangXinXi', title: '故障码'},
                {field: 'muBiaoLie', title: '目标列'},
                {field: 'dongZuoZhiLing', title: '动作指令'},
                {field: 'dongZuoRenWuHao', title: '动作任务码'}
            ],
        }
    );
}

/**
 * 堆垛机数据刷新
 */
function monitorDuiDuoJi() {
    $('#table_monitor_info').bootstrapTable(
        "refreshOptions",
        {
            url: 'http://localhost:8080/wmsWap/monitoringCc/getMonitorData?type=' + typeAll, // 获取数据的地址
            columns: [
                {field: 'name', title: '名称'},
                {field: 'ziShouDong', title: '自/手动'},
                {field: 'daiMing', title: '待命'},
                {field: 'kongXian', title: '空闲'},
                {field: 'tiSheng', title: '上升'},
                {field: 'xiaJiang', title: '下降'},
                {field: 'qianJinZhong', title: '前进'},
                {field: 'houTuiZhong', title: '后退'},
                // {field: 'huoChaZuoShen', title: '左伸'},
                // {field: 'huoChaZuoHui', title: '左回'},
                // {field: 'huoChaYouShen', title: '右伸'},
                // {field: 'huoChaYouHui', title: '右回'},
                // {field: 'huoChaZhongWei', title: '中位'},
                {field: 'zaiWu', title: '载荷'},
                {field: 'zaiChe', title: '载车'},
                {field: 'shiFouGuZhang', title: '故障'},
                {field: 'beiYongZhuangTai', title: '备用'},
                {field: 'renWuMa', title: '任务码'},
                {field: 'lie', title: '列'},
                {field: 'ceng', title: '层'},
                {field: 'guZhangXinXi', title: '故障码'},
                {field: 'muBiaoLie', title: '目标列'},
                {field: 'muBiaoCeng', title: '目标层'},
                {field: 'dongZuoZhiLing', title: '动作指令'},
                {field: 'dongZuoRenWuHao', title: '动作任务码'}
            ],
        }
    );
}

/**
 * 升降机数据刷新
 */
function monitorShengJiangJi() {
    $('#table_monitor_info').bootstrapTable(
        "refreshOptions",
        {
            url: 'http://localhost:8080/wmsWap/monitoringCc/getMonitorData?type=' + typeAll, // 获取数据的地址
            columns: [
                {field: 'name', title: '名称'},
                {field: 'ziShouDong', title: '自/手动'},
                {field: 'daiMing', title: '待命'},
                {field: 'kongXian', title: '空闲'},
                {field: 'tiSheng', title: '上升'},
                {field: 'xiaJiang', title: '下降'},
                // {field: 'huoChaZuoShen', title: '左伸'},
                // {field: 'huoChaZuoHui', title: '左回'},
                // {field: 'huoChaYouShen', title: '右伸'},
                // {field: 'huoChaYouHui', title: '右回'},
                // {field: 'huoChaZhongWei', title: '中位'},
                {field: 'zaiWu', title: '载荷'},
                {field: 'zaiChe', title: '载车'},
                {field: 'shiFouGuZhang', title: '故障'},
                {field: 'beiYongZhuangTai', title: '备用'},
                {field: 'fuWei', title: '复位'},
                {field: 'renWuMa', title: '任务码'},
                {field: 'ceng', title: '层'},
                {field: 'guZhangXinXi', title: '故障码'},
                {field: 'muBiaoCeng', title: '目标层'},
                {field: 'dongZuoZhiLing', title: '动作指令'},
                {field: 'dongZuoRenWuHao', title: '动作任务码'}
            ],
        }
    );
}

/**
 * 输送线数据刷新
 */
function monitorShuSongXian() {
    $('#table_monitor_info').bootstrapTable(
        "refreshOptions",
        {
            url: 'http://localhost:8080/wmsWap/monitoringCc/getMonitorData?type=' + typeAll, // 获取数据的地址
            columns: [
                {field: 'name', title: '名称'},
                {field: 'ZhuangTai', title: '状态'},
                {field: 'daiMing', title: '待命'},
                {field: 'kongXian', title: '空闲'},
                {field: 'waiWeiZhangAi', title: '外围障碍'},
                {field: 'qiDong', title: '启动'},
                {field: 'zaiWu', title: '载荷'},
                {field: 'renWuMa', title: '任务码'},
                {field: 'dongZuoZhiLing', title: '动作指令'},
                {field: 'wcsRenWuMa', title: 'wcs任务码'}
            ],
        }
    );
}

/**
 * 开启/关闭 1/0 监控
 * @param type
 */
function monitoring(type) {
    $.ajax({
        data: {"type": type},
        dataType: "json",
        type: "post",
        url: "http://localhost:8080/wmsWap/monitoringCc/startMonitor",
        success: function (resultData) {
            if (resultData.result) {
                modelDiv(2);
                if (type == 1) {
                    $("#startMonitor_btn").button('loading');
                    $("#stopMonitor_btn").button('reset');
                } else {
                    $("#startMonitor_btn").button('reset');
                    $("#stopMonitor_btn").button('loading');
                }
            } else {
                modelDiv(3);
            }
        },
        error: function () {
            modelDiv(3);
        }
    });
}

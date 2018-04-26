$(".form_datetime").datetimepicker({
    format: 'yyyy-mm-dd hh:ii',//显示格式
    todayHighlight: 1,//今天高亮
    language: 'zh-CN',
    todayBtn: 1,
    // minView: "month",//设置只显示到月份
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    autoclose: 1//选择后自动关闭
});

/**
 * 新增
 */
function addCargoDate() {
    var jsonSerInfo = $('#cargoAdd').serializeObject();
    $.ajax({
        url: "http://localhost:8080/wmsWap/wcsCc/addCargoDate",
        type: "post",
        data: {"data": JSON.stringify(jsonSerInfo)},
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


function showDivModel() {
    $("#cargoAdd").show();
}
$(function () {
    initSendMsgData();
});

/**
 * 初始化设备连接
 */
function initSendMsgData() {
    $.ajax({
        url: "http://localhost:8080/wmsWap/socketCc/getAutoMachineData",
        type: "post",
        dataType: "json",
        success: function (resultData) {
            var html = "<option selected='selected'>请选择</option>";
            for (var i = 0; i < resultData.length; i++) {
                html += "<option value='" + resultData[i].idNum + "'>" + resultData[i].name + "</option>";
            }
            $("#msgIdNum").append(html);
        }, error: function () {
            parent.modelDiv(3);
        }
    });
}

/**
 * 发送消息
 * @param btnID
 */
function sendMsgWCS(btnID) {
    var data = $('#from_sendMsgWCS').serializeObject();
    //转化成json字符串
    var submitData = JSON.stringify(data);
    $.ajax({
        type: "post",
        dataType: "json",
        data: {"data": submitData},
        url: "http://localhost:8080/wmsWap/socketCc/wcsSendMsg",
        success: function (resultData) {

        },
        error: function () {

        }
    });
}
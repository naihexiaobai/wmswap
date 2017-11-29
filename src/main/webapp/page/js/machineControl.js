$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * 发送动作消息
 */
function machineSendMsg() {
    var jsonuserinfo = $('#machine_msg').serializeObject();
    $.ajax({
        url: "http://localhost:8080/wmsWap/monitoringCc/wcsSendMsg",
        type: "post",
        data: {"msg": JSON.stringify(jsonuserinfo)},
        dataType: "json",
        beforeSend: function () {
            $("#btnSendMsg").button('loading');
        },
        success: function (resultData) {
            if (resultData.result) {
                modelDiv(2);
                $("#btnSendMsg").button('reset');
            } else {
                modelDiv(3);
                $("#btnSendMsg").button('reset');
            }
        }, error: function () {
            modelDiv(3);
        }
    });
}
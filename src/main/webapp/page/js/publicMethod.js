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
 * 弹出模态框
 * @param type 1,info ；2，success ； 3，失败；
 */
function modelDiv(type) {
    if (type == 1) {
        $("#model_info").modal('show');
    } else if (type == 2) {
        $("#model_success").modal('show');
    } else if (type == 3) {
        $("#model_fail").modal('show');
    }
}

/**
 * 关闭模态框
 * @param type 1,info ；2，success ； 3，失败；
 */
function modelDivHiden(type) {
    if (type == 1) {
        $("#model_info").modal('hide');
    } else if (type == 2) {
        $("#model_success").modal('hide');
    } else if (type == 3) {
        $("#model_fail").modal('hide');
    }
}

/**
 * 页面跳转
 * @param URL
 */
function goToPage(URL) {
    $("#div-iframe").attr('src', URL);
}
/**
 * 初始化表单数据
 */
$(function () {
    getFormData();
    formValidator();
});

/**
 * 表单校验
 */
function formValidator() {
    $('#from_socketInfo').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                socketInfo_id: {
                    message: '编号验证失败',
                    validators: {
                        notEmpty: {
                            message: '编号不能为空'
                        }
                    }
                },
                socketInfo_name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        }
                    }
                },
                socketInfo_description: {
                    validators: {
                        notEmpty: {
                            message: '描述不能为空'
                        }
                    }
                }
                ,
                socketInfo_ip:
                    {
                        validators: {
                            notEmpty: {
                                message: 'IP地址不能为空'
                            }
                        }
                    }
                ,
                socketInfo_port:
                    {
                        validators: {
                            notEmpty: {
                                message: '端口号不能为空'
                            }
                        }
                    }
            }
        }
    );
}

/**
 * 初始化序号
 */
function getFormData() {
    $.ajax({
        url: "http://localhost:8080/wmsWap/socketCc/initData",
        type: "post",
        dataType: "json",
        beforeSend: function () {
        },
        success: function (datas) {
            $("#socketInfo_idNum").val(datas.idNum);
        },
        error: function () {
        }
    });
}

/**
 * 创建socket
 * @param btnId  按钮ID
 */
function submitForm(btnId) {
    var data = $('#from_socketInfo').serialize();
    //序列化获得表单数据，结果为：user_id=12&user_name=John&user_age=20
    var submitData = decodeURIComponent(data, true);
    //submitData是解码后的表单数据，结果同上
    $.ajax({
        url: 'http://localhost:8080/wmsWap/socketCc/createSocket',
        // url: 'socketCc/test',
        data: {"submitData": submitData},
        type: "post",
        dataType: "json",
        beforeSend: function () {
            var bootstrapValidator = $("#from_socketInfo");
            //手动触发验证
            bootstrapValidator.data("bootstrapValidator").validate();
            if (bootstrapValidator.data("bootstrapValidator").isValid()) {

            } else {
                return false;
            }
        },
        success: function (datas) {
            // modelDivHiden(1);
            if (datas.result) {
                modelDiv(2);
                $("#" + btnId + "").button('reset');
                document.getElementById("from_socketInfo").reset();
                $("#from_socketInfo").data('bootstrapValidator').resetForm(true);
                getFormData();
            } else {
                modelDiv(3);
                $("#" + btnId + "").button('reset');
            }
        }
        ,
        error: function () {
            // modelDivHiden(1);
            modelDiv(3);
            $("#" + btnId + "").button('reset');
        }
    });
}

/**
 * 一键创建client
 * @param btnId
 */
function createClientFast(btnId) {
    var idNum = $("#socketInfo_idNum").val();
    $("#socketInfo_id").val("A" + idNum + "");
    $("#socketInfo_name").val("子车A" + idNum + "");
    $("#socketInfo_ip").val("10.11.43.41");
    $("#socketInfo_port").val("8888");
    $("#socketInfo_description").val("这是一个测试" + idNum + "");
    submitForm(btnId);
}
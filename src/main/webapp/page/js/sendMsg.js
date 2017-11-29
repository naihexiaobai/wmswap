$(function () {
    formValidator();
    initSendMsgData();
    initTime();
});

/**
 * 时间控件
 */
function initTime() {
    $(".form_datetime").datetimepicker({
        format: 'yyyymmddhhiiss',
        todayBtn: true,      //是否显示今天按钮
        autoclose: true,     //选择之后立即关闭时间控件
        language: 'zh-CN',
        todayHighlight: true //是否高亮今日日期
    });
}

/**
 * 初始化页面
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
 */
function sendMsg(btnId) {
    //发送前序列化为json
    var data = $('#from_sendMsg').serializeObject();
    //序列化获得表单数据，结果为：user_id=12&user_name=John&user_age=20
    var submitData = decodeURIComponent(data, true);
    //submitData是解码后的表单数据，结果同上
    $.ajax({
        url: 'http://localhost:8080/wmsWap/socketCc/sendMsg',
        data: {"submitData": submitData},
        type: "post",
        dataType: "json",
        beforeSend: function () {
            // //获取表单验证结果
            $("#" + btnId + "").button('loading');
            var bootstrapValidator = $("#from_sendMsg");
            //手动触发验证
            bootstrapValidator.data("bootstrapValidator").validate();
            if (bootstrapValidator.data("bootstrapValidator").isValid()) {
            } else {
                return;
            }
        },
        success: function (datas) {
            if (datas.result) {
                modelDiv(2);
                $("#" + btnId + "").button('reset');
                $("#from_sendMsg").data('bootstrapValidator').resetForm(true);
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
 * 表单校验
 */
function formValidator() {
    $('#from_sendMsg').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                msgIdNum: {
                    message: '设备名称验证失败',
                    validators: {
                        notEmpty: {
                            message: '设备名称不能为空！'
                        }
                    }
                },
                msgTime: {
                    validators: {
                        notEmpty: {
                            message: '发送时间不能为空！'
                        }
                    }
                },
                msgOrderNum: {
                    validators: {
                        notEmpty: {
                            message: '任务序号不能为空！'
                        }
                    }
                }
                ,
                msgCommandType:
                    {
                        validators: {
                            notEmpty: {
                                message: '指令类型必须选择！'
                            }
                        }
                    }
                ,
                msgCommandData:
                    {
                        validators: {
                            notEmpty: {
                                message: '指令内容必须选择！'
                            }
                        }
                    },
                msgCountNum:
                    {
                        validators: {
                            notEmpty: {
                                message: '发送次数不能为空！'
                            }
                        }
                    }
            }
        }
    );
}
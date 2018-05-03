/**
 * 一层
 * @type {number}
 */
var y = 1;

/**
 * 源货位赋值标识
 * @type {number}
 */
var moveOutStorage = 0;
/**
 * 目标货位赋值标识
 * @type {number}
 */
var moveInStorage = 0;

$(function () {
    //时间控件初始化
    initTime();
    //库位地图初始化
    postCc(y);

    btnLoadingReset();
});

/**
 * 按钮初始化
 */
function btnLoadingReset() {
    $("#outStorage").button("loading");
}

/**
 * 初始化货位信息 1层
 */
function initTable(jsonData) {
    $('#tableStorageMap').empty();
    var htmlTable = "";
    var json_storage = "";
    var htmlTbody = "";
    var htmlTableTbodyHead = "<tbody>";
    var htmlTableTbodyFoot = "</tbody>";
    var htmlTableTrHead = "<tr>";
    var htmlTableTrFoot = "</tr>";
    for (var i = 1; i < 8; i++) {
        var td_htmls = "";
        if (i == 1) {
            json_storage = JSON.parse(jsonData.x1);
        }
        if (i == 2) {
            json_storage = JSON.parse(jsonData.x2);
        }
        if (i == 3) {
            json_storage = JSON.parse(jsonData.x3);
        }
        if (i == 4) {
            json_storage = JSON.parse(jsonData.x4);
        }
        if (i == 5) {
            json_storage = JSON.parse(jsonData.x5);
        }
        if (i == 6) {
            json_storage = JSON.parse(jsonData.x6);
        }
        if (i == 7) {
            json_storage = JSON.parse(jsonData.x7);
        }
        for (var n = 0; n < json_storage.length; n++) {
            var td_html = createTd(json_storage[n]);
            td_htmls += td_html;
        }
        htmlTbody += htmlTableTrHead + td_htmls + htmlTableTrFoot;
    }
    htmlTable = htmlTableTbodyHead + htmlTbody + htmlTableTbodyFoot;
    $("#tableStorageMap").append(htmlTable);
}

/**
 * status 0-空闲，1-有货，2-禁用，3-障碍物，4-无货位，5-输送线，11-母车巷道
 */
function createTd(json_storage) {
    var td_html = "";
    if (json_storage.status == 0) {
        td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: chartreuse' width='60px'>" +
            "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color: chartreuse' onclick=commandTask('" + json_storage.storageNo + "') ></button>" +
            "</td>";
    } else if (json_storage.status == 1) {
        td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: yellow' width='60px'>" +
            "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color: yellow' onclick=commandTask('" + json_storage.storageNo + "')></button>" +
            "</td>";
    } else if (json_storage.status == 2) {
        td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: red' width='60px'>" +
            "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color: red' onclick=commandTask('" + json_storage.storageNo + "')> </button>" +
            "</td>";
    } else if (json_storage.status == 3) {
        td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: #5e5e5e' width='60px'>" +
            "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color:  #5e5e5e' disabled='disabled'></button>" +
            "</td>";
    } else if (json_storage.status == 4) {
        td_html = "<td style='height: 60px;background-color: white' width='60px'></td>";
    } else if (json_storage.status == 5) {
        if (json_storage.z == 5 && json_storage.y == 1) {
            td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: #31b0d5' width='60px'>" +
                "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color:  #31b0d5'>T1</button>" +
                "</td>";
        } else {
            td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: #31b0d5' width='60px'>" +
                "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color:  #31b0d5' disabled='disabled'></button>" +
                "</td>";
        }
    } else if (json_storage.status == 6 || json_storage.status == 7) {
        td_html = "<td style='height: 60px;border: 1px solid #000000;background-color: darkgreen' width='60px'>" +
            "<button style='height: 59px;width:59px;margin-top: 1px;margin-left: 1px;background-color: darkgreen' disabled='disabled' ></button>" +
            "</td>";
    } else if (json_storage.status == 11) {
        td_html = "<td style='height: 60px;border: 1px solid #122b40;background-color: #122b40' width='60px'></td>";
    }
    return td_html;
}

/**
 * 获取数据
 * @param y-层
 */
function postCc(y) {
    yBtnInit(y);
    $.ajax({
        type: "post",
        data: {"data": y},
        dataType: "json",
        url: "http://localhost:8080/wmsWap/wcsCc/getStorage",
        success: function (resultData) {
            initTable(resultData);
        }, error: function () {
            modelDiv(3);
        }
    });
}

/**
 * 层 按钮 样式修改
 * @param y
 */
function yBtnInit(y) {
    if (y == 1) {
        $("#btnY1").addClass("btn-success");
        $("#btnY1").addClass("active");
        $("#btnY2").removeClass("active");
        $("#btnY2").removeClass("btn-success");
        $("#btnY3").removeClass("active");
        $("#btnY3").removeClass("btn-success");
    } else if (y == 2) {
        $("#btnY1").removeClass("btn-success");
        $("#btnY1").removeClass("active");
        $("#btnY2").addClass("active");
        $("#btnY2").addClass("btn-success");
        $("#btnY3").removeClass("active");
        $("#btnY3").removeClass("btn-success");
    } else if (y == 3) {
        $("#btnY1").removeClass("btn-success");
        $("#btnY1").removeClass("active");
        $("#btnY2").removeClass("active");
        $("#btnY2").removeClass("btn-success");
        $("#btnY3").addClass("active");
        $("#btnY3").addClass("btn-success");
    }
}

/**
 *  库位编码
 * @param storageNo
 */
function commandTask(storageNo) {
    if (moveInStorage == 1) {
        $("#cargoStorageNoMoveIn").val(storageNo);
        moveInStorage = 0;
    } else if (moveOutStorage == 1) {
        $("#cargoStorageNoMoveOut").val(storageNo);
        moveOutStorage = 0;
    } else {
        $("#cargoStorageNo").val(storageNo);
        $("#cargoStorageNoOut").val(storageNo);
    }
    $("#divStorageMap").modal('hide');
}

/**
 * 导航
 * @param type,类型
 */
function onclickA(type, btnId) {
    if (type == 1) {
        $("#outCargoInfo").show();
        $("#inCargoInfo").hide();
        $("#moveCargoInfo").hide();
        $("#outStorage").button("loading");
        $("#inStorage").button("reset");
        $("#moveStorage").button("reset");
    } else if (type == 2) {
        $("#inCargoInfo").show();
        $("#outCargoInfo").hide();
        $("#moveCargoInfo").hide();
        $("#outStorage").button("reset");
        $("#inStorage").button("loading");
        $("#moveStorage").button("reset");
    } else {
        $("#moveCargoInfo").show();
        $("#outCargoInfo").hide();
        $("#inCargoInfo").hide();
        $("#outStorage").button("reset");
        $("#moveStorage").button("loading");
        $("#inStorage").button("reset");
    }
    commandTask("");
}

/**
 * div地图显示控制
 * @param type
 */
function mapStorage() {
    $("#divStorageMap").show();
    postCc(y);
}

/**
 * div 库存地图关闭
 */
function closeStorageMapDiv() {
    $("#divStorageMap").hide();
}


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
 *
 * @param type 1-出库，2-入库,3-移库
 * @constructor
 */
function CargoBtn(type) {
    if (type == 1) {
        outCargo();
    } else if (type == 2) {
        inCargo();
    } else {
        moveCargo()
    }
    //刷新当前页面
}


function valueTo(typeMove) {
    if (typeMove == 1) {
        moveOutStorage = 1;
    } else {
        moveInStorage = 1;
    }
}

/**
 * 出库
 */
function outCargo() {
    var jsonSerInfo = $('#outCargoInfo').serializeObject();
    $.ajax({
        url: "http://localhost:8080/wmsWap/wcsCc/outStorageCargo",
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

/**
 * 入库
 */
function inCargo() {
    var jsonSerInfo = $('#inCargoInfo').serializeObject();
    $.ajax({
        url: "http://localhost:8080/wmsWap/wcsCc/inStorageCargo",
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

/**
 * 移库
 */
function moveCargo() {
    var jsonSerInfo = $('#moveCargoInfo').serializeObject();
    $.ajax({
        url: "http://localhost:8080/wmsWap/wcsCc/moveStorageCargo",
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


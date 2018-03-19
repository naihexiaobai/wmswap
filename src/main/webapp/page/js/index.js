/**
 * 自动设置侧边栏高度
 */
function changeFrameHeight() {
    var ifm = document.getElementById("div-iframe");
    ifm.height = document.documentElement.clientHeight;
}

window.onresize = function () {
    changeFrameHeight();
}

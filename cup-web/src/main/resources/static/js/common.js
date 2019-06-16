$(document).ready(function () {

    var height = parseInt(document.documentElement.clientHeight - 100) + "px";
    $(".auto-center").css({
        "height": height,
        "line-height": height,
        "vertical-align": "middle",
        "text-align": "center"
    });

    $("#menu li").click(function () {
        $(this).parent().children().removeClass("active");
        $(this).addClass("active");
    });
    $("#menu li").hover(function () {
        //$(this).tClass("active");
    });
});


function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first': 'icon-double-angle-left bigger-140',
        'ui-icon-seek-prev': 'icon-angle-left bigger-140',
        'ui-icon-seek-next': 'icon-angle-right bigger-140',
        'ui-icon-seek-end': 'icon-double-angle-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}

function styleCheckbox(table) {
    $("body").find('input:checkbox').addClass('ace')
        .wrap('<label />')
        .after('<span class="lbl align-top" />')
    $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
        .find('input.cbox[type=checkbox]').addClass('ace')
        .wrap('<label />').after('<span class="lbl align-top" />');

}

function setToolsBarIcon(grid_selector, pager_selector) {
    jQuery(grid_selector).jqGrid('navGrid', pager_selector,
        {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: true,
            refreshicon: 'icon-refresh green'
        }
    );
}


function getFormatDate(date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    }
    return date.format(pattern);
}


function getFormatDateByLong(dateForLong, pattern) {
    return getFormatDate(new Date(dateForLong), pattern);
}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


/**
 * 时间对象的格式化;
 */
Date.prototype.format = function (format) {
    format = format || "yyyy-MM-dd hh:mm:ss";

    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
            - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
/**数据字典项查询**/
function loadDictionarySelect(options) {
    var divId = options.divId;
    var dictType = options.type;
    $.ajax({
        type: "post",
        async: false,
        dataType: "json",
        url: "../select/selectDictionary.do",
        data: {
            "dictType": dictType
        },
        success: function (data) {
            if (data) {
                var selectStr = "<option value=''>请选择</option>";
                $.each(data, function (index, item) {
                    selectStr += "<option value='" + item.value + "'>" + item.text + "</option>";
                });
                $('#' + divId).append(selectStr);
            }
        }
    });
}

/**根据当前用户所在迷你仓填充储区下拉数据**/
function loadZoneSelect(options) {
    var divId = options.divId;
    $.ajax({
        type: "post",
        async: false,
        dataType: "json",
        url: "../select/loadZoneSelect.do",
        success: function (data) {
            if (data) {
                var selectStr = "<option value=''>请选择</option>";
                $.each(data, function (index, item) {
                    selectStr += "<option value='" + item.value + "'>" + item.text + "</option>";
                });
                $('#' + divId).append(selectStr);
            }
        }
    });
}

/**储区类型下拉数据**/
function loadZoneTypeSelect(options) {
    var divId = options.divId;
    $.ajax({
        type: "post",
        async: false,
        dataType: "json",
        url: "../select/loadZoneTypeSelect.do",
        success: function (data) {
            if (data) {
                var selectStr = "<option value=''>请选择</option>";
                $.each(data, function (index, item) {
                    selectStr += "<option value='" + item.value + "'>" + item.text + "</option>";
                });
                $('#' + divId).append(selectStr);
            }
        }
    });
}

/**询一些表单项选择是和否**/
function loadYnFlagSelect(options) {
    var divId = options.divId;
    $.ajax({
        type: "post",
        async: false,
        dataType: "json",
        url: "../select/selectYnFlag.do",

        success: function (data) {
            if (data) {
                var selectStr = "<option value=''>请选择</option>";
                $.each(data, function (index, item) {
                    selectStr += "<option value='" + item.value + "'>" + item.text + "</option>";
                });
                $('#' + divId).append(selectStr);
            }
        }
    });
}


/***格式化Yn(1:是,2:否)**/
function formatYnFlag(flag) {
    if (flag == 1) {
        return "是";
    } else if (flag == 2) {
        return "否";
    } else {
        return "";
    }
}
/***获取字典的名称**/
function getDictionaryName(options) {
    var dictType = options.dictType;
    var dictValue = options.dictValue;
    var dictName;
    $.ajax({
        type: "post",
        async: false,
        dataType: "json",
        data: {
            "dictType": dictType,
            "dictValue": dictValue
        },
        url: "../select/getDictionaryName.do?rand=" + Math.random(),
        success: function (result) {
            if (result.code == '1') {
                dictName = result.data
            }
        }
    });
    if (undefined != dictName) {
        return dictName;
    } else return "";




//JQGrid加载分页图标
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
            'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
            'ui-icon-seek-next' : 'icon-angle-right bigger-140',
            'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
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
}


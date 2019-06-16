/**三级原因级联**/
function loadReason(oneLevelReasonSelectId, twoLevelReasonSelectId, threeLevelReasonSelectId) {
    //加载一级原因
    if (undefined != oneLevelReasonSelectId && oneLevelReasonSelectId.length > 0) {
        jQuery.ajax({
            url: "../select/selectReason.do?random=" + Math.random(),
            type: "post",
            data: {
                "preasonNo": "",
                "reasonLevel": 1
            },
            dataType: "json",
            success: function (oneLevelReasonJsonArray) {
                if (undefined != oneLevelReasonJsonArray) {
                    //if (oneLevelReasonJsonArray.code == '1') {//成功
                    //生成option串
                    var opts = generateOpts(oneLevelReasonJsonArray);
                    if (opts.length > 0) {
                        $("#" + oneLevelReasonSelectId).html("").append(opts);
                    }
                    //}
                }
            }
        })

        //注册一级原因change事件
        $("#" + oneLevelReasonSelectId).change(function () {
            var oneLevelReasonNo = $(this).val();
            //如果二级原因id不为空,则根据一级原因id，去加载二级原因
            if (undefined != twoLevelReasonSelectId && twoLevelReasonSelectId.length > 0) {
                if (undefined != oneLevelReasonNo && oneLevelReasonNo.length > 0) {
                    jQuery.ajax({
                        url: "../select/selectReason.do?random=" + Math.random(),
                        type: "post",
                        data: {
                            "preasonNo": oneLevelReasonNo,
                            "reasonLevel": 2
                        },
                        dataType: "json",
                        success: function (twoLevelReasonJsonArray) {
                            if (undefined != twoLevelReasonJsonArray) {
                                //if (twoLevelReasonJsonArray.code == '1') {//成功
                                //生成option串
                                var opts = generateOpts(twoLevelReasonJsonArray);
                                if (opts.length > 0) {
                                    $("#" + twoLevelReasonSelectId).html("").append(opts);
                                }
                                // }
                            }
                        }
                    })
                } else {
                    $("#" + twoLevelReasonSelectId).html("");
                }
            }
        });

        //如果二级原因select的id不为空，则给它注册change事件
        if (undefined != twoLevelReasonSelectId && twoLevelReasonSelectId.length > 0) {
            $("#" + twoLevelReasonSelectId).change(function () {
                var twoLevelReasonNo = $(this).val();
                //如果二级原因id不为空,则根据一级原因id，去加载二级原因
                if (undefined != threeLevelReasonSelectId && threeLevelReasonSelectId.length > 0) {
                    if (undefined != twoLevelReasonNo && twoLevelReasonNo.length > 0) {
                        jQuery.ajax({
                            url: "../select/selectReason.do?random=" + Math.random(),
                            type: "post",
                            data: {
                                "preasonNo": twoLevelReasonNo,
                                "reasonLevel": 3
                            },
                            dataType: "json",
                            success: function (threeLevelReasonJsonArray) {
                                if (undefined != threeLevelReasonJsonArray) {
                                    // if (threeLevelReasonJsonArray.code == '1') {//成功
                                    //生成option串
                                    var opts = generateOpts(threeLevelReasonJsonArray);
                                    if (opts.length > 0) {
                                        $("#" + threeLevelReasonSelectId).html("").append(opts);
                                    }
                                    // }
                                }
                            }
                        })
                    } else {
                        $("#" + threeLevelReasonSelectId).html("");
                    }
                }
            });
        }
    }

}

//根据josn数组数据，填充select
function generateOpts(jsonArray) {
    if (undefined != jsonArray && jsonArray.length > 0) {
        var optArray = [];
        optArray.push("<option value=''>请选择</option>");
        jQuery.each(jsonArray, function () {
            optArray.push("<option value='" + this.value + "'>" + this.text + "</option>");
        })
        return optArray.join("");
    } else return "";
}
	
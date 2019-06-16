$(function () {
    var user_grid_selector = "#user-grid-table";
    var user_pager_selector = "#user-grid-pager";

    jQuery(user_grid_selector).jqGrid({
        url: "sys/queryUserSystemList",
        datatype: "json",
        postData: {
            sysCode: $("#systemCode").val()
        },
        height: 200,
        colNames: ['id', 'ERP', '姓名', '操作'],
        colModel: [
            {name: 'id', index: 'id', hidden: true},

            {name: 'userNo', index: 'userNo', width: 200},
            {name: 'userName', index: 'userName', width: 200},
            {
                name: '', index: '', width: 120,
                formatter: function (cellvalue, options, rowObject) {
                    var admin = rowObject.admin;
                    var rowUserNo = rowObject.userNo;
                    var id = rowObject.id;
                    var tmp = jQuery("#admin").val() == "true";
                    if (tmp && admin == 0) {
                        var myval = "<button type='button' class='btn btn-xs btn-danger' onclick=deleteUserSystem('" + id + "')>删除</button>";
                        return myval;
                    } else {
                        return "";
                    }
                }
            }
        ],
        viewrecords: true,
        rownumbers: true,//添加左侧行号
        rowNum: 9,
        rowList: [9, 20, 30],
        pager: user_pager_selector,
        jsonReader: {
            page: "page",
            total: "pages",
            pageSize: "pageSize",
            records: "total",
            rows: "rows"
        },
        altRows: true,
        multiselect: false,
        multiboxonly: true,
        loadComplete: function () {

        },
        sortable: true,
        sortname: 'endTime',
        sortorder: 'desc',
        autowidth: true,
        shrinkToFit: true,
        autoScroll: true
    });
    setToolsBarIcon(user_grid_selector, user_pager_selector);
    $("#user-grid-pager_left").css("width", "80");

    /** 查询按钮**/
    $("#querySystemBtn").on('click', function () {
        jQuery(user_grid_selector).jqGrid('setGridParam', {
            postData: {
                systemName: $("#query_systemName").val()
            },
            page: 1
        }).trigger("reloadGrid"); //重新载入 
    });


    /** 查询按钮**/
    $("#queryErp").on('click', function () {
    	debugger;
        var erpNumber = $("#erpNumber").val();
        if (erpNumber == '') {
        	swal({title:"帐号不能为空！"});
        }
        var systemCode = $("#systemCode").val();
        jQuery.ajax({
            type: "post",
            url: "sys/findUserByErp",
            data: "erp=" + erpNumber + "&systemCode=" + systemCode,
            success: function (data) {
                if (data.code == 1) {
                	swal({title:"用户授权成功！"});
                    jQuery(user_grid_selector).jqGrid('setGridParam', {
                        postData: {
                            systemCode: $("#systemCode").val()
                        },
                        page: 1
                    }).trigger("reloadGrid"); //重新载入
                } else {
                	swal({title:data.msg});
                }
            }
        });
    });


    /** 新增按钮**/
    $("#addSystemBtn").on('click', function () {
        $('#systemModal').load("system/gotoSystemEdit.do").modal();
    });
});

function deleteUserSystem(id) {
    swal({
        title: "您确定要删除这条用户授权信息吗",
        text: "删除后将无法恢复，请谨慎操作！",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        closeOnConfirm: false
    }, function () {
        jQuery.ajax({
            type: "post",
            url: "sys/deleteUserSystem",
            data: "id=" + id,
            success: function (data) {
                data = JSON.parse(data)
                if (data.success == true) {
                	swal({ title: "成功删除用户授权信息！" }); 
                    jQuery("#user-grid-table").jqGrid('setGridParam', {
                        postData: {
                            systemCode: $("#systemCode").val()
                        },
                        page: 1
                    }).trigger("reloadGrid"); //重新载入
                } else {
                	swal({ title: data.error }); 
                }
            }
        });
    });
}

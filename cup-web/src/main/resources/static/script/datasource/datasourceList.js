$(function () {
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";

    jQuery(grid_selector).jqGrid({
        url: "db/selectByConditions",
        datatype: "json",
        postData: {
            "obType": $("#obType").val(),
            "obStatus": $("#obStatus").val()
        },
        height: 350,
        colNames: ['id','系统编码','系统名称','数据源名称', '数据库名称', 'host'],
        colModel: [
            {name: 'id', index: 'id', width: 50, hidden: true},
            {name: 'sysCode', index: 'sysCode', width: 400 },
            {name: 'sysName', index: 'sysName', width: 400 },
            {name: 'code', index: 'code', width: 400},
            {name: 'name', index: 'name', width: 400},
            {name: 'host', index: 'host', width: 220}
        ],
        viewrecords: true,
        rownumbers: true,//添加左侧行号
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        jsonReader: {
            page: "page",
            total: "pages",
            pageSize: "pageSize",
            records: "total",
            rows: "rows"
        },
        altRows: true,
        multiselect: true,
        multiboxonly: true,
        loadComplete: function () {

        },
        sortable: true,
        sortname: 'endTime',
        sortorder: 'desc',
        autowidth: true,
        autoScroll: true
    });
    setToolsBarIcon(grid_selector, pager_selector);

    /** 查询按钮**/
    $("#queryDatasourceBtn").on('click', function () {
    	jQuery(grid_selector).jqGrid('setGridParam',{ 
            postData: {
                name:$("#query_name").val(),
                host:$("#query_host").val()
            },
            page:1 
        }).trigger("reloadGrid"); //重新载入 
    });  
    
    
    /** 新增按钮**/
    $("#addDatasourceBtn").on('click', function () {
        $('#datasourceModal').load("db/gotoDatasourceEdit").modal();
    });

    $("#updateDatasourceBtn").on('click', function () {
        var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
        if(id==''){
            bootbox.alert("请选择行");
            return;
        }if(id.length==0){
            bootbox.alert("请选择行");
            return;
        }else if(id.length>1){
            bootbox.alert("修改只能修改单行");
            return;
        }else{
            $('#datasourceModal').load("db/gotoDatasourceEdit",{"id":id[0]}).modal();
        }
    });


    /** 删除按钮**/
    $("#deleteDatasourceBtn").on('click', function () {
        var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
        if(id==''){
            bootbox.alert("请选择行");
        }else{
            bootbox.confirm("确定要删除吗?", function(result) {
                if(result) {

                    jQuery.ajax({
                        type: "post",
                        contentType: "application/json; charset=utf-8",
                        url: "db/deleteByIds",
                        data: "["+id+"]",
                        success: function(data){
                            data = JSON.parse(data)
                            if (data.success == true) {
                                $('#systemModal').modal("hide");
                                jQuery(grid_selector).jqGrid('setGridParam',{
                                    postData: {
                                        systemCode:$("#query_systemCode").val(),
                                        systemName:$("#query_systemName").val()
                                    },
                                    page:1
                                }).trigger("reloadGrid"); //重新载入
                            } else {
                                bootbox.alert(data.body);// 关闭当前对话框
                            }
                            $("#system_sumbit").removeAttr("disabled");
                        }
                    });
                }
            });
        }

    });




       $("#query_name").keypress(function(e) {
        // 回车键事件
           if(e.which == 13) {
                jQuery("#queryDatasourceBtn").click();
           }
        });

        $("#query_host").keypress(function(e) {
            // 回车键事件
           if(e.which == 13) {
                jQuery("#queryDatasourceBtn").click();
           }
        });



});


$(function () {

    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";

    jQuery(grid_selector).jqGrid({
        url: "monitor/selectDetailByConditions",
        datatype: "json",
        postData: {
             sysCode:$("#query_sysCode").val(),
             type:$("#query_type").val()
        },
        height: 360,
        colNames: ['id','系统编码', '代理程序', '报警类型','报警详情','发生时间'],
        colModel: [
            {name: 'id', index: 'id', width: 50, hidden: true},
            {name: 'sysCode', index: 'sysCode', width: 200 },
            {name: 'agentCode', index: 'agentCode', width: 200 },
            {name: 'type', index: 'type', width: 250,formatter:function(v){
                if(v==0){
                    v='代理程序死亡';
                }else if(v==1){
                    v='慢SQL';
                }else if(v==2){
                     v='系统异常';
                }
                return v;
            }},
            {name: 'detail', index: 'detail', width: 1600},
            {name: 'createTime', index: 'createTime', width: 350,formatter:function(v){
                            if(v){
                                v = getFormatDateByLong(v, undefined);
                            }
                            return v;
                        }},
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
    $("#queryMoniConfBtn").on('click', function () {
    	jQuery(grid_selector).jqGrid('setGridParam',{ 
            postData: {
                sysCode:$("#query_sysCode").val(),
                type:$("#query_type").val()
            },
            page:1 
        }).trigger("reloadGrid"); //重新载入 
    });  

    /** 删除按钮**/
    $("#deleteMoniConfBtn").on('click', function () {
        var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
        if(id==''){
            swal({title:"请选择行"});
        }else{
			swal({
				title : "您确定要删除这条信息吗",
				text : "删除后将无法恢复，请谨慎操作！",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "删除",
				closeOnConfirm : false
			}, function() {
                jQuery.ajax({
                    type: "post",
                    contentType: "application/json; charset=utf-8",
                    url: "monitor/deleteDetailByIds",
                    data: "["+id+"]",
                    success: function(data){
                        data = JSON.parse(data)
                        swal({ title : "删除成功！" });
                        if (data.success == true) {
                            jQuery(grid_selector).jqGrid('setGridParam',{
                                postData: {
                                    systemCode:$("#query_sysCode").val(),
                                    systemName:$("#query_name").val()
                                },
                                page:1
                            }).trigger("reloadGrid"); //重新载入
                        } else {
                        	swal({title:data.error});
                        }
                    }
                });
			});
        }
    });


     $("#query_sysCode").keypress(function(e) {

                // 回车键事件
           if(e.which == 13) {
                jQuery("#queryMoniConfBtn").click();
           }
        });

    $("#query_type").keypress(function(e) {
            // 回车键事件
       if(e.which == 13) {
            jQuery("#queryMoniConfBtn").click();
       }
    });


});


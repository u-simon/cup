$(function () {

    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";

    jQuery(grid_selector).jqGrid({
        url: "monitor/selectByConditions",
        datatype: "json",
        postData: {
        },
        height: 350,
        colNames: ['id','系统编码','agent编码','agent名称','监控名称','监控邮件地址'],
        colModel: [
            {name: 'id', index: 'id', width: 50, hidden: true},
            {name: 'sysCode', index: 'sysCode', width: 400 },
            {name: 'agentCode', index: 'agentCode', width: 400 },
            {name: 'agentName', index: 'agentName', width: 400 },
            {name: 'name', index: 'name', width: 400 },
            {name: 'monitorEmail', index: 'monitorEmail', width: 400 }
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
                name:$("#query_name").val(),
                sysCode:$("#query_sysCode").val()
            },
            page:1 
        }).trigger("reloadGrid"); //重新载入 
    });  
    
    
    /** 新增按钮**/
    $("#addMoniConfBtn").on('click', function () {
        $('#moniConfModal').load("monitor/gotoMoniConfEdit").modal();
    });

    $("#updateMoniConfBtn").on('click', function () {
        var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
        if(id==''){
            swal({title:"请选择行"});
        }if(id.length==0){
            swal({title:"请选择行"});
        }else if(id.length>1){
            swal({title:"修改只能修改单行"});
        }else{
            $('#moniConfModal').load("monitor/gotoMoniConfEdit",{"id":id[0]}).modal();
        }
    });


    /** 删除按钮**/
    $("#deleteMoniConfBtn").on('click', function () {
        var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
        if(id==''){
            swal({title:"请选择行"});
        }else{
   		    swal({
		        title: "您确定要删除这条信息吗",
		        text: "删除后将无法恢复，请谨慎操作！",
		        type: "warning",
		        showCancelButton: true,
		        confirmButtonColor: "#DD6B55",
		        confirmButtonText: "删除",
		        closeOnConfirm: false
		    }, function () {
                jQuery.ajax({
                    type: "post",
                    contentType: "application/json; charset=utf-8",
                    url: "monitor/deleteByIds",
                    data: "["+id+"]",
                    success: function(data){
                        data = JSON.parse(data)
                        if (data.success == true) {
                        	swal({ title: "删除成功！" });
                            $('#moniConfModal').modal("hide");
                            jQuery(grid_selector).jqGrid('setGridParam',{
                                postData: {
                                    systemCode:$("#query_sysCode").val(),
                                    systemName:$("#query_name").val()
                                },
                                page:1
                            }).trigger("reloadGrid"); //重新载入
                        } else {
                        	swal({ title : data.error });
                        }
                        $("#moniConf_sumbit").removeAttr("disabled");
                    }
                });
		    });
        }
    });



      $("#query_sysCode").keypress(function(e) {
           if(e.which == 13) {
                jQuery("#queryMoniConfBtn").click();
           }
        });

        $("#query_name").keypress(function(e) {
           if(e.which == 13) {
                jQuery("#queryMoniConfBtn").click();
           }
        });
        doResize();
});


function doResize() {
	var ss = getPageSize();
//	$("#gridTable").jqGrid('setGridWidth', ss.WinW - 10).jqGrid('setGridHeight', ss.WinH - 100);
    $("#grid-table").jqGrid('setGridHeight', ss.WinH - 100);
}

function getPageSize() {
	//http://www.blabla.cn/js_kb/javascript_pagesize_windowsize_scrollbar.html
	var winW, winH;
	if (window.innerHeight) { // all except IE
		winW = window.innerWidth;
		winH = window.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) { // IE 6 Strict Mode
		winW = document.documentElement.clientWidth;
		winH = document.documentElement.clientHeight;
	} else if (document.body) { // other
		winW = document.body.clientWidth;
		winH = document.body.clientHeight;
	} // for small pages with total size less then the viewport
	return {
		WinW: winW,
		WinH: winH
	};
}

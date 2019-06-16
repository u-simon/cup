$(function () {
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";
    
	var winHeight = $(window).height();
	var gridHeight = winHeight - 255;
	var rowNum = Math.floor(gridHeight / 35);
	
    jQuery(grid_selector).jqGrid({
        url: "sys/selectByConditions",
        datatype: "json",
        caption:'',
        postData: {
        	systemCode:$("#query_systemCode").val(),
        	systemName:$("#query_systemName").val()
        },
        height: gridHeight,
        colNames: ['id', '系统编码', '系统名称', '备注'],
        colModel: [
            {name: 'id', index: 'id', width: 50, hidden: true},
            {name: 'sysCode', index: 'sysCode', width: 250},
            {name: 'name', index: 'name', width: 300},
            {name: 'description', index: 'description', width: 520}
        ],
        viewrecords: true,
        rownumbers: true,//添加左侧行号
        rowNum: rowNum,
        rowList: [rowNum, 50, 100],
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
       //shrinkToFit: false,
        autoScroll: true

    });
    setToolsBarIcon(grid_selector, pager_selector);
    
    
    /** 查询按钮**/
    $("#querySystemBtn").on('click', function () {
    	jQuery(grid_selector).jqGrid('setGridParam',{ 
            postData: {
            	code:$("#query_systemCode").val(),
            	name:$("#query_systemName").val()
            },
            page:1 
        }).trigger("reloadGrid"); //重新载入 
    });  
    
    
    /** 新增按钮**/
    $("#addSystemBtn").on('click', function () {
        $('#systemModal').load("sys/gotoSystemEdit").modal();
    });  
    
    /** 更新按钮**/
    $("#updateSystemBtn").on('click', function () {
    	var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
    	if(id==''){
    		swal({title:"请选择行"});
    		return;
    	}if(id.length==0){
            swal({title:"请选择行"});
            return;
        }else if(id.length>1){
            swal({title:"修改只能修改单行"});
            return;
        }else{
		 $('#systemModal').load("sys/gotoSystemEdit",{"id":id[0]}).modal();
    	}
    });
    
    /** 用户授权按钮**/
    $("#userListBtn").on('click', function () {
    	var id = $(grid_selector).jqGrid('getGridParam','selarrrow');
    	var systemCode = $(grid_selector).jqGrid('getCell',id,"sysCode");
    	if(id==''){
    		swal({title:"请选择行"});
    	}else{
    		$('#systemModal').load("sys/gotoUserList?systemCode="+systemCode).modal();
    	}
     }
    );
    
    /** 删除按钮**/
    $("#deleteSystemBtn").on('click', function () {
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
                    //application/json
                    contentType: "application/json; charset=utf-8",
        			url: "sys/deleteByIds",
        			data: "["+id+"]",
        			success: function(data){
                        data = JSON.parse(data)
                        if (data.success == true) {
                        	swal({ title: "删除成功！" }); 
        					$('#systemModal').modal("hide");
        					jQuery(grid_selector).jqGrid('setGridParam',{ 
        			            postData: {
        			            	systemCode:$("#query_systemCode").val(),
        			            	systemName:$("#query_systemName").val()
        			            },
        			            page:1 
        			        }).trigger("reloadGrid"); //重新载入 
        				} else {
        					bootbox.alert(data.error);// 关闭当前对话框
        				}
        				$("#system_sumbit").removeAttr("disabled");
        			}
        		});
		    });
    	}
    });

   /** 删除按钮**/
    $("#shutdown").on('click', function () {
    		jQuery.ajax({
                			type: "post",
                            //application/json
                            contentType: "application/json; charset=utf-8",
                			url: "shutdown",
                			success: function(data){
                               alert("success");
                			}
                		});

    });



 $("#query_systemCode").keypress(function(e) {
        // 回车键事件
       if(e.which == 13) {
            jQuery("#querySystemBtn").click();
       }
    });

    $("#query_type").keypress(function(e) {
        // 回车键事件
       if(e.which == 13) {
            jQuery("#querySystemBtn").click();
       }
    });

});


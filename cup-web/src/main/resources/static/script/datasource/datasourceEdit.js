$(function () {
	
	/** 表单提交* */
	$("#datasource_sumbit").on('click', (function() {
		$('#datasourceEdit_form').bootstrapValidator('validate');
		if ($('#datasourceEdit_form').data('bootstrapValidator').isValid()) {
			$("#datasource_sumbit").attr({"disabled":"disabled"});
			$('#datasourceEdit_form').ajaxSubmit({
				url : 'db/saveOrUpdate',
				data: {
                    sysName:$("#sysCode option:selected").text()
                },
				success : function(data) {
					data = JSON.parse(data)
					if (data.success == true) {
						$('#datasourceModal').modal("hide");
						jQuery("#grid-table").jqGrid('setGridParam',{
				            postData: {
				            },
				            page:1 
				        }).trigger("reloadGrid"); //重新载入 
					} else {
						bootbox.alert(data.body);// 关闭当前对话框
					}
					  $("#datasource_sumbit").removeAttr("disabled");
				}
			});
		}
		return false;
	}));
	 $('#datasourceEdit_form').bootstrapValidator({
	        fields: {

	            code: {
	                selector: '#code',
	                validators: {
	                    notEmpty: {}
	                }
	            },
	            name: {
	                selector: '#name',
	                validators: {
	                    notEmpty: {}
	                }
	            },
	            host: {
	                selector: '#host',
	                validators: {
	                    notEmpty: {}
	                }
	            }
	        }
	    });
	
	  //汉化日期控件
    $('.dateSelect').datetimepicker({
    	format: 'yyyy-mm-dd',
        weekStart: 1,  
        autoclose: true,  
        startView: 2,  
        minView: 2,  
        forceParse: false,  
        language: 'zh-CN' 
    });
    
    
    //汉化日期控件
    $('.timeSelect').datetimepicker({
    	format: 'hh:ii',
        weekStart: 2,  
        autoclose: true,  
        startView: 0,  
        minView: 4,  
        forceParse: false,  
        language: 'zh-CN' 
    });




});


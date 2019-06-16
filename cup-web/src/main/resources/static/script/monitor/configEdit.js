
var loadAgent = function(){
	var sv=$("#sysCode").children('option:selected').val();//这就是selected的值
	jQuery.ajax({
		type: "post",
		url: "agent/queryAgents",
        dataType: "json",
		data: {'sysCode':sv},
		success: function(data){
            loadLocalAgent(data);

		},
		error : function() {
			bootbox.alert("Error");// 关闭当前对话框
		}
	});
};



var loadLocalAgent = function(data){
	$("#agentCode").empty();
    var arry = data["body"];
    //jQuery("#agentCode").append("<option value='"-1"'>"+"请选择agent"+"</option>");
    jQuery("#agentCode").append("<option value='-1'>请选择agent</option>");


	for(var i =0; i <arry.length; i++){
		var row=arry[i];
		jQuery("#agentCode").append("<option value='"+row.code+"'>"+row.name+"</option>");


	}
};


$(function () {
	
	/** 表单提交* */
	$("#moniConf_sumbit").on('click', (function() {
		$('#moniConfEdit_form').bootstrapValidator('validate');
		if ($('#moniConfEdit_form').data('bootstrapValidator').isValid()) {
			$("#moniConf_sumbit").attr({"disabled":"disabled"});
			$('#moniConfEdit_form').ajaxSubmit({
				url : 'monitor/saveOrUpdate',
				data: {
                    agentName:$("#agentCode option:selected").text()
                },
				success : function(data) {

					debugger;
					data = JSON.parse(data)
					if (data.success == true) {
						$('#moniConfModal').modal("hide");
						jQuery("#grid-table").jqGrid('setGridParam',{
				            postData: {
				            },
				            page:1 
				        }).trigger("reloadGrid"); //重新载入 
					} else {
                        swal({title:data.error});
						/*bootbox.alert(data.error);// 关闭当前对话框*/
					}
					  $("#moniConf_sumbit").removeAttr("disabled");
				}
			});
		}
		return false;
	}));

	$('#sysCode').change(function(){
    		loadAgent();
    	});


	 $('#moniConfEdit_form').bootstrapValidator({
    	feedbackIcons: {
    		valid: 'glyphicon glyphicon-ok',
    		invalid: 'glyphicon glyphicon-remove',
    		validating: 'glyphicon glyphicon-refresh'
    	},
    	fields: {
    		sysCode: {
    			sysCode: '#sysCode',
    			validators: {
    				notEmpty: {}
    			}
    		},
    		agentCode: {
    			agentCode: '#agentCode',
    			validators: {
    				notEmpty: {}
    			}
    		},
            name: {
    			name: '#name',
    			validators: {
    				notEmpty: {}
    			}
    		},
           agentBeatRate: {
                agentBeatRate: '#agentBeatRate',
                validators: {
                    notEmpty: {},
                     integer: {
                           message: '只能为整数'
                       },
                       callback: {
                            message: '必须不小于5',
                            callback: function (value, validator, $field) {
                                if(value <5){
                                    return false;
                                }
                                return true;
                            }
                     }
                }
            },
            monitorEmail: {
                monitorEmail: '#monitorEmail',
    			validators: {
    				notEmpty: {}
    				/*李勇2018-8-23注销多地址校验正则*/
                    /*regexp: {
                        regexp: /^(\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*)(;(\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*))*$/,
                        message: '邮箱地址格式有误'
                    }*/
    			}
    		}
    	}
    });



});


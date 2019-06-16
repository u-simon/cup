$(function () {
	/** 表单提交* */
	$("#agent_sumbit").on('click', (function() {
		$('#agentEdit_form').bootstrapValidator('validate');
		if ($('#agentEdit_form').data('bootstrapValidator').isValid()) {
			$("#agent_sumbit").attr({"disabled":"disabled"});
			$('#agentEdit_form').ajaxSubmit({
				url : 'agent/saveOrUpdate',
				success : function(data) {
					data = JSON.parse(data)
					if (data.success == true) {
						//bootbox.alert(data.resultMsg);// 关闭当前对话框
						$('#agentModal').modal("hide");

						jQuery("#grid-table").jqGrid('setGridParam',{ 
				            postData: {
				            },
				            page:1 
				        }).trigger("reloadGrid"); //重新载入 
					} else {
						bootbox.alert(data.error);// 关闭当前对话框
					}
					  $("#agent_sumbit").removeAttr("disabled");
				}
			});
		}
		return false;
	}));

    $('#agentEdit_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            exeBeginHour: {
                exeBeginHour: '#exeBeginHour',
                validators: {
                    notEmpty: {},
                    integer: {
                        message: '只能为整数'
                    },
                    callback: {
                             message: '必须大于0且小于23',
                             callback: function (value, validator, $field) {
                                 if(value <=0|| value>23){
                                     return false;
                                 }
                                 return true;

                             }
                      }
                }
            },
            exeBeginMinute: {
                exeBeginMinute: '#exeBeginMinute',
                validators: {
                    notEmpty: {},
                    integer: {
                        message: '只能为整数'
                    },
                     callback: {
                              message: '必须不小于0且不大于60',
                              callback: function (value, validator, $field) {
                                  if(value <0|| value>60){
                                      return false;
                                  }
                                  return true;

                              }
                       }
                }
            },
            exeTimeStep: {
                    exeTimeStep: '#exeTimeStep',
                    validators: {
                        notEmpty: {},
                        integer: {
                            message: '只能为整数'
                        }
                        ///////////////
                        ,callback: {
                               message: '必须大于0且不大于1080',
                               callback: function (value, validator, $field) {
                                   if(value <=0|| value>1080){
                                       return false;
                                   }
                                   return true;

                               }
                        }
                        /////////////////
                    }
                }
            }
    });


//     //汉化日期控件
//        $('#timeSelect').datetimepicker({
//        	format: 'hh:ii',
//        	 changeMonth: false,   // 允许选择月份
//            weekStart: 2,
//            autoclose: true,
//            startView: 0,
//            minView: 4,
//            forceParse: false,
//            language: 'zh-CN'
//        });


//         $('#rollbackDate').datetimepicker({
//                	  showMonthAfterYear: true, // 月在年之后显示
//                      changeMonth: true,   // 允许选择月份
//                      changeYear: true,   // 允许选择年份
//                      minView: "month",//不会在跳到时分秒界面去选择
//                      language: 'zh-CN',
//                      autoclose:true,//选择日期后自动关闭
//                      format: "yyyy-mm-dd"//选择日期后，文本框显示的日期格式
//                });

});


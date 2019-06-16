$('#deadline-dp .clockpicker').clockpicker({
	afterDone : function() {
		var val = $('#deadline').val();
		$('#deadline').val(val + ":00");
	}
});

var cronInput = $("#cronTrigger")
var jqcron = cronInput.jqcron({
    disabled: false,
    value: cronInput.attr("value"),
});

// 设置目标分组可选项
setDbMasterItems();
// 设置Agent可选项
$("#agentCode").children("option").each(function() {
	var selSysCode = $("#sysCode").find("option:selected").val();
	if (selSysCode == this.attributes['syscode'].value) {
		this.style.display=''
	} else {
		this.style.display='none';
	}
});

// 按照系统过滤代理及目标分组
$('#sysCode').on('change', function() {
	var selSysCode = $(this).find("option:selected").val();

    $("#agentCode").children("option").each(function() {
    	if (selSysCode == this.attributes['syscode'].value) {
    		this.style.display=''
    	} else {
    		this.style.display='none';
    	}
    });
	$("#agentCode").prop("selectedIndex", -1);
	
	setDbMasterItems();
	$("#dbMaster").prop("selectedIndex", 0);
});

//按照系统过滤代理及目标分组
$('#agentCode').on('change', function() {
	setDbMasterItems();
});


$('#dbMaster').on('click', function() {
	setDbMasterItems();
});

function setDbMasterItems() {
	var selSysCode = $("#sysCode").find("option:selected").val();
	var selAgentCode = $("#agentCode").find("option:selected").val();
	
    $("#dbMaster").children("option").each(function() {
    	var curSysCode = this.attributes['sysCode'].value;
    	var curAgentCode = this.attributes['agentCode'].value;
    	if (curSysCode == "NULL"
    		|| (selSysCode == curSysCode && selAgentCode == curAgentCode)) {
    		this.style.display=''
    	} else {
    		this.style.display='none';
    	}
    });
}


$(function() {
	var isFromCopy = false;
	/** 表单提交**/
	$("#task_sumbit").on('click', (function() {
		$('#taskEdit_form').bootstrapValidator('validate');
		if ($('#taskEdit_form').data('bootstrapValidator').isValid()) {
			$("#task_sumbit").attr({
				"disabled" : "disabled"
			});

			$('#taskEdit_form').ajaxSubmit({
				url : 'associateJob/saveOrUpdate',
				success : function(data) {
					debugger;
					data = JSON.parse(data)
					if (data.success == true) {
                        //val fromCopy = $("#taskEdit_form #id").val();
						// 如果不是复制的单据，需关闭
                        $('#taskEdit_form').data('bootstrapValidator').resetForm();
						if (!isFromCopy) {
                            $('#taskModal').modal("hide");
						}
                        isFromCopy = false;
                        jQuery("#grid-table").jqGrid('setGridParam', {
                            postData : {},
                            page : 1
                        }).trigger("reloadGrid"); // 重新载入
					} else {
						swal({
							type: 'error',
							title: "保存失败",
							text: data.error.substr(0, 500),
							html: true
						});
					}
					$("#task_sumbit").removeAttr("disabled");
				}
			});
		}
		return false;
	}));

    /** 表单复制 **/
    $("#task_copy").on('click', (function() {
        isFromCopy = true;
    	// 清空ID
        $("#taskEdit_form #id").val('');
        // 清空任务名称
        $("#taskEdit_form #name").val('');
        // 清空结转表名
        $("#taskEdit_form #tbName").val('');
    }));


    $('#taskEdit_form').bootstrapValidator({
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			sysCode : {
				sysCode : '#sysCode',
				validators : {
					notEmpty : {}
				}
			},
			name : {
				name : '#name',
				validators : {
					notEmpty : {
						message : '任务名称不能为空'
					}
				}
			},

			type : {
				type : '#type',
				validators : {
					notEmpty : {
						message : '结转类型不能为空'
					}
				}
			},
			tbName : {
				tbName : '#tbName',
				validators : {
					notEmpty : {
						message : '结转表名不能为空'
					}
				}
			},
			tsColumn : {
				tsColumn : '#tsColumn',
				validators : {
					notEmpty : {
						message : '时间字段不能为空'
					}
				}
			},
			safeDays : {
				safeDays : '#safeDays',
				validators : {
					notEmpty : {
						message : '安全天数不能为空'
					},
					integer : {
						message : '只能为整数'
					},
					callback : {
						message : '不能小于-1',
						callback : function(value, validator,
								$field) {
							if (value < -1) {
								return false;
							}
							return true;
						}
					}
				}
			},
			deadline : {
				deadline : '#deadline',
				validators : {
					regexp : {
						regexp : /^(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$/,
						message : '时间格式不正确'
					},
					notEmpty : {
						message : '终止时间不能为空'
					}
				}
			},
			agentCode : {
				agentCode : '#agentCode',
				validators : {
					notEmpty : {
						message : '代理编码 不能为空'
					}
				}
			}
		}
	});

});

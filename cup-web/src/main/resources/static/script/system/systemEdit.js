$(function () {
	/** 表单提交* */
	$("#system_sumbit").on('click', (function() {
		$('#systemEdit_form').bootstrapValidator('validate');
		if ($('#systemEdit_form').data('bootstrapValidator').isValid()) {
			$("#system_sumbit").attr({"disabled":"disabled"});
			$('#systemEdit_form').ajaxSubmit({
				url : 'sys/saveOrUpdate',
				success : function(data) {
					data = JSON.parse(data)
					if (data.success == true) {
						//bootbox.alert(data.resultMsg);// 关闭当前对话框
						$('#systemModal').modal("hide");

						jQuery("#grid-table").jqGrid('setGridParam',{ 
				            postData: {
				            },
				            page:1 
				        }).trigger("reloadGrid"); //重新载入 
					} else {
						bootbox.alert(data.error);// 关闭当前对话框
					}
					  $("#system_sumbit").removeAttr("disabled");
				}
			});
		}
		return false;
	}));


 $('#systemEdit_form').bootstrapValidator({
	feedbackIcons: {
		valid: 'glyphicon glyphicon-ok',
		invalid: 'glyphicon glyphicon-remove',
		validating: 'glyphicon glyphicon-refresh'
	},
	fields: {
		systemName: {
			selector: '#systemName',
			validators: {
				notEmpty: {}
			}
		},
		systemCode: {
			systemCode: '#systemCode',
			validators: {
				notEmpty: {}
			}
		}
	}
});

});


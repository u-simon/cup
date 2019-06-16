$(function() {
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";

	var grid_selector_detail = "#grid-table_detail";
	var pager_selector_detail = "#grid-pager_detail";
	
	var winHeight = $(window).height();
	var gridHeight = winHeight - 295;
	var rowNum = Math.floor(gridHeight / 35);
	
	jQuery(grid_selector).jqGrid({
		url : "job/selectByConditions",
		datatype : "json",
		mtype : 'POST',
		postData : {
			"name" : $("#query_name").val(),
			"status" : $("#query_status").val(),
			"isRevert": false
		},
		height : gridHeight,
		// sys_code name del_db_code sel_db_code ins_db_code type
		// tb_name exe_condition deadline status agent_code
		colNames : [ 'id', '任务名称', '类型', '表名', '时间字段',
				'安全天数', '执行条件', '触发规则', '终止时间', '状态', '目标分组', '系统编码', '代理程序' ],
		colModel : [ {
			name : 'id',
			index : 'id',
			width : 50,
			hidden : true
		},{
			name : 'name',
			index : 'name',
			width : 120
		}, {
			name : 'type',
			index : 'type',
			width : 45,
			formatter : function(v) {
				if (v == 0) {
					v = '结取';
				} else if (v == 1) {
					v = '结转';
				}
				return v;
			}
		}, {
			name : 'tbName',
			index : 'tbName',
			width : 140
		}, {
			name : 'tsColumn',
			index : 'tsColumn',
			width : 140
		}, {
			name : 'safeDays',
			index : 'safeDays',
			width : 65
		}, {
			name : 'exeCondition',
			index : 'exeCondition',
			width : 150
		}, {
			name : 'cronTrigger',
			index : 'cronTrigger',
			width : 75
		}, {
			name : 'deadline',
			index : 'deadline',
			width : 70
		}, {
			name : 'status',
			index : 'status',
			width : 50,
			formatter : function(v) {
				if (v == 0) {
					v = '待运行';
				} else if (v == 1) {
					v = '运行中';
				} else if (v == 2) {
					v = '运行超时';
				} else if (v == -1) {
					v = '异常';
				} else if (v == -2) {
					v = '停止';
				}
				return v;
			}
		}, {
			name : 'dbMaster',
			index : 'dbMaster',
			width : 170
		}
		,  {
			name : 'sysCode',
			index : 'sysCode',
			width : 70
		}, {
			name : 'agentCode',
			index : 'agentCode',
			width : 100
		} ],
		viewrecords : true,
		rownumbers : true,// 添加左侧行号
		rowNum : rowNum,
		rowList : [ rowNum, 50, 100 ],
		pager : pager_selector,
		jsonReader : {
			page : "page",
			total : "pages",
			pageSize : "pageSize",
			records : "total",
			rows : "rows"
		},
		altRows : true,
		multiselect : true,
		multiboxonly : true,
		loadComplete : function() {

		},
		sortable : true,
		sortname : 'endTime',
		sortorder : 'desc',
		autowidth : true,
		shrinkToFit : false,
		autoScroll : true
	});

	$("#grid-table_cb").css({
		"padding-top" : "6px",
		"padding-bottom" : "6px"
	});

	$(grid_selector).closest(".ui-jqgrid-bdiv").css({
		'overflow-x' : 'scroll'
	});
	
	var preJobId = "-1";
	$('#jobTab a').click(function (e) {
		var target = event.target.id;
		if (target == "tab-jobd") {
			var curJobId = $(grid_selector).jqGrid('getGridParam', 'selrow');
			if (curJobId == null) {
				$(grid_selector_detail).clearGridData();   
				preJobId = -1;
				return;
			} else if (preJobId != curJobId) {
			    var width = $('#table_ct').width();
			    $(grid_selector_detail).setGridWidth(width);
			    
				preJobId = curJobId;
				queryJobDetail();
			}
		}
	})
		
	jQuery(grid_selector_detail).jqGrid({
		datatype : "json",
		height : gridHeight,
		colNames : [ 'id', '系统编码', '代理程序', '表名', '分组', '启动时间', '结束时间', '已结转量'],
		colModel : [ {
			name : 'id',
			width : 50,
			hidden : true
		}, {
			name : 'sysCode',
			width : 80
		}, {
			name : 'agentCode',
			width : 150
		}, {
			name : 'tbName',
			width : 150
		}, {
			name : 'dbMaster',
			width : 300
		}, {
			name : 'startTime',
			width : 150
		}, {
			name : 'stopTime',
			width : 150
		}, {
			name : 'archivedQty',
			width : 80
		}],
		viewrecords : true,
		rownumbers : true,// 添加左侧行号
		rowNum : rowNum,
		rowList : [ rowNum, 50, 100 ],
		pager : pager_selector_detail,
		jsonReader : {
			page : "page",
			total : "pages",
			pageSize : "pageSize",
			records : "total",
			rows : "rows"
		},

		sortable : true,
		sortname : 'archiveTime',
		sortorder : 'desc',
		autowidth: true,   
		autoScroll : true
	});

	$("#grid-table_detail_cb").css({
		"padding-top" : "6px",
		"padding-bottom" : "6px"
	});
		
	/** 查询按钮 **/
	$("#queryTaskBtn_detail").on('click', function() {
		queryJobDetail();
	});

	/** 立即执行按钮 **/
	$("#startAtOnceBtn").on('click', function() {
		var sids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (sids == '') {
			swal({
				title : "请选择需要立即执行的任务！"
			});
			return;
		}
		
		if (sids.length > 1) {
			swal({
				title : "只能选择单行执行！"
			});
			return;
		}	
		
		var selectedId = sids[0];
		var rowData = $(grid_selector).jqGrid('getRowData', selectedId);
		swal({
			title : "您确定要立即执行该任务吗?",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "立即执行",
			cancelButtonText : "取消",
			closeOnConfirm : false
		}, function() {
			jQuery.ajax({
				type: "post",
				dataType: "json",
				url: "job/runJob",
				data:{
					"sysCode": rowData["sysCode"],
					"agentCode": rowData["agentCode"],
					"tbName": rowData["tbName"],
					"dbMaster": rowData["dbMaster"]
				},
				success : function(data) {
					if (data.success == true) {
						swal({
							title : "任务启动成功！"
						});
						$('#taskModal').modal("hide");
						jQuery(grid_selector).jqGrid('setGridParam', {
							postData : {
								systemCode : $("#query_name").val(),
								systemName : $("#query_status").val()
							},
							page : 1
						}).trigger("reloadGrid"); // 重新载入
					} else {
						swal({
							type: 'error',
							title: "启动任务失败",
							text: data.error.substr(0, 500),
							html: true
						});
					}
					$("#startAtOnceBtn").removeAttr("disabled");
				}
			});
		});
	});
	
	var queryJobDetail = function() {
		var selectedId = $(grid_selector).jqGrid('getGridParam', 'selrow');
		if (selectedId == null) {
			swal({
				title : "请在任务信息选择任务！"
			});
			return;
		}
		var rowData = $(grid_selector).jqGrid('getRowData', selectedId);
		// var id = [0];
		jQuery("#grid-table_detail").jqGrid('setGridParam', {
			url : "job/selectDetailByMids",
			postData : {
				"sysCode" : rowData["sysCode"],
				"agentCode" : rowData["agentCode"],
				"tbName" : rowData["tbName"],
				"dbMaster" : rowData["dbMaster"],
				"isRevert" : false,
				"archiveTime" : $("#archiveDate").val()
			},
			page : 1
		}).trigger("reloadGrid"); // 重新载入
	}
	
	/** 查询按钮* */
	$("#queryTaskBtn").on('click', function() {
		jQuery(grid_selector).jqGrid('setGridParam', {
			postData : {
				"name" : $("#query_name").val(),
				"status" : $("#query_status").val()
			},
			page : 1
		}).trigger("reloadGrid"); // 重新载入
	});

	/** 新增按钮* */
	$("#addTaskBtn").on('click', function() {
		$('#taskModal').load("job/gotoTaskEdit").modal();
	});

    /** 导入按钮* */
    $("#importTaskBtn").on('click', function() {
        $('#importModel').load("job/gotoImportEdit").modal();
    });

	/** 更新按钮* */
	$("#updateTaskBtn").on('click', function() {
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (id == '') {
			swal({
				title : "请选择行"
			});
		} else if (id.length > 1) {
			swal({
				title : "修改只能修改单行"
			});
			return;
		} else {
			$('#taskModal').load("job/gotoTaskEdit?id=" + id).modal();
		}
	});


	/** 删除按钮* */
	$("#deleteTaskBtn").on('click', function() {
		var sids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (sids == '') {
			swal({
				title : "请选择需要删除的任务！"
			});
			return;
		}
		
		if (sids.length > 1) {
			swal({
				title : "只能选择单行删除！"
			});
			return;
		}
		
		var param = {
			"id" : sids[0]
		};

		swal({
			title : "您确定要删除该任务吗?",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			cancelButtonText : "取消",
			closeOnConfirm : false
		}, function() {
			jQuery.ajax({
				type : "post",
				dataType : "json",
				url : "job/deleteById",
				data : param,
				success : function(data) {
					if (data.success == true) {
						swal({
							title : "成功删除任务！"
						});
						$('#taskModal').modal("hide");
						jQuery(grid_selector).jqGrid('setGridParam', {
							postData : {
								systemCode : $("#query_name").val(),
								systemName : $("#query_status").val()
							},
							page : 1
						}).trigger("reloadGrid"); // 重新载入
					} else {
						swal({
							title : data.error
						});
					}
					$("#deleteTaskBtn").removeAttr("disabled");
				}
			});
		});
	});

	
	/** 启用按钮* */
	$("#startTaskBtn").on('click', function() {
		debugger;
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		var param = {
			"id" : id[0],
			"status" : 0
		};
		if (id == '') {
			swal({
				title : "请选择行"
			});
		} else {
			swal({
				title : "您确定要启用该任务吗?",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "启动",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				jQuery.ajax({
					type : "post",
					dataType : "json",
					url : "job/modifyStatusById",
					data : param,
					success : function(data) {
						if (data.success == true) {
							swal({
								title : "任务启动成功！"
							});
							$('#taskModal').modal("hide");
							jQuery(grid_selector).jqGrid('setGridParam', {
								postData : {
									systemCode : $("#query_name").val(),
									systemName : $("#query_status").val()
								},
								page : 1
							}).trigger("reloadGrid"); // 重新载入
						} else {
							swal({
								title : data.error
							});
						}
						$("#startTaskBtn").removeAttr("disabled");
					}
				});
			});
		}
	});

	/** 停止任务按钮* */
	$("#stopTaskBtn").on('click', function() {
		debugger;
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (id == '') {
			swal({
				title : "请选择行"
			});
		} else {
			swal({
				title : "您确定要停止该任务吗?",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "停止",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				var param = {
					"id" : id[0],
					"status" : -2
				};
				jQuery.ajax({
					type : "post",
					dataType : "json",
					url : "job/modifyStatusById",
					data : param,
					success : function(data) {
						// alert(data)
						// data = JSON.parse(data)
						if (data.success == true) {
							swal({
								title : "任务已经停止！"
							});
							$('#taskModal').modal("hide");
							jQuery(grid_selector).jqGrid('setGridParam', {
								postData : {
									systemCode : $("#query_name").val(),
									systemName : $("#query_status").val()
								},
								page : 1
							}).trigger("reloadGrid"); // 重新载入
						} else {
							swal({
								title : data.error
							});
						}
						$("#stopTaskBtn").removeAttr("disabled");
					}
				});
			});
		}
	});


	laydate({
		elem : "#archiveDate",
		event : "focus"
	});

});

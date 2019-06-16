$(function() {
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	
	var winHeight = $(window).height();
	var gridHeight = winHeight - 255;
	var rowNum = Math.floor(gridHeight / 35);
	
	jQuery(grid_selector).jqGrid(
			{
				url : "agent/selectByConditions",
				datatype : "json",
				postData : {
					code : $("#query_code").val(),
					name : $("#query_name").val()
				},
				height : gridHeight,
				colNames : [ 'id', '系统编码', '代理编码', '代理名称', '主机', '端口', '状态',
						'心跳次数', '更新时间', '版本', '生产库-主', '生产库-从', '历史库' ],
				colModel : [ {
					name : 'id',
					index : 'id',
					width : 50,
					hidden : true
				}, {
					name : 'sysCode',
					index : 'sysCode',
					width : 80
				}, {
					name : 'code',
					index : 'code',
					width : 80
				}, {
					name : 'name',
					index : 'name',
					width : 80
				}, {
					name : 'host',
					index : 'host',
					width : 100
				}, {
					name : 'port',
					index : 'port',
					width : 45
				}, {
					name : 'status',
					index : 'status',
					width : 60,
					formatter : function(v) {
						if (v == 0) {
							v = "关闭";
						} else if (v == 1) {
							v = "启用";
						}
						return v;
					}
				}, {
					name : 'beatCount',
					index : 'beatCount',
					width : 70
				}, {
					name : 'updateTime',
					index : 'updateTime',
					width : 130,
					formatter : function(v) {
						if (v) {
							v = getFormatDateByLong(v, undefined);
						}
						return v;
					}
				}, {
					name : 'version',
					index : 'version',
					width : 70
				}, {
					name : 'dbMaster',
					index : 'dbMaster',
					width : 320
				}, {
					name : 'dbSlave',
					index : 'dbSlave',
					width : 320
				}, {
					name : 'dbHistory',
					index : 'dbHistory',
					width : 320
				},

				],
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

	$(grid_selector).closest(".ui-jqgrid-bdiv").css({
		'overflow-x' : 'scroll'
	});

	setToolsBarIcon(grid_selector, pager_selector);

	/** 查询按钮* */
	$("#queryAgentBtn").on('click', function() {
		jQuery(grid_selector).jqGrid('setGridParam', {
			postData : {
				code : $("#query_code").val(),
				name : $("#query_name").val()
			},
			page : 1
		}).trigger("reloadGrid"); // 重新载入
	});

	/** 删除按钮* */
	$("#deleteAgentBtn").on('click', function() {
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (id == '') {
			swal({
				title : "请选择行"
			});
		} else {
			var rowDatas = $(grid_selector).jqGrid('getRowData', id);
			var status = rowDatas["status"];
			if (status != '关闭') {
				swal({
					title : "禁止删除非关闭状态agent"
				});
				return;
			}

			swal({
				title : "您确定要删除这条信息吗",
				text : "删除后将无法恢复，请谨慎操作！",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "删除",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				jQuery.ajax({
					type : "post",
					contentType : "application/json; charset=utf-8",
					url : "agent/deleteByIds",
					data : "[" + id + "]",
					success : function(data) {
						data = JSON.parse(data)
						if (data.success == true) {
							swal({
								title : "成功删除代理程序！"
							});
							jQuery(grid_selector).jqGrid('setGridParam', {
								postData : {
									code : $("#query_code").val(),
									name : $("#query_name").val()
								},
								page : 1
							}).trigger("reloadGrid"); // 重新载入
						} else {
							swal({
								title : data.error
							});
						}
						$("#agent_sumbit").removeAttr("disabled");
					}
				});
			});
		}

	});

	/** 启动按钮* */
	$("#startAgentBtn").on('click', function() {
		// var id =
		// $(grid_selector).jqGrid('getGridParam','selarrrow');//多行使用selarrrow
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');

		if (id == '') {
			swal({
				title : "请选择行"
			});
			return;
		}
		if (id.length > 1) {
			swal({
				title : "请选择单行"
			});
			return;
		} else {
			var rowDatas = $(grid_selector).jqGrid('getRowData', id);
			var status = rowDatas["status"];
			if (status == '启用') {
				swal({
					title : "禁止重复启动"
				});
				return;
			}
			swal({
				title : "您确定要启动该代理程序吗？",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "启动",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				var rowDatas = $(grid_selector).jqGrid('getRowData', id);
				var host = rowDatas["host"];
				var port = rowDatas["port"];
				var sysCode = rowDatas["sysCode"];
				var code = rowDatas["code"];

				var exeBeginHour = rowDatas["exeBeginHour"];
				var exeBeginMinute = rowDatas["exeBeginMinute"];
				var exeTimeStep = rowDatas["exeTimeStep"];

				var param = {
					"host" : host,
					"port" : port,
					"sysCode" : sysCode,
					"code" : code,
				};
				jQuery.ajax({
					type : "post",
					dataType : "json",
					url : "agent/start",
					data : param,
					success : function(data) {
						if (data.success == true) {
							swal({
								title : "成功启动代理程序！"
							});
							jQuery(grid_selector).jqGrid('setGridParam', {
								postData : {
									code : $("#query_code").val(),
									name : $("#query_name").val()
								},
								page : 1
							}).trigger("reloadGrid"); // 重新载入
						} else {
							swal({
								title : data.error
							});
						}
						$("#agent_sumbit").removeAttr("disabled");
					}
				});
			});
		}

	});

	/** 停止按钮* */
	$("#stopAgentBtn").on('click', function() {
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (id == '' || id == null || id == undefined) {
			swal({
				title : "请选择行"
			});
			return;
		}
		if (id.length > 1) {
			swal({
				title : "请选择单行"
			});
			return;
		}
		var rowDatas = $(grid_selector).jqGrid('getRowData', id);
		var status = rowDatas["status"];
		if (status == '关闭') {
			swal({
				title : "禁止重复关闭"
			});
			return;
		}
		swal({
			title : "您确定要停止该代理程序吗？",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "停止",
			cancelButtonText : "取消",
			closeOnConfirm : false
		}, function() {
			var rowDatas = $(grid_selector).jqGrid('getRowData', id);
			var host = rowDatas["host"];
			var port = rowDatas["port"];
			var sysCode = rowDatas["sysCode"];
			var code = rowDatas["code"];
			var param = {
				"host" : host,
				"port" : port,
				"sysCode" : sysCode,
				"code" : code
			};
			jQuery.ajax({
				type : "post",
				dataType : "json",
				url : "agent/stop",
				data : param,
				success : function(data) {
					if (data.success == true) {
						swal({
							title : "成功停止该代理程序！"
						});
						jQuery(grid_selector).jqGrid('setGridParam', {
							postData : {
								code : $("#query_code").val(),
								name : $("#query_name").val()
							},
							page : 1
						}).trigger("reloadGrid"); // 重新载入
					} else {
						swal({
							title : data.error
						});
					}
					$("#agent_sumbit").removeAttr("disabled");
				}
			});
		});
	});

	/** 升级按钮* */
	$("#upgradeAgentBtn").on('click', function() {
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (id == '') {
			swal({
				title : "请选择行"
			});
		} else {
			var rowDatas = $(grid_selector).jqGrid('getRowData', id);
			var status = rowDatas["status"];
			if (status != '关闭') {
				swal({
					title : "禁止升级非关闭状态agent"
				});
				return;
			}

			swal({
				title : "您确定要升级代理程序吗？",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "升级",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				var host = rowDatas["host"];
				var port = rowDatas["port"];
				var sysCode = rowDatas["sysCode"];
				var code = rowDatas["code"];
				var param = {
					"host" : host,
					"port" : port,
					"sysCode" : sysCode,
					"code" : code
				};
				
				jQuery.ajax({
					type : "post",
					dataType : "json",
					url : "agent/upgrade",
					data : param,
					success : function(data) {
						if (data.success == true) {
							swal({
								title : "成功启动升级程序，请稍后查看心跳次数及版本检验是否成功！"
							});
							jQuery(grid_selector).jqGrid('setGridParam', {
								postData : {
									code : $("#query_code").val(),
									name : $("#query_name").val()
								},
								page : 1
							}).trigger("reloadGrid"); // 重新载入
						} else {
							swal({
								title : data.error
							});
						}
						$("#agent_sumbit").removeAttr("disabled");
					}
				});
			});
		}

	});

	/** 回退按钮* */
	$("#rollbackAgentBtn").on('click', function() {
		var id = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		if (id == '') {
			swal({
				title : "请选择行"
			});
		} else {
			var rowDatas = $(grid_selector).jqGrid('getRowData', id);
			var status = rowDatas["status"];
			if (status != '关闭') {
				swal({
					title : "禁止回退非关闭状态agent"
				});
				return;
			}

			swal({
				title : "您确定要回退上个版本的代理程序吗？",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "回退",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function() {
				var host = rowDatas["host"];
				var port = rowDatas["port"];
				var sysCode = rowDatas["sysCode"];
				var code = rowDatas["code"];
				var param = {
					"host" : host,
					"port" : port,
					"sysCode" : sysCode,
					"code" : code
				};
				
				jQuery.ajax({
					type : "post",
					dataType : "json",
					url : "agent/rollback",
					data : param,
					success : function(data) {
						if (data.success == true) {
							swal({
								title : "成功启动回退程序，请稍后查看心跳次数及版本检验是否成功！"
							});
							jQuery(grid_selector).jqGrid('setGridParam', {
								postData : {
									code : $("#query_code").val(),
									name : $("#query_name").val()
								},
								page : 1
							}).trigger("reloadGrid"); // 重新载入
						} else {
							swal({
								title : data.error
							});
						}
						$("#agent_sumbit").removeAttr("disabled");
					}
				});
			});
		}

	});
});

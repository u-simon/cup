$(function() {
	var agent_num = $("#agents").val();

	$(".agent-ibox").each(function() {
		var syscode = $(this).find("#sys_name").attr('code');
		var agentcode = $(this).find("#agent_name").attr('code');
		var myChart = echarts.init(document.getElementById(agentcode), 'macarons');
		
		jQuery.ajax({
			"type" : "post",
			"url" : "statAgentArchivedQty",
			"data" : {
				sysCode : syscode,
				agentCode : agentcode
			},
			"dataType" : "json",
			"success" : function(result) {
				if (result) {
					// 指定图表的配置项和数据
					var option1 = {
						toolbox: {
							show: true,
							height: '0px'
						},
						grid : {
							top: 20,
							left: 70,
							right: 30,
							z:200
						},
						tooltip : {
							trigger : 'axis'
						},
						xAxis : {
							type : 'category',
							boundaryGap : false,
							data : result.xs
						},
						yAxis : {
							type : 'value',
							axisLabel : {
								formatter : '{value}'
							}
						},
						series : [ {
							name : '',
							type : 'line',
							data : result.ys
						}]
						
					};
					myChart.clear();
					myChart.hideLoading();
					myChart.setOption(option1);
				}
			},
			"error" : function() {
				alert("未发现");// 关闭当前对话框
			}
		});
		
	});
	 
	debugger;
	var q = 1;
	while (q <= agent_num) {
		$($("#" + q)).change( function() {
			var tbname = this.value;
			var agentcode = $(this).attr("agentCode");
			var syscode = $(this).attr("sysCode");
			var myChart1 = echarts.init(document.getElementById(agentcode), 'macarons');

			jQuery.ajax({
				"type" : "post",
				"url" : "statTableArchivedQty",
				"data" : {
					sysCode : syscode,
					agentCode : agentcode,
					tbName : tbname
				},
				"dataType" : "json",
				"success" : function(result) {
					if (result) {
						// 指定图表的配置项和数据
						var option1 = {
							toolbox: {
								show: true,
								height: '0px'
							},
							grid : {
								top: 20,
								left: 70,
								right: 30,
								z:200
							},
							tooltip : {
								trigger : 'axis'
							},
							xAxis : {
								type : 'category',
								boundaryGap : false,
								data : result.xs
							},
							yAxis : {
								type : 'value',
								axisLabel : {
									formatter : '{value}'
								}
							},
							series : [ {
								name : '',
								type : 'line',
								data : result.ys
							}]
							
						};
						myChart1.clear();
						myChart1.hideLoading();
						myChart1.setOption(option1);
					}
				},
				"error" : function() {
					alert("未发现");// 关闭当前对话框
				}
			});
		});
		q++;
	}
});
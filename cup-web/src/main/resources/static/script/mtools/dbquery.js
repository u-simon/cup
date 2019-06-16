var myTextarea = document.getElementById('editor');
var CodeMirrorEditor = CodeMirror.fromTextArea(myTextarea, {
    mode: "text/x-mysql",
    theme: "eclipse",
    lineNumbers: true
});

var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";

var winHeight = $(window).height();

$(function () {
    $('#dbqueryBody').on('keydown', function (evt) {
    	if (evt.which == 116) {
            doQuery();
            return false;
        }
    });
});

/** 查询按钮* */
$("#queryBtn").on('click', function() {
	doQuery();
});

/** 查询按钮* */
$("#runBtn").on('click', function() {
	doRun();
});

function doQuery() {
    $(grid_selector).jqGrid("clearGridData");
    $.jgrid.gridUnload("grid-table");
    
	var sql =  CodeMirrorEditor.getValue();
	if (sql == null || sql.length <= 0) {
		return;
	}
	
	var cmdHeight = $("#cmd-area").height();
	var gridHeight = winHeight - cmdHeight - 260;
	var rowNum = Math.floor(gridHeight / 35);
	$.post("/db/query",{sql:sql}, function(result) {
		if (result == null || result.length <= 0) {
			return;
		}
		var record = result[0];
		var colModel = [];
		var culNames = [];
		for(var key in record) {  
	        colModel.push({'name':key, 'width': 80,});
	        culNames.push(key);
	    }  
		jQuery(grid_selector).jqGrid({
			'colModel': colModel,
			colNames: culNames,
			data: result,
			datatype: "local",
			height : gridHeight,
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

	    console.log(result);
	});
}

function doRun() {
    $(grid_selector).jqGrid("clearGridData");
    $.jgrid.gridUnload("grid-table");
    
	var sql =  CodeMirrorEditor.getValue();
	if (sql == null || sql.length <= 0) {
		return;
	}
	
	var cmdHeight = $("#cmd-area").height();
	var gridHeight = winHeight - cmdHeight - 260;
	var rowNum = Math.floor(gridHeight / 35);
	
	swal({
		title : "您确定要执行这条SQL语句吗？",
		text : "执行后将无法恢复，请谨慎操作！",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#DD6B55",
		confirmButtonText : "执行",
		cancelButtonText : "取消",
		closeOnConfirm : false
	}, function() {
		$.post("/db/run",{sql:sql}, function(result) {
			data = JSON.parse(result)
			if (data.success == true) {
				swal({
					title : "成功执行！"
				});
			} else {
				swal({
					title : "执行失败",
					text: data.error
				});
			}

		    console.log(result);
		});
	});
}


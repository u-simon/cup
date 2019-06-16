/**
 * 
 */

initFileInput();

function initFileInput() {    
    var control = $('#input-agent'); 
    control.on('fileuploaded', function(event, data, previewId, index) {
        var response = data.response;
        if (response.success == true) {
			swal({
				title : "上传文件成功！"
			});
        } else {
			swal({
				title : "上传文件失败！"
			});
        }
        $('#file-upload .kv-upload-progress').remove();
        jQuery("#grid-table").jqGrid().trigger("reloadGrid"); //重新载入
    });
}

$(function () {
    jQuery("#grid-table").jqGrid({
        url: "files/loadAgents",
        datatype: "json",
        height: 370,
        colNames: ['id', '文件名', '文件大小', '上传时间', '操作'],
        colModel: [
            {name: 'id', index: 'id', hidden: true},      
            {name: 'fileName', index: 'fileName', width: 200},
            {name: 'size', index: 'size', width: 100},
            {name: 'uploadTime', index: 'uploadTime', width: 150},
            {
                name: '', index: '', width: 120,
                formatter: function (cellvalue, options, rowObject) {
                	var fileName = rowObject.fileName;
                    var myval = "<button type='button' class='btn btn-xs btn-danger' onclick=deleteFile('" + fileName + "')>删除</button>";
                    return myval;
                }
            }
        ],
        viewrecords: true,
        rownumbers: true,//添加左侧行号

        altRows: true,
        multiselect: false,
        multiboxonly: true,
        loadComplete: function () {

        },
        sortable: true,
        sortname: 'endTime',
        sortorder: 'desc',
        autowidth: true,
        shrinkToFit: true,
        autoScroll: true
    });
});

function deleteFile(fileName) {
    swal({
        title: "您确定要删除该版本的代理程序吗",
        text: "删除后将无法恢复，请谨慎操作！",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        closeOnConfirm: false
    }, function () {
        jQuery.ajax({
            type: "post",
            url: "files/delete",
            data: "fileName=" + fileName,
            success: function (data) {
                data = JSON.parse(data)
                if (data.success == true) {
                	swal({ title: "成功删除代理程序！" }); 
                    jQuery("#grid-table").jqGrid().trigger("reloadGrid"); //重新载入
                } else {
                	swal({ title: data.error }); 
                }
            }
        });
    });
}

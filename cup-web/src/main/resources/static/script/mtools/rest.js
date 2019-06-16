var inputEditor = CodeMirror.fromTextArea( document.getElementById('editor'), {
	mode: "message/http",
    lineNumbers: true
});
inputEditor.setSize('100%', '35');

var resultEditor = CodeMirror.fromTextArea( document.getElementById('result'), {
	mode: "application/ld+json",
	matchBrackets: true,
	autoCloseBrackets: true,
    readOnly: true,
    lineWrapping: true,
    lineNumbers: true
});
resultEditor.setSize('100%', $(window).height()-220);

$(function () {
    $('#cmdBody').on('keydown', function (evt) {
    	if (evt.which == 116) {
            doQuery();
            return false;
        }
    });
});

$("#runBtn").on('click', function() {
	doQuery();
});

function doQuery() {
	var cmd =  inputEditor.getValue();
	if (cmd == null || cmd.length <= 0) {
		return;
	}
	
    var agentId = $('#agentId').val();
    if (agentId== undefined || agentId=='' || agentId==-1) {
		swal({
			title: "请选择代理"
		});
        return;
    }
    
    var params = {};
    params.agentId = agentId;
    params.cmd = cmd;
    
    $("#runBtn").button('loading');
    
    $.ajax({
        type: "POST",
        url: "ssh/exeRest",
        data: params,
        dataType: 'json',
        cache: false,
        success: function (data, textStatus) {
        	console.log(data.body);
        	$("#runBtn").button('reset');
            if (data.success) {
            	resultEditor.setValue(data.body);
            } else {
            	resultEditor.setValue(data.body);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	$("#runBtn").button('reset');
            if (XMLHttpRequest.status >= 500) {
                textStatus = '查询异常,请联系管理员';
            }
            alert('Error', XMLHttpRequest.status + ':' + textStatus);
        }
    });
}


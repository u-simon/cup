$(function () {

    $("#OK").click
    (
        function () {
            debugger;
            var objFile = document.getElementById("uploadFile").files[0];
            if (typeof (objFile) == "undefined" || objFile.size <= 0) {
                alert("请选择文件");
                return;
            }
            var fileName = objFile.name;
            var ext = fileName.substr(fileName.lastIndexOf(".") + 1);

            var reg = RegExp('.xls|.xlsx');
            if (!reg.exec(fileName)) {
                alert("不是有效的excel文件");
                return;
            }

            var param = new FormData();
            param.append("files", objFile);

            $.ajax({
                url: "job/importTask",
                type: 'POST',
                dataType: 'json',
                data: param,
                cache: false,
                processData: false,
                contentType: false,
                success: function (data) {
                    if (data.code == "0")
                        alert(data.error);
                    else
                        alert("导入成功");
                },
                error: function (data) {
                    if (data.code == "0")
                        alert(data.error);
                }
            });
        }
    )


    var $eleBtn2 = $("#downLoadFile");
    $eleBtn2.click(function () {
        var path = "";
        $.ajax({
            url: "job/getResourcePath",
            type: 'POST',
            dataType: 'json',
            data: null,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                alert("sucess")
                path = data.message;
                alert(path);
            },
            error: function (data) {
                alert("下载文件出错");
            }
        });

        var $eleForm = $("<form method='get'></form>");
        //https://codeload.github.com/douban/douban-client/legacy.zip/master
        $eleForm.attr("action", path);

        $(document.body).append($eleForm);

        //提交表单，实现下载
        $eleForm.submit();
    });

});

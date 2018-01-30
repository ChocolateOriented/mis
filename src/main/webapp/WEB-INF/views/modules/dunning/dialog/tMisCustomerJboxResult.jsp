<%--
  Created by IntelliJ IDEA.
  User: qtzhou
  Date: 2017/11/6
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>Title</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
    $(document).ready(function() {
        $('#resultSave').click(function() {
            if($("#inputForm").valid()) {

                $("#resultSave").attr('disabled',"true");
                $.ajax({
                    type: 'POST',
                    url : "${ctx}/dunning/taskIssue/resultSave",
                    data: $('#inputForm').serialize(),             //获取表单数据
                    success : function(data) {
                        window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
                        window.parent.window.jBox.close();
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown){
                        //通常情况下textStatus和errorThrown只有其中一个包含信息
                        alert("保存失败:"+textStatus);
                    }
                });
            }
        });

        $('#esc').click(function() {
            window.parent.window.jBox.close();
        });
    });

</script>
</head>
<body>
<form id="inputForm" method="post" class="breadcrumb form-search">
    <textarea name="handlingresult" cols="30" rows="10" style="width: 175px;height: 100px" ></textarea>
    <input name="id" type="hidden" value="${id}"/>
    <div style="margin-top: 20px">
        <input id="resultSave" class="btn btn-primary" type="button" value="已解决" />
        <input id="esc" class="btn btn-primary" type="button" value="取消" style="margin-left: 60px"/>
    </div>
</form>
</body>
</html>

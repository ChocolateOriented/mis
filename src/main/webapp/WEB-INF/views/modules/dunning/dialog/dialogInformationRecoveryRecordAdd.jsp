<%--
  Created by IntelliJ IDEA.
  User: jxguo
  Date: 2017/10/24
  Time: 10:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>调整金额</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
            $('#saveHistoryRemark').click(function() {
                if($("#inputForm").valid()){
                    $("#saveHistoryRemark").attr('disabled',"true");
                    $.ajax({
                        type: 'POST',
                        url : "${ctx}/dunning/tMisDunningTask/saveHistoryRemark",
                        data: $('#inputForm').serialize(),             //获取表单数据
                        success : function(data) {
                            if (data == "OK") {
                                alert("保存成功");
                                window.parent.childPage($("#refresh"));
                                window.parent.window.jBox.close();            //关闭子窗体
                            } else {
                                alert("保存失败"+data);
                            }
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown){
                            //通常情况下textStatus和errorThrown只有其中一个包含信息
                            alert("保存失败:"+textStatus);
                        }
                    });
                }
            });


            // 取消
            $('#esc').click(function() {
                window.parent.window.jBox.close();
            });
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
</ul>
<br/>
<form id="inputForm"   class="form-horizontal">
    <input style="display: none" id="dealCode" name="dealCode" value="${dealCode}">
    <input style="display: none" id="buyerid" name="buyerid" value="${buyerid}">
    <input style="display: none" id="id" name="id" value="${id}">
    <a style="display: none" id="refresh" href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/informationRecovery" onclick="childPage(this)"></a>
    <label class="control-label">备注：</label>
    <div class="controls">
        <textarea id="historyRemarks" rows="4" name="historyRemarks"  maxlength="50" required></textarea>
    </div>
    <div style= "padding:19px 180px 20px;" >
        <input id="saveHistoryRemark" class="btn btn-primary" type="button" value="保存"/>&nbsp;
        <input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
    </div>

</form>


</body>
</html>



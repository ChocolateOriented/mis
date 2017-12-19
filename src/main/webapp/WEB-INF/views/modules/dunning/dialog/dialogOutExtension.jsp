<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>手动留案</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">

        $(document).ready(function() {
            check_orders = [];
            var orders = window.parent.document.getElementsByName("orders");
            for(k in orders){
                if(orders[k].checked){
                    check_orders.push(orders[k].id);
                }
            }

            $("#orders").val(check_orders);
            $("#selectedOrders").text(check_orders.length);
            $('#extensionSave').click(function() {
                confirmx('是否确认留案?', saveConfirm);
            });

            $('#esc').click(function() {
                window.parent.window.jBox.close();
            });

            function saveConfirm(){
                if($("#inputForm").valid()){
                    $("#distributionSave").attr('disabled',"true");
                    $.ajax({
                        type: 'POST',
                        url : "${ctx}/dunning/tMisDunningOuterTask/outExtensionSave",
                        data: $('#inputForm').serialize(),             //获取表单数据
                        success : function(data) {
                            if (data == "OK") {
                                alert("保存成功");
                                window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
                                window.parent.window.jBox.close();            //关闭子窗体
                            } else {
                                alert(data);
                                window.parent.page();
                                window.parent.window.jBox.close();
                            }
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown){
                            //通常情况下textStatus和errorThrown只有其中一个包含信息
                            alert("保存失败:"+textStatus);
                        }
                    });
                }
            }
        });
    </script>


</head>
<body>
<ul class="nav nav-tabs">
</ul>
<br/>
<form id="inputForm"  class="form-horizontal" style="margin-left: 20px;">
    <input type="hidden" id="orders" name="orders"/>
    <input type="hidden" id="dunningcycle" name="dunningcycle" value="${dunningcycle}"/>
    <span>已选案件数:</span><span id="selectedOrders"></span>
    <div class="control-group" >
        <div style="width:20%;display:inline-block;">
            <p>委外截止日期：</p>
        </div>
        <div style="width:40%;display:inline-block;">
            <input name="outsourcingenddate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'%y-%M-%d'});"/>
        </div>
    </div>

    <div class="form-actions">
        <shiro:hasPermission name="dunning:tMisDunningTask:directorview">
            <input id="extensionSave" class="btn btn-primary" type="button" value="留案"/>&nbsp;
            <input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
        </shiro:hasPermission>
    </div>
</form>

</body>
</html>


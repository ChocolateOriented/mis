<!-- 催收留案功能-留案交互框 Patch 0001 by GQWU at 2016-11-9 start-->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收留案</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var message = "${message}";
			if(message != ""){
				alert(message);
                window.parent.window.jBox.close();
               /*  $.jBox.tip(message, 'warning'); */
			}
			$('#dunningDeadlineSave').click(function() {
				if($("#deferDate").val() == null){
					$.jBox.tip("请选择留案日期", 'warning');
					return;
				};
 			 if($("#inputForm").valid()){
 				 $("#dunningDeadlineSave").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisDunningTask/dunningDeadlineSave",
 	                    //提交数据
 	                    data: {"deferDate":$("#deferDate").val(),
 	                    	"dealcodes":'${dealcodes}'}, 
 	                    
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
 	                       window.parent.page();
                           window.parent.window.jBox.close();
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
	<form id="inputForm"  class="form-horizontal">
	
	<ul class="nav nav-tabs">
	</ul>
		<div class="control-group">
			<label>留案至</label>
				<input id="hiddenDeferDate" type="hidden" value="<fmt:formatDate value="${maxDeadline}" pattern="yyyy-MM-dd"/>"/>
				<input id="deferDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${maxDeadline}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'hiddenDeferDate\')}', dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<br/>
			<label>备注：将修改${dealcodesLength}条数据，</label>
		</div>
		
		<div class="form-actions">
 			<shiro:hasPermission name="dunning:tMisDunningTask:directorview">
				<input id="dunningDeadlineSave" class="btn btn-primary" type="button" value="留案"/>&nbsp;
 				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
 			</shiro:hasPermission>
		</div>
	</form>
	
</body>
</html>
<!-- 催收留案功能-留案交互框 Patch 0001 by GQWU at 2016-11-9 end-->

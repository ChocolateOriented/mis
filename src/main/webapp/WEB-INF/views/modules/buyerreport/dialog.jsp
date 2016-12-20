<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#asd').click(function() { 
			 if($("#inputForm").valid()){
// 					$.post("${ctx}/buyerreport/mRiskBuyerReport/dialogsave", $('#inputForm').serialize(), function(data) { alert(data); });
	                $.ajax({
	                    type: 'POST',
	                    url : "${ctx}/buyerreport/mRiskBuyerReport/dialogsave",
	                    data: $('#inputForm').serialize(),             //获取表单数据
// 	                    dataType:"json",
	                    success : function(data) {
	                        if (data == "asd") {
	                            alert("保存成功");
	                            window.parent.page();                                     //调用父窗体方法，当关闭子窗体刷新父窗体
	                            window.parent.window.jBox.close();            //关闭子窗体
	                        } else {
	                            alert("保存失败:"+data.message);
	                            window.parent.page();
	                            window.parent.window.jBox.close();
	                        }
	                    }
	                });
		          }
				
// 				if(!$('inputForm').valid())  
// 			        return;  
// 			    $('#inputForm').submit();//你确定这个表单名没写错 
// 			    $('div:has(iframe)', parent.document).hide(); 
// 			    parent.window.location.reload(); 
			}); 
// 			$("#inputForm").validate({
// 				submitHandler: function(form){
// 					loading('正在提交，请稍等...');
// 					form.submit();
// // 					var result = paramName.value;  
// // 					window.parent.addSort(result);  
// // 					window.parent.showPop('close');
// 				},
// 				errorContainer: "#messageBox",
// 				errorPlacement: function(error, element) {
// 					$("#messageBox").text("输入有误，请先更正。");
// 					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
// 						error.appendTo(element.parent().parent());
// 					} else {
// 						error.insertAfter(element);
// 					}
// 				}
// 			});
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mRiskBuyerReport"  class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">merchant_id：</label>
			<div class="controls">
				<form:input path="merchantId" htmlEscape="false" maxlength="10" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">count：</label>
			<div class="controls">
				<form:input path="count" htmlEscape="false" maxlength="10" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="buyerreport:mRiskBuyerReport:edit"><input id="asd" class="btn btn-primary" type="button" value="保 存"/>&nbsp;</shiro:hasPermission>
		</div>
	</form:form>
</body>
</html>


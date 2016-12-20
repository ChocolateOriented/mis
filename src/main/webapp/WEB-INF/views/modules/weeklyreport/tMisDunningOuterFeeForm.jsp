<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>基础佣金费率表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/weeklyreport/tMisDunningOuterFee/">基础佣金费率表列表</a></li>
		<li class="active"><a href="${ctx}/weeklyreport/tMisDunningOuterFee/form?id=${tMisDunningOuterFee.id}">基础佣金费率表<shiro:hasPermission name="weeklyreport:tMisDunningOuterFee:edit">${not empty tMisDunningOuterFee.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="weeklyreport:tMisDunningOuterFee:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tMisDunningOuterFee" action="${ctx}/weeklyreport/tMisDunningOuterFee/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">委外方ID：</label>
			<div class="controls">
<%-- 				<form:input path="dunningpeopleid" htmlEscape="false" maxlength="255" class="input-xlarge "/> --%>
				<form:select id="dunningpeopleid" path="dunningpeopleid" class="input-medium" >
					<form:option selected="selected" value="${tMisDunningOuterFee.dunningpeopleid}" label="${tMisDunningOuterFee.dunningpeoplename}"/>
					<form:options  items="${peoples}" itemLabel="name" itemValue="id"  htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">起始时间段：</label>
			<div class="controls">
				<form:input path="dunningdaybegin" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">截止时间段：</label>
			<div class="controls">
				<form:input path="dunningdayend" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">委外佣金费率：</label>
			<div class="controls">
				<form:input path="dunningfee" htmlEscape="false" class="input-xlarge  number" maxlength="4"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前费率执行起始时间：</label>
			<div class="controls">
				<input name="datetimebegin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisDunningOuterFee.datetimebegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label">当前费率执行截止时间：</label> -->
<!-- 			<div class="controls"> -->
<!-- 				<input name="datetimeend" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " -->
<%-- 					value="<fmt:formatDate value="${tMisDunningOuterFee.datetimeend}" pattern="yyyy-MM-dd HH:mm:ss"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="form-actions">
			<shiro:hasPermission name="weeklyreport:tMisDunningOuterFee:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
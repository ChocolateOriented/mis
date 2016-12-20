<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户报表管理</title>
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
		<shiro:hasPermission name="weeklyreport:sUserConversionReport:view"><li><a href="${ctx}/weeklyreport/sUserConversionReport/">用户报表编辑列表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="weeklyreport:vUserConversionReportWeek:view"><li><a href="${ctx}/weeklyreport/vUserConversionReportWeek">用户周报表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="weeklyreport:vUserConversionReportMonth:view"><li><a href="${ctx}/weeklyreport/vUserConversionReportMonth">用户月报表</a></li></shiro:hasPermission>
		<li class="active"><a href="${ctx}/weeklyreport/sUserConversionReport/form?id=${sUserConversionReport.id}">用户报表<shiro:hasPermission name="weeklyreport:sUserConversionReport:edit">${not empty sUserConversionReport.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="weeklyreport:sUserConversionReport:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sUserConversionReport" action="${ctx}/weeklyreport/sUserConversionReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		
		<div class="control-group">
			<label class="control-label">日期：</label>
			<div class="controls">
				<input name="createtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${sUserConversionReport.createtime}" pattern="yyyy-MM-dd"/>"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">APP新增装机量：</label>
			<div class="controls">
				<form:input path="newapp" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新增微信装机量：</label>
			<div class="controls">
				<form:input path="newwechat" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="weeklyreport:sUserConversionReport:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
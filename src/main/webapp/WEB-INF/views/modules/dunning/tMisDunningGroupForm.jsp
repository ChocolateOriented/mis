<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>催收小组管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	$("#inputForm").validate({
		submitHandler: function(form){
			$("#btnSubmit").prop("disabled",true);			
			loading('正在提交，请稍等...');
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#btnSubmit").prop("disabled",false);		
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
		<li><a href="${ctx}/dunning/tMisDunningGroup/">催收小组列表</a></li>
		<li class="active">
		<a href="${ctx}/dunning/tMisDunningGroup/
			${not empty TMisDunningGroup.id? 'edit?id=${TMisDunningGroup.id}">催收小组修改' : 'form">催收小组添加'}
		</a>
		</li>
	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisDunningGroup" action="${ctx}/dunning/tMisDunningGroup/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>	
		<input type="hidden" id="groupId" name="id" value="${TMisDunningGroup.id}"/>
		<div class="control-group">
			<label class="control-label">催收小组名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">小组组长：</label>
			<div class="controls">
					<form:select id="leaderId" path="leader.id" class="input-medium required" >
						<form:option value=""></form:option>
						<form:options  items="${users}" itemLabel="name" itemValue="id"  htmlEscape="false"/>
					</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">组类型：</label>
			<div class="controls">
					<form:select id="groupType" path="type" class="input-medium required">
						<form:options items="${groupTypes}" htmlEscape="false"/>
					</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
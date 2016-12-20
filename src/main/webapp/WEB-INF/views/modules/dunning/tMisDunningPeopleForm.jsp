<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收人员管理</title>
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
			
			$("#dunningPeopleid").change(function(){
// 					alert($("#dunningPeopleid").val());
// 					alert($("#dunningPeopleid option:selected").text());
					$("#dunningPeoplename").val($("#dunningPeopleid option:selected").text());
					
			 });
			
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisDunningPeople/">催收人员列表</a></li>
		<li class="active"><a href="${ctx}/dunning/tMisDunningPeople/form?id=${tMisDunningPeople.id}">催收人员<shiro:hasPermission name="dunning:tMisDunningPeople:edit">${not empty tMisDunningPeople.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="dunning:tMisDunningPeople:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="TMisDunningPeople" action="${ctx}/dunning/tMisDunningPeople/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>		
		<input type="hidden" id="dbid" name="dbid" value="${tMisDunningPeople.dbid}"/>
		<input type="hidden" id="dunningPeoplename" name="name" value="${tMisDunningPeople.name}"/>
		<div class="control-group">
			<label class="control-label">催收人员名称：</label>
			<div class="controls">
					<form:select id="dunningPeopleid" path="id" class="input-medium" >
						<form:option selected="selected" value="${tMisDunningPeople.id}" label="${tMisDunningPeople.name}"/>
						<form:options  items="${users}" itemLabel="name" itemValue="id"  htmlEscape="false"/>
					</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">人员类型：</label>
			<div class="controls">
			<form:select id="dunningpeopletype" path="dunningpeopletype" class="input-medium">
				<form:option  value="inner" label="内部催收人员"/>
				<form:option value="outer" label="委外公司"/>
			</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否自动分配：</label>
			<div class="controls">
			<form:select id="auto" path="auto" class="input-medium">
				<form:option  value="t" label="是"/>
				<form:option value="f" label="否"/>
			</form:select>
			</div>
		</div>
		<div class="control-group">
			<label title="大于1为单笔固定费率，小于1大于0为单笔百分比费率" class="control-label">单笔费率 ：</label>
			<div class="controls">
				<form:input path="rate" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">逾期周期起始：</label>
			<div class="controls">
				<form:input path="begin" htmlEscape="false" maxlength="3" class="input-xlarge digits required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">逾期周期截至：</label>
			<div class="controls">
				<form:input path="end" htmlEscape="false" maxlength="3" class="input-xlarge digits required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="dunning:tMisDunningPeople:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
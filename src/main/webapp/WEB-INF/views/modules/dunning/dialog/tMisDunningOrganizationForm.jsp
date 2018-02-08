<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>催收机构管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">

	function save() {
		if ($("#inputForm").valid()) {
			$("#btnSubmit").attr('disabled',"true");
			$.ajax({
				type: 'POST',
				url : "${ctx}/dunning/tMisDunningOrganization/save",
				data: $('#inputForm').serialize(),             //获取表单数据
				success: function(data) {
					if (data != "OK") {
						alert("保存失败");
					}
				  	window.parent.page();
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
				  alert("保存失败:" + textStatus);
                  window.parent.page();
                }
			});
		}
	}
	
	function esc() {
		window.parent.window.jBox.close();    
	}
</script>
</head>
<body>
	<br/>
	<form:form id="inputForm" modelAttribute="tMisDunningOrganization" action="${ctx}/dunning/tMisDunningOrganization/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>	
		<div class="control-group">
			<label class="control-label">催收机构名称：</label>
			<div class="controls">
				<c:if test="${opr eq 'add'}">
					<form:input path="name" htmlEscape="false" maxlength="128" class="input-medium required"/>
				</c:if>
				<c:if test="${opr eq 'edit'}">
					<form:select path="id" htmlEscape="false" class="input-medium required">
						<form:option value=""></form:option>
						<form:options items="${organizations}" itemLabel="name" itemValue="id"/>
					</form:select>
				</c:if>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">监理：</label>
			<div class="controls">
				<form:select id="supervisorId" path="supervisor.id" class="input-medium required">
					<form:option value=""></form:option>
					<form:options items="${peoples}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="save();"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="取 消" onclick="esc();"/>

		</div>
	</form:form>
</body>
</html>
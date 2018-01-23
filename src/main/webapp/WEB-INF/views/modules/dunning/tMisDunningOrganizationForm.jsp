<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>催收机构管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function add() {
		if ($("#inputForm").valid()) {
			$("#btnSubmit").attr('disabled',"true");
			$("#inputForm").submit();
		}
	}
	
	function edit() {
		if ($("#inputForm").valid()) {
			$("#btnSubmit").attr('disabled',"true");
			$.ajax({
				type: 'POST',
				url : "${ctx}/dunning/tMisDunningOrganization/edit",
				data: $('#inputForm').serialize(),             //获取表单数据
				success: function(data) {
					if (data == "OK") {
						alert("保存成功");
					} else {
						alert("保存失败");
					}
					window.parent.window.location.reload();
					window.parent.window.jBox.close();
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
					alert("保存失败:" + textStatus);
					window.parent.window.jBox.close();
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
	<c:if test="${opr eq 'add'}">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/dunning/tMisDunningGroup/">催收小组列表</a></li>
			<li>
			<a href="${ctx}/dunning/tMisDunningGroup/
				${not empty TMisDunningOrganization.id? 'edit?id=${TMisDunningOrganization.id}">催收小组修改' : 'form">催收小组添加'}
			</a>
			</li>
			<li class="active"><a href="${ctx}/dunning/tMisDunningOrganization/form?opr=add">催收机构添加</a></li>
		</ul>
	</c:if>
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
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="${opr}();"/>&nbsp;
			<c:if test="${opr eq 'add'}">
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1);"/>
			</c:if>
			<c:if test="${opr eq 'edit'}">
				<input id="btnCancel" class="btn" type="button" value="取 消" onclick="esc();"/>
			</c:if>
			
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
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
		<li><a href="${ctx}/dunning/tMisDunningTask/">催收任务列表</a></li>
		<li class="active"><a href="${ctx}/dunning/tMisDunningTask/form?id=${tMisDunningTask.id}">催收任务<shiro:hasPermission name="dunning:tMisDunningTask:edit">${not empty tMisDunningTask.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="dunning:tMisDunningTask:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="TMisDunningTask" action="${ctx}/dunning/tMisDunningTask/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">催讨人员id：</label>
			<div class="controls">
				<form:input path="dunningpeopleid" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">贷款订单号：</label>
			<div class="controls">
				<form:input path="dealcode" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">贷款本金 单位 分：</label>
			<div class="controls">
				<form:input path="capitalamount" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务起始时间：</label>
			<div class="controls">
				<input name="begin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisDunningTask.begin}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务截至时间：</label>
			<div class="controls">
				<input name="deadline" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisDunningTask.deadline}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务结束时间：</label>
			<div class="controls">
				<input name="end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisDunningTask.end}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">催讨周期-逾期周期起始：</label>
			<div class="controls">
				<form:input path="dunningperiodbegin" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">催讨周期-逾期周期截至：</label>
			<div class="controls">
				<form:input path="dunningperiodend" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">催回金额：</label>
			<div class="controls">
				<form:input path="dunnedamount" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务所对应的订单是否还清：</label>
			<div class="controls">
				<form:input path="ispayoff" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">减免金额 单位 分：</label>
			<div class="controls">
				<form:input path="reliefamount" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">催款任务状态：</label>
			<div class="controls">
				<form:input path="dunningtaskstatus" htmlEscape="false" maxlength="3" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">field1：</label>
			<div class="controls">
				<form:input path="field1" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">createby：</label>
			<div class="controls">
				<form:input path="createBy.id" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">createdate：</label>
			<div class="controls">
<!-- 				<input name="createdate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " -->
<%-- 					value="<fmt:formatDate value="${tMisDunningTask.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -->
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">updateby：</label>
			<div class="controls">
<%-- 				<form:input path="updateby" htmlEscape="false" maxlength="64" class="input-xlarge "/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">updatedate：</label>
			<div class="controls">
<!-- 				<input name="updatedate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " -->
<%-- 					value="<fmt:formatDate value="${tMisDunningTask.updatedate}" pattern="yyyy-MM-dd HH:mm:ss"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -->
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="dunning:tMisDunningTask:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷日报表管理</title>
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
		<li><a href="${ctx}/dailyreport/vRiskOrderDailyReport/">现金贷日报表列表</a></li>
		<li class="active"><a href="${ctx}/dailyreport/vRiskOrderDailyReport/form?id=${vRiskOrderDailyReport.id}">现金贷日报表<shiro:hasPermission name="dailyreport:vRiskOrderDailyReport:edit">${not empty vRiskOrderDailyReport.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="dailyreport:vRiskOrderDailyReport:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="VRiskOrderDailyReport" action="${ctx}/dailyreport/vRiskOrderDailyReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">统计日期：</label>
			<div class="controls">
				<input name="createtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${vRiskOrderDailyReport.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新增用户数：</label>
			<div class="controls">
				<form:input path="newusernum" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新用户订单数：</label>
			<div class="controls">
				<form:input path="newuserordernum" htmlEscape="false" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">老用户订单数：</label>
			<div class="controls">
				<form:input path="olduserordernum" htmlEscape="false" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单来源是APP：</label>
			<div class="controls">
				<form:input path="platformappnum" htmlEscape="false" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单来源是WECHAT：</label>
			<div class="controls">
				<form:input path="platformwechatnum" htmlEscape="false" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">成功放款订单数：</label>
			<div class="controls">
				<form:input path="remitordernum" htmlEscape="false" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当日到期订单数：</label>
			<div class="controls">
				<form:input path="expireordernum" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当日还款订单数：</label>
			<div class="controls">
				<form:input path="payoffordernum" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当日贷款收益：</label>
			<div class="controls">
				<form:input path="amountincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当日逾期费收益：</label>
			<div class="controls">
				<form:input path="overdueincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">待审核订单数：</label>
			<div class="controls">
				<form:input path="pendingordernum" htmlEscape="false" maxlength="21" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="dailyreport:vRiskOrderDailyReport:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
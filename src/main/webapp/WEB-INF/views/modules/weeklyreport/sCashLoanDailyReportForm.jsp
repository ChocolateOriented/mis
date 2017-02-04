<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务日报管理</title>
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
		<li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/list">财务月报列表</a></li>
		<li class="active"><a href="${ctx}/financdailyreport/sCashLoanDailyReport/form?id=${sCashLoanDailyReport.id}">财务日报<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:edit">${not empty sCashLoanDailyReport.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="financdailyreport:sCashLoanDailyReport:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="SCashLoanDailyReport" action="${ctx}/financdailyreport/sCashLoanDailyReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">日期：</label>
			<div class="controls">
				<input name="createtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${sCashLoanDailyReport.createtime}" pattern="yyyy-MM-dd"/>"  />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">本期总成本 - 本次统计周期内, 使用第三方征信数据所产生的费用总和：</label>
			<div class="controls">
				<form:input path="creditsumcost" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">媒体宣传费：</label>
			<div class="controls">
				<form:input path="mediacost" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">委外佣金 - 委外回款所扣除的佣金总金额, 佣金月结：</label>
			<div class="controls">
				<form:input path="entrustcommission" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">资金成本 - 利息+手续费：</label>
			<div class="controls">
				<form:input path="cashcostamount" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
	
		<div class="form-actions">
			<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
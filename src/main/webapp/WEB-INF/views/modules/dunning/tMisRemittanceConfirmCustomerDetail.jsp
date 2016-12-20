<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
	
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
       		<li><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmList">汇款确认列表</a></li>
 			<shiro:hasPermission name="dunning:tMisRemittanceConfirm:edit"><li  class="active"><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmCustomerDetail?buyerId=${buyerId}&dealcode=${dealcode}">汇款确认详情</a></li></shiro:hasPermission>	
 	</ul> 
	<sys:message content="${message}"/>
	个人信息 
	<table id="customerTable" class="table table-striped table-bordered table-condensed">
		<tbody>
		<tr>
			<th>姓名</th>
			<td>${personalInfo.realName}</td>
			<th>手机号码</th>
			<td>${personalInfo.mobile}</td>
		</tr>
		<tr>
			<th>身份证</th>
			<td>${personalInfo.idcard}</td>
			<th>欠款金额</th>
			<td>${personalInfo.amount/100}元</td>
		</tr>
		<tr>
			<th>性别</th>
			<td>${personalInfo.sex}</td>
			<th></th>
			<td></td>
		</tr>
		
		<tr>
			<th>本金</th>
			<td>${personalInfo.corpusAmount/100}元</td>
			<th>手续费</th>
			<td>${personalInfo.costAmout/100}元</td>
		</tr>
		<tr>
			<th>逾期费</th>
			<td>${personalInfo.overdueAmount/100}元</td>
			<th>借款期限</th>
			<td>${personalInfo.days}天</td>
		</tr>
		<tr>
			<th>逾期天数</th>
			<td>${personalInfo.overdueDays}天</td>
			<th>续期次数</th>
			<td>${personalInfo.delayCount}次</td>
		</tr>
		<tr>
			<th>到期还款日期</th>
			<td><fmt:formatDate value="${personalInfo.repaymentTime}" pattern="yyyy-MM-dd"/></td>
			<th>当前应催金额</th>
			<td>${personalInfo.creditAmount/100}元</td>
		</tr>
		<tr>
			<th>减免金额</th>
			<td>${personalInfo.modifyAmount/100}元</td>
			<th>还清日期</th>
			<td><fmt:formatDate value="${personalInfo.payOffTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		</tr>
		</tbody>
	</table>
	
</body>
</html>
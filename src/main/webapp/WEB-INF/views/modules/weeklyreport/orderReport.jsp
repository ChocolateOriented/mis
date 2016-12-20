<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/?reportType=orderReport">订单信息</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanWeeklyReport" action="${ctx}/weeklyreport/sCashLoanWeeklyReport/?reportType=orderReport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="DatetimeEnd"/>
		<ul class="ul-form">
			<li><label>时间区间：</label>
				<form:input path="intervaldatetime" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
<!-- 			<li><label>累计订单数</label> -->
<%-- 				<form:input path="ordernumincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>放款新用户：</label> -->
<%-- 				<form:input path="ordernumnewuser" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>放款老用户：</label> -->
<%-- 				<form:input path="ordernumolduserincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>累积还清：</label> -->
<%-- 				<form:input path="ordernumpayoffincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>逾期订单数：</label> -->
<%-- 				<form:input path="ordernumoverdue" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>委外订单数：</label> -->
<%-- 				<form:input path="ordernumoutsource" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>时间区间</th>
				<th>累积放款订单数</th>
				<th>放款新用户订单笔数</th>
				<th>放款老用户订单笔数</th>
				<th>累积还清订单笔数</th>
				<th>逾期订单笔数</th>
				<th>委外订单笔数</th>
<%-- 				<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sCashLoanWeeklyReport">
			<tr>
				<td><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form?id=${sCashLoanWeeklyReport.id}">
					${sCashLoanWeeklyReport.intervaldatetime}
				</a></td>
				<td>
					${sCashLoanWeeklyReport.ordernumincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumnewuser}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumolduserincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumpayoffincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumoverdue}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumoutsource}
				</td>
<%-- 				<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><td> --%>
<%--     				<a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form?id=${sCashLoanWeeklyReport.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/delete?id=${sCashLoanWeeklyReport.id}" onclick="return confirmx('确认要删除该现金贷款周报吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
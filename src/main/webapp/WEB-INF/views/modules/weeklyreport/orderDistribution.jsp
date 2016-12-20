<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单分布</title>
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
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/?reportType=orderDistribution">订单分布</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanWeeklyReport" action="${ctx}/weeklyreport/sCashLoanWeeklyReport/?reportType=orderDistribution" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="DatetimeEnd"/>
		<ul class="ul-form">
			<li><label>时间区间：</label>
				<form:input path="intervaldatetime" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
<!-- 			<li><label>APP占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodapppercent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>微信占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodwechatpercent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>500元占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodamount500percent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>1000元占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodamount1000percent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>1500元占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodamount1500percent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>7天占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodinterval7percent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>14天占比：</label> -->
<%-- 				<form:input path="ordernumneworderperiodinterval14percent" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>时间区间 </th>
				<th>APP占比(%)</th>
				<th>Wechat占比(%)</th>
				<th>500元占比(%)</th>
				<th>1000元占比(%)</th>
				<th>1500元占比(%)</th>
				<th>7天占比(%)</th>
				<th>14天占比(%)</th>
<%-- 				<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sCashLoanWeeklyReport">
			<tr>
				
				<td>
					${sCashLoanWeeklyReport.intervaldatetime}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodapppercentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodwechatpercentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount500percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1000percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1500percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval7percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval14percentincludedelay}
				</td>
				<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><td>
<%--     				<a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form?id=${sCashLoanWeeklyReport.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/delete?id=${sCashLoanWeeklyReport.id}" onclick="return confirmx('确认要删除该现金贷款周报吗？', this.href)">删除</a> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
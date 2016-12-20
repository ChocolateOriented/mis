<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户&营收&成本</title>
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
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/?reportType=customerReport">用户&营收&成本</a></li>
<%-- 		<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form">现金贷款周报添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanWeeklyReport" action="${ctx}/weeklyreport/sCashLoanWeeklyReport/?reportType=customerReport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="DatetimeEnd"/>
		<ul class="ul-form">
			<li><label>时间区间：</label>
				<form:input path="intervaldatetime" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
<!-- 			<li><label>累计放款用户数：</label> -->
<%-- 				<form:input path="singleusernum" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>贷款余额：</label> -->
<%-- 				<form:input path="amountnotrecovered" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>剩余委外：</label> -->
<%-- 				<form:input path="amountnotrecoveredoutsource" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>放贷金额：</label> -->
<%-- 				<form:input path="amountperiodincreasedincludedelay" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>总收益：</label> -->
<%-- 				<form:input path="amountallincome" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>信审收益：</label> -->
<%-- 				<form:input path="amountcreditincome" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>延期收益：</label> -->
<%-- 				<form:input path="amountdelayincome" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>逾期收益：</label> -->
<%-- 				<form:input path="amountoverdueincome" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
<!-- 			<li><label>委外收益：</label> -->
<%-- 				<form:input path="amountoutsourceincome" htmlEscape="false" class="input-medium"/> --%>
<!-- 			</li> -->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable"  border="1"  style="background-color: #FFFFFF" class="table  table-bordered table-striped">
		<thead>
			<tr>
				<th>时间区间 </th>
				<th>累计放款用户数</th>
				<th>当前贷款余额</th>
				<th>当前剩余委外金额</th>
				<th>放贷金额(含延期)</th>
<!-- 				<th>放贷金额(不含延期)</th> -->
				<th>总收益</th>
				<th>信审收益</th>
				<th>延期收益</th>
				<th>逾期收益</th>
				<th>委外收益</th>
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
					${sCashLoanWeeklyReport.singleusernum}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountnotrecovered}= --%>
					${sCashLoanWeeklyReport.amountnotrecoveredText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountnotrecoveredoutsource}= --%>
					${sCashLoanWeeklyReport.amountnotrecoveredoutsourceText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountperiodincreasedincludedelay}= --%>
					${sCashLoanWeeklyReport.amountperiodincreasedincludedelayText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountallincome}= --%>
					${sCashLoanWeeklyReport.amountallincomeText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountcreditincome}= --%>
					${sCashLoanWeeklyReport.amountcreditincomeText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountdelayincome}= --%>
					${sCashLoanWeeklyReport.amountdelayincomeText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountoverdueincome}= --%>
					${sCashLoanWeeklyReport.amountoverdueincomeText}
				</td>
				<td>
<%-- 				${sCashLoanWeeklyReport.amountoutsourceincome}= --%>
					${sCashLoanWeeklyReport.amountoutsourceincomeText}
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
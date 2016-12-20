<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>月逾期订单催回率管理</title>
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
		<li class="active"><a href="${ctx}/weeklyreport/sRiskOrderWithRepaymenttime/">月逾期订单催回率列表</a></li>
		<shiro:hasPermission name="weeklyreport:sRiskOrderWithRepaymenttime:edit"><li><a href="${ctx}/weeklyreport/sRiskOrderWithRepaymenttime/form">月逾期订单催回率添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SRiskOrderWithRepaymenttime" action="${ctx}/weeklyreport/sRiskOrderWithRepaymenttime/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
<!-- 		<ul class="ul-form"> -->
<!-- 			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li> -->
<!-- 			<li class="clearfix"></li> -->
<!-- 		</ul> -->
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>统计月份</th>
				<th>自然逾期率</th>
				<th>1-7天催回率</th>
				<th>8-14天催回率</th>
				<th>14天催回率</th>
				<th>15-21天催回率</th>
				<th>22-35天催回率</th>
				<th>35天催回率</th>
				<th>36+催回率</th>
				<th>Mo9催回率</th>
				<th>总催回率</th>
				<th>Mo9催回金额</th>
				<th>总催回金额</th>
				<shiro:hasPermission name="weeklyreport:sRiskOrderWithRepaymenttime:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sRiskOrderWithRepaymenttime">
			<tr>
				<td>
					${sRiskOrderWithRepaymenttime.month}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.overduerate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.within7daysrate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.within14daysrate}
				</td>
				<td>
					${sRiskOrderWithRemittime.with14DaysRate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.within21daysrate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.within35daysrate}
				</td>
				<td>
					${sRiskOrderWithRemittime.with35DaysRate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.over36daysrate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.mo9rate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.allrate}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.mo9amount}
				</td>
				<td>
					${sRiskOrderWithRepaymenttime.allamount}
				</td>
				<shiro:hasPermission name="weeklyreport:sRiskOrderWithRepaymenttime:edit"><td>
    				<a href="${ctx}/weeklyreport/sRiskOrderWithRepaymenttime/form?id=${sRiskOrderWithRepaymenttime.id}">修改</a>
					<a href="${ctx}/weeklyreport/sRiskOrderWithRepaymenttime/delete?id=${sRiskOrderWithRepaymenttime.id}" onclick="return confirmx('确认要删除该月逾期订单催回率吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷日报表管理</title>
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
		<li class="active"><a href="${ctx}/dailyreport/vRiskOrderDailyReport/">现金贷日报表列表</a></li>
		<shiro:hasPermission name="dailyreport:VRiskOrderDailyReport:edit"><li><a href="${ctx}/dailyreport/vRiskOrderDailyReport/form">现金贷日报表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="VRiskOrderDailyReport" action="${ctx}/dailyreport/vRiskOrderDailyReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="createtime"/>
		<ul class="ul-form">
			<li><label>统计日期：</label>
<!-- 				<input name="createtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${vRiskOrderDailyReport.createtime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -->
				<input name="beginDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VRiskOrderDailyReport.beginDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VRiskOrderDailyReport.endDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>统计日期</th>
				<th>新增用户数</th>
				<th>新用户订单数</th>
				<th>新用户待审核数</th>
				<th>老用户订单数</th>
				<th>来源APP</th>
				<th>来源WECHAT</th>
				<th>放款订单数</th>
				<th>到期订单数</th>
				<th>还款订单数</th>
				<th>贷款收益</th>
				<th>逾期费收益</th>
				<th>待审核订单数</th>
<%-- 				<shiro:hasPermission name="dailyreport:vRiskOrderDailyReport:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vRiskOrderDailyReport">
			<tr>
				<td>
					<fmt:formatDate value="${vRiskOrderDailyReport.createtime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${vRiskOrderDailyReport.newusernum}
				</td>
				<td>
					${vRiskOrderDailyReport.newuserordernum}
				</td>
				
				<td>
					${vRiskOrderDailyReport.newuserwaitremitnum}
				</td>
				
				<td>
					${vRiskOrderDailyReport.olduserordernum}
				</td>
				<td>
					${vRiskOrderDailyReport.platformappnum}
				</td>
				<td>
					${vRiskOrderDailyReport.platformwechatnum}
				</td>
				<td>
					${vRiskOrderDailyReport.remitordernum}
				</td>
				<td>
					${vRiskOrderDailyReport.expireordernum}
				</td>
				<td>
					${vRiskOrderDailyReport.payoffordernum}
				</td>
				<td>
<%-- 					${vRiskOrderDailyReport.amountincome} --%>
					${vRiskOrderDailyReport.amountincomeText}
				</td>
				<td>
<%-- 					${vRiskOrderDailyReport.overdueincome} --%>
					${vRiskOrderDailyReport.overdueincomeText}
				</td>
				<td>
					${vRiskOrderDailyReport.pendingordernum}
				</td>
<%-- 				<shiro:hasPermission name="dailyreport:vRiskOrderDailyReport:edit"><td> --%>
<%--     				<a href="${ctx}/dailyreport/vRiskOrderDailyReport/form?id=${vRiskOrderDailyReport.id}">修改</a> --%>
<%-- 					<a href="${ctx}/dailyreport/vRiskOrderDailyReport/delete?id=${vRiskOrderDailyReport.id}" onclick="return confirmx('确认要删除该现金贷日报表吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		<tr bgcolor="#FEE7AB">
				<td>
					合计：
				</td>
				<td>
					${sumReport.newusernum}
				</td>
				<td>
					${sumReport.newuserordernum}
				</td>
				<td>
					${sumReport.newuserwaitremitnum}
				</td>
				<td>
					${sumReport.olduserordernum}
				</td>
				<td>
					${sumReport.platformappnum}
				</td>
				<td>
					${sumReport.platformwechatnum}
				</td>
				<td>
					${sumReport.remitordernum}
				</td>
				<td>
					${sumReport.expireordernum}
				</td>
				<td>
					${sumReport.payoffordernum}
				</td>
				<td>
					${sumReport.amountincomeText}
				</td>
				<td>
					${sumReport.overdueincomeText}
				</td>
				<td>
					${sumReport.pendingordernum}
				</td>
			</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
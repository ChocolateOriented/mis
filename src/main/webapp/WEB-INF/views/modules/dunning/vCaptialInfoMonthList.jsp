<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资金成本月报管理</title>
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
		<li class="active"><a href="${ctx}/dunning/vCaptialInfoMonth/">资金成本月报列表</a></li>
<%-- 		<shiro:hasPermission name="dunning:vCaptialInfoMonth:edit"><li><a href="${ctx}/dunning/vCaptialInfoMonth/form">资金成本月报添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="VCaptialInfoMonth" action="${ctx}/dunning/vCaptialInfoMonth/" method="post" class="breadcrumb form-search">
<%-- 		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/> --%>
<%-- 		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> --%>
<!-- 		<ul class="ul-form"> -->
<!-- 			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li> -->
<!-- 			<li class="clearfix"></li> -->
<!-- 		</ul> -->
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>日期</th>
				<th>交易金额</th>
				<th>收入</th>
				<th>收入比</th>
				<th>渠道商消费金额</th>
				<th>总成本</th>
				<th>渠道费</th>
				<th>渠道费占比</th>
				<th>摩币返利</th>
				<th>摩币返利占比</th>
				<th>短信费</th>
				<th>短信费占比</th>
<%-- 				<shiro:hasPermission name="dunning:vCaptialInfoMonth:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vCaptialInfoMonth">
			<tr>
				<td>
<%-- 				<a href="${ctx}/dunning/vCaptialInfoMonth/form?id=${vCaptialInfoMonth.id}"> --%>
					${vCaptialInfoMonth.month}
<!-- 				</a> -->
				</td>
				<td>
					${vCaptialInfoMonth.amountText}
				</td>
				<td>
					${vCaptialInfoMonth.txnAmountText}
				</td>
				<td>
					${vCaptialInfoMonth.feePercentText}
				</td>
				<td>
					${vCaptialInfoMonth.distributorAmountText}
				</td>
				<td>
					${vCaptialInfoMonth.totalAmountText}
				</td>
				<td>
					${vCaptialInfoMonth.channelfeeAmountText}
				</td>
				<td>
					${vCaptialInfoMonth.channelfeePercentText}
				</td>
				<td>
					${vCaptialInfoMonth.mobiAmountText}
				</td>
				<td>
					${vCaptialInfoMonth.mobiPercentText}
				</td>
				<td>
					${vCaptialInfoMonth.smsAmountText}
				</td>
				<td>
					${vCaptialInfoMonth.smsPercentText}
				</td>
<%-- 				<shiro:hasPermission name="dunning:vCaptialInfoMonth:edit"><td> --%>
<%--     				<a href="${ctx}/dunning/vCaptialInfoMonth/form?id=${vCaptialInfoMonth.id}">修改</a> --%>
<%-- 					<a href="${ctx}/dunning/vCaptialInfoMonth/delete?id=${vCaptialInfoMonth.id}" onclick="return confirmx('确认要删除该资金成本月报吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
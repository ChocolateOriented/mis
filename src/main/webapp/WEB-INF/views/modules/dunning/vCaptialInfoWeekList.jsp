<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资金成本周报管理</title>
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
		<li class="active"><a href="${ctx}/dunning/vCaptialInfoWeek/">资金成本周报列表</a></li>
<%-- 		<shiro:hasPermission name="dunning:vCaptialInfoWeek:edit"><li><a href="${ctx}/dunning/vCaptialInfoWeek/form">资金成本周报添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="VCaptialInfoWeek" action="${ctx}/dunning/vCaptialInfoWeek/" method="post" class="breadcrumb form-search">
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
<!-- 				<th>week</th> -->
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
<%-- 				<shiro:hasPermission name="dunning:vCaptialInfoWeek:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vCaptialInfoWeek">
			<tr>
<%-- 				<td><a href="${ctx}/dunning/vCaptialInfoWeek/form?id=${vCaptialInfoWeek.id}"> --%>
<%-- 					${vCaptialInfoWeek.week} --%>
<!-- 				</a></td> -->
				<td>
					${vCaptialInfoWeek.weekdesc}
				</td>
				<td>
					${vCaptialInfoWeek.amountText}
				</td>
				<td>
					${vCaptialInfoWeek.txnAmountText}
				</td>
				<td>
					${vCaptialInfoWeek.feePercentText}
				</td>
				<td>
					${vCaptialInfoWeek.distributorAmountText}
				</td>
				<td>
					${vCaptialInfoWeek.totalAmountText}
				</td>
				<td>
					${vCaptialInfoWeek.channelfeeAmountText}
				</td>
				<td>
					${vCaptialInfoWeek.channelfeePercentText}
				</td>
				<td>
					${vCaptialInfoWeek.mobiAmountText}
				</td>
				<td>
					${vCaptialInfoWeek.mobiPercentText}
				</td>
				<td>
					${vCaptialInfoWeek.smsAmountText}
				</td>
				<td>
					${vCaptialInfoWeek.smsPercentText}
				</td>
<%-- 				<shiro:hasPermission name="dunning:vCaptialInfoWeek:edit"><td> --%>
<%--     				<a href="${ctx}/dunning/vCaptialInfoWeek/form?id=${vCaptialInfoWeek.id}">修改</a> --%>
<%-- 					<a href="${ctx}/dunning/vCaptialInfoWeek/delete?id=${vCaptialInfoWeek.id}" onclick="return confirmx('确认要删除该资金成本周报吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
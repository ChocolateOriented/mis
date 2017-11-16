<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>还款流水</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="serialRepay" action="${ctx}/dunning/tMisDunningTask/orderSerialRepayList" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li>
				<label>还款方式：</label>
				<form:select id="repayWay" path="repayWay" class="input-medium">
					<form:option selected="selected" value="" label="全部"/>
					<form:options items="${repayWays}" itemLabel="desc" />
				</form:select>
			</li>
			<li>
				<label>状态：</label>
				<form:select id="repayStatus" path="repayStatus" class="input-medium">
					<form:option selected="selected" value="" label="全部"/>
					<form:option value="succeeded" label="成功"/>
					<form:option value="failed" label="失败"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
		<input type="hidden" name="dealcode" value="${dealcode}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>还款方式</th>
				<th>还款渠道</th>
				<th>状态</th>
				<th>金额</th>
				<th>入账类型</th>
				<th>还款时间</th>
				<th>状态更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${repayList}" var="repay">
			<tr>
				<td>
						${repay.repayWay.desc}
				</td>
				<td>
						${repay.repayChannel}
				</td>
				<td>
						${repay.repayStatus.desc}
				</td>
				<td>
						${repay.repayAmount}
				</td>
				<td>
						${repay.paytypeText}
				</td>
				<td>
					<fmt:formatDate value="${repay.repayTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${repay.statusTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
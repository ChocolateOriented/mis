<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资金成本日报管理</title>
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
		<li class="active"><a href="${ctx}/dunning/vCaptialInfoDay/">资金成本日报列表</a></li>
		<shiro:hasPermission name="dunning:vCaptialInfoDay:edit"><li><a href="${ctx}/dunning/vCaptialInfoDay/form">资金成本日报添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="VCaptialInfoDay" action="${ctx}/dunning/vCaptialInfoDay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>日期：</label>
				<input name="beginCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VCaptialInfoDay.beginCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VCaptialInfoDay.endCreatetime}" pattern="yyyy-MM-dd"/>"
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
<%-- 				<shiro:hasPermission name="dunning:vCaptialInfoDay:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vCaptialInfoDay">
			<tr>
				<td>
<%-- 				<a href="${ctx}/dunning/vCaptialInfoDay/form?id=${vCaptialInfoDay.id}"> --%>
					<fmt:formatDate value="${vCaptialInfoDay.createtime}" pattern="yyyy-MM-dd"/>
<!-- 				</a> -->
				</td>
				<td>
					${vCaptialInfoDay.amountText}
				</td>
				<td>
					${vCaptialInfoDay.txnAmountText}
				</td>
				<td>
					${vCaptialInfoDay.feePercentText}
				</td>
				<td>
					${vCaptialInfoDay.distributorAmountText}
				</td>
				<td>
					${vCaptialInfoDay.totalAmountText}
				</td>
				<td>
					${vCaptialInfoDay.channelfeeAmountText}
				</td>
				<td>
					${vCaptialInfoDay.channelfeePercentText}
				</td>
				<td>
					${vCaptialInfoDay.mobiAmountText}
				</td>
				<td>
					${vCaptialInfoDay.mobiPercentText}
				</td>
				<td>
					${vCaptialInfoDay.smsAmountText}
				</td>
				<td>
					${vCaptialInfoDay.smsPercentText}
				</td>
<%-- 				<shiro:hasPermission name="dunning:vCaptialInfoDay:edit"><td> --%>
<%--     				<a href="${ctx}/dunning/vCaptialInfoDay/form?id=${vCaptialInfoDay.id}">修改</a> --%>
<%-- 					<a href="${ctx}/dunning/vCaptialInfoDay/delete?id=${vCaptialInfoDay.id}" onclick="return confirmx('确认要删除该资金成本日报吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
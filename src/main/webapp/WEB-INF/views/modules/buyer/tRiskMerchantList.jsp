<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户报表管理</title>
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
		<li class="active"><a href="${ctx}/buyer/tRiskMerchant/">用户报表列表</a></li>
		<shiro:hasPermission name="buyer:tRiskMerchant:edit"><li><a href="${ctx}/buyer/tRiskMerchant/form">用户报表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="TRiskMerchant" action="${ctx}/buyer/tRiskMerchant/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>商户名：</label>
				<form:input path="creditMerchantName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>商户key：</label>
				<form:input path="creditMerchantKey" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>logo文件id：</label>
				<form:input path="logoFileId" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tRiskMerchant.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>更新时间：</label>
				<input name="updateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tRiskMerchant.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>商户名</th>
				<th>商户唯一key</th>
				<th>logo文件id</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="buyer:tRiskMerchant:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tRiskMerchant">
			<tr>
				<td><a href="${ctx}/buyer/tRiskMerchant/form?id=${tRiskMerchant.id}">
					${tRiskMerchant.id}
				</a></td>
				<td>
					${tRiskMerchant.creditMerchantName}
				</td>
				<td>
					${tRiskMerchant.creditMerchantKey}
				</td>
				<td>
					${tRiskMerchant.logoFileId}
				</td>
				<td>
					<fmt:formatDate value="${tRiskMerchant.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tRiskMerchant.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="buyer:tRiskMerchant:edit"><td>
    				<a href="${ctx}/buyer/tRiskMerchant/form?id=${tRiskMerchant.id}">修改</a>
					<a href="${ctx}/buyer/tRiskMerchant/delete?id=${tRiskMerchant.id}" onclick="return confirmx('确认要删除该用户报表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
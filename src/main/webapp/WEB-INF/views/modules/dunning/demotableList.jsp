<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>徐盛管理</title>
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
		<li class="active"><a href="${ctx}/dunning/demotable/">徐盛列表</a></li>
		<shiro:hasPermission name="dunning:demotable:edit"><li><a href="${ctx}/dunning/demotable/form">徐盛添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="demotable" action="${ctx}/dunning/demotable/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>dealcodenum：</label>
				<form:input path="dealcodenum" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>dealcodeamount：</label>
				<form:input path="dealcodeamount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>datetime：</label>
				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${demotable.datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>name</th>
				<th>dealcodenum</th>
				<th>dealcodeamount</th>
				<th>datetime</th>
				<shiro:hasPermission name="dunning:demotable:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="demotable">
			<tr>
				<td><a href="${ctx}/dunning/demotable/form?id=${demotable.id}">
					${demotable.name}
				</a></td>
				<td>
					${demotable.dealcodenum}
				</td>
				<td>
					${demotable.dealcodeamount}
				</td>
				<td>
					<fmt:formatDate value="${demotable.datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="dunning:demotable:edit"><td>
    				<a href="${ctx}/dunning/demotable/form?id=${demotable.id}">修改</a>
					<a href="${ctx}/dunning/demotable/delete?id=${demotable.id}" onclick="return confirmx('确认要删除该徐盛吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
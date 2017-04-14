<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningGroup/">催收小组列表</a></li>
		<shiro:hasPermission name="dunning:TMisDunningGroup:edit"> 
			<li><a href="${ctx}/dunning/tMisDunningGroup/form">催收小组添加</a></li>
		</shiro:hasPermission>
	</ul>
	
	<form:form id="searchForm" modelAttribute="TMisDunningGroup" action="${ctx}/dunning/tMisDunningGroup/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li>
				<label>组名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li>
				<label>组长名：</label>
				<form:input path="leader.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li>
				<label>组类别：</label>
				<form:select id="groupTypes" path="type" class="input-medium">
					<form:option value="">全部</form:option>
					<form:options items="${groupTypes}" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>组名</th>
				<th>组长名</th>
				<th>组类型</th>
				<th>组员列表</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisDunningGroup">
			<tr>
				<td>
					${tMisDunningGroup.name}
				</td>
				<td>
					${tMisDunningGroup.leader.name}
				</td>
				<td>
					${groupTypes[tMisDunningGroup.type]}
				</td>
				<td>
					<a href="${ctx}/dunning/tMisDunningGroup/form?id=${tMisDunningGroup.id}">
					组员列表
					</a>
				</td>
				<shiro:hasPermission name="dunning:TMisDunningGroup:edit">
				<td>
    				<a href="${ctx}/dunning/tMisDunningGroup/edit?id=${tMisDunningGroup.id}">修改</a>
					<a href="${ctx}/dunning/tMisDunningGroup/delete?id=${tMisDunningGroup.id}" onclick="return confirmx('确认要删除该催收小组吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
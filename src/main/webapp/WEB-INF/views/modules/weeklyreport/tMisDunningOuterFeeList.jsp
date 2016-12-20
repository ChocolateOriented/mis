<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>基础佣金费率管理</title>
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
		<li class="active"><a href="${ctx}/weeklyreport/tMisDunningOuterFee/">基础佣金费率列表</a></li>
		<shiro:hasPermission name="weeklyreport:tMisDunningOuterFee:edit"><li><a href="${ctx}/weeklyreport/tMisDunningOuterFee/form">基础佣金费率添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisDunningOuterFee" action="${ctx}/weeklyreport/tMisDunningOuterFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>委外方ID：</label>
				<form:select id="dunningpeopleid" path="dunningpeopleid" class="input-medium" >
<%-- 					<form:option selected="selected" value="${TMisDunningOuterFee.dunningpeopleid}" label="${TMisDunningOuterFee.dunningpeoplename}"/> --%>
					<form:option selected="selected" value="" label="全部人员"/>
					<form:options  items="${peoples}" itemLabel="name" itemValue="id"  htmlEscape="false"/>
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
				<th>委外方</th>
				<th>起始时间段</th>
				<th>截止时间段</th>
				<th>委外佣金费率</th>
				<th>当前费率执行起始时间</th>
				<th>当前费率执行截止时间</th>
				<shiro:hasPermission name="weeklyreport:tMisDunningOuterFee:admin"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisDunningOuterFee">
			<tr>
				<td>
<%-- 				<a href="${ctx}/weeklyreport/tMisDunningOuterFee/form?id=${tMisDunningOuterFee.id}"> --%>
					${tMisDunningOuterFee.dunningpeoplename}
<!-- 				</a> -->
				</td>
				<td>
					${tMisDunningOuterFee.dunningdaybegin}
				</td>
				<td>
					${tMisDunningOuterFee.dunningdayend}
				</td>
				<td>
					${tMisDunningOuterFee.dunningfee}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningOuterFee.datetimebegin}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningOuterFee.datetimeend}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="weeklyreport:tMisDunningOuterFee:admin"><td>
    				<a href="${ctx}/weeklyreport/tMisDunningOuterFee/form?id=${tMisDunningOuterFee.id}">修改</a>
					<a href="${ctx}/weeklyreport/tMisDunningOuterFee/delete?id=${tMisDunningOuterFee.id}" onclick="return confirmx('确认要删除该基础佣金费率表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
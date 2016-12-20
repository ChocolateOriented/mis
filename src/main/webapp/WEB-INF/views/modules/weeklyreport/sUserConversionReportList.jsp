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
		<shiro:hasPermission name="weeklyreport:sUserConversionReport:view"><li class="active"><a href="${ctx}/weeklyreport/sUserConversionReport/">用户报表编辑列表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="weeklyreport:vUserConversionReportWeek:view"><li><a href="${ctx}/weeklyreport/vUserConversionReportWeek">用户周报表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="weeklyreport:vUserConversionReportMonth:view"><li><a href="${ctx}/weeklyreport/vUserConversionReportMonth">用户月报表</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SUserConversionReport" action="${ctx}/weeklyreport/sUserConversionReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>日期：</label>
				<input name="beginCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SUserConversionReport.beginCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SUserConversionReport.endCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
<%-- 				<form:input path="createtime" htmlEscape="false" class="input-medium"/> --%>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
<!-- 				<th>id</th> -->
				<th>日期</th>
				<th>APP新增装机量</th>
				<th>新增微信装机量</th>
				<shiro:hasPermission name="weeklyreport:sUserConversionReport:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sUserConversionReport">
			<tr>
<%-- 				<td><a href="${ctx}/weeklyreport/sUserConversionReport/form?id=${sUserConversionReport.id}"> --%>
<%-- 					${sUserConversionReport.id} --%>
<!-- 				</a></td> -->
				<td><a href="${ctx}/weeklyreport/sUserConversionReport/form?id=${sUserConversionReport.id}">
					<fmt:formatDate value="${sUserConversionReport.createtime}" pattern="yyyy-MM-dd"/>
				</a></td>
				<td>
					${sUserConversionReport.newapp}
				</td>
				<td>
					${sUserConversionReport.newwechat}
				</td>
				<shiro:hasPermission name="weeklyreport:sUserConversionReport:edit"><td>
    				<a href="${ctx}/weeklyreport/sUserConversionReport/form?id=${sUserConversionReport.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
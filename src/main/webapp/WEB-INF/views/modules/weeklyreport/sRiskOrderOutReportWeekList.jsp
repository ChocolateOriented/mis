<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>委外周报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出委外周报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/weeklyreport/sRiskOrderOutReportWeek/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/weeklyreport/sRiskOrderOutReportWeek/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sRiskOrderOutReportWeek/">委外周报表列表</a></li>
<%-- 		<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportWeek:edit"><li><a href="${ctx}/weeklyreport/sRiskOrderOutReportWeek/form">委外周报表添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="SRiskOrderOutReportWeek" action="${ctx}/weeklyreport/sRiskOrderOutReportWeek/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>催收方名称：</label>
				<form:input path="dunningpeoplename" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>日期</th>
				<th>催收方名称</th>
				<th>委外单数</th>
				<th>委外本金</th>
				<th>委外金额</th>
				<th>应催单数</th>
				<th>应催本金</th>
				<th>应催金额</th>
				<th>催回单数</th>
				<th>催回金额</th>
				<th>催回率</th>
				<th>基础佣金</th>
<%-- 				<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportWeek:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sRiskOrderOutReportWeek">
			<tr>
				<td>
<%-- 				<a href="${ctx}/weeklyreport/sRiskOrderOutReportWeek/form?id=${sRiskOrderOutReportWeek.id}"> --%>
					${sRiskOrderOutReportWeek.createtime}
<!-- 				</a> -->
				</td>
				<td>
					${sRiskOrderOutReportWeek.dunningpeoplename}
				</td>
				<td>
					${sRiskOrderOutReportWeek.dunningordernum}
				</td>
				<td>
					${sRiskOrderOutReportWeek.dunningordercapitalamount}
				</td>
				<td>
					${sRiskOrderOutReportWeek.dunningorderamount}
				</td>
				<td>
					${sRiskOrderOutReportWeek.repayordernum}
				</td>
				<td>
					${sRiskOrderOutReportWeek.repayordercapitalamount}
				</td>
				<td>
					${sRiskOrderOutReportWeek.repayorderamount}
				</td>
				<td>
					${sRiskOrderOutReportWeek.payoffordernum}
				</td>
				<td>
					${sRiskOrderOutReportWeek.payofforderamount}
				</td>
				<td>
					${sRiskOrderOutReportWeek.payofforderrate}
				</td>
				<td>
					${sRiskOrderOutReportWeek.basiccommission}
				</td>
<%-- 				<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportWeek:edit"><td> --%>
<%--     				<a href="${ctx}/weeklyreport/sRiskOrderOutReportWeek/form?id=${sRiskOrderOutReportWeek.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/sRiskOrderOutReportWeek/delete?id=${sRiskOrderOutReportWeek.id}" onclick="return confirmx('确认要删除该委外周报表吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
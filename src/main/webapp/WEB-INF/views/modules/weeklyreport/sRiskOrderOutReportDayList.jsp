<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>委外日报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出委外日报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/weeklyreport/sRiskOrderOutReportDay/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/weeklyreport/sRiskOrderOutReportDay/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sRiskOrderOutReportDay/list">委外日报表列表</a></li>
<%-- 		<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportDay:edit"><li><a href="${ctx}/weeklyreport/sRiskOrderOutReportDay/form">委外日报表添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="SRiskOrderOutReportDay" action="${ctx}/weeklyreport/sRiskOrderOutReportDay/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		
			<li>
			<label>日期：</label>
			<input name="beginDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${SRiskOrderOutReportDay.beginDatetime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
			<input name="endDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${SRiskOrderOutReportDay.endDatetime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			
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
				<th>按日统计</th>
				<th>催收方名称</th>
				<th>委外单数</th>
				<th>委外本金</th>
				<th>委外金额</th>
				<th>应催单数</th>
				<th>应催本金</th>
				<th>应催金额</th>
				<th>催回单数</th>
				<th>催回金额</th>
				<th>基础佣金</th>
<%-- 				<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportDay:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sRiskOrderOutReportDay">
			<tr>
				<td>
<%-- 				<a href="${ctx}/weeklyreport/sRiskOrderOutReportDay/form?id=${sRiskOrderOutReportDay.id}"> --%>
					<fmt:formatDate value="${sRiskOrderOutReportDay.createtime}" pattern="yyyy-MM-dd"/>
<!-- 				</a> -->
				</td>
				<td>
					${sRiskOrderOutReportDay.dunningpeoplename}
				</td>
				<td>
					${sRiskOrderOutReportDay.dunningordernum}
				</td>
				<td>
					${sRiskOrderOutReportDay.dunningordercapitalamount}
				</td>
				<td>
					${sRiskOrderOutReportDay.dunningorderamount}
				</td>
				<td>
					${sRiskOrderOutReportDay.repayordernum}
				</td>
				<td>
					${sRiskOrderOutReportDay.repayordercapitalamount}
				</td>
				<td>
					${sRiskOrderOutReportDay.repayorderamount}
				</td>
				<td>
					${sRiskOrderOutReportDay.payoffordernum}
				</td>
				<td>
					${sRiskOrderOutReportDay.payofforderamount}
				</td>
				<td>
					${sRiskOrderOutReportDay.basiccommission}
				</td>
<%-- 				<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportDay:edit"><td> --%>
<%--     				<a href="${ctx}/weeklyreport/sRiskOrderOutReportDay/form?id=${sRiskOrderOutReportDay.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/sRiskOrderOutReportDay/delete?id=${sRiskOrderOutReportDay.id}" onclick="return confirmx('确认要删除该委外日报表吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
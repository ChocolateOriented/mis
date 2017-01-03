<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资金流日报管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出资金流日报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/weeklyreport/sCashFlowReport/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/weeklyreport/sCashFlowReport/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sCashFlowReport/">资金流日报列表</a></li>
<%-- 		<shiro:hasPermission name="weeklyreport:sCashFlowReport:edit"><li><a href="${ctx}/weeklyreport/sCashFlowReport/form">资金流日报添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashFlowReport" action="${ctx}/weeklyreport/sCashFlowReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>统计时间：</label>
				<input name="beginCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SCashFlowReport.beginCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SCashFlowReport.endCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			
			<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>日期</th>
				<th>放款总额</th>
				<th>口袋放款</th>
				<th>mo9放款</th>
				<th>人工放款</th>
<!-- 				<th>江湖救急放款笔数</th> -->
				<th>还款金额</th>
<!-- 				<th>江湖救急还款笔数</th> -->
				<th>放还差额</th>
				<th>先玩后付充值金额</th>
<%-- 				<shiro:hasPermission name="weeklyreport:sCashFlowReport:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sCashFlowReport">
			<tr>
				<td>
					<fmt:formatDate value="${sCashFlowReport.createtime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${sCashFlowReport.payamount}
				</td>
				<td>
					${sCashFlowReport.kdpayamount}
				</td>
				<td>
					${sCashFlowReport.mo9payamount}
				</td>
				<td>
					${sCashFlowReport.manualpayamount}
				</td>
<!-- 				<td> -->
<%-- 					${sCashFlowReport.paynumbers} --%>
<!-- 				</td> -->
				<td>
					${sCashFlowReport.payoffamount}
				</td>
<!-- 				<td> -->
<%-- 					${sCashFlowReport.repaynumber} --%>
<!-- 				</td> -->
				<td>
					${sCashFlowReport.diffamount}
				</td>
				<td>
					${sCashFlowReport.gpayamount}
				</td>
				
<%-- 				<shiro:hasPermission name="weeklyreport:sCashFlowReport:edit"><td> --%>
<%--     				<a href="${ctx}/weeklyreport/sCashFlowReport/form?id=${sCashFlowReport.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/sCashFlowReport/delete?id=${sCashFlowReport.id}" onclick="return confirmx('确认要删除该资金流日报吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		<tr bgcolor="#FEE7AB">
				<td>
					合计：
				</td>
				<td>
					${sumCashFlowReport.payamount}
				</td>
				<td>
					${sumCashFlowReport.kdpayamount}
				</td>
				<td>
					${sumCashFlowReport.mo9payamount}
				</td>
				<td>
					${sumCashFlowReport.manualpayamount}
				</td>
<!-- 				<td> -->
<%-- 					${sumCashFlowReport.paynumbers} --%>
<!-- 				</td> -->
				<td>
					${sumCashFlowReport.payoffamount}
				</td>
<!-- 				<td> -->
<%-- 					${sumCashFlowReport.repaynumber} --%>
<!-- 				</td> -->
				<td>
					${sumCashFlowReport.diffamount}
				</td>
				<td>
					${sumCashFlowReport.gpayamount}
				</td>
			</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>贷后催款情况报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出贷后催款情况报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/afterloanpushing/vRiskOrderAfterLoanPushing/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		function page(n,s){
// 			$("#pageNo").val(n);
// 			$("#pageSize").val(s);
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/afterloanpushing/vRiskOrderAfterLoanPushing/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/afterloanpushing/vRiskOrderAfterLoanPushing/">贷后催款情况报表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="VRiskOrderAfterLoanPushing" action="${ctx}/afterloanpushing/vRiskOrderAfterLoanPushing/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="datetime"/>
		<ul class="ul-form">
			<li><label>日期：</label>
				<input name="beginDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VRiskOrderAfterLoanPushing.beginDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VRiskOrderAfterLoanPushing.endDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page();"/>
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
				<th>1-7天催回率</th>
				<th>8-14天催回率</th>
				<th>15-21天催回率</th>
				<th>22-35天催回率</th>
				<th>36+天催回率</th>
				<th>总催回率</th>
				<th>催回金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vRiskOrderAfterLoanPushing">
			<tr>
				<td>
					<fmt:formatDate value="${vRiskOrderAfterLoanPushing.datetime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.orderpayoffover7Percentage}
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.orderpayoffover14Percentage}
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.orderpayoffover21Percentage}
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.orderpayoffover35Percentage}
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.orderpayoffover36Percentage}
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.orderpayoffoverSumPercentage}
				</td>
				<td>
					${vRiskOrderAfterLoanPushing.repaymentamount}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
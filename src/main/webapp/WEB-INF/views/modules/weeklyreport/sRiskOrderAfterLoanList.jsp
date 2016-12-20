<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>贷后风险管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出贷后风险情况报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/afterloan/sRiskOrderAfterLoan/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/afterloan/sRiskOrderAfterLoan/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/afterloan/sRiskOrderAfterLoan/">贷后风险列表</a></li>
		<shiro:hasPermission name="afterloan:sRiskOrderAfterLoan:edit"><li><a href="${ctx}/afterloan/sRiskOrderAfterLoan/form">贷后风险添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SRiskOrderAfterLoan" action="${ctx}/afterloan/sRiskOrderAfterLoan/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="createtime"/>
		<ul class="ul-form">
			<li><label>日期：</label>
<!-- 				<input name="createtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${sRiskOrderAfterLoan.createtime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -->
				<input name="beginDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SRiskOrderAfterLoan.beginDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SRiskOrderAfterLoan.endDatetime}" pattern="yyyy-MM-dd"/>"
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
				<th>累计申请笔数</th>
				<th>累计贷款笔数</th>
				<th>累计贷款本金</th>
				<th>累计放款本金</th>
				<th>累计已还放款本金</th>
				<th>累计未还放款本金</th>
				<th>累计已还本金</th>
				<th>累计贷款手续费</th>
				<th>累计已还手续费</th>
				<th>已使用抵用券金额</th>
				<th>累计延期本金</th>
				<th>累计已收延期费用</th>
				<th>累计逾期费用</th>
				<th>累计已还逾期费用</th>
				<th>减免金额</th>
				<th>待收总额</th>
				<th>逾期总金额</th>
				<th>逾期1-7天金额</th>
				<th>逾期8-14天金额</th>
				<th>逾期15-21天金额</th>
				<th>逾期22-28天金额</th>
				<th>逾期29-35天金额</th>
				<th>逾期36-119天金额</th>
				<th>逾期120天以上金额</th>
				
				<th>部分还款本金</th>
				<th>部分还款手续费</th>
				<th>部分还款延期费</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sRiskOrderAfterLoan">
			<tr>
				<td>
					<fmt:formatDate value="${sRiskOrderAfterLoan.createtime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
<%-- 				<a href="${ctx}/afterloan/sRiskOrderAfterLoan/form?id=${sRiskOrderAfterLoan.id}"> --%>
					${sRiskOrderAfterLoan.orderapply}
<!-- 				</a> -->
				</td>
				<td>
					${sRiskOrderAfterLoan.orderloan}
				</td>
				<td>
					${sRiskOrderAfterLoan.captialloan}
				</td>
				<td>
					${sRiskOrderAfterLoan.captialremit}
				</td>
				<td>
					${sRiskOrderAfterLoan.capitalremitpaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.capitalremitunpaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.captialloanpaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.commissionamount}
				</td>
				<td>
					${sRiskOrderAfterLoan.commissionamountpaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.couponamountpaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.capitaldelay}
				</td>
				<td>
					${sRiskOrderAfterLoan.delaypaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.overdueamount}
				</td>
				<td>
					${sRiskOrderAfterLoan.overdueamountpaid}
				</td>
				<td>
					${sRiskOrderAfterLoan.reliefamount}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedamount}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount7}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount14}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount21}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount28}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount29}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount119}
				</td>
				<td>
					${sRiskOrderAfterLoan.receivedoverdueamount120}
				</td>
				
				<td>
					${sRiskOrderAfterLoan.partialcapitalText}
				</td>
				<td>
					${sRiskOrderAfterLoan.partialcostText}
				</td>
				<td>
					${sRiskOrderAfterLoan.partialoverdueText}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
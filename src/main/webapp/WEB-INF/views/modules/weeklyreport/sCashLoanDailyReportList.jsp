<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务日报管理</title>
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
		<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:dailyview"><li class="active"><a href="${ctx}/financdailyreport/sCashLoanDailyReport/list">财务日报列表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:weekview"><li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/weeklist">财务周报列表</a></li></shiro:hasPermission>
        <shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:monthview"><li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/monthlist">财务月报列表</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanDailyReport" action="${ctx}/financdailyreport/sCashLoanDailyReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>日期：</label>
				<input name="beginCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SCashLoanDailyReport.beginCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SCashLoanDailyReport.endCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
				<th ></th>
<!-- 				<th>month</th> -->
<!-- 				<th>week</th> -->
<!-- 				<th>weekdesc</th> -->
				<th colspan="5" style="text-align:center;">交易总额</th>
				
				<th colspan="5" style="text-align:center;">收入</th>
				<th colspan="2" style="text-align:center;">支付渠道费</th>
				<th colspan="1" style="text-align:center;">征信成本</th>
				<th colspan="2" style="text-align:center;">市场推广</th>
				<th colspan="4" style="text-align:center;">坏账</th>
				
				<th  colspan="1" style="text-align:center;">资金成本</th>
				<th  colspan="1" style="text-align:center;"></th>
			</tr>
		
			<tr>
				<th>日期</th>
<!-- 				<th>月</th> -->
<!-- 				<th>周</th> -->
<!-- 				<th>周</th> -->
				<th>本期累计</th>
				<th>周增长率</th>
				<th>月增长率</th>
				<th>新增交易用户</th>
				<th>放贷笔数</th>
				
				<th>本期收入</th>
				<th>周收入增长率</th>
				<th>月收入增长率</th>
				<th>周单笔收入</th>
				<th>月单笔收入</th>

				<th>放款成本</th>
				<th>还款成本</th>
				
				<th>本期总成本</th>
<!-- 				<th>人均成本</th> -->
				
				<th>减免</th>
				<th>媒体宣传费</th>
				
				<th>目前累计</th>
				<th>周本期累计</th>
				<th>月本期累计</th>
<!-- 				<th>坏账率</th> -->
				<th>委外佣金</th>
				
<!-- 				<th>毛利</th> -->
<!-- 				<th>毛利率</th> -->
				<th>利息+手续费</th>
				
				<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		
		<tbody>
		<c:forEach items="${page.list}" var="sCashLoanDailyReport">
			<tr>
				<td><a href="${ctx}/financdailyreport/sCashLoanDailyReport/form?id=${sCashLoanDailyReport.id}">
					<fmt:formatDate value="${sCashLoanDailyReport.createtime}" pattern="yyyy-MM-dd"/>
				</a></td>
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.month} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.week} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.weekdesc} --%>
<!-- 				</td> -->
				<td>
					${sCashLoanDailyReport.remitamountText}
				</td>
				<td>
					${sCashLoanDailyReport.weekincreasedText}
				</td>
				<td>
					${sCashLoanDailyReport.monthincreasedText}
				</td>
				<td>
					${sCashLoanDailyReport.newuserText}
				</td>
				<td>
					${sCashLoanDailyReport.neworderText}
				</td>
				<td>
					${sCashLoanDailyReport.incomeamountText}
				</td>
				<td>
					${sCashLoanDailyReport.incomeincreasedText}
				</td>
				<td>
					${sCashLoanDailyReport.monthincomeincreasedText}
				</td>
				<td>
					${sCashLoanDailyReport.incomepercentText}
				</td>
				<td>
					${sCashLoanDailyReport.monthincomepercentText}
				</td>
				<td>
					${sCashLoanDailyReport.loancostText}
				</td>
				<td>
					${sCashLoanDailyReport.repaycostText}
				</td>
				<td>
					${sCashLoanDailyReport.creditsumcostText}
				</td>
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.creditavgcostText} --%>
<!-- 				</td> -->
				<td>
					${sCashLoanDailyReport.couponcostText}
				</td>
				<td>
					${sCashLoanDailyReport.mediacostText}
				</td>
				<td>
					${sCashLoanDailyReport.debatamountText}
				</td>
				<td>
					${sCashLoanDailyReport.weekdebatamountText}
				</td>
				<td>
					${sCashLoanDailyReport.monthdebatamountText}
				</td>
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.debatpercentText} --%>
<!-- 				</td> -->
				<td>
					${sCashLoanDailyReport.entrustcommissionText}
				</td>
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.grossprofileText} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sCashLoanDailyReport.grossprofilepercentText} --%>
<!-- 				</td> -->
				<td>
					${sCashLoanDailyReport.cashcostamountText}
				</td>
				<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:edit"><td>
    				<a href="${ctx}/financdailyreport/sCashLoanDailyReport/form?id=${sCashLoanDailyReport.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
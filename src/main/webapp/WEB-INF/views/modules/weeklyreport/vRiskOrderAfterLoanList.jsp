<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>贷后还款情况管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出贷后还款情况报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/repaymentcondition/vRiskOrderAfterLoan/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/repaymentcondition/vRiskOrderAfterLoan/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/repaymentcondition/vRiskOrderAfterLoan/list">贷后还款情况列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="VRiskOrderAfterLoan" action="${ctx}/repaymentcondition/vRiskOrderAfterLoan/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="datetime"/>
		<ul class="ul-form">
			<li><label>日期：</label>
<!-- 				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${vRiskOrderAfterLoan.datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -->
				<input name="beginDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VRiskOrderAfterLoan.beginDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endDatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${VRiskOrderAfterLoan.endDatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="clearfix"></li>
			
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th colspan="3" style="text-align:center;">到期订单</th>
				<th colspan="3" style="text-align:center;">提前还款</th>
				<th colspan="3" style="text-align:center;">按时还款</th>
				<th colspan="3" style="text-align:center;">逾期订单</th>
				<th colspan="2" style="text-align:center;">还款订单</th>
				<th colspan="2" style="text-align:center;">提前还款</th>
				<th colspan="2" style="text-align:center;">按时还款</th>
				<th colspan="2" style="text-align:center;">逾期还款</th>
			</tr>
			<tr>
				<th>日期</th>
				
				<th>单数</th>
				<th>应收金额</th>
				<th>未还订单数</th>
				
				<th>单数</th>
				<th>金额</th>
				<th>提前率</th>
				
				<th>单数</th>
				<th>金额</th>
				<th>按时率</th>
				
				<th>单数</th>
				<th>金额</th>
				<th>自然逾期率</th>
<!-- 				<th>待还清</th> -->
				
				<th>单数</th>
				<th>还款收益</th>
				
				<th>单数</th>
				<th>占比</th>
				<th>单数</th>
				<th>占比</th>
				<th>单数</th>
				<th>占比</th>
			</tr>

		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vRiskOrderAfterLoan">
			<tr>
				<td><a >
					<fmt:formatDate value="${vRiskOrderAfterLoan.datetime}" pattern="yyyy-MM-dd"/>
				</a></td>
				
				<td>
					${vRiskOrderAfterLoan.orderexpirenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpireamount}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderUnrepayNum}
				</td>
				
				<td>
					${vRiskOrderAfterLoan.orderexpirebeforenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpirebeforeamount}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpirebeforenumPercentage}
				</td>
				
				<td>
					${vRiskOrderAfterLoan.orderexpireontimenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpireontimeamount}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpireontimenumPercentage}
				</td>
				
				<td>
					${vRiskOrderAfterLoan.orderexpireoverduenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpireoverdueamount}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderexpireoverduenumPercentage}
				</td>
<!-- 				<td> -->
<%-- 					${vRiskOrderAfterLoan.orderexpirenorepay} --%> 
<!-- 				</td> -->
				
				
				<td>
					${vRiskOrderAfterLoan.orderremitnum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderincomeamount}
				</td>
				
				<td>
					${vRiskOrderAfterLoan.orderremitbeforenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderremitbeforenumPercentage}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderremitontimenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderremitontimenumPercentage}
				</td>
				
				<td>
					${vRiskOrderAfterLoan.orderremitoverduenum}
				</td>
				<td>
					${vRiskOrderAfterLoan.orderremitoverduenumPercentage}
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
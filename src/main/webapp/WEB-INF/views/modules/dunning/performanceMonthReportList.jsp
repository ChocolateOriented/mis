<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收员绩效月报</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出催收员绩效月报数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/tMisDunningTask/performanceMonthReportExport");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/tMisDunningTask/findPerformanceMonthReport");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningTask/findPerformanceMonthReport">催收员绩效月报</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="performanceMonthReport" action="${ctx}/dunning/tMisDunningTask/findPerformanceMonthReport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>时间：</label>
				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${performanceMonthReport.datetime}" pattern="yyyy-MM"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
			</li>
			
			
			<li><label>催款员：</label>
				<form:select id="personnel"  path="personnel" class="input-medium" >
					<form:option selected="selected" value="" label="全部人员"/>
					<form:options items="${dunningPeoples}" itemLabel="name" itemValue="name" htmlEscape="false" />
				</form:select>
			</li>
			
			<li><label>催收周期：</label>
				<form:input  path="begin"  htmlEscape="false" maxlength="3" class="digits"  style="width:35px;"  />
				- 
				<form:input  path="end"  htmlEscape="false" maxlength="3" class="digits" style="width:35px;"   />
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
				<th>日期</th>
				<th>催收周期</th>
				<th>催收员</th>
				<th>应催金额</th>
				<th>还款金额</th>
				<th>应催订单</th>
				<th>还清订单</th>
				<th>催回率</th>
				<th>电话量</th>
				<th>短信量</th>
				<th>还款本金</th>
				<th>还款利润</th>
			</tr>

		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="performanceMonthReport">
			<tr>
				<td>
					<fmt:formatDate value="${performanceMonthReport.datetime}" pattern="yyyy-MM"/>
				</td>
				<td>
					${performanceMonthReport.begin}-${performanceMonthReport.end}
				</td>
				<td>
					${performanceMonthReport.personnel}
				</td>
				<td>
					${performanceMonthReport.creditamountText}
				</td>
				<td>
					${performanceMonthReport.payamountText}
				</td>
				<td>
					${performanceMonthReport.creditorder}
				</td>
				<td>
					${performanceMonthReport.payorder}
				</td>
				<td>
					${performanceMonthReport.payoffpercentageText}
				</td>
				<td>
					${performanceMonthReport.telnum}
				</td>
				<td>
					${performanceMonthReport.smsnum}
				</td>
				<td>
					${performanceMonthReport.payoffCapitalText}
				</td>
				<td>
					${performanceMonthReport.profileAmountText}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
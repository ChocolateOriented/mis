<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收月绩效管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出催收月绩效数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/sMisDunningTaskMonthReport/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/sMisDunningTaskMonthReport/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/sMisDunningTaskMonthReport/">催收月绩效列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="SMisDunningTaskMonthReport" action="${ctx}/dunning/sMisDunningTaskMonthReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>月份：</label>
<%-- 				<form:input path="datetime" htmlEscape="false" maxlength="10" class="input-medium"/> --%>
				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SMisDunningTaskMonthReport.datetime}" pattern="yyyy-MM"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
			</li>
<!-- 			<li><label>催收人姓名：</label> -->
<%-- 				<form:input path="dunningpeoplename" htmlEscape="false" maxlength="20" class="input-medium"/> --%>
<!-- 			</li> -->
			<li><label>催款员：</label>
				<form:select id="dunningpeoplename"  path="dunningpeoplename" class="input-medium" >
					<form:option selected="selected" value="" label="全部人员"/>
					<form:options items="${dunningPeoples}" itemLabel="name" itemValue="name" htmlEscape="false" />
				</form:select>
			</li>
			<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>月份</th>
				<th>催收人姓名</th>
				<th>催收周期</th>
				<th>催收任务数</th>
				<th>完成催收任务数</th>
				<th>催回率</th>
				<th>电话数</th>
				<th>短信数</th>
				<th>应催金额</th>
				<th>催回金额</th>
				<th>催回本金</th>
				<th>催回利润</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sMisDunningTaskMonthReport">
			<tr>
				<td><a href="${ctx}/dunning/sMisDunningTaskMonthReport/form?id=${sMisDunningTaskMonthReport.id}">
					${sMisDunningTaskMonthReport.month}
				</a></td>
				<td>
					${sMisDunningTaskMonthReport.dunningpeoplename}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningperiod}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningtasknum}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningtaskfinished}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningtaskrate}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningtelnum}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningsmsnum}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningrepaymentamount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningpayoffamount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningpayoffcapital}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningprofitamount}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
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

				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SMisDunningTaskMonthReport.datetime}" pattern="yyyy-MM"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
					
				<form:select id="monthdesc"  path="monthdesc" class="input-medium" >
					<form:option  selected="selected" value="" label="整月"/>
					<form:option  value="上半月" label="上半月"/>
					<form:option  value="下半月" label="下半月"/>
				</form:select>
					
			</li>
			<li><label>催款员：</label>
				<form:select id="name"  path="name" class="input-medium" >
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
				<th>催收员队列</th>
				<th>案件队列</th>
				<th>应催订单数</th>
				<th>Q0剩余应催订单数</th>
				<th>催回订单数</th>
				<th>应催本金</th>
				<th>Q0剩余应催本金</th>
				<th>催回本金</th>
				<th>应催金额</th>
				<th>催回金额</th>
				<th>绩效催回金额</th>
				<th>催回本金（含续期）</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sMisDunningTaskMonthReport">
			<tr>
				<td>
					${sMisDunningTaskMonthReport.months}-${sMisDunningTaskMonthReport.monthdesc}
				</td>
				<td>
					${sMisDunningTaskMonthReport.name}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningcycle}
				</td>
				<td>
					${sMisDunningTaskMonthReport.taskdunningcycle}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningordernumber}
				</td>
				<td>
					${sMisDunningTaskMonthReport.unfinishedtask}
				</td>
				<td>
					${sMisDunningTaskMonthReport.finishedordernumber}
				</td>
				<td>
					${sMisDunningTaskMonthReport.dunningcorpusamount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.unfinishedcorpusamount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.finishedcorpusamount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.amount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.creditamount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.finishedAndDelayAmount}
				</td>
				<td>
					${sMisDunningTaskMonthReport.finishedanddelaycorpusamount}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
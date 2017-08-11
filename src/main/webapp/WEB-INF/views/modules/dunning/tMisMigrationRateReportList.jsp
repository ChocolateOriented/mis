<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>贷后迁徙日报</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	 $("#migrateReportExport").click(function(){
			top.$.jBox.confirm("确认要导出迁徙数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					location.href="${ctx}/dunning/tMisMigrationRateReport/migrateExport?datetimeEnd="+$("#datetimeEnd").val()+"&monthUpDown="+$("#monthUpDown").val();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		 });
	
});
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/dunning/tMisMigrationRateReport/list");
	$("#searchForm").submit();
	return false;
}


</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisMigrationRateReport/list">贷后迁徙日报</a></li>
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/migratechart">贷后迁徙户数图表</a></li>
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/migrateAmountchart">贷后迁徙本金图表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisMigrationRateReport" action="${ctx}/dunning/tMisMigrationRateReport/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li>
				<input name="datetime" type="text" readonly="readonly" maxlength="10" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisMigrationRateReport.datetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			
			<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="日期查询" onclick="return page();" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<form:form id="searchForm1" modelAttribute="TMisMigrationRateReport" action="${ctx}/dunning/tMisMigrationRateReport/list" method="post" class="breadcrumb form-search">
		<input id="pageNo1" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize1" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li>
				<input id="datetimeEnd" name="datetimeEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisMigrationRateReport.datetimeEnd}" pattern="yyyy-MM"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
				<form:select  id="monthUpDown" path="monthUpDown" >
					<form:option selected="selected" value="up" label="上半月"/>
					 <form:option value="down" label="下半月"/>
				</form:select>
			</li>
			
			<li class="btns">
			<input id="btnSubmit1" class="btn btn-primary" type="submit" value="周期查询"  "/>
			<input id="migrateReportExport" class="btn btn-primary" type="button" value="导出" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th ></th>
				<th ></th>
				<th colspan="4" style="text-align:center;">户数迁徙</th>
				<th colspan="4" style="text-align:center;">本金迁徙</th>
			</tr>
			<tr>
				<th >日期</th>
				<th >周期</th>
				<th >C-P1</th>
				<th >C-P2</th>
				<th >C-P3</th>
				<th >C-P4</th>
				<th >C-P1</th>
				<th >C-P2</th>
				<th >C-P3</th>
				<th >C-P4</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${page.list}" var="migrate" varStatus="vs">
				<tr>
				  <td><fmt:formatDate value="${migrate.datetime }" pattern="yyyy-MM-dd"/></td>
				  <td>${migrate.datetimeEndText}</td>
				  <td>${migrate.cp1newText}</td>
				  <td>${migrate.cp2newText }</td>
				  <td>${migrate.cp3newText }</td>
				  <td>${migrate.cp4newText }</td>
				  <td>${migrate.cp1corpusText}</td>
				  <td>${migrate.cp2corpusText}</td>
				  <td>${migrate.cp3corpusText }</td>
				  <td>${migrate.cp4corpusText }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>	
	<div class="pagination">${page}</div>
</body>
</html>
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
					$.ajax({
						url:"${ctx}/dunning/tMisMigrationRateReport/migrateExport",
						type:"GET",
						data:{},
						success:function(data){
							if("OK"==data){
								$.jBox.tip("导出成功");
								return;
							}else{
								$.jBox.tip("导出失败");
								return;
							}
							
						},
						 error : function(XMLHttpRequest, textStatus, errorThrown){
		                     alert("删除失败:"+textStatus);
		                  }
				});
			}
		 });
	 });
});
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
location.href="${ctx}/dunning/tMisMigrationRateReport/list?pageNo="+n+"&pageSize="+s;
}


</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisMigrationRateReport/list">贷后迁徙日报</a></li>
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/migratechart">贷后迁徙户数图表</a></li>
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/migrateAmountchart">贷后迁徙本金图表</a></li>
	</ul>
<!-- 	  <li class="btns"><input id="migrateReportExport" class="btn btn-primary" type="button" value="导出" /></li> -->
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th ></th>
				<th colspan="4" style="text-align:center;">户数迁徙</th>
				<th colspan="4" style="text-align:center;">本金迁徙</th>
			</tr>
			<tr>
				<th >日期</th>
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
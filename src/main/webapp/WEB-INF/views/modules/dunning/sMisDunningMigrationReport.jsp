<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>贷后迁徙日报</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	
});


</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/sMisDunningTaskMonthReport/migrationdata">贷后迁徙日报</a></li>
		<li><a href="${ctx}/dunning/sMisDunningTaskMonthReport/migrationRate">贷后迁徙图表</a></li>
	</ul>
	
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
			<tr>
			  <td>20170701</td>
			  <td>1%</td>
			  <td>2%</td>
			  <td>3%</td>
			  <td>4%</td>
			  <td>5%</td>
			  <td>6%</td>
			  <td>7%</td>
			  <td>8%</td>
			</tr>
			<tr>
			  <td>20170702</td>
			  <td>11%</td>
			  <td>22%</td>
			  <td>33%</td>
			  <td>44%</td>
			  <td>55%</td>
			  <td>66%</td>
			  <td>77%</td>
			  <td>88%</td>
			</tr>
		</tbody>
	</table>	
</body>
</html>
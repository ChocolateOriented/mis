<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>本期收入</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>

	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>分类</th>
				<th>金额</th>
				<th>金额占比</th>
			</tr>
		</thead>
		<tbody>
		<tr>
			<td> 
				信审收入
			</td>
			<td>
				${detailsBean.costamountText}
			</td>
			<td>
				${detailsBean.costamountpercentText}
			</td>
		</tr>
		<tr>
			<td> 
				延期收入
			</td>
			<td>
				${detailsBean.delayedamountText}
			</td>
			<td>
				${detailsBean.delayedamountpercentText}
			</td>
		</tr>
		<tr>
			<td> 
				逾期收入
			</td>
			<td>
				${detailsBean.overdueamountText}
			</td>
			<td>
				${detailsBean.overdueamountpercentText}
			</td>
		</tr>
		</tbody>
	</table>
</body>
</html>
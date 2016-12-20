<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>本期累计</title>
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
				放款
			</td>
			<td>
				${detailsBean.remitorderamountText}
			</td>
			<td>
				${detailsBean.remitorderamountpercentText}
			</td>
		</tr>
		<tr>
			<td> 
				延期
			</td>
			<td>
				${detailsBean.delayedorderamountText}
			</td>
			<td>
				${detailsBean.delayedorderamountpercentText}
			</td>
		</tr>
		</tbody>
	</table>
</body>
</html>
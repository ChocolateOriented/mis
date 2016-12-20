<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>减免</title>
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
<!-- 				<th>老用户消耗</th> -->
<!-- 				<th>新用户消耗</th> -->
			</tr>
		</thead>
		<tbody>
		<tr>
			<td> 
				抵扣券
			</td>
			<td>
				${detailsBean.couponamountText}
			</td>
		</tr>
		<tr>
			<td> 
				催收减免
			</td>
			<td>
				${detailsBean.pressamountText}
			</td>
		</tr>
		<tr>
			<td> 
				等待减免
			</td>
			<td>
				${detailsBean.waitingamountText}
			</td>
		</tr>
		</tbody>
	</table>
</body>
</html>
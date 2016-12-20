<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收金额详情</title>
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
				<th>委外时段</th>
				<th>单数</th>
				<th>金额</th>
				<th>佣金</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="d">
				<tr>
					<td> 
						${d.dunningperiod} (天)
					</td>
					<td>
						${d.payoffOrderNum}
					</td>
					<td>
						${d.payoffOrderAmount}
					</td>
					<td>
						${d.basicCommission}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
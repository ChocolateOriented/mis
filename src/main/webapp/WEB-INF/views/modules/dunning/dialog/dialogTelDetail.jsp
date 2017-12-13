<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通话记录</title>
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
				<th>时间</th>
				<th>催收人</th>
				<th>操作人</th>
				<th>行动码</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${tmiscontantrecord}" var="tmiscontantrecord">
			<tr>
				<td> 
					<fmt:formatDate value="${tmiscontantrecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tmiscontantrecord.peoplename}
				</td>
				<td>${tmiscontantrecord.createBy.name}</td>
				<td>
					${tmiscontantrecord.telstatusstr}
				</td>
				<td>
					${tmiscontantrecord.remark}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
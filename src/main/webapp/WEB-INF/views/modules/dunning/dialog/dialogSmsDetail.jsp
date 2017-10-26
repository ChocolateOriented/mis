<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信记录</title>
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
				<th>短信内容</th>
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
					${tmiscontantrecord.content}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title >呼叫信息</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	function page(n, s) {
		if (n) window.parent.$("#page").val(n);
		if (s) window.parent.$("#pageSize").val(s);
		window.location = "${pageContext.request.contextPath}/f/numberPhone/${type}?" + window.parent.$("#searchForm").serialize();
		return false;
	}
</script>
</head>
<body>
	<table id="accountTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>时间</th>
				<th>通话类型</th>
				<th>号码</th>
				<th>归属地</th>
				<th>姓名</th>
				<th>通话状态</th>
				<th>通话时长</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="calling" varStatus="status">
				<tr>
					<td>
						${ status.index + 1}
					</td>
					<td>
						<fmt:formatDate value="${calling.callTime}" pattern="HH:mm:ss"/>
					</td>
					<td>
						${calling.callType.desc}
					</td>
					<td>
						${calling.targetNumber}
					</td>
					<td>
						${calling.location}
					</td>
					<td>
						${calling.targetName}
					</td>
					<td>
						<c:choose>
							<c:when test="${type eq 'callAll' and (calling.callStateText eq '未接' or calling.callStateText eq '队列中放弃')}">
								<strong style="color:red;">${calling.callStateText}</strong>
							</c:when>
							<c:otherwise>
								${calling.callStateText}
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						${calling.durationTimeText}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>

</body>
</html>
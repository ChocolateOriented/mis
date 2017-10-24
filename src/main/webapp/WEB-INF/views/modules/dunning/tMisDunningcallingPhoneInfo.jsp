<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title >呼出呼入</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

    

  function page(n, s) {
          if (n) window.parent.$("#page").val(n);
          if (s) window.parent.$("#pageSize").val(s);
          if('${call}'=="callin"){
	          window.location = "/mis/f/numberPhone/callin?" + window.parent.$("#searchForm").serialize();
          }
          if('${call}'=="callout"){
	          window.location = "/mis/f/numberPhone/callout?" + window.parent.$("#searchForm").serialize();
          }
          return false;
   }
	</script>
</head>
<body>
	<table id="accountTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<c:if test="${call eq 'callout' }">
				<th>呼出时间</th>
				<th>呼出号码</th>
				<th>通话时长</th>
				</c:if>
				<c:if test="${call eq 'callin' }">
				<th>呼入时间</th>
				<th>呼入号码</th>
				<th>通话时长</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="calling" varStatus="status">
				<tr>
					<td>
						${ status.index + 1}
					</td>
					<c:if test="${call eq 'callout' }">
						<td>
							${calling.callOutTime}
							
						</td>
						<td>
							${calling.target }
						</td>
						<td>
							<c:if test="${calling.channelAnswerTime eq 0 }">
							 无应答
							</c:if>
							<c:if test="${calling.channelAnswerTime ne 0}">
							${calling.callTotalTime }
							</c:if>
						</td>
					</c:if>
					<c:if test="${call eq 'callin' }">
						<td>
							${calling.startTime}
						</td>
						<td>
							${calling.caller }
						</td>
						<td>
							<c:if test="${calling.agentStartTime eq 0 }">
							 未接
							</c:if>
							<c:if test="${calling.agentStartTime ne 0}">
							${calling.callTotalTime }
							</c:if>
						</td>
					</c:if>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>

</body>
</html>
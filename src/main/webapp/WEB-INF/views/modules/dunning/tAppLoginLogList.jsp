<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
<!-- 				<th>序号</th> -->
				<th>手机号</th>
				<th>设备手机号</th>
				<th>设备ID</th>
				<th>mo9产品</th>
				<th>市场名</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${appLoginLogs}" var="appLoginLog" varStatus="vs"> 
			<tr>
<!-- 				<td> -->
<%-- 					${vs.index +1} --%>
<!-- 				</td> -->
				<td>
					${appLoginLog.mobile}
				</td>
				<td>
					${appLoginLog.localMobile}
				</td>
				<td>
					${appLoginLog.deviceModel}
				</td>
				<td>
<%-- 					${appLoginLog.mo9ProductName} --%>
					${fns:getDictLabel(appLoginLog.mo9ProductName, 'mo9ProductName', appLoginLog.mo9ProductName)}
				</td>
				<td>
					${fns:getDictLabel(appLoginLog.marketName, 'marketName', appLoginLog.marketName)}
				</td>
				<td>
					<fmt:formatDate value="${appLoginLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach> 
		</tbody>
	</table>
</body>
</html>
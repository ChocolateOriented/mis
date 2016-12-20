<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>还款成本</title>
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
				<th>渠道</th>
				<th>子渠道</th>
				<th>费率</th>
				<th>金额</th>
				<th>金额占比</th>
				<th>笔数</th>
				<th>笔数占比</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${repayBeans}" var="repayBean">
			<tr>
				<td> 
					${repayBean.channelname}
				</td>
				<td>
					${repayBean.subchannelname}
				</td>
				<td>
					${repayBean.feerateText}
				</td>
				<td> 
					${repayBean.repayamountText}
				</td>
				<td>
					${repayBean.repayamountpercentText}
				</td>
				<td>
					${repayBean.repaycountText}
				</td>
				<td>
					${repayBean.repaycountpercentText}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
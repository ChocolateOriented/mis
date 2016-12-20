<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新增交易用户</title>
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
				<th>来源</th>
				<th>用户数</th>
				<th>用户占比</th>
			</tr>
		</thead>
		<tbody>
		<tr>
			<td> 
				App
			</td>
			<td>
				${detailsBean.newuserappText}
			</td>
			<td>
				${detailsBean.newuserapppercentText}
			</td>
		</tr>
		<tr>
			<td> 
				Wechat
			</td>
			<td>
				${detailsBean.newuserwechatText}
			</td>
			<td>
				${detailsBean.newuserwechatpercentText}
			</td>
		</tr>
		</tbody>
	</table>
</body>
</html>
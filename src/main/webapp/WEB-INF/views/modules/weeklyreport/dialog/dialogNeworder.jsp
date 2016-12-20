<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>放贷笔数</title>
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
				<th>笔数</th>
				<th>笔数占比</th>
			</tr>
		</thead>
		<tbody>
		<tr>
			<td> 
				新用户放款
			</td>
			<td>
				${detailsBean.newUserNewOrdersText}
			</td>
			<td>
				${detailsBean.newUserNewOrdersPercentText}
			</td>
		</tr>
		<tr>
			<td> 
				老用户放款
			</td>
			<td>
				${detailsBean.oldUserNewOrdersText}
			</td>
			<td>
				${detailsBean.oldUserNewOrdersPercentText}
			</td>
		</tr>
		<tr>
			<td> 
				老用户延期
			</td>
			<td>
				${detailsBean.oldUserDelayOrdersText}
			</td>
			<td>
				${detailsBean.oldUserDelayOrdersPercentText}
			</td>
		</tr>
		</tbody>
	</table>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>500元</th>
				<th>1000元</th>
				<th>1500元</th>
			</tr>
		</thead>
		<tbody>
		<tr>
			<td> 
				7天
			</td>
			<td>
				${detailsBean.order7DaysPercent500Text}
			</td>
			<td>
				${detailsBean.order7DaysPercent1000Text}
			</td>
			<td>
				${detailsBean.order7DaysPercent1500Text}
			</td>
		</tr>
		<tr>
			<td> 
				14天
			</td>
			<td>
				${detailsBean.order14DaysPercent500Text}
			</td>
			<td>
				${detailsBean.order14DaysPercent1000Text}
			</td>
			<td>
				${detailsBean.order14DaysPercent1500Text}
			</td>
		</tr>
		</tbody>
	</table>
</body>
</html>
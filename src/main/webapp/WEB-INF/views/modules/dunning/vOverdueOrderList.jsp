<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>逾期未还订单视图管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function test(){
			$.ajax({url:"${ctx}/dunning/vOverdueOrder/test",async:false});
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/VOverdueOrder/">逾期未还订单视图列表</a></li>
		<shiro:hasPermission name="dunning:vOverdueOrder:edit"><li><a href="${ctx}/dunning/vOverdueOrder/form">逾期未还订单视图添加</a></li></shiro:hasPermission>
	</ul>
	<button type="button" onclick = "test()">测试专用</button>
	<form:form id="searchForm" modelAttribute="VOverdueOrder" action="${ctx}/dunning/vOverdueOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>逾期订单号：</label>
				<form:input path="dealcode" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>借款人手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="13" class="input-medium"/>
			</li>
			<li><label>借款人姓名：</label>
				<form:input path="realName" htmlEscape="false" maxlength="30" class="input-medium"/>
			</li>
			<li><label>借款人身份证号：</label>
				<form:input path="idcard" htmlEscape="false" maxlength="18" class="input-medium"/>
			</li>
			<li><label>逾期天数：</label>
				<form:input path="overdueDays" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>逾期订单号</th>
				<th>创建时间</th>
				<th>放款时间</th>
				<th>订单应还款时间</th>
				<th>借款人id</th>
				<th>借款人手机</th>
				<th>借款人姓名</th>
				<th>借款人身份证号</th>
				<th>借款周期</th>
				<th>逾期天数</th>
				<th>本金</th>
				<th>手续费</th>
				<th>订单金额</th>
				<th>逾期罚息金额</th>
				<th>折扣金额</th>
				<th>已还金额</th>
				<th>催收减免金额</th>
				<th>实际应还金额</th>
				<shiro:hasPermission name="dunning:vOverdueOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vOverdueOrder">
			<tr>
				<td><a href="${ctx}/dunning/vOverdueOrder/form?id=${vOverdueOrder.id}">
					${vOverdueOrder.dealcode}
				</a></td>
				<td>
					<fmt:formatDate value="${vOverdueOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${vOverdueOrder.remitTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${vOverdueOrder.repaymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${vOverdueOrder.buyerId}
				</td>
				<td>
					${vOverdueOrder.mobile}
				</td>
				<td>
					${vOverdueOrder.realName}
				</td>
				<td>
					${vOverdueOrder.idcard}
				</td>
				<td>
					${vOverdueOrder.days}
				</td>
				<td>
					${vOverdueOrder.overdueDays}
				</td>
				<td>
					${vOverdueOrder.corpusAmount}
				</td>
				<td>
					${vOverdueOrder.costAmout}
				</td>
				<td>
					${vOverdueOrder.amount}
				</td>
				<td>
					${vOverdueOrder.overdueAmount}
				</td>
				<td>
					${vOverdueOrder.discountAmount}
				</td>
				<td>
					${vOverdueOrder.balance}
				</td>
				<td>
					${vOverdueOrder.modifyAmount}
				</td>
				<td>
					${vOverdueOrder.creditAmount}
				</td>
				<shiro:hasPermission name="dunning:vOverdueOrder:edit"><td>
    				<a href="${ctx}/dunning/vOverdueOrder/form?id=${vOverdueOrder.id}">修改</a>
					<a href="${ctx}/dunning/vOverdueOrder/delete?id=${vOverdueOrder.id}" onclick="return confirmx('确认要删除该逾期未还订单视图吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
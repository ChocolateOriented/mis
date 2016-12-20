<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务确认汇款信息管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/">财务确认汇款信息列表</a></li>
		<shiro:hasPermission name="dunning:tMisRemittanceMessage:edit"><li><a href="${ctx}/dunning/tMisRemittanceMessage/form">财务确认汇款信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisRemittanceMessage" action="${ctx}/dunning/tMisRemittanceMessage/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>汇款人姓名：</label>
				<form:input path="remittancename" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>汇款时间：</label>
				<input name="remittancetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tMisRemittanceMessage.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>汇款渠道：</label>
				<form:input path="remittancechannel" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>财务确认人：</label>
				<form:input path="financialuser" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>汇款人姓名</th>
				<th>汇款时间</th>
				<th>金额</th>
				<th>汇款渠道</th>
				<th>汇款帐号</th>
				<th>财务确认人</th>
				<th>财务确认时间</th>
				<th>汇款图片</th>
				<shiro:hasPermission name="dunning:tMisRemittanceMessage:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisRemittanceMessage">
			<tr>
				<td><a href="${ctx}/dunning/tMisRemittanceMessage/form?id=${tMisRemittanceMessage.id}">
					${tMisRemittanceMessage.id}
				</a></td>
				<td>
					${tMisRemittanceMessage.remittancename}
				</td>
				<td>
					<fmt:formatDate value="${tMisRemittanceMessage.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisRemittanceMessage.remittanceamount}
				</td>
				<td>
					${tMisRemittanceMessage.remittancechannel}
				</td>
				<td>
					${tMisRemittanceMessage.remittanceaccount}
				</td>
				<td>
					${tMisRemittanceMessage.financialuser}
				</td>
				<td>
					<fmt:formatDate value="${tMisRemittanceMessage.financialtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisRemittanceMessage.remittanceimg}
				</td>
				<shiro:hasPermission name="dunning:tMisRemittanceMessage:edit"><td>
    				<a href="${ctx}/dunning/tMisRemittanceMessage/form?id=${tMisRemittanceMessage.id}">修改</a>
					<a href="${ctx}/dunning/tMisRemittanceMessage/delete?id=${tMisRemittanceMessage.id}" onclick="return confirmx('确认要删除该财务确认汇款信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务log管理</title>
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
		<li class="active"><a href="${ctx}/dunning/tMisDunningTaskLog/">催收任务log列表</a></li>
		<shiro:hasPermission name="dunning:tMisDunningTaskLog:edit"><li><a href="${ctx}/dunning/tMisDunningTaskLog/form">催收任务log添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tMisDunningTaskLog" action="${ctx}/dunning/tMisDunningTaskLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="dealcode" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>行为状态：</label>
				<form:select path="behaviorstatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>订单状态：</label>
				<form:select path="dealcodestatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>还清时间：</label>
				<input name="payofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tMisDunningTaskLog.payofftime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>用户姓名：</label>
				<form:input path="realname" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>dbid</th>
				<th>id</th>
				<th>订单号</th>
				<th>催收人id</th>
				<th>催收人姓名</th>
				<th>催收周期(队列)</th>
				<th>行为状态</th>
				<th>订单状态</th>
				<th>还清时间</th>
				<th>借款时长</th>
				<th>用户姓名</th>
				<th>手机号</th>
				<th>到期还款日期</th>
				<th>逾期天数</th>
				<th>本金</th>
				<th>手续费</th>
				<th>实际应还金额</th>
				<th>逾期费</th>
				<th>催收减免金额</th>
				<th>延期费</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>修改人</th>
				<th>修改时间</th>
				<shiro:hasPermission name="dunning:tMisDunningTaskLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisDunningTaskLog">
			<tr>
				<td><a href="${ctx}/dunning/tMisDunningTaskLog/form?id=${tMisDunningTaskLog.id}">
					${tMisDunningTaskLog.dbid}
				</a></td>
				<td>
					${tMisDunningTaskLog.id}
				</td>
				<td>
					${tMisDunningTaskLog.dealcode}
				</td>
				<td>
					${tMisDunningTaskLog.dunningpeopleid}
				</td>
				<td>
					${tMisDunningTaskLog.dunningpeoplename}
				</td>
				<td>
					${tMisDunningTaskLog.dunningcycle}
				</td>
				<td>
					${fns:getDictLabel(tMisDunningTaskLog.behaviorstatus, '', '')}
				</td>
				<td>
					${fns:getDictLabel(tMisDunningTaskLog.dealcodestatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningTaskLog.payofftime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisDunningTaskLog.days}
				</td>
				<td>
					${tMisDunningTaskLog.realname}
				</td>
				<td>
					${tMisDunningTaskLog.mobile}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningTaskLog.repaymenttime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisDunningTaskLog.overduedays}
				</td>
				<td>
					${tMisDunningTaskLog.corpusamount}
				</td>
				<td>
					${tMisDunningTaskLog.costamout}
				</td>
				<td>
					${tMisDunningTaskLog.creditamount}
				</td>
				<td>
					${tMisDunningTaskLog.overdueamount}
				</td>
				<td>
					${tMisDunningTaskLog.modifyamount}
				</td>
				<td>
					${tMisDunningTaskLog.delayamount}
				</td>
				<td>
					${tMisDunningTaskLog.createby}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningTaskLog.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisDunningTaskLog.updateby}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningTaskLog.updatedate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="dunning:tMisDunningTaskLog:edit"><td>
    				<a href="${ctx}/dunning/tMisDunningTaskLog/form?id=${tMisDunningTaskLog.id}">修改</a>
					<a href="${ctx}/dunning/tMisDunningTaskLog/delete?id=${tMisDunningTaskLog.id}" onclick="return confirmx('确认要删除该催收任务log吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
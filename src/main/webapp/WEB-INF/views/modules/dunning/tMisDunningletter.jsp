<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>信函管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			
			
			
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningletter/findOrderPageList">信函管理</a></li>
		<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
			<span id="successRate" style="float:right;padding:8px;"></span>
		</shiro:hasPermission>
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="TMisDunningLetter" action="${ctx}/dunning/tMisDunningletter/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

		<ul class="ul-form">
			<li><label>姓名</label>
				<form:input path="realname"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>订单号</label>
				<form:input path="dealcode"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>发函结果</label>
			<form:select  id="status" path="status" class="input-medium">
			</form:select>
			</li>
			
			<li><label>逾期天数</label>
				<form:input  path="beginOverduedays"  htmlEscape="false" maxlength="3" class="digits"  style="width:35px;"  />
				- 
				<form:input  path="endOverduedays"  htmlEscape="false" maxlength="3" class="digits" style="width:35px;"   />
			</li>
			
			<li><label>信函发送时间</label>
				<input name="beginPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginPayofftime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
			</li>
			<li><label>结果更新时间</label>
				<input name="beginRepaymenttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginRepaymenttime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page();"/></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
			<li class="btns"><input id="exportOuterFile" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>

	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder"  /></th>
				<th>序号</th>
				<th>订单号</th>
				<th>姓名</th>
				<th>发函结果</th>
				<th>欠款金额</th>
				<th>逾期天数</th>
				<th>户籍地址</th>
				<th>信函发送时间</th>
				<th>结果更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dunningOrder" varStatus="vs">
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
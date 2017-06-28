<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>入账对公明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/detail");
			$("#searchForm").submit();
        	return false;
        }
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/detail">催收任务列表</a></li>
		
		<span id="successRate" style="float:right;padding:8px;"></span>
		
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm"  modelAttribute="TMisRemittanceMessage" action="${ctx}/dunning/tMisRemittanceMessage/detail" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易流水号</label>
				<form:input path="remittanceSerialNumber"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>交易时间</label>
				<input name="begindealtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
<%-- 					value="<fmt:formatDate value="1997-12-12" pattern="yyyy-MM-dd"/>" --%>
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="enddealtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
<%-- 					value="<fmt:formatDate value="1997-12-18" pattern="yyyy-MM-dd"/>" --%>
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>交易金额</label>
				<form:input path="remittanceAmount"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>对方账户</label>
				<form:input path="remittanceAccount"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>对方姓名</label>
				<form:input  path="remittanceName"  htmlEscape="false" maxlength="128" class="input-medium" />
			</li>
			
			<li><label>入账状态</label>
			<form:select  id="status" path="accountStatus" class="input-medium">
				<form:option selected="selected" value="" label="全部状态"/>
				<c:forEach items="${statusList}" var="status">
				 	<form:option value="${status}" label="${status.desc}"/>
				</c:forEach>
			</form:select>
			</li>
			</br>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
		</ul>
	</form:form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>交易流水号</th>
				<th>交易时间</th>
				<th>交易渠道</th>
				<th>交易金额（元）</th>
				<th>对方名称</th>
				<th>对方账户</th>
				<th>备注</th>
				<th>入账状态</th>
				<th>上传时间</th>
				<th>上传人</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tmessage" varStatus="status">
			<tr>
				<td>
				 	${status.count}
				</td>
				<td>
					 ${tmessage.remittanceSerialNumber } 
				</td>
				<td>
					 <fmt:formatDate value="${tmessage.remittanceTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					 ${tmessage.remittanceChannel }
				</td>
				<td>
					 ${tmessage.remittanceAmount }
				</td>
				<td>
					  ${tmessage.remittanceName }
				</td>
				<td>
					 ${tmessage.remittanceAccount}
				</td>
				<td>
					  ${tmessage.remark }  
				</td>
				<td>
					  ${tmessage.accountStatus }  
				</td>
				<td>
					  ${tmessage.createDate }  
				</td>
				<td>
					  ${tmessage.financialUser }
				</td>
				
				</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title >已完成</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		});
	</script>
</head>
<body>
	<input type="hidden" id="childIfam" value="completed">
	<ul class="nav nav-tabs">
	<li><a href="${ctx}/dunning/tMisRemittanceMessage/checked">已查账</a></li>
	<li  class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/completed">已完成</a></li>
	</ul> 
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号2</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>订单编号</th>
				<th>崔收入</th>
				<th>欠款金额</th>
				<th>减免金额</th>
				<th>应还金额</th>
				<th>交易流水号</th>
				<th>查账人</th>
				<th>订单状态</th>
				<th>入账标签</th>
				<th>入账类型</th>
				<th>更新时间</th>
				<th>入账人</th>
				
				
			</tr>
		</thead>
		<tbody>
<%-- 		<c:forEach items="${page.list}" var="tmessage" varStatus="status"> --%>
<!-- 			<tr> -->
				
<!-- 				<td> -->
<%-- 				 	${status.count} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					 ${tmessage.remittanceSerialNumber }  --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					 <fmt:formatDate value="${tmessage.remittancetime }" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					 ${tmessage.remittancechannel }  --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					 ${tmessage.remittanceamount }  --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					  ${tmessage.remittancename }   --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					 ${tmessage.remittanceaccount}  --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					  ${tmessage.remark }   --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					  ${tmessage.accountStatus }   --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					  ${tmessage.createDate }   --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					  ${tmessage.financialuser }   --%>
<!-- 				</td> -->
				
<!-- 				</tr> -->
<%-- 		</c:forEach> --%>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
</body>
</html>
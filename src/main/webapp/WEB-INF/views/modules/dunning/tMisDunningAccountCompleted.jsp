<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title id="title">已完成</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		});
		
		 function page(n, s) {
		        if (n) window.parent.$("#pageNo").val(n);
		        if (s) window.parent.$("#pageSize").val(s);
		       window.parent. $("#searchForm").attr("action", "${ctx}/dunning/tMisRemittanceMessage/confirmList?childPage=completed");
		       window.parent.$("#searchForm").submit();
		        return false;
		      }
	</script>
</head>
<body>
	<input type="hidden" id="childIfam" value="completed">
	<ul class="nav nav-tabs">
	<li ><a href="${ctx}/dunning/tMisRemittanceMessage/checked?child=true">已查账</a></li>
	<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/completed?child=true">已完成</a></li>
	</ul> 
	<sys:message content="${message}"/>
	<table id="accountTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
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
		<c:forEach items="${pagecompleted.list}" var="tmessage" varStatus="status">
			<tr>
				
				<td>
				 	${status.count}
				</td>
				<td>
					 ${tmessage.realName } 
				</td>
				<td>
				 	${tmessage.mobile } 
				</td>
				<td>
					 ${tmessage.dealcode } 
				</td>
				<td>
					 ${tmessage.nickName } 
				</td>
				<td>
					  ${tmessage.amount }  
				</td>
				<td>
					  ${tmessage.modifyamount} 
				</td>
				<td>
					  ${tmessage.creditamount }  
				</td>
				<td>
					  ${tmessage.remittanceSerialNumber }  
				</td>
				<td>
					    
				</td>
				<td>
					  ${tmessage.orderStatus }  
				</td>
				<td>
					  ${tmessage.remittanceTag }  
				</td>
				<td>
					  ${tmessage.payType } 
				</td>
				
				<td>
					 <fmt:formatDate value="${tmessage.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>   
				</td>
				<td>
					  ${tmessage.completePeople }
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${pagecompleted}</div>
	
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function openSerialRepayWindow(dealcode) {
          var url = "${ctx}/dunning/tMisDunningTask/orderSerialRepayList?dealcode=" + dealcode;
          $.jBox.open("iframe:" + url, "还款流水", 900, 400, {
            buttons: {},
            loaded: function (h) {
              $(".jbox-content", document).css("overflow-y", "hidden");
            }
          });
        }
	</script>
</head>
<body>
	<%-- <ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisDunningTask/">催收任务列表</a></li>
		<li class="active">
		<a> 催收信息 </a></li>
	</ul> --%>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>订单编号</th>
				<th>订单类型</th>
				<th>订单金额</th>
				<th>借款期限</th>
				<th>订单提交时间</th>
				<th>订单放款时间</th>
				<th>到期还款日期</th>
				<th>逾期天数</th>
				<th>逾期费</th>
				<th>订单状态</th>
				<th>还清日期</th>
				<th>还款金额</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${orderHistories}" var="orderHistory" varStatus="vs"> 
			<tr>
				<td>
					${vs.index +1}
				</td>
				<td>
                    <a onclick="openSerialRepayWindow('${orderHistory.dealcode}')">
                        ${orderHistory.dealcode}
                    </a>
				</td>
				<td>
					${orderHistory.ordertype}
				</td>
				<td>
					${orderHistory.creditamountText}
				</td>
				<td>
					${orderHistory.days}
				</td>
				<td>
 					<%-- <fmt:formatDate value="${orderHistory.createtime}" pattern="yyyy-MM-dd "/> --%>
					<%-- ${orderHistory.createtime} --%>
					${fn:substring(orderHistory.createtime, 0, 16)}
				</td>
				<td>
					${orderHistory.remittime}
				</td>
				<td>
					<fmt:formatDate value="${orderHistory.repaymenttime}" pattern="yyyy-MM-dd "/>
<%-- 					${orderHistory.repaymenttime} --%>
				</td>
				<td>
					${orderHistory.delaydays}
				</td>
				<td>
					${orderHistory.overdueamountText}
				</td>
				<td>
					${orderHistory.status}
				</td>
				<td>
					<fmt:formatDate value="${orderHistory.payofftime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${orderHistory.amountText}
				</td>
			</tr>
		</c:forEach> 
		</tbody>
	</table>
</body>
</html>
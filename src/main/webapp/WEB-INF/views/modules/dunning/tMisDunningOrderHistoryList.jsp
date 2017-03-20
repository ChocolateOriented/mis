<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#btnPaid').attr("disabled","disabled");
			var url = "${ctx}/dunning/tMisDunningTask/apploginlogList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&mobile=" + $('#mobile', parent.document).val();
			$("#applogiglog_a").attr("href",url);
			
			if("${ispayoff}" == "true"){
				$('#btnSms').attr("disabled","disabled");
				$('#btnTel').attr("disabled","disabled");
				$('#btnAmount').attr("disabled","disabled");
				$('#btnPaid').attr("disabled","disabled");
				$('#btnConfirm').attr("disabled","disabled");
				window.parent.$("#btnTelTaskFather").attr("disabled","disabled");
			}
		});
		
		function collectionfunction(obj){
			var method = $(obj).attr("method");
			var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}" ;
			$.jBox.open("iframe:" + url, $(obj).attr("value") , 600, 430, {            
               buttons: {
//	            	   "确定": "ok", "取消": true
            	   },
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
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">单位&联系人</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">
			${hasContact=='true' ? '通讯录' :  '通讯录(无)'}
			</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationRecord?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">通话记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunnedConclusion/list?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">电催结论记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisContantRecord/list?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">催款历史</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li class="active"><a href="${ctx}/dunning/tMisDunningTask/orderHistoryList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">历史借款信息</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisRemittanceConfirm:insertForm"><li><a href="${ctx}/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">汇款信息</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view">
	        <li><a id="applogiglog_a" href="#" >登录日志</a></li>
        </shiro:hasPermission>
	</ul> 
	
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
				<th>续期费</th>
				<th>订单状态</th>
				<th>还清日期</th>
				<th>还款金额</th>
				<th>主订单编号</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${orderHistories}" var="orderHistory" varStatus="vs"> 
			<tr>
				<td>
					${vs.index +1}
				</td>
				<td>
<%-- 				<a href="${ctx}/dunning/tMisDunningTask/form?id=${tMisDunningTask.id}"> --%>
					${orderHistory.dealcode}
<!-- 				</a> -->
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
					${orderHistory.defaultinterestamountText}
				</td>
				<td>
					${orderHistory.status}
				</td>
				<td>
					<fmt:formatDate value="${orderHistory.payofftime}" pattern="yyyy-MM-dd HH:mm"/>
<%-- 					${orderHistory.payofftime} --%>
				</td>
				<td>
					${orderHistory.amountText}
				</td>
				<td>
					${orderHistory.roodealcode}
				</td>
			</tr>
		</c:forEach> 
		</tbody>
	</table>
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary"  method="Sms" type="button" value="催收短信" />
	<input id="btnTel" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Tel"  type="button" value="电催结论" disabled/>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
	<input id="btnAmount" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Amount"  type="button" value="调整金额" />
	<input id="btnPaid" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Paid"  type="button" value="代付" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:outsourcingview">
	<input id="btnConfirm" name="btnCollection" class="btn btn-primary" method="Confirm"  type="button" value="确认还款" />
	</shiro:hasPermission>
</body>
</html>
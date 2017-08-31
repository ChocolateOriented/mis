<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#btnPaid').attr("disabled","disabled");
			var url = "${ctx}/dunning/tMisDunningTask/apploginlogList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}&mobile=" + $('#mobile', parent.document).val();
			$("#applogiglog_a").attr("href",url);
			
			if("${ispayoff}" == "true"){
				$('#btnSms').attr("disabled","disabled");
				$('#btnTel').attr("disabled","disabled");
				$('#btnAmount').attr("disabled","disabled");
				$('#btnPaid').attr("disabled","disabled");
				$('#btnConfirm').attr("disabled","disabled");
				$('#btnDeduct').attr("disabled","disabled");
				window.parent.disableBtn();
			}
			if("false"==window.parent.$("#daikouStatus").val()){
				$("#btnDeduct").attr("disabled",true);
			}
		});
		
		function collectionfunction(obj, width, height){
			var method = $(obj).attr("method");
			var contactstype = $(obj).attr("contactstype");
			var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactstype=" + contactstype ;
			$.jBox.open("iframe:" + url, $(obj).attr("value") , width || 600, height || 430, {            
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
	<ul class="nav nav-tabs">
		<c:if test="${overdueDays>1}">
			<shiro:hasPermission name="dunning:tMisDunningTask:view"><li ><a href="${ctx}/dunning/tMisDunningTask/customerDetails?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">单位&联系人</a></li></shiro:hasPermission>
			<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationDetails?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">${hasContact=='true' ? '通讯录' :  '通讯录(无)'}</a></li></shiro:hasPermission>
	        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationRecord?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">通话记录</a></li></shiro:hasPermission>
		</c:if>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunnedConclusion/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">电催结论记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisContantRecord/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">催款历史</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/orderHistoryList?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">历史借款信息</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisRemittanceConfirm:insertForm">
			<c:if test="${not ispayoff}"><li><a href="${ctx}/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">汇款信息</a></li></c:if>
		</shiro:hasPermission> 
		<shiro:hasPermission name="dunning:tMisDunningTask:view">
	        <li class="active"><a id="applogiglog_a" href="#" >登录日志</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningDeduct:view"><li><a href="${ctx}/dunning/tMisDunningDeduct/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}">扣款信息</a></li></shiro:hasPermission>
	</ul> 
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
<!-- 				<th>序号</th> -->
				<th>手机号</th>
				<th>设备手机号</th>
				<th>设备ID</th>
				<th>mo9产品</th>
				<th>市场名</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${appLoginLogs}" var="appLoginLog" varStatus="vs"> 
			<tr>
<!-- 				<td> -->
<%-- 					${vs.index +1} --%>
<!-- 				</td> -->
				<td>
					${appLoginLog.mobile}
				</td>
				<td>
					${appLoginLog.localMobile}
				</td>
				<td>
					${appLoginLog.deviceModel}
				</td>
				<td>
<%-- 					${appLoginLog.mo9ProductName} --%>
					${fns:getDictLabel(appLoginLog.mo9ProductName, 'mo9ProductName', appLoginLog.mo9ProductName)}
				</td>
				<td>
					${fns:getDictLabel(appLoginLog.marketName, 'marketName', appLoginLog.marketName)}
				</td>
				<td>
					<fmt:formatDate value="${appLoginLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach> 
		</tbody>
	</table>
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary" contactstype="${overdueDays<=1 ? 'SELF' : ''}" method="Sms" type="button" value="催收短信" />
	<input id="btnTel" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Tel"  type="button" value="电催结论" disabled/>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
	<input id="btnAmount" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Amount"  type="button" value="调整金额" />
	<!-- <input id="btnPaid" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Paid"  type="button" value="代付" /> -->
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
	<input id="btnDeduct" name="btnCollection" onclick="window.parent.deductPreCheck(collectionfunction.bind(null, this, null, 480), document, this)" class="btn btn-primary" method="Deduct"  type="button" value="代扣" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTag:edit">
	<input id="btnTag" onclick="window.parent.tagPopup(this)" class="btn btn-primary" method="Tag" type="button" value="敏感标签" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:outsourcingview">
	<input id="btnConfirm" name="btnCollection" class="btn btn-primary" method="Confirm"  type="button" value="确认还款" />
	</shiro:hasPermission>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#btnPaid').attr("disabled","disabled");
			var url = "${ctx}/dunning/tMisDunningTask/apploginlogList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}&mobile=" + $('#mobile', parent.document).val();
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
				if("daikoufalse"==window.parent.$("#daikouStatus").val()){
					$("#btnDeduct").attr("disabled",true);
				}
		});
		
		function collectionfunction(obj, width, height){
				var method = $(obj).attr("method");
				var contactstype = $(obj).attr("contactstype");
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&mobileSelf=${mobileSelf}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactstype=" + contactstype ;
				$.jBox.open("iframe:" + url, $(obj).attr("value") , width || 600, height || 430, {            
	               buttons: {
// 	            	   "确定": "ok", "取消": true
	            	   },
	                   submit: function (v, h, f) {
// 	                	   alert(v);
// 	                       if (v == "ok") {
// 	                           var iframeName = h.children(0).attr("name");
// 	                           var iframeHtml = window.frames[iframeName];               //获取子窗口的句柄
// 	                           iframeHtml.saveOrUpdate();
// 	                           return false;
// 	                       }
	                   },
	               loaded: function (h) {
	                   $(".jbox-content", document).css("overflow-y", "hidden");
	               }
	         });
		}
		
		function page(n,s){
			$(".pageNo").val(n);
			$(".pageSize").val(s); 
			$(".submitForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<c:if test="${dunningCycle ne 'Q0'}">
			<shiro:hasPermission name="dunning:tMisDunningTask:view"><li ><a href="${ctx}/dunning/tMisDunningTask/customerDetails?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">单位&联系人</a></li></shiro:hasPermission>
			<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationDetails?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">${hasContact=='true' ? '通讯录' :  '通讯录(无)'}</a></li></shiro:hasPermission>
	        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationRecord?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">通话记录</a></li></shiro:hasPermission>
		</c:if>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunnedConclusion/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">电催结论记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisContantRecord/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">催款历史</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/orderHistoryList?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">历史借款信息</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisRemittanceConfirm:insertForm">
			<c:if test="${not ispayoff}"><li><a href="${ctx}/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">汇款信息</a></li></c:if>
		</shiro:hasPermission> 
		<shiro:hasPermission name="dunning:tMisDunningTask:view">
	        <li><a id="applogiglog_a" href="#" >登录日志</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningDeduct:view"><li  class="active"><a href="${ctx}/dunning/tMisDunningDeduct/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&dunningCycle=${dunningCycle}">扣款信息</a></li></shiro:hasPermission>
	</ul> 
	
	<form:form class="submitForm"  action="${ctx}/dunning/tMisContantRecord/list" method="get" style="display: none;">
		<input class="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input class="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/>
		<input id ="dealcode" name="dealcode" type="hidden" value="${dealcode}"/>
		<input id = "dunningtaskdbid" name="dunningtaskdbid" type="hidden" value="${dunningtaskdbid}"/>
		<input id = "mobileSelf" name="mobileSelf" type="hidden" value="${mobileSelf}"/>
		<input id = "hasContact" name="hasContact" type="hidden" value="${hasContact}"/>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>扣款发起时间</th>
				<th>扣款银行</th>
				<th>扣款卡号</th>
				<th>扣款类型</th>
				<th>扣款金额</th>
				<th>扣款渠道</th>
				<th>扣款状态</th>
				<th>扣款成功时间</th>
				<th>还款状态</th>
				<th>扣款人</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${page.list}" var="tMisDunningDeduct" varStatus="vs"> 
			<tr>
				<td>
					${(vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningDeduct.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisDunningDeduct.bankname}
				</td>
				<td>
					${fn:substring(tMisDunningDeduct.bankcard, 0, 4)} **** **** ${fn:substring(tMisDunningDeduct.bankcard, 12, 16)} ${fn:substring(tMisDunningDeduct.bankcard, 16, -1)}
				</td>
				<td>
					${tMisDunningDeduct.paytypeText}
				</td>
				<td>
					${tMisDunningDeduct.payamountText}
				</td>
				<td>
					${tMisDunningDeduct.paychannel}
				</td>
				<td title="${tMisDunningDeduct.statusDesc}">
					<c:choose>
						<c:when test="${fn:length(tMisDunningDeduct.statusDesc) > 20}">
							<c:out value="${fn:substring(tMisDunningDeduct.statusDesc, 0, 20)}..." />
						</c:when>
						<c:otherwise>
							<c:out value="${tMisDunningDeduct.statusDesc}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatDate value="${tMisDunningDeduct.finishtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisDunningDeduct.repaymentstatus.desc}
				</td>
				<td>
					${tMisDunningDeduct.createBy.name}
				</td>
			</tr>
			
		</c:forEach> 
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary" contactstype="${dunningCycle eq 'Q0' ? 'SELF' : ''}" method="Sms" type="button" value="催收短信" />
	<input id="btnTel" name="btnCollection" class="btn btn-primary" method="TelConclusion"  type="button" value="电催结论" disabled/>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
	<input id="btnAmount" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Amount"  type="button" value="调整金额" />
	<!-- <input id="btnPaid" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Paid"  type="button" value="代付" /> -->
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
	<input id="btnDeduct" name="btnCollection" onclick="window.parent.deductPreCheck(collectionfunction.bind(null, this, null, 480), document, this)" class="btn btn-primary" method="Deduct"  type="button" value="代扣" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:outsourcingview">
	<input id="btnConfirm" name="btnCollection" class="btn btn-primary" method="Confirm"  type="button" value="确认还款" />
	</shiro:hasPermission>
</body>
</html>
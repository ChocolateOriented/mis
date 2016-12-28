<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var url = "${ctx}/dunning/tMisDunningTask/apploginlogList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}&mobile=" + $('#mobile', parent.document).val();
			$("#applogiglog_a").attr("href",url);
			
			if("${ispayoff}" == "true"){
				$('#btnSms').attr("disabled","disabled");
				$('#btnTel').attr("disabled","disabled");
				$('#btnAmount').attr("disabled","disabled");
				$('#btnPaid').attr("disabled","disabled");
				$('#btnConfirm').attr("disabled","disabled");
			}
		});
		
		function collectionfunction(obj){
				var method = $(obj).attr("method");
	// 			var aid = $(obj).attr("aid");
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}" ;
// 				alert(url);
				$.jBox.open("iframe:" + url, $(obj).attr("value") , 600, 500, {            
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
	<%-- <ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisDunningTask/">催收任务列表</a></li>
		<li class="active">
		<a>
		催收信息</a></li>
	</ul> --%>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">单位&联系人</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">
			${hasContact=='true' ? '通讯录' :  '通讯录(无)'}
			</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationRecord?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">通话记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li class="active"><a href="${ctx}/dunning/tMisContantRecord/list?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">催款历史</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/orderHistoryList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">历史借款信息</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisRemittanceConfirm:insertForm"><li><a href="${ctx}/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">汇款信息</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view">
	        <li><a id="applogiglog_a" href="#" >登录日志</a></li>
        </shiro:hasPermission>
	</ul> 
	
	<form:form class="submitForm"  action="${ctx}/dunning/tMisContantRecord/list" method="get" style="display: none;">
		<input class="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input class="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/>
		<input id ="dealcode" name="dealcode" type="hidden" value="${dealcode}"/>
		<input id = "dunningtaskdbid" name="dunningtaskdbid" type="hidden" value="${dunningtaskdbid}"/>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>订单ID</th>
				<th>还款到期日</th>
				<th>订单状态</th>
				<th>操作类型</th>
				<th>短信类型</th>
				<th>联系人类型</th>
				<th>联系人电话</th>
				<th>应答状态</th>
				<th>备注</th>
				<th>催收人</th>
				<th>操作时间</th>
<%-- 				<shiro:hasPermission name="dunning:tMisDunningTask:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${page.list}" var="tMisContantRecord" varStatus="vs"> 
			<tr>
				<td>
					${(vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
<%-- 				<a href="${ctx}/dunning/tMisDunningTask/form?id=${tMisDunningTask.id}"> --%>
<%-- 				<a href="javascript:void(0)"  aid="${tMisDunningTask.id}" onclick="ckFunction(this)" > --%>
					${tMisContantRecord.dealcode}
<!-- 				</a> -->
				</td>
				<td>
					<fmt:formatDate value="${tMisContantRecord.repaymenttime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<c:choose>
						<c:when test = "${tMisContantRecord.orderstatus=='true'}">已还清</c:when>
						<c:when test = "${tMisContantRecord.orderstatus=='false'}">未还清</c:when>
						<c:otherwise>${tMisContantRecord.orderstatus}</c:otherwise>
					</c:choose>
				</td>
				<td>
					${tMisContantRecord.contanttypestr}
				</td>
				<td>
					${tMisContantRecord.smstempstr}
				</td>
				<td>
					${tMisContantRecord.contactstypestr}
				</td>
				<td title="${tMisContantRecord.contanttarget}">
					<c:choose>  
						<c:when test="${fn:length(tMisContantRecord.contanttarget) > 11}">  
							<c:out value="${fn:substring(tMisContantRecord.contanttarget, 0, 11)}..." />
						</c:when>
						<c:otherwise>
							<c:out value="${tMisContantRecord.contanttarget}" />
						</c:otherwise>  
					</c:choose>
				</td>
				<td>
					${tMisContantRecord.telstatusstr}
				</td>
				<td>
<%-- 					<c:choose>   --%>
<%-- 						<c:when test="${fn:length(tMisContantRecord.remark) > 10}">   --%>
<%-- 							<c:out value="${fn:substring(tMisContantRecord.remark, 0, 10)}..." /> --%>
<%-- 						</c:when> --%>
<%-- 						<c:otherwise> --%>
<%-- 							<c:out value="${tMisContantRecord.remark}" /> --%>
<%-- 						</c:otherwise>   --%>
<%-- 					</c:choose> --%>
     				${tMisContantRecord.remark}
				</td>
				<td>
					${tMisContantRecord.peoplename}
				</td>
				<td>
					<fmt:formatDate value="${tMisContantRecord.dunningtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
<%-- 				<shiro:hasPermission name="dunning:tMisDunningTask:edit"><td> --%>
<%--     				<a href="${ctx}/dunning/tMisDunningTask/form?id=${tMisDunningTask.id}">修改</a> --%>
<%-- 					<a href="${ctx}/dunning/tMisDunningTask/delete?id=${tMisDunningTask.id}" onclick="return confirmx('确认要删除该催收任务吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
			
		</c:forEach> 
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary"  method="Sms" type="button" value="催收短信" />
	<input id="btnTel" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Tel"  type="button" value="催收电话" />
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
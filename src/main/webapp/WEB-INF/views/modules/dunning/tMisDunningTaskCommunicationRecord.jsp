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
// 				$('#btnSms').attr("disabled","disabled");
// 				$('#btnTel').attr("disabled","disabled");
// 				$('#btnAmount').attr("disabled","disabled");
// 				$('#btnPaid').attr("disabled","disabled");
// 				$('#btnConfirm').attr("disabled","disabled");
				$("input[name='btnCollection']").attr("disabled","disabled");
				window.parent.$("#btnTelTaskFather").attr("disabled","disabled");
			}
		});
		
		function page(n,s){
			$(".pageNo").val(n);
			$(".pageSize").val(s); 
			$(".submitForm").submit();
        	return false;
        }
		
		function collectionfunction(obj, width, height){
				var method = $(obj).attr("method");
				var contactMobile = $(obj).attr("contactMobile");
				var contactstype = $(obj).attr("contactstype");
				var contactsname = $(obj).attr("contactsname") || "未知";
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactMobile=" + contactMobile + "&contactstype=" + contactstype ;
// 				alert(url);
				$.jBox.open("iframe:" + url, $(obj).attr("value"), width || 600, height || 430, {            
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
                        var iframeName = h.children(0).attr("name");
                        var iframeHtml = window.frames[iframeName];               //获取子窗口的句柄
                        iframeHtml.$("#contactsname").val(contactsname);
	               }
	         });
		}
		
		
		function onClickMonthDetails(obj){
			var method = $(obj).attr("method");
			var mobile = $(obj).attr("mobile");
			var type = $(obj).attr("type");
			var url = "${ctx}/dunning/tMisContantRecord/" + method + "?contanttarget="+ mobile +"&dealcode=${dealcode}&contanttype=" + type;
			$.jBox.open("iframe:" + url, $(obj).attr("title"), 600, 350, {            
                   buttons: {"确定": "ok"},
                   loaded: function (h) {
                       $(".jbox-content", document).css("overflow-y", "hidden");
                   }
             });
		}
		
		
	</script>
</head>
<body>
	<!-- <ul class="nav nav-tabs">
		<li class="active">
		<a>催收信息</a></li>
	</ul> -->
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/customerDetails?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">单位&联系人</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationDetails?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">${hasContact=='true' ? '通讯录' :  '通讯录(无)'}</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li  class="active"><a href="${ctx}/dunning/tMisDunningTask/communicationRecord?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">通话记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunnedConclusion/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">电催结论记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisContantRecord/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">催款历史</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/orderHistoryList?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">历史借款信息</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisRemittanceConfirm:insertForm">
			<c:if test="${not ispayoff}"><li><a href="${ctx}/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">汇款信息</a></li></c:if>
		</shiro:hasPermission> 
		<shiro:hasPermission name="dunning:tMisDunningTask:view">
	        <li><a id="applogiglog_a" href="#" >登录日志</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningDeduct:view"><li><a href="${ctx}/dunning/tMisDunningDeduct/list?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">扣款信息</a></li></shiro:hasPermission>
	</ul> 
<!-- 	通话记录 -->
	<sys:message content="${message}"/>
	
	<form:form class="submitForm"  action="${ctx}/dunning/tMisDunningTask/communicationRecord" method="post" style="display: none;">
		<input class="pageNo" name="pageNo" type="hidden" value="${communicationsPage.pageNo}"/>
		<input class="pageSize" name="pageSize" type="hidden" value="${communicationsPage.pageSize}"/>
		<input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/>
		<input id ="dealcode" name="dealcode" type="hidden" value="${dealcode}"/>
		<input id = "dunningtaskdbid" name="dunningtaskdbid" type="hidden" value="${dunningtaskdbid}"/>
	</form:form> 
	<table id="unitTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>姓名</th>
				<th>匹配</th>
				<th>号码</th>
				<th>所属地</th>
				<th>累计通话时长</th>
				<th>通话次数</th>
				<th>历史记录</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${communicationsPage.list}" var="p" varStatus="vs">
			<tr>
				<td>
					${(vs.index+1) + (communicationsPage.pageNo-1) * communicationsPage.pageSize} 
				</td>
				<td>${p.name}</td>
				<td>
				<c:choose>
						<c:when test = "${p.rcname != '未知'}">
							${p.rcname}
							<c:choose>
								<c:when test = "${p.familyRelation=='married'}">(夫妻)</c:when>
								<c:when test = "${p.familyRelation=='workmate'}">(同事)</c:when>
								<c:when test = "${p.familyRelation=='parent'}">(父母)</c:when>
								<c:when test = "${p.familyRelation=='children'}">(子女)</c:when>
								<c:when test = "${p.familyRelation=='relatives'}">(亲属)</c:when>
								<c:when test = "${p.familyRelation=='friend'}">(朋友)</c:when>
								<c:when test = "${p.familyRelation=='callLog'}">(通话记录)</c:when>
								<c:when test = "${p.familyRelation=='unknown'}"></c:when>
								<c:otherwise></c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
						
						</c:otherwise>
					</c:choose>
<%-- 				${p.rcname} --%>
<%-- 					<c:choose> --%>
<%-- 						<c:when test = "${p.familyRelation=='married'}">(夫妻)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='workmate'}">(同事)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='parent'}">(父母)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='children'}">(子女)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='relatives'}">(亲属)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='friend'}">(朋友)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='callLog'}">(通话记录)</c:when> --%>
<%-- 						<c:when test = "${p.familyRelation=='unknown'}">(未知)</c:when> --%>
<%-- 						<c:otherwise></c:otherwise> --%>
<%-- 					</c:choose> --%>
				</td>
				<td>${p.tel}</td>
				<td>${p.location}</td>
				<td>
					${p.times}
				</td>
				<td>
					${p.number}
				</td>
				<td>					
					短信
					<a href="javascript:void(0)" title="短信记录" onclick="onClickMonthDetails(this)" mobile="${p.tel}" method="dialogSmsDetail" type="sms">
						(${p.smsNum}) 
					</a>
					通话
					<a href="javascript:void(0)" title="通话记录" onclick="onClickMonthDetails(this)" mobile="${p.tel}" method="dialogTelDetail" type="tel">
						(${p.telNum})
					</a>
				</td>
				<td>
				<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
					<input  name="btnCollection" contactMobile="${p.tel}" contactstype="COMMUNCATE" onclick="collectionfunction(this)"  class="btn btn-primary" method="Sms"  type="button" value="短信" />
					<input  name="btnCollection" contactMobile="${p.tel}" contactstype="COMMUNCATE" contactsname="${p.name}" onclick="collectionfunction(this, 650)"  class="btn btn-primary" method="Tel"  type="button" value="电话" />
<%-- 					<input  name="btnCollection" contactMobile="${p.tel}" contactstype="COMMUNCATE" onclick="collectionfunction(this)"  class="btn btn-primary" method="Tel"  type="button" value="催收电话" /> --%>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${communicationsPage}</div>
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary" contactMobile="${personMobile}" contactstype=""  method="Sms" type="button" value="催收短信" />
	<input id="btnTel" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Tel"  type="button" value="电催结论" disabled/>
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
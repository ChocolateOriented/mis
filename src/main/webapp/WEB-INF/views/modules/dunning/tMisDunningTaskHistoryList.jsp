<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var iseffective;
		var resultcode;
		var target = [];
		$(document).ready(function() {
			$('#btnPaid').attr("disabled","disabled");
			
			if("${ispayoff}" == "true"){
				$('#btnSms').attr("disabled","disabled");
				$('#btnTel').attr("disabled","disabled");
				$('#btnAmount').attr("disabled","disabled");
				$('#btnPaid').attr("disabled","disabled");
				$('#btnConfirm').attr("disabled","disabled");
				$('#btnDeduct').attr("disabled","disabled");
				$('#btnTag').attr("disabled","disabled");
				window.parent.disableBtn();
			}
			
			$("#allAction").click(function() {
				var checked = $("#allAction").prop('checked');
				$("input[name='actions']:not(:disabled)").each(function() {
					$(this).prop('checked', checked);
				});
			});
			if("false"==window.parent.$("#daikouStatus").val()){
				$("#btnDeduct").attr("disabled",true);
			}
		});
		
		function collectionfunction(obj, width, height, param){
				var method = $(obj).attr("method");
				var contactstype = $(obj).attr("contactstype");
	// 			var aid = $(obj).attr("aid");
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactstype=" + contactstype ;
				if (param) {
					for (var name in param) {
						if (typeof param[name] != "function") {
							url = url + "&" + name + "=" + param[name] || "";
						}
					}
				}
// 				alert(url);
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
		
		function telAction(obj) {
			var actions = [];
			target = [];
			iseffective = "0";
			resultcode = null;
			$("input[name='actions']:checked").each(function() {
				var elem = $(this);
				actions.push(elem.val());
				var obj = {
						contactstype : elem.attr("contactstypestr") || "",
						contactsname : elem.attr("contactsname") || "",
						contanttarget : elem.attr("contanttarget") || "",
						resultcode : elem.attr("telstatusstr") || "",
						dunningtime : elem.attr("dunningtime")
				};
				target.push(obj);
				if (elem.attr("iseffective") == "true") {
					iseffective = "1";
				}
				if (elem.attr("telstatus") == "PTP") {
					resultcode = "PTP"
				}
			});
			if (actions.length == 0) {
				$.jBox.tip("请勾选需总结的电催Action", "warning");
				return;
			}
			collectionfunction(obj, 750, null, {actions : actions});
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
	
	<form:form class="submitForm"  action="${ctx}/dunning/tMisContantRecord/list" method="get" style="display: none;">
		<input class="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input class="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/>
		<input id ="dealcode" name="dealcode" type="hidden" value="${dealcode}"/>
		<input id = "dunningtaskdbid" name="dunningtaskdbid" type="hidden" value="${dunningtaskdbid}"/>
		<input id = "mobileSelf" name="mobileSelf" type="hidden" value="${mobileSelf}"/>
		<input id = "dunningCycle" name="dunningCycle" type="hidden" value="${dunningCycle}"/>
 		<input id = "overdueDays" name="overdueDays" type="hidden" value="${overdueDays}"/>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
<!-- 				<th><input id="allAction" type="checkbox"/></th> -->
				<th>序号</th>
				<th>订单ID</th>
				<th>还款到期日</th>
				<th>订单状态</th>
				<th>操作类型</th>
				<th>短信模板</th>
				<th>短信类型</th>
				<th>联系人类型</th>
				<th>联系人姓名</th>
				<th>联系人电话</th>
				<th>是否有效联络</th>
				<th>结果代码</th>
				<th>备注</th>
				<th>催收人</th>
				<th>操作时间</th>
<%-- 				<shiro:hasPermission name="dunning:tMisDunningTask:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${page.list}" var="tMisContantRecord" varStatus="vs"> 
			<tr> 
<!-- 				<td> -->
<%-- 					<input type="checkbox" name="actions" value="${tMisContantRecord.id}" iseffective="${tMisContantRecord.iseffective}" telstatus="${tMisContantRecord.telstatus}" --%>
<%-- 						contactstypestr="${tMisContantRecord.contactstypestr}" contactsname="${tMisContantRecord.contactsname}" contanttarget="${tMisContantRecord.contanttarget}" --%>
<%-- 						telstatusstr="${tMisContantRecord.telstatusstr}" dunningtime="${tMisContantRecord.dunningtime}" --%>
<%-- 						<c:if test="${not empty tMisContantRecord.conclusionid || empty tMisContantRecord.iseffective || tMisContantRecord.dunningpeoplename != fns:getUser() || tMisContantRecord.contanttype == 'sms'}">disabled</c:if> /> --%>
<!-- 				</td> -->
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
					${tMisContantRecord.templateName}
				</td>
				<td>
					<c:if test="${tMisContantRecord.smsType eq 'wordText'}">
					文字
					</c:if>
					<c:if test="${tMisContantRecord.smsType eq 'voice'}">
					语音
					</c:if>
				<td>
					${tMisContantRecord.contactstypestr}
				</td>
				<td>
					${tMisContantRecord.contactsname}
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
					<c:choose>
						<c:when test = "${tMisContantRecord.iseffective==true}">是</c:when>
						<c:when test = "${tMisContantRecord.iseffective==false}">否</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
<%-- 					${tMisContantRecord.telstatusstr} --%>
					${tMisContantRecord.telstatus}
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
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary" contactstype="${overdueDays<=1 ? 'SELF' : ''}"  method="Sms" type="button" value="催收短信" />
<!-- 	<input id="btnTel" name="btnCollection" onclick="telAction(this)" class="btn btn-primary" method="TelConclusion"  type="button" value="电催结论"/> -->
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
	<input id="btnAmount" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Amount"  type="button" value="调整金额" />
	<!-- <input id="btnPaid" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Paid"  type="button" value="代付" /> -->
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
	<input id="btnDeduct" name="btnCollection" onclick="window.parent.deductPreCheck(collectionfunction.bind(null, this, null, 480), document, this)" class="btn btn-primary" method="Deduct"  type="button" value="代扣" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTag:edit">
	<input id="btnTag" name="btnCollection" onclick="window.parent.tagPopup(this)" class="btn btn-primary" method="Tag" type="button" value="敏感标签" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:outsourcingview">
	<input id="btnConfirm" name="btnCollection" class="btn btn-primary" method="Confirm"  type="button" value="确认还款" />
	</shiro:hasPermission>
</body>
</html>
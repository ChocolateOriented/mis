<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#btnPaid').attr("disabled","disabled");
			
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
	// 			var aid = $(obj).attr("aid");
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?mobileSelf=${mobileSelf}&buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactstype=" + contactstype ;
// 				alert(url);
				$.jBox.open("iframe:" + url, $(obj).attr("value") , width || 600, height || 430, {
				   top: '0%',
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
	
	<form:form class="submitForm"  action="${ctx}/dunning/tMisDunnedConclusion/list" method="get" style="display: none;">
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
				<th>序号</th>
				<th>订单ID</th>
				<th>还款到期日</th>
				<th>订单状态</th>
				<th>是否有效联络</th>
				<th>逾期原因</th>
				<th>结果代码</th>
				<th>用户承诺还款日</th>
				<th>下次跟进日期</th>
				<th>备注</th>
				<th>催收人</th>
				<th>操作时间</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${page.list}" var="tMisDunnedConclusion" varStatus="vs"> 
			<tr>
				<td>
					${(vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					${tMisDunnedConclusion.dealcode}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.repaymenttime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<c:choose>
						<c:when test = "${tMisDunnedConclusion.orderstatus=='true'}">已还清</c:when>
						<c:when test = "${tMisDunnedConclusion.orderstatus=='false'}">未还清</c:when>
						<c:otherwise>${tMisContantRecord.orderstatus}</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test = "${tMisDunnedConclusion.iseffective=='true'}">是</c:when>
						<c:when test = "${tMisDunnedConclusion.iseffective=='false'}">否</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
					${tMisDunnedConclusion.overduereasonstr}
				</td>
				<td>
					${tMisDunnedConclusion.resultcodestr}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.promisepaydate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.nextfollowdate}" pattern="yyyy-MM-dd"/>
					
				</td>
				<td title="${tMisDunnedConclusion.remark}">
					<c:choose>  
						<c:when test="${fn:length(tMisDunnedConclusion.remark) > 50}">  
							<c:out value="${fn:substring(tMisDunnedConclusion.remark, 0, 50)}..." />
						</c:when>
						<c:otherwise>
							<c:out value="${tMisDunnedConclusion.remark}" />
						</c:otherwise>  
					</c:choose>
				</td>
				<td>
					${tMisDunnedConclusion.dunningpeoplename}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.dunningtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			
		</c:forEach> 
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
	<input id="btnSms"   name="btnCollection"  onclick="collectionfunction(this)" class="btn btn-primary" contactstype="${overdueDays<=1? 'SELF' : ''}" method="Sms" type="button" value="催收短信" />
<!-- 	<input id="btnTel" name="btnCollection" class="btn btn-primary" method="TelConclusion"  type="button" value="电催结论" disabled/> -->
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
	<input id="btnAmount" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Amount"  type="button" value="调整金额" />
	<!-- <input id="btnPaid" name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Paid"  type="button" value="代付" /> -->
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
	<input id="btnDeduct" name="btnCollection" onclick="window.parent.deductPreCheck(collectionfunction.bind(null, this, null, 475), document, this)" class="btn btn-primary" method="Deduct"  type="button" value="代扣" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTag:edit">
	<input id="btnTag" onclick="window.parent.tagPopup(this)" class="btn btn-primary" method="Tag" type="button" value="敏感标签" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:outsourcingview">
	<input id="btnConfirm" name="btnCollection" class="btn btn-primary" method="Confirm"  type="button" value="确认还款" />
	</shiro:hasPermission>
</body>
</html>
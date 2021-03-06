<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通讯录</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#btnPaid').attr("disabled","disabled");
			
			if("${ispayoff}" == "true"){
				$("input[name='btnCollection']").attr("disabled","disabled");
				window.parent.disableBtn();
			}
			if("false"==window.parent.$("#daikouStatus").val()){
				$("#btnDeduct").attr("disabled",true);
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
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?mobileSelf=${mobileSelf}&thisCreditAmount=${thisCreditAmount}&buyerId=${buyerId}&dealcode=${dealcode}&mobileSelf=${mobileSelf}&dunningtaskdbid=${dunningtaskdbid}&contactMobile=" + encodeURIComponent(contactMobile) + "&contactstype=" + contactstype ;
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
			var url = "${ctx}/dunning/tMisContantRecord/" + method + "?contanttarget="+ encodeURIComponent(mobile) +"&dealcode=${dealcode}&contanttype=" + type;
			$.jBox.open("iframe:" + url, $(obj).attr("title"), 600, 350, {            
                   buttons: {"确定": "ok"},
                   loaded: function (h) {
                       $(".jbox-content", document).css("overflow-y", "hidden");
                   }
             });
		}
		//点击号码跳转软电话页面直接拨打
		function phonePage(obj) {
			window.parent.phoneFatherPage($(obj).html(),$(obj).attr("showName"));
		}
	</script>
</head>
<body>
	<%-- <ul class="nav nav-tabs">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisDunningTask/">催收任务列表</a></li>
		<li class="active">
		<a>催收信息</a></li>
	</ul> --%>
<!-- 	通讯录 -->
	<sys:message content="${message}"/>
<%-- 	<form:form class="submitForm"  action="${ctx}/dunning/tMisDunningTask/communicationDetails" method="get" style="display: none;"> --%>
<%-- 		<input class="pageNo" name="pageNo" type="hidden" value="${contactPage.pageNo}"/> --%>
<%-- 		<input class="pageSize" name="pageSize" type="hidden" value="${contactPage.pageSize}"/> --%>
<%-- 		<input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/> --%>
<%-- 		<input id ="dealcode" name="dealcode" type="hidden" value="${dealcode}"/> --%>
<%-- 		<input id = "dunningtaskdbid" name="dunningtaskdbid" type="hidden" value="${dunningtaskdbid}"/> --%>
<%-- 		<input id = "mobileSelf" name="mobileSelf" type="hidden" value="${mobileSelf}"/> --%>
<%-- 		<input id = "hasContact" name="hasContact" type="hidden" value="${hasContact}"/> --%>
<%-- 		<input id = "dunningCycle" name="dunningCycle" type="hidden" value="${dunningCycle}"/> --%>
<%--  		<input id = "overdueDays" name="overdueDays" type="hidden" value="${overdueDays}"/> --%>
<%-- 	</form:form> --%>
	<table id="customerTable" class="table table-striped table-bordered table-condensed">		
		<thead>
			<th>序号</th>
			<th>关联人姓名</th>
			<th>匹配</th>
			<th>号码</th>
			<th>历史记录</th>
			<th>操作</th>
		</thead>
		<tbody id = "contactTbody">
			<c:forEach items="${contacts}" var="cp" varStatus="vs">
			<tr>
				<td>
					${(vs.index+1)}
				</td>
				<td style="${cp.relativeMatch ? 'background-color:#fffe93;color:#a4a912;' : ''}">${cp.contactName}</td>
				<td>
					<c:choose>
						<c:when test = "${not empty cp.rcname && cp.rcname != '未知'}">
						${cp.rcname}
							<c:choose>
								<c:when test = "${cp.familyrelation=='married'}">(夫妻)</c:when>
								<c:when test = "${cp.familyrelation=='workmate'}">(同事)</c:when>
								<c:when test = "${cp.familyrelation=='parent'}">(父母)</c:when>
								<c:when test = "${cp.familyrelation=='children'}">(子女)</c:when>
								<c:when test = "${cp.familyrelation=='relatives'}">(亲属)</c:when>
								<c:when test = "${cp.familyrelation=='friend'}">(朋友)</c:when>
								<c:when test = "${cp.familyrelation=='callLog'}">(关联人联系记录)</c:when>
								<c:when test = "${cp.familyrelation=='worktel'}">(单位)</c:when>
								<c:when test = "${cp.familyrelation=='unknown'}"></c:when>
								<c:otherwise></c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
						 
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<shiro:hasPermission name="dunning:phone:view"> 
					<a href="javascript:void(0)" onclick="phonePage(this)" showName="${cp.contactName}">
					</shiro:hasPermission>
					${cp.contactMobile}
					<shiro:hasPermission name="dunning:phone:view"> 
					</a>
					</shiro:hasPermission>
				</td>
				<td>
					短信
					<a href="javascript:void(0)" title="短信记录" onclick="onClickMonthDetails(this)" mobile="${cp.contactMobile}" method="dialogSmsDetail" type="sms">
						(${cp.smsNum}) 
					</a>
					通话
					<a href="javascript:void(0)" title="通话记录" onclick="onClickMonthDetails(this)" mobile="${cp.contactMobile}" method="dialogTelDetail" type="tel"
						style="${cp.effectiveActionNum > 0 ? 'color:red;' : ''}">
						(${cp.telNum})
					</a>
				</td>
				<td>
				<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
					<input  name="btnCollection" contactMobile="${cp.contactMobile}" contactstype="CANTACT" onclick="collectionfunction(this)" class="btn btn-primary"  method="Sms" type="button" value="短信" />
					<input  name="btnCollection" contactMobile="${cp.contactMobile}" contactstype="CANTACT" contactsname="${cp.contactName}" onclick="collectionfunction(this, 650)"  class="btn btn-primary" method="Tel"  type="button" value="电话" />
				</shiro:hasPermission>
				</td>
			</tr>
			</c:forEach>
		</tbody>	
	</table>
	<br/>
</body>
</html>
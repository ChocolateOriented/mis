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
				$("input[name='btnCollection']").attr("disabled","disabled");
				window.parent.disableBtn();
			}
			if("false"==window.parent.$("#daikouStatus").val()){
				$("#btnDeduct").attr("disabled",true);
			}
		});
		
		function collectionfunction(obj, width, height){
		    //alert(${thisCreditAmount});
				var method = $(obj).attr("method");
				var contactMobile = $(obj).attr("contactMobile");
				var contactstype = $(obj).attr("contactstype");
				var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&thisCreditAmount=${thisCreditAmount}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&mobileSelf=${mobileSelf}&contactMobile=" + encodeURIComponent(contactMobile) + "&contactstype=" + contactstype;
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
		<li><a href="${ctx}/dunning/tMisDunningTask/">催收任务列表</a></li>
		<li class="active">
		<a>催收信息</a></li>
	</ul> --%>
	<sys:message content="${message}"/>
	<h4 >&nbsp;&nbsp;单位信息</h4>
	<table  class="table table-striped table-bordered table-condensed">
		<thead>
			<th>职业：${workInfo.companyPosition}</th>
			<th>单位名称：${workInfo.companyName}</th>
			<th>单位地址：${workInfo.companyAddress}</th>
		</thead>
	</table>
	
	<table id="unitTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<th>单电序号</th>
			<th>号码</th>
			<th>历史记录</th>
			<th>操作</th>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>
					<shiro:hasPermission name="dunning:phone:view"> 
					<c:if test="${not empty workInfo.companyTel}">
					<a href="javascript:void(0)" onclick="phonePage(this)" showName="${workInfo.companyName}">
					</c:if>
					</shiro:hasPermission>
					${not empty workInfo.companyTel ? workInfo.companyTel : "无号码"}
					<shiro:hasPermission name="dunning:phone:view"> 
					<c:if test="${not empty workInfo.companyTel}">
					</a>
					</c:if>
					</shiro:hasPermission>
				</td>
				<td>
				通话
				<a href="javascript:void(0)" title="通话记录" onclick="onClickMonthDetails(this)" mobile="${workInfo.companyTel}" method="dialogTelDetail" type="tel">
					(${workInfo.telNum1})
				</a>
				</td>
				<td>
				<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
					<input  name="btnCollection" contactMobile="${workInfo.companyTel}" contactstype="WORKTEL" onclick="collectionfunction(this, 650)"  class="btn btn-primary" method="Tel"  type="button" value="电话" />
				</shiro:hasPermission>
				</td>
			</tr>
			
			<c:if test="${not empty workInfo.companyTel2}">
			<tr>
				<td>2</td>
				<td>
					<shiro:hasPermission name="dunning:phone:view"> 
					<a href="javascript:void(0)" onclick="phonePage(this)" showName="${workInfo.companyName}">
					</shiro:hasPermission>
					${workInfo.companyTel2}
					<shiro:hasPermission name="dunning:phone:view"> 
					</a>
					</shiro:hasPermission>
				
				</td>
				<td>
				通话
				<a href="javascript:void(0)" title="通话记录" onclick="onClickMonthDetails(this)" mobile="${workInfo.companyTel2}" method="dialogTelDetail" type="tel">
					(${workInfo.telNum2})
				</a>
				</td>
				<td>
				<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
					<input  name="btnCollection" contactMobile="${workInfo.companyTel2}" contactstype="WORKTEL" onclick="collectionfunction(this, 650)"  class="btn btn-primary" method="Tel"  type="button" value="电话" />
				</shiro:hasPermission>
				</td>
			</tr>
			</c:if>
			
		</tbody>
	</table>
	
	
	<h4>&nbsp;&nbsp;联系人</h4>
	<table id="contact" class="table table-striped table-bordered table-condensed">
		<thead>
			<th>序号</th>
			<th>关系</th>
			<th>姓名</th>
			<th>号码</th>
			<th>历史记录</th>
			<th>操作</th>
		</thead>
		<tbody>
		<c:forEach items="${contacts}" var="c" varStatus="status">
			<tr>
				<td>${status.index+1}</td>
				<td>
					<c:choose>
						<c:when test = "${c.familyRelation=='married'}">夫妻</c:when>
						<c:when test = "${c.familyRelation=='workmate'}">同事</c:when>
						<c:when test = "${c.familyRelation=='parent'}">父母</c:when>
						<c:when test = "${c.familyRelation=='children'}">子女</c:when>
						<c:when test = "${c.familyRelation=='relatives'}">亲属</c:when>
						<c:when test = "${c.familyRelation=='friend'}">朋友</c:when>
						<c:when test = "${c.familyRelation=='callLog'}">通话记录</c:when>
						<c:when test = "${c.familyRelation=='unknown'}">(未知)</c:when>
						<c:otherwise>(其他)</c:otherwise>
					</c:choose>
				</td>
				<td  >${c.name}</td>
				<td >
					<shiro:hasPermission name="dunning:phone:view"> 
					<a href="javascript:void(0)" onclick="phonePage(this)" showName="${c.name}">
					</shiro:hasPermission>
						${c.tel}
					<shiro:hasPermission name="dunning:phone:view"> 
					</a>
					</shiro:hasPermission>
				</td>
				<td>					
					短信
					<a href="javascript:void(0)" title="短信记录" onclick="onClickMonthDetails(this)" mobile="${c.tel}" method="dialogSmsDetail" type="sms">
						(${c.smsNum}) 
					</a>
					通话
					<a href="javascript:void(0)" title="通话记录" onclick="onClickMonthDetails(this)" mobile="${c.tel}" method="dialogTelDetail" type="tel">
						(${c.telNum})
					</a>
				</td>
				<td>
				<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
					<input  name="btnCollection" contactMobile="${c.tel}" contactstype="${c.familyRelation}" onclick="collectionfunction(this)" class="btn btn-primary"  method="Sms" type="button" value="短信" />
					<input  name="btnCollection" contactMobile="${c.tel}" contactstype="${c.familyRelation}" onclick="collectionfunction(this, 650)"  class="btn btn-primary" method="Tel"  type="button" value="电话" />
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<br/>
</body>
</html>
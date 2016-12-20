<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务确认汇款信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});

		function openDialog(){
			  $("#bannerImage").click(); 
		}
		
		function FileUpload(){
			$("#inputForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/uploadImage");
			$("#inputForm").submit();
		}
		
		function collectionfunction(obj){
			var method = $(obj).attr("method");
			var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}" ;
			$.jBox.open("iframe:" + url, $(obj).attr("value") , 600, 500, {            
               buttons: {
            	},
               submit: function (v, h, f) {
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
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">基本信息</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">
			<c:choose>
				<c:when test = "${hasContact=='true'}">通讯录</c:when>
				<c:when test = "${hasContact=='false'}">通讯录(无)</c:when>
				<c:otherwise>通讯录(无)</c:otherwise>
			</c:choose>
			</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/communicationRecord?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">通话记录</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisContantRecord/list?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">催款历史</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="${ctx}/dunning/tMisDunningTask/orderHistoryList?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">历史借款信息</a></li></shiro:hasPermission>
        <shiro:hasPermission name="dunning:tMisRemittanceMessage:view"><li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/form?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}">财务确认信息</a></li></shiro:hasPermission>
	</ul> 
	
	<form:form id="inputForm" modelAttribute="TMisRemittanceMessage" action="${ctx}/dunning/tMisRemittanceMessage/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<sys:message content="${message}"/>		
		<form:hidden path="id"/>
		<input type="hidden" name="dealcode" value="${dealcode}" />
		<input type="hidden" name="dunningtaskdbid" value="${dunningtaskdbid}" />
		<input type="hidden" name="buyerId" value="${buyerId}" />
		<div class="control-group">
			<label class="control-label">汇款人姓名：</label>
			<div class="controls">
				<form:input path="remittancename" htmlEscape="false" maxlength="128" class="input-xlarge required error"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款时间：</label>
			<div class="controls">
				<input name="remittancetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${TMisRemittanceMessage.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">金额：</label>
			<div class="controls">
				<form:input path="remittanceamount" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款渠道：</label>
			<div class="controls">
<%-- 			<form:input path="remittancechannel" htmlEscape="false" maxlength="64" class="input-xlarge "/> --%>
				<form:select  path="remittancechannel" class="input-medium">
					<form:option  value="银行转账" label="银行转账"/>
					<form:option  value="支付宝" label="支付宝"/>
					<form:option value="微信" label="微信"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款帐号：</label>
			<div class="controls">
				<form:input path="remittanceaccount" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">财务确认人：</label>
			<div class="controls">
				<form:input path="financialuser" readonly="true" htmlEscape="false" maxlength="128" class="input-xlarge"  value="${not empty TMisRemittanceMessage.financialuser ? TMisRemittanceMessage.financialuser : fns:getUser().name}"/>
<%-- 				<input id="financialuser" disabled="disabled" name="financialuser" value="${not empty TMisRemittanceMessage.financialuser ? TMisRemittanceMessage.financialuser : fns:getUser().name}"  /> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">财务确认时间：</label>
			<div class="controls">
				<input name="financialtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${TMisRemittanceMessage.financialtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remark"  rows="3" maxlength="500"></form:textarea>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
				<shiro:hasPermission name="dunning:tMisRemittanceMessage:edit">
					<input  class="btn" type="button" value="选择"  onclick = "openDialog()"/>
				</shiro:hasPermission>
				汇款图片→
			</label>
			
			<div class="controls">
				<%--  ${uploadpath} --%>
				<input type="hidden" name="remittanceimg" value="${filePath}" />
				<c:if test="${not empty filePath}"> 
					<img src="${filePath}"  style = "margin-left: 5px; margin-bottom: 5px;width: 450px;height: 350px;">
				</c:if>
				<input type="file" name="file" id = "bannerImage" style = "display:none" onchange="FileUpload()" />		
			</div>
			
		</div>
		
			<div class="form-actions">
				<shiro:hasPermission name="dunning:tMisRemittanceMessage:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
	<!-- 			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/> -->
				</shiro:hasPermission>
<%-- 				${not empty tMisDunningPeople.id?'修改':'添加'} --%>
				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
					<input  id="btnPaid" ${not empty TMisRemittanceMessage.dbid ? '' : 'disabled="disabled"'} name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary" method="Confirmpay"  type="button" value="确认还款" />
				</shiro:hasPermission>
			</div>
		
	</form:form>

<%-- <img src="http://localhost:8089/remittanceimg/${fileName}" style="margin-left: 5px; margin-bottom: 5px;width: 150px;height: 150px;"> --%>
<!-- <img src="file://C:/apache-tomcat-7.0.65.war/webapps/img/1471067350816RemittanceMesssage.jpg" style="margin-left: 5px; margin-bottom: 5px;width: 150px;height: 150px;"> -->
<!-- <img src="/../remittanceimg/1471075001350RemittanceMesssage.jpg" style="margin-left: 5px; margin-bottom: 5px;width: 150px;height: 150px;"> -->
</body>
</html>
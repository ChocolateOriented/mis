<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>汇款确认信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
       		<li><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmList">汇款确认列表</a></li>
 			<shiro:hasPermission name="dunning:tMisRemittanceConfirm:edit"><li  class="active"><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmDetail?id=${tMisRemittanceConfirm.id}">汇款确认详情</a></li></shiro:hasPermission>	
 	</ul> 
 			
	<form:form id="inputForm" modelAttribute="TMisRemittanceConfirm" action="${ctx}/dunning/tMisRemittanceConfirm/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
			
	 	<div class="control-group">
			<label class="control-label">[催]汇款人：</label>
			<div class="controls">
				<form:input path="remittancename" htmlEscape="false" disabled="true" maxlength="128" class="input-xlarge "/>
			</div>
			<label class="control-label">[财]汇款人：</label>
			<div class="controls">
				<form:input path="financialremittancename" disabled="true" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">[催]汇款时间：</label>
			<div class="controls">
				<input name="remittancetime" type="text" readonly="readonly" disabled="true"  maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisRemittanceConfirm.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
			<label class="control-label">[财]到账时间：</label>
			<div class="controls">
				<input name="accounttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " disabled="true"
					value="<fmt:formatDate value="${tMisRemittanceConfirm.accounttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">[催]汇款金额：</label>
			<div class="controls">
				<form:input path="remittanceamount" htmlEscape="false" disabled="true"  class="input-xlarge "/>
			</div>
			<label class="control-label">[财]到账金额：</label>
			<div class="controls">
				<form:input path="accountamount" disabled="true" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">[催]汇款渠道：</label>
			<div class="controls">
				<form:input path="remittancechannel" htmlEscape="false"  disabled="true"  maxlength="128" class="input-xlarge "/>
			</div>
			<label class="control-label">[财]汇款渠道：</label>
			<div class="controls">
				<form:input path="financialremittancechannel" disabled="true" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">[催]备注：</label>
			<div class="controls">
				<form:textarea path="remark" disabled="true" rows="3" maxlength="500"></form:textarea>
			</div>
			<label class="control-label">[财]备注：</label>
			<div class="controls">
				<form:textarea path="financialremark" disabled="true" rows="3" maxlength="500"></form:textarea>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">还款金额：</label>
			<div class="controls">
				<form:input path="payamount" htmlEscape="false" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">还款类型：</label>
			<div class="controls">
				<form:input path="paytype" htmlEscape="false" disabled="true" maxlength="64" class="input-xlarge " value="${tMisRemittanceConfirm.paytypeText}"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
				催收凭证：
			</label>
			<div class="controls">
				<c:if test="${not empty  tMisRemittanceConfirm.receivablesImg1}"> 
					<img src="${tMisRemittanceConfirm.receivablesImg1}"  style = "margin-left: 5px; margin-bottom: 5px;width: 350px;height: 500px;">
				</c:if>
				<c:if test="${not empty  tMisRemittanceConfirm.receivablesImg2}"> 
					<img src="${tMisRemittanceConfirm.receivablesImg2}"  style = "margin-left: 5px; margin-bottom: 5px;width: 350px;height: 500px;">
				</c:if>
			</div>
		</div>
	 	<div class="control-group">
			<label class="control-label">
				财务凭证：
			</label>
			<div class="controls">
				<c:if test="${not empty tMisRemittanceConfirm.financialImg1}"> 
					<img src="${tMisRemittanceConfirm.financialImg1}"  style = "margin-left: 5px; margin-bottom: 5px;width: 920px;height: 150px;">
				</c:if>
				<c:if test="${not empty tMisRemittanceConfirm.financialImg2}"> 
					<img src="${tMisRemittanceConfirm.financialImg2}"  style = "margin-left: 5px; margin-bottom: 5px;width: 920px;height: 150px;">
				</c:if>
			</div>
		</div>
		
	</form:form>
</body>
</html>
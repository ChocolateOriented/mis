<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>汇款确认信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$('#financialremittancechannel').change(function(){
				if($("#financialremittancechannel").val() == "先玩后付"){
					$("#financialserialnumber_div").hide();
					$("#financialserialnumber").val("");
				}else{
					$("financialserialnumber_div").show();
				}
			});
			
			if($("#financialremittancechannel").val() == "先玩后付"){
				$("#financialserialnumber_div").hide();
			}else{
				$("#financialserialnumber_div").show();
			}
			
			//$("#name").focus();
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
		
		function openDialog(obj){
			var file = $(obj).attr("bannerImage");
			$("#"+file).click(); 
		}
		
		function FileUpload(obj){
			var filenum = $(obj).attr("id");
			$("#inputForm").attr("action","${ctx}/dunning/tMisRemittanceConfirm/uploadImage?filenum=" + filenum);
			$("#inputForm").submit();
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
    	<li><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmList?ro=ch_submit">汇款确认列表</a></li>
		<shiro:hasPermission name="dunning:tMisRemittanceConfirm:financialEdit"><li  class="active"><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceFinancialConfirmForm?id=${tMisRemittanceConfirm.id}">汇款确认</a></li></shiro:hasPermission>	
 	</ul> 
	
	<form:form id="inputForm" modelAttribute="TMisRemittanceConfirm" action="${ctx}/dunning/tMisRemittanceConfirm/financialUpdate" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<input type="hidden" name="formurl" value="tMisRemittanceFinancialConfirmForm" />
		
		<div class="control-group">
			<label class="control-label">汇款人：</label>
			<div class="controls">
				<form:input path="financialremittancename"  htmlEscape="false" maxlength="128" class="input-xlarge " 
				value="${not empty tMisRemittanceConfirm.financialremittancename ? tMisRemittanceConfirm.financialremittancename : tMisRemittanceConfirm.remittancename}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">到账时间：</label>
			<div class="controls">
				<input name="accounttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "  
					value="<fmt:formatDate value="${not empty tMisRemittanceConfirm.accounttime ? tMisRemittanceConfirm.accounttime : tMisRemittanceConfirm.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">到账金额：</label>
			<div class="controls">
				<form:input path="accountamount" htmlEscape="false" class="input-xlarge number required"
				value="${not empty tMisRemittanceConfirm.accountamount ? tMisRemittanceConfirm.accountamount : tMisRemittanceConfirm.remittanceamount}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款渠道：</label>
			<div class="controls">
				<form:select path="financialremittancechannel" class="input-xlarge ">
<%-- 					<form:options items="${fns:getDictList('remittancechannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
					<form:option selected="selected" value="${tMisRemittanceConfirm.remittancechannel}" label="${tMisRemittanceConfirm.remittancechannel}"/>
				</form:select>
			</div>
		</div>
		<div id="financialserialnumber_div" class="control-group">
			<label class="control-label">汇款流水号：</label>
			<div class="controls">
				<form:input path="financialserialnumber" htmlEscape="false" maxlength="128" class="input-xlarge required" 
				value="${not empty tMisRemittanceConfirm.financialserialnumber ? tMisRemittanceConfirm.financialserialnumber : tMisRemittanceConfirm.serialnumber}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">
				<input  class="btn" type="button" value="上传凭证①" bannerImage="file3"  onclick = "openDialog(this)"/>
			</label>
			<div class="controls">
				<input type="hidden" name="FinancialImg1" value="${filePath3}" />
				<c:if test="${not empty filePath3}"> 
					<img src="${filePath3}"  style = "margin-left: 5px; margin-bottom: 5px;width: 920px;height: 150px;">
				</c:if>
				<input type="file" name="file3" id = "file3" style = "display:none" onchange="FileUpload(this)" />		
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
					<input  class="btn" type="button" value="上传凭证②" bannerImage="file4"  onclick = "openDialog(this)"/>
			</label>
			<div class="controls">
				<input type="hidden" name="FinancialImg2" value="${filePath4}" />
				<c:if test="${not empty filePath4}"> 
					<img src="${filePath4}"  style = "margin-left: 5px; margin-bottom: 5px;width: 920px;height: 150px;">
				</c:if>
				<input type="file" name="file4" id = "file4" style = "display:none" onchange="FileUpload(this)" />		
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="financialremark"  rows="3" maxlength="500"></form:textarea>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="dunning:tMisRemittanceConfirm:financialEdit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交确认" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		
		<ul class="nav nav-tabs">
	     	<li  class="active"><a>催收提交信息</a></li>
	 	</ul> 
	 	
	 	<div class="control-group">
			<label class="control-label">汇款人：</label>
			<div class="controls">
				<form:input path="remittancename" htmlEscape="false" disabled="true" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款时间：</label>
			<div class="controls">
				<input name="remittancetime" type="text" readonly="readonly" disabled="true"  maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisRemittanceConfirm.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款金额：</label>
			<div class="controls">
				<form:input path="remittanceamount" htmlEscape="false" disabled="true"  class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款渠道：</label>
			<div class="controls">
				<form:input path="remittancechannel" htmlEscape="false"  disabled="true"  maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div id="serialnumber_div" class="control-group">
			<label class="control-label">汇款流水号：</label>
			<div class="controls">
				<form:input path="serialnumber" htmlEscape="false" disabled="true" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remark" disabled="true" rows="3" maxlength="500"></form:textarea>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
				催收凭证①：
			</label>
			<div class="controls">
				<c:if test="${not empty tMisRemittanceConfirm.receivablesImg1}"> 
					<img src="${tMisRemittanceConfirm.receivablesImg1}"  style = "margin-left: 5px; margin-bottom: 5px;width: 350px;height: 500px;">
				</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">
				催收凭证②：
			</label>
			<div class="controls">
				<c:if test="${not empty tMisRemittanceConfirm.receivablesImg2}"> 
					<img src="${tMisRemittanceConfirm.receivablesImg2}"  style = "margin-left: 5px; margin-bottom: 5px;width: 350px;height: 500px;">
				</c:if>
			</div>
		</div>
		
<!-- 		<ul class="nav nav-tabs"> -->
<!-- 	     	<li  class="active"><a>财务审核</a></li> -->
<!-- 	 	</ul>  -->
		
		
		
	</form:form>
</body>
</html>
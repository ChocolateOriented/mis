<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>汇款确认信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$('#remittancechannel').change(function(){
				if($("#remittancechannel").val() == "先玩后付"){
					$("#serialnumber_div").hide();
					$("#serialnumber").val("");
				}else{
					$("#serialnumber_div").show();
					$("#serialnumber").val("${tMisRemittanceConfirm.serialnumber}");
				}
			});
			
			if($("#remittancechannel").val() == "先玩后付"){
				$("#serialnumber_div").hide();
			}else{
				$("#serialnumber_div").show();
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
       		<li><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmList">汇款确认列表</a></li>
 			<shiro:hasPermission name="dunning:tMisRemittanceConfirm:edit"><li  class="active"><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmForm?id=${tMisRemittanceConfirm.id}">催收汇款修改</a></li></shiro:hasPermission>	
 	</ul> 
	
	<form:form id="inputForm" modelAttribute="TMisRemittanceConfirm" action="${ctx}/dunning/tMisRemittanceConfirm/remittanceUpdate" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<input type="hidden" name="formurl" value="tMisRemittanceConfirmForm" />
		<div class="control-group">
			<label class="control-label">汇款人：</label>
			<div class="controls">
				<form:input path="remittancename" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款时间：</label>
			<div class="controls">
				<input name="remittancetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tMisRemittanceConfirm.remittancetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款金额：</label>
			<div class="controls">
				<form:input path="remittanceamount" htmlEscape="false" class="input-xlarge number required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款渠道：</label>
			<div class="controls">
				<form:select path="remittancechannel" class="input-xlarge ">
					<form:options items="${fns:getDictList('remittancechannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div id="serialnumber_div" class="control-group">
			<label class="control-label">汇款流水号：</label>
			<div class="controls">
				<form:input path="serialnumber"  htmlEscape="false" maxlength="128" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="256" class="input-xlarge "/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
				<input  class="btn" type="button" value="上传凭证①" bannerImage="file1"  onclick = "openDialog(this)"/>
			</label>
			<div class="controls">
				<input type="hidden" name="ReceivablesImg1" value="${filePath1}" />
				<c:if test="${not empty filePath1}"> 
					<img src="${filePath1}"  style = "margin-left: 5px; margin-bottom: 5px;width: 350px;height: 500px;">
				</c:if>
				<input type="file" name="file1" id = "file1" style = "display:none" onchange="FileUpload(this)" />		
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
					<input  class="btn" type="button" value="上传凭证②" bannerImage="file2"  onclick = "openDialog(this)"/>
			</label>
			<div class="controls">
				<input type="hidden" name="ReceivablesImg2" value="${filePath2}" />
				<c:if test="${not empty filePath2}"> 
					<img src="${filePath2}"  style = "margin-left: 5px; margin-bottom: 5px;width: 350px;height: 500px;">
				</c:if>
				<input type="file" name="file2" id = "file2" style = "display:none" onchange="FileUpload(this)" />		
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="dunning:tMisRemittanceConfirm:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交财务" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		
		<ul class="nav nav-tabs">
	     	<li  class="active"><a>财务确认信息</a></li>
	 	</ul> 
		<div class="control-group">
			<label class="control-label">汇款人：</label>
			<div class="controls">
				<form:input path="financialremittancename" disabled="true" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">到账金额：</label>
			<div class="controls">
				<form:input path="accountamount" disabled="true" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">到账时间：</label>
			<div class="controls">
				<input name="accounttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " disabled="true"
					value="<fmt:formatDate value="${tMisRemittanceConfirm.accounttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款渠道：</label>
			<div class="controls">
				<form:input path="financialremittancechannel" disabled="true" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div id="financialserialnumber_div" class="control-group">
			<label class="control-label">汇款流水号：</label>
			<div class="controls">
				<form:input path="financialserialnumber"  disabled="true" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">
				财务凭证①：
			</label>
			<div class="controls">
				<c:if test="${not empty tMisRemittanceConfirm.financialImg1}"> 
					<img src="${tMisRemittanceConfirm.financialImg1}"  style = "margin-left: 5px; margin-bottom: 5px;width: 920px;height: 150px;">
				</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">
				财务凭证②：
			</label>
			<div class="controls">
				<c:if test="${not empty tMisRemittanceConfirm.financialImg2}"> 
					<img src="${tMisRemittanceConfirm.financialImg2}"  style = "margin-left: 5px; margin-bottom: 5px;width: 920px;height: 150px;">
				</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="financialremark" disabled="true" rows="3" maxlength="500"></form:textarea>
			</div>
		</div>
		
		
	</form:form>
</body>
</html>
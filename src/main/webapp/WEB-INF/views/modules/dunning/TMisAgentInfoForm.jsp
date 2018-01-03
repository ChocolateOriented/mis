<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>坐席管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	//$("#name").focus();
	$("#inputForm").validate({
		rules:{
			agent:{
				remote: {
				    url: "${ctx}/dunning/tMisAgentInfo/validateAgent",    
				    type: "post",               
				    dataType: "json",            
				    data: {                    
				    	agent: function() {
				            return $("#agent").val();
				        },
				    	id:function() {
				            return $("#agentId").val();
				        }
				    }
				}
			},
			queue:{
				remote: {
				    url: "${ctx}/dunning/tMisAgentInfo/validateQueue",    
				    type: "post",               
				    dataType: "json",            
				    data: {                    
				    	queue: function() {
				            return $("#queue").val();
				        },
				    	id:function() {
				            return $("#agentId").val();
				        }
				    }
				}
			},
			extension:{
				remote: {
				    url: "${ctx}/dunning/tMisAgentInfo/validateExtension",    
				    type: "post",               
				    dataType: "json",            
				    data: {                    
				    	extension: function() {
				            return $("#extension").val();
				        },
				    	id:function() {
				            return $("#agentId").val();
				        }
				    }
				}
			}
		},
		messages:{
			agent:{
				remote:"该坐席已存在"
			},
			queue:{
				remote:"该队列已存在"
			},
			extension:{
				remote:"该分机号已存在"
			}
		},
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


</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisAgentInfo/">坐席管理</a></li>
		<li class="active"><a href="${ctx}/dunning/tMisAgentInfo/form?id=${tmisAgentInfo.id}">${not empty tmisAgentInfo.id?'坐席修改':'坐席添加'}</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="TMisAgentInfo" action="${ctx}/dunning/tMisAgentInfo/save" method="post" class="form-horizontal">
		<sys:message content="${message}" />
		<input type="hidden" id="agentId" name="id" value="${tmisAgentInfo.id}" />
		
		<div class="control-group">
			<label class="control-label">坐席名称：</label>
			<div class="controls">
				<form:input path="agent" htmlEscape="false" maxlength="64" class="input-medium required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">队列：</label>
			<div class="controls">
				<form:input path="queue" htmlEscape="false" maxlength="64" class="input-medium required" />
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">分机号：</label>
			<div class="controls">
				<form:input path="extension" htmlEscape="false" maxlength="64" class="input-medium required" />
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">对外分机号：</label>
			<div class="controls">
				<form:input path="direct" htmlEscape="false" maxlength="64" class="input-medium required" />
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
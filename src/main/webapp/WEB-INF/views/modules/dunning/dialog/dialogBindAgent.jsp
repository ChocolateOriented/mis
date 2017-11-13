<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>绑定坐席页面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		}); 
		
			$("#bindAgent").click(function(){
				
				if($("#inputForm").valid()){
					$.post("${ctx}/dunning/tMisAgentInfo/bindAgent",$("#inputForm").serialize(),function(data){
						 if (data) {
							 alert("绑定成功");
	                            window.parent.window.location.reload(); 
	                            window.parent.window.jBox.close();            
	                        } else {
	                        	 alert("绑定失败");
	                            window.parent.window.jBox.close();
	                        }
					});
				}
				
			});
		});
		
	</script>
</head>
<body>
<ul class="nav nav-tabs">

</ul>
	<form:form id="inputForm" modelAttribute="TMisAgentInfo"  class="form-horizontal">
		<div class="control-group">
			<label class="control-label">催收人账号：</label>
			<div class="controls">
					<form:select  path="peopleId" class="input-medium  required">
						<form:option value="">请选择</form:option>
						<form:options items="${users}" itemLabel="nickname" itemValue="id" />
					</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<input type="hidden" id="agentId" name="id" value="${id}" />
		<div style= "padding:8px 180px;" >
				<input id="bindAgent" class="btn btn-primary" type="button" value="绑 定"/>&nbsp;
				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
		
		
	</form:form>
</body>
</html>
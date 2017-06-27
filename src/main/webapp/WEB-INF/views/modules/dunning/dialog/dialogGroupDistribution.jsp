<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>手动分配</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(document).ready(function() {
		$('#distributionSave').click(function() {
			if($("#inputForm").valid()){
				$("#distributionSave").attr('disabled',"true");
				$.ajax({
					type: 'POST',
					url : "${ctx}/dunning/tMisDunningGroup/saveDistribution",
					data: $('#inputForm').serialize(),             //获取表单数据
					success : function(data) {
						if (data == "OK") {
							alert("保存成功");
							window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
							window.parent.window.jBox.close();            //关闭子窗体
						} else {
							alert(data);
							window.parent.page();
							window.parent.window.jBox.close();
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
						//通常情况下textStatus和errorThrown只有其中一个包含信息
						alert("保存失败:"+textStatus);
					}
				});
			}
		}); 
			
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		}); 
	});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<br/>
	<form:form id="inputForm" class="form-horizontal" modelAttribute="TMisDunningGroup">
		<div class="control-group">
			<label class="control-label">催收人：</label>
			<select id="supervisorId" path="" name="supervisor.id" class="input-medium required">
				<option value=""></option>
				<c:forEach items="${users}" var="user">
					<option value="${user.id}">${user.name}</option>
				</c:forEach>
			</select>
			<span class="help-inline"><font color="red">*</font></span>
		</div>
		<input style="display:none;" name="name" value="123" path=""/>
		<c:forEach items="${groups}" var="group" varStatus="vs">
 			<input style="display:none;" name="groupIds" value="${group}"/>
 		</c:forEach>
		<div class="form-actions">
			<input id="distributionSave" class="btn btn-primary" type="submit" value="分配"/>&nbsp;
 			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
	
</body>
</html>


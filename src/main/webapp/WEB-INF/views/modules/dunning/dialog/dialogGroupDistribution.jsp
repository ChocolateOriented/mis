<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>分配监理</title>
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
		
		$('#orgnizationId').change(function() {
			var option = $(this).children(":selected");
			$('#supervisor').text(option.attr('supervisor') || '');
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
			<label class="control-label">所属机构：</label>
			<select id="orgnizationId" name="organization.id" class="input-medium required">
				<option value=""></option>
				<c:forEach items="${organizations}" var="organization">
					<option value="${organization.id}" supervisor="${organization.supervisor.name}">${organization.name}</option>
				</c:forEach>
			</select>
			<span class="help-inline"><font color="red">*</font></span>
		</div>
		<div class="control-group">
			<label class="control-label">监理：</label>
			<label id="supervisor" style="padding:3px;">&nbsp;</label>
		</div>
		<c:forEach items="${groups}" var="group" varStatus="vs">
 			<input style="display:none;" name="groupIds" value="${group}"/>
 		</c:forEach>
		<div class="form-actions">
			<input id="distributionSave" class="btn btn-primary" type="button" value="分配"/>&nbsp;
 			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
	
</body>
</html>


<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>催收小组管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	$("#inputForm").validate({
		submitHandler: function(form){
			$("#btnSubmit").prop("disabled",true);			
			loading('正在提交，请稍等...');
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#btnSubmit").prop("disabled",false);		
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				error.appendTo(element.parent().parent());
			} else {
				error.insertAfter(element);
			}
		}
	});

  $("#btnSubmit").on("click",function () {
    if(!$("#inputForm").valid()){
      return;
    }
    $.ajax({
      type: 'POST',
      url : "${ctx}/dunning/tMisDunningGroup/save",
      data: $('#inputForm').serialize(),             //获取表单数据
      success : function(data) {
        if (data == "OK") {
          window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
        } else {
          alert(data);
        }
      },
      error : function(XMLHttpRequest, textStatus, errorThrown){
        //通常情况下textStatus和errorThrown只有其中一个包含信息
        alert("保存失败:"+textStatus);
      }
    });
  });

	$('#orgnizationId').change(function() {
		var option = $(this).children(":selected");
		$('#supervisor').text(option.attr('supervisor') || '');
	});
});
</script>
</head>
<body>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisDunningGroup" action="${ctx}/dunning/tMisDunningGroup/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>	
		<input type="hidden" id="groupId" name="id" value="${TMisDunningGroup.id}"/>
		<div class="control-group">
			<label class="control-label">催收小组名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">小组组长：</label>
			<div class="controls">
					<form:select id="leaderId" path="leader.id" class="input-medium required" >
						<form:option value=""></form:option>
						<form:options  items="${users}" itemLabel="name" itemValue="id"  htmlEscape="false"/>
					</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">组类型：</label>
			<div class="controls">
					<form:select id="groupType" path="type" class="input-medium required">
						<form:options items="${groupTypes}" htmlEscape="false"/>
					</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属机构：</label>
			<div class="controls">
				<form:select id="orgnizationId" path="organization.id" class="input-medium required">
					<form:option value=""></form:option>
					<c:forEach items="${organizations}" var="organization">
						<form:option value="${organization.id}" supervisor="${organization.supervisor.name}">${organization.name}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">监理：</label>
			<div class="controls">
				<label id="supervisor" style="padding:3px;">${TMisDunningGroup.organization.supervisor.name}&nbsp;</label>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>催收人员管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	//$("#name").focus();
	$("#inputForm").validate({
		rules:{
			nickname:{
				remote: {
				    url: "${ctx}/dunning/tMisDunningPeople/isUniqueNickname",     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    data: {                     //要传递的数据
				    	nickname: function() {
				            return $("#nickname").val();
				        },
				    	id: function() {
				            return $("#dunningPeopleid").val();
				        }
				    }
				}
			}
		},
		messages:{
			nickname:{
				remote:"该花名已被占用"
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
	
	//选择催收人后同时更新name
	$("#dunningPeopleid").on("change",function(){
		$("#dunningPeoplename").val($("#dunningPeopleid option:selected").text());
	 });
	
	//选择小组后 ,更新组类型
	$("#groupId").on("change",function(event){
		changGroupType();
	});

	$("#btnSubmit").on("click",function () {
      	if(!$("#inputForm").valid()){
      	  return;
        }
      $.ajax({
        type: 'POST',
        url : "${ctx}/dunning/tMisDunningPeople/save",
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
    })
});

function changGroupType(){
	var groupType = $("#groupId option:selected").attr("groupType");
	$("#groupType").select2("val", groupType);
}

</script>
</head>
<body>
	<br />
	<sys:message content="${message}" />
	<form:form id="inputForm" modelAttribute="TMisDunningPeople" action="${ctx}/dunning/tMisDunningPeople/save" method="post" class="form-horizontal" >
		<input type="hidden" id="dbid" name="dbid" value="${tMisDunningPeople.dbid}" />
		<input type="hidden" id="dunningPeoplename" name="name" value="${tMisDunningPeople.name}" />
		<div class="control-group">
			<label class="control-label">催收人账号：</label>
			<div class="controls">
				<c:choose>
					<c:when test="${not empty TMisDunningPeople.dbid}">
						<form:select id="dunningPeopleid" path="id" class="input-medium  required" disabled="true">
							<form:option value="${TMisDunningPeople.id}">${TMisDunningPeople.name}</form:option>
						</form:select>
						<input type="hidden"  name="id" value="${tMisDunningPeople.id}" />
					</c:when>
					<c:otherwise>
						<form:select id="dunningPeopleid" path="id" class="input-medium  required">
							<form:option value="">请选择</form:option>
							<form:options items="${users}" itemLabel="name" itemValue="id" />
						</form:select>
					</c:otherwise>
				</c:choose>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">催收人花名：</label>
			<div class="controls">
				<form:input path="nickname" htmlEscape="false" maxlength="64" class="input-medium required" />
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属小组：</label>
			<div class="controls">
				<form:select id="groupId" path="group.id" class="input-medium required">
					<!-- 添加组类型为optgroup -->
					<c:forEach items="${groupTypes}" var="type">
						<optgroup label="${type.value}">
							<!-- 添加类型对应的小组 -->
							<c:forEach items="${groupList}" var="item">
								<c:if test="${item.type == type.key}">
									<option value="${item.id}" groupType="${item.type}" <c:if test="${tMisDunningPeople.group.id == item.id }">selected="selected"</c:if>>${item.name}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">组类型：</label>
			<div class="controls">
				<select id="groupType" class="input-medium" disabled="disabled">
					<c:forEach items="${groupTypes}" var="type">
						<option value="${type.key}" <c:if test="${tMisDunningPeople.group.type == type.key }">selected="selected"</c:if>>${type.value}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品：</label>
			<div class="controls">
				<form:checkboxes path="bizTypes" items="${bizTypes}" itemLabel="desc" class="input-medium  required" />
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否自动分配：</label>
			<div class="controls">
				<form:select id="auto" path="auto" class="input-medium">
					<form:option value="t" label="是" />
					<form:option value="f" label="否" />
					<c:if test="${not empty tMisDunningPeople.dbid}">
					 <form:option value="c" label="关闭" />
					</c:if>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">催收队列：</label>
			<div class="controls">
				<c:choose>
					<c:when test="${not empty tMisDunningPeople.dbid}">
						${tMisDunningPeople.dunningcycle}
					</c:when>
					<c:otherwise>
						<form:checkboxes path="dunningcycle" items="${fns:getDictList('dunningCycle1')}" itemLabel="label" itemValue="label" htmlEscape="false" class="required" />
						<span class="help-inline"><font color="red">*</font> </span>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="dunning:tMisDunningPeople:edit">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
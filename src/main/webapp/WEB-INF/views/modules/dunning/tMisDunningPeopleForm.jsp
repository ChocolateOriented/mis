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
// 			extensionNumber:{
// 				remote:{
// 					url:"${ctx}/dunning/tMisDunningPeople/extensionNumberYanZheng",
//    					type:"post",
//    					dataType: "json", 
//     				data:{
// 	    				extensionNumber:function() {
// 				            return $("#extensionNumber").val();
// 				        	}
// 						}
   					
// 				}
// 			}
		},
		messages:{
			nickname:{
				remote:"该花名已被占用"
			}
// 			extensionNumber:{
// 				remote:"请填写正确的格式"
// 			}
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
	
	
	$("#batchAdd").click(function(){
		$.jBox($("#importBox").html(), {title:"导入数据", width: 500, 
		    height:450,  buttons:{"关闭":true}} );
	});
});

function changGroupType(){
	var groupType = $("#groupId option:selected").attr("groupType");
	$("#groupType").select2("val", groupType);
}

function batchAddPage(){
	var url = "${ctx}/dunning/tMisDunningPeople/batchAddPage";
	$.jBox.open("iframe:" + url, diaName , 500, 450, {
           buttons: {},
           loaded: function (h) {
               $(".jbox-content", document).css("overflow-y", "hidden");
           }
    });
	
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisDunningPeople/">催收人员列表</a></li>
		<li class="active"><a href="${ctx}/dunning/tMisDunningPeople/form?id=${tMisDunningPeople.id}">催收人员<shiro:hasPermission name="dunning:tMisDunningPeople:edit">${not empty tMisDunningPeople.dbid?'修改':'添加'}</shiro:hasPermission> <shiro:lacksPermission name="dunning:tMisDunningPeople:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<div id="importBox" class="hide">
		<form  id="importExcel" action="${ctx}/dunning/tMisDunningPeople/fileUpload" method="post" enctype="multipart/form-data"  >
			<div style="border:1px dashed ;width:300px;height:260px;margin:10px 0px 0px 100px;text-align:center;">
			
				<div  style="margin-top: 10px ;">
	            <input   id="file" type="file" accept=".xls,.xlsx,.csv" name="file" />
				
				</div>	
				<div>
				
				</div>	
				<div  style="margin-top: 180px ;text-align:left; "> 
					<input type="submit" class="btn btn-primary" id="save"  value="确定上传" />
				</div>
			</div>
			
		</form>
	</div>
	<form:form id="inputForm" modelAttribute="TMisDunningPeople" action="${ctx}/dunning/tMisDunningPeople/save" method="post" class="form-horizontal">
		<sys:message content="${message}" />
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
				<input type="button"  class="btn btn-primary" id="batchAdd" value="批量添加" />
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
			<label class="control-label">是否自动分配：</label>
			<div class="controls">
				<form:select id="auto" path="auto" class="input-medium">
					<form:option value="t" label="是" />
					<form:option value="f" label="否" />
				</form:select>
			</div>
		</div>
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label">催收员坐席号：</label> -->
<!-- 			<div class="controls"> -->
<%-- 				<input  value="${tMisDunningPeople.agent }" id=agent name="agent" htmlEscape="false"  class="input-xlarge required "  /> --%>
<!-- 				<span class="help-inline"><font color="red">*</font></span> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label">催收员分机号：</label> -->
<!-- 			<div class="controls"> -->
<%-- 				<input  value="${tMisDunningPeople.extensionNumber }" id="extensionNumber" name="extensionNumber" htmlEscape="false"  class="input-xlarge required "  /> --%>
<!-- 				<span class="help-inline"><font color="red">*</font></span> -->
<!-- 			</div> -->
<!-- 		</div> -->
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

<!-- 		<div class="control-group"> -->
<!-- 			<label title="大于1为单笔固定费率，小于1大于0为单笔百分比费率" class="control-label">单笔费率 ：</label> -->
<!-- 			<div class="controls"> -->
<%-- 				<form:input path="rate" htmlEscape="false" class="input-xlarge " /> --%>
<!-- 			</div> -->
<!-- 		</div> -->
		<!-- 		<div class="control-group"> -->
		<!-- 			<label class="control-label">逾期周期起始：</label> -->
		<!-- 			<div class="controls"> -->
		<%-- 				<form:input path="begin" htmlEscape="false" maxlength="3" class="input-xlarge digits required"/> --%>
		<!-- 			</div> -->
		<!-- 		</div> -->
		<!-- 		<div class="control-group"> -->
		<!-- 			<label class="control-label">逾期周期截至：</label> -->
		<!-- 			<div class="controls"> -->
		<%-- 				<form:input path="end" htmlEscape="false" maxlength="3" class="input-xlarge digits required"/> --%>
		<!-- 			</div> -->
		<!-- 		</div> -->
		<div class="form-actions">
			<shiro:hasPermission name="dunning:tMisDunningPeople:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
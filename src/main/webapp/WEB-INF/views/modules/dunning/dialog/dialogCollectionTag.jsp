<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>敏感标记</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#save').click(function() {
			if($("#inputForm").valid()){
				
				$("#save").attr('disabled',"true");
				
				$.ajax({
					type: 'POST',
					url : "${ctx}/dunning/tMisDunningTag/${tagOpr}Tag",
					data: $('#inputForm').serialize(),             //获取表单数据
					success : function(data) {
						if (!data) {
							alert("保存失败");
						} else if (data.status != "OK") {
							alert(data.msg);
						} else {
							if ("${tagOpr}" == "save") {
								window.parent.window.addTag(data.id);
							} else {
								window.parent.window.refreshTag(data.id);
							}
						}

						window.parent.window.jBox.close();
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
						//通常情况下textStatus和errorThrown只有其中一个包含信息
						alert("保存失败:" + textStatus);
						window.parent.window.jBox.close();
                    }
				});
				
			}
		});
		
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		});
		
		//标签类型选择事件
		$("#tagtype").change(function() {
			var tagtype = $(this).val();
			if (tagtype == "SensitiveOcp") {
				$("#occupationGroup").css("display", "block");
			} else {
				$("#occupationGroup").css("display", "none");
				$("#occupation").val("");
				$("#s2id_occupation span.select2-chosen").text("");
			}
		});
		
		$("#tagtype").change();
		
		if ("${tagOpr}" == "edit") {
			$("#tagtype").attr("readonly", true);
		}
	});

	</script>
</head>
<body>
<ul class="nav nav-tabs">

	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisDunningTag" class="form-horizontal">
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<div class="control-group">
			<label class="control-label">标签类型：</label>
			<div class="controls">
				<select path="" class="input-medium required" id="tagtype" name="tagtype">
					<option value=""></option>
					<c:if test="${empty exist['SensitiveOcp']}">
						<option value="SensitiveOcp" ${tMisDunningTag.tagtype eq 'SensitiveOcp' ? 'selected' : ''}>敏感职业</option>
					</c:if>
					<c:if test="${empty exist['Complaint']}">
						<option value="Complaint" ${tMisDunningTag.tagtype eq 'Complaint' ? 'selected' : ''}>投诉或投诉倾向</option>
					</c:if>
				</select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div id="occupationGroup" class="control-group" style="display:none;">
			<label class="control-label">职业类型：</label>
			<div class="controls">
				<select path="" class="input-medium required" id="occupation" name="occupation">
					<option value=""></option>
					<option value="PublicSecurityOrgan" ${tMisDunningTag.occupation eq 'PublicSecurityOrgan' ? 'selected' : ''}>公检法</option>
					<option value="Journalist" ${tMisDunningTag.occupation eq 'Journalist' ? 'selected' : ''}>记者</option>
				</select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<textarea class="large required" path="" id="remark" name="remark" maxlength="200">${tMisDunningTag.remark}</textarea>
				<span class="help-inline"><font color="red">*</font></span>
 			</div>
		</div>
		
		<input style="display:none;" id="buyerid" name="buyerid" value="${buyerId}" />
		<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		<input style="display:none;" id="id" name="id" value="${tagId}" />

		<div style= "padding:19px 180px 20px;" >
			<input id="save" class="btn btn-primary" type="button" value="确定"/>&nbsp;
			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
</body>
</html>


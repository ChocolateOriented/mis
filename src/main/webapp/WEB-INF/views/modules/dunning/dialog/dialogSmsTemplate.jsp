<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改短信模板的页面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			if(($("#sendMethod").val())=="labourSend"){
				
			       $("#sendTime").hide();
			       $("#acceptType").attr("readonly",false);
			       
				}else{
					 $("#sendTime").show();
					 $("#acceptType").val("self");
					 $("#s2id_acceptType span.select2-chosen").text("本人"); 
					 $("#acceptType").attr("readonly",true); 
					 $("#reasons").hide();
				}
		
			
			
			if("all"==$("#acceptType").val()){
				 $("#overday").hide();
			}else{
				 $("#reasons").hide();
			}
			
			
			$("#acceptType").change(function(){
				if("all"==$("#acceptType").val()){
					 $("#overday").hide();
					 $("#numbefore").val("");
					 $("#numafter").val("");
					 $("#reasons").show();
				}else{
					 $("#overday").show();
					 $("#reasons").hide();
					 $("#sendReason").val("");
					
				}
				
			});
			
			
			
			$("#sendMethod").change(function(){
				if(($("#sendMethod").val())=="labourSend"){
					
			       $("#sendTime").hide();
			       $("#sTime").val("");
			       $("#acceptType").attr("readonly",false);
			       if("all"==$("#acceptType").val()){
			       $("#reasons").show();
			       }
			       $.ajax({
						url:"${ctx}/dunning/TmisDunningSmsTemplate/findOne?templateName="+$("#templateName").val()+"&sendMethod="+$("#sendMethod").val()+"&englishTemplateName="+$("#englishTemplateName").val(),
						type:"GET",
						data:{},
						success:function(data){
							
							if(data=="allName"){
								$("#etipName").html("名字已存在");
								$("#tipName").html("名字已存在");
								$("#templateName").val("");
								$("#englishTemplateName").val("");
							}
							if(data=="eName"){
								$("#etipName").html("名字已存在");
								$("#englishTemplateName").val("");
							}
							if(data=="tName"){
								$("#tipName").html("名字已存在");
								$("#templateName").val("");
							}
							if(data=="OK"){
								$("#tipName").html("");
								$("#etipName").html("");
								
							}
							
						},
						error : function(XMLHttpRequest, textStatus, errorThrown){
		                       //通常情况下textStatus和errorThrown只有其中一个包含信息
		                       $("#templateName").val("");
		                       alert("查询失败:"+textStatus);
		                    }
						
					});
				}else{
					 $("#sendTime").show();
					 $("#acceptType").val("self");
					 $("#s2id_acceptType span.select2-chosen").text("本人"); 
					 $("#acceptType").attr("readonly",true);
					 $("#overday").show();
					 $("#reasons").hide();
					 $("#sendReason").val("");
				}
							
			});
			
			//保证人工中文名字唯一
			$("#templateName").blur(function(){
				$.ajax({
					url:"${ctx}/dunning/TmisDunningSmsTemplate/findName?templateName="+$("#templateName").val()+"&sendMethod="+$("#sendMethod").val(),
					type:"GET",
					data:{},
					success:function(data){
						
						if(data=="false"){
							
							$("#tipName").html("名字已存在");
							$("#templateName").val("");
						}
						if(data=="OK"){
							$("#tipName").html("");
							
						}
						
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
	                       $("#templateName").val("");
	                       alert("查询失败:"+textStatus);
	                    }
					
				});
			});
			
			//保证人工英文名字名字唯一
			$("#englishTemplateName").blur(function(){
				$.ajax({
					url:"${ctx}/dunning/TmisDunningSmsTemplate/findEnglishName?englishTemplateName="+$("#englishTemplateName").val()+"&sendMethod="+$("#sendMethod").val(),
					type:"GET",
					data:{},
					success:function(data){
						
						if(data=="false"){
							
							$("#etipName").html("名字已存在");
							$("#englishTemplateName").val("");
						}
						if(data=="OK"){
							$("#etipName").html("");
							
						}
						
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
	                       $("#englishTemplateName").val("");
	                       alert("查询失败:"+textStatus);
	                    }
					
				});
			});
			
			//保存
		$('#smsSave').click(function() {
			var controls=false;
			$("[name='bizTypes']").each(function(){
				if(this.checked){
					controls=true;
		        	return false;
		        }
		    });
			if(!controls){
				$("#bizTip").html("必选信息 ");
				 $("#smsSave").attr('disabled',"true");
				return;
			}
			//判断选择的逾期天数是否正确
			 
			if($("#numbefore").val()){
				if(isNaN($("#numbefore").val())){
					$("#tipnum").html("请重新修改并填写正确的格式");
					 $("#smsSave").attr('disabled',"true");
					return;
				}
			}
			if($("#numafter").val()){
				if(isNaN($("#numafter").val())){
					$("#tipnum").html("请重新修改并填写正确的格式");
					 $("#smsSave").attr('disabled',"true");
					return;
				}
				
			}
			if($("#numbefore").val() &&$("#numafter").val()){
				if(parseInt($("#numafter").val())<parseInt($("#numbefore").val())){
					$("#tipnum").html("请重新配置并填写正确的格式");
					 $("#smsSave").attr('disabled',"true");
					return;
				}
			}
			
			 if($("#inputForm").valid()){
				 $("#smsSave").attr('disabled',"true");
				 
				 $.ajax({
	                    url : "${ctx}/dunning/TmisDunningSmsTemplate/saveSmsTemplate",
	                    type: 'POST',
	                    data: $('#inputForm').serialize(),             //获取表单数据
	                    success : function(data) {
	                        if (data == "OK") {
	                            alert("修改成功");
	                            window.parent.window.location.reload(); 
	                            window.parent.window.jBox.close();            //关闭子窗体
	                           
	                        } else {
	                            alert("修改失败:"+data.message);
	                            window.parent.window.jBox.close();
	                        }
	                    },
	                    error : function(XMLHttpRequest, textStatus, errorThrown){
	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
	                       alert("修改失败:"+textStatus);
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
	<form:form id="inputForm" modelAttribute="TMisContantRecord"  class="form-horizontal">
	  <input type="hidden" name="id" value="${tSTemplate.id }"/>
	  <input type="hidden" name="invalid" value="t"/>
		<div class="control-group">
			<label class="control-label">英文模板名称：</label>
			<div class="controls">
				<input  value="${tSTemplate.englishTemplateName}" id="englishTemplateName" name="englishTemplateName" htmlEscape="false"  class="input-xlarge required "  />
					<span class="help-inline"><font color="red">*</font> </span>
					<span ><font color="red" id="etipName"></font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模板名称：</label>
			<div class="controls">
				<input  value="${tSTemplate.templateName}" id="templateName" name="templateName" htmlEscape="false"  class="input-xlarge required "  />
					<span class="help-inline"><font color="red">*</font> </span>
					<span ><font color="red" id="tipName"></font></span>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">发送方式：</label>
			<div class="controls">
				<select path="" class="input-medium" id="sendMethod" name="sendMethod">
					<option value="autoSend" <c:if test="${'autoSend' eq  tSTemplate.sendMethod}">selected</c:if>>系统</option>
					<option value="labourSend" <c:if test="${'labourSend' eq  tSTemplate.sendMethod}">selected</c:if>>人工</option>
				</select>
			</div>
		</div>
		
		<div class="control-group" id="sendTime">
			<label class="control-label">发送时间：</label>
			<div class="controls">
				<input name="sendTime" id="sTime" type="text" readonly="readonly" maxlength="20" class="input-xlarge required "
					value="<fmt:formatDate value="${tSTemplate.sendTimeDate}" pattern='HH:mm'/>"
					onclick="WdatePicker({dateFmt:'HH:mm',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
				
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">短信类型：</label>
			<div class="controls">
				<select path="" class="input-medium" id="smsType" name="smsType" >
					<option value="wordText" <c:if test="${'wordText' eq tSTemplate.smsType}">selected</c:if>>文字</option>
					<option value="voice" <c:if test="${'voice' eq tSTemplate.smsType}">selected</c:if>>语音</option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">接受号码类型：</label>
			<div class="controls">
				<select path="" class="input-medium" id="acceptType" name="acceptType">
					<option value="self" <c:if test="${'self' eq  tSTemplate.acceptType}">selected</c:if>>本人</option>
					<option value="others" <c:if test="${'others' eq tSTemplate.acceptType}">selected</c:if>>第三方</option>
					<option value="all" <c:if test="${'all' eq tSTemplate.acceptType}">selected</c:if>>本人和第三方</option>
				</select>
			</div>
		</div>
		
		<div class="control-group" id="reasons">
			<label class="control-label">发送逻辑：</label>
			<div class="controls">
				<input name="sendReason" id="sendReason"  value="${tSTemplate.sendReason}" type="text"  maxlength="20" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
					<span ><font color="red" id="tips"></font></span>
			</div>
		</div>
		
		<div id="overday">
			<label class="control-label ">可发送逾期天数：</label>
			<div >
				<input id="numbefore" name="numbefore" type="text" value="${  tSTemplate.numbefore}"  placeholder="不写默认为无下限-9999"/>——
			
				
				<input id="numafter" name="numafter" type="text" value="${ tSTemplate.numafter}"  placeholder="不写默认为无上限9999"/>
			</div>
				<span class="help-inline" ><font color="red" id="tipnum"></font> </span>
		</div>
		
		<div class="control-group">
			<label class="control-label ">发送产品：</label>
			<div class="controls ">
				<input type="checkbox" name="bizTypes" id="bizTypes1" value="mo9" <c:if test="${fn:contains(tSTemplate.bizTypes,'mo9')}">checked</c:if> />mo9
				<input type="checkbox" name="bizTypes" id="bizTypes2" value="weixin36" <c:if test="${fn:contains(tSTemplate.bizTypes,'weixin36')}">checked</c:if> />weixin36
				<span class="help-inline"><font color="red">*</font> </span>
				<span ><font color="red" id="bizTip"></font></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">模板内容：</label>
			<div class="controls">
				<textarea id="content" rows="10" name="smsCotent" htmlEscape="false" maxlength="500" class="input-xlarge required " />${tSTemplate.smsCotent}</textarea>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div style= "padding:8px 180px;" >
				<input id="smsSave" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
		
		
	</form:form>
</body>
</html>
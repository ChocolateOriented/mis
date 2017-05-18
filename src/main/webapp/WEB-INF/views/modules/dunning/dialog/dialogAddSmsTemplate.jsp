<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加短信模板</title>
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
				}
			 $("#reasons").hide();
			
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
			
			//保证人工名字唯一
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
			
			//添加
		$('#smsSave').click(function() {
			
			
			 
			//判断选择的逾期天数是否正确
			
			if($("#numbefore").val()){
				if(isNaN($("#numbefore").val())){
					$("#tipnum").html("请重新配置并填写正确的格式");
					 $("#smsSave").attr('disabled',"true");
					return;
				}
			}
			if($("#numafter").val()){
				if(isNaN($("#numafter").val())){
					$("#tipnum").html("请重新配置并填写正确的格式");
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
	                    url : "${ctx}/dunning/TmisDunningSmsTemplate/addSmsTemplate",
	                    type: 'POST',
	                    data: $('#inputForm').serialize(),             //获取表单数据
	                    success : function(data) {
	                        if (data == "OK") {
	                            alert("添加成功");
	                            window.parent.window.location.reload(); 
	                            window.parent.window.jBox.close();            //关闭子窗体
	                           
	                        } else {
	                            alert("添加失败:"+data.message);
	                            window.parent.window.jBox.close();
	                        }
	                    },
	                    error : function(XMLHttpRequest, textStatus, errorThrown){
	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
	                       alert("添加失败:"+textStatus);
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
		
		<div class="control-group">
			<label class="control-label">英文模板名称：</label>
			<div class="controls">
				<input  value="" id="englishTemplateName" name="englishTemplateName" htmlEscape="false"  class="input-xlarge required "  />
					<span class="help-inline"><font color="red">*</font> </span>
					<span ><font color="red" id="etipName"></font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模板名称：</label>
			<div class="controls">
				<input  value="" id="templateName" name="templateName" htmlEscape="false"  class="input-xlarge required "  />
					<span class="help-inline"><font color="red">*</font> </span>
					<span ><font color="red" id="tipName"></font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送方式：</label>
			<div class="controls">
				<select path="" class="input-medium" id="sendMethod" name="sendMethod">
					<option value="autoSend">系统</option>
					<option value="labourSend" >人工</option>
				</select>
			</div>
		</div>
		
		<div class="control-group" id="sendTime">
			<label class="control-label">发送时间：</label>
			<div class="controls">
				<input name="sendTime" type="text" readonly="readonly" maxlength="20" class="input-xlarge required "
					onclick="WdatePicker({dateFmt:'HH:mm',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		
		
		<div class="control-group">
			<label class="control-label">短信类型：</label>
			<div class="controls">
				<select path="" class="input-medium" id="smsType" name="smsType">
					<option value="wordText" >文字</option>
					<option value="voice">语音</option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">接受号码类型：</label>
			<div class="controls">
				<select path="" class="input-medium" id="acceptType" name="acceptType">
					<option value="others">第三方</option>
					<option value="self" >本人</option>
					<option value="all" >本人和第三方</option>
				</select>
			</div>
		</div>
		
		<div class="control-group" id="reasons">
			<label class="control-label">发送逻辑：</label>
			<div class="controls">
				<input name="sendReason" id="sendReason" type="text"  maxlength="20" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
					<span ><font color="red" id="tips"></font></span>
			</div>
		</div>
		
		<div id="overday">
			<label class="control-label ">可发送逾期天数：</label>
			<div >
				<input id="numbefore" name="numbefore" type="text" value=""  placeholder="不写默认为-9999"/>——
			
				
				<input id="numafter" name="numafter" type="text" value=""  placeholder="不写默认为9999"/>
			</div>
				<span class="help-inline" ><font color="red" id="tipnum"></font> </span>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">模板内容：</label>
			<div class="controls">
				<textarea id="content" rows="10" name="smsCotent" htmlEscape="false" maxlength="500" class="input-xlarge required " /></textarea>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div style= "padding:8px 180px;" >
				<input id="smsSave" class="btn btn-primary" type="button" value="添加"/>&nbsp;
				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
		
		
	</form:form>
</body>
</html>
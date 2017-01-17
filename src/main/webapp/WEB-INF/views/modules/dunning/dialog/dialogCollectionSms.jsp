<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收短信</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#smsSave').click(function() {
 			 if($("#inputForm").valid()){
 				$("#smsSave").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisContantRecord/saveRecord",
 	                    data: $('#inputForm').serialize(),             //获取表单数据
 	                    success : function(data) {
 	                        if (data == "OK") {
 	                            alert("保存成功");
//  	                            window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
 	                            window.parent.window.jBox.close();            //关闭子窗体
 	                        } else {
 	                            alert("保存失败:"+data.message);
 	                            //window.parent.page();
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
			
			$("#contactstype").change(function(){
				telEmpty();
				if($("#contactstype").val().toLowerCase()=="self" || $("#contactstype").val().toLowerCase()=="married" || $("#contactstype").val().toLowerCase()=="parent"
					|| $("#contactstype").val().toLowerCase()=="children" || $("#contactstype").val().toLowerCase()=="relatives"
					|| $("#contactstype").val().toLowerCase()=="workmate" || $("#contactstype").val().toLowerCase()=="worktel" || $("#contactstype").val().toLowerCase()=="friend"){
					$.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisContantRecord/getTelInfos",
 	                    data: {buyerId:$("#buyerId").val(),type:$("#contactstype").val()},             
 	                    success : function(data) {
 	                    	console.log(data);
 	                    	if(data.length>0){
 	                    		$("#contanttarget").val(data[0].tel);
 	                    	}
 	                    	
 	                    }
					})
				}else{				
					var url = "${ctx}/dunning/tMisContantRecord/dialogTelInfos?buyerId=" + $("#buyerId").val() + "&type=" + $("#contactstype").val() +"&dialogType=sms&dealcode=${dealcode}";
					$.jBox.open("iframe:" + url , $("#contactstype option:selected").text() + "手机号码" , 530, 250, {            
		               buttons: { "确定": "ok", "取消": true },
		                   submit: function (v, h, f) {
		                       if (v == "ok") {
		                    	   telEmpty();
		                           var iframeName = h.children(0).attr("name");
		                           var iframeHtml = window.frames[iframeName];                
		                           check_val = [];
		                           var sendMsgs = iframeHtml.document.getElementsByName("sendMsgInfo");
		                           for(k in sendMsgs){
			                       		if(sendMsgs[k].checked)
			                       			check_val.push(sendMsgs[k].value);
			                       	}
		                           $("#contanttarget").val(check_val);
		                           return true;
		                       }
		                   },
		               loaded: function (h) {
		                   $(".jbox-content", document).css("overflow-y", "hidden");
		               }
		         	});
				}
			 });
				
			$('#esc').click(function() {
				window.parent.window.jBox.close();    
			}); 
			
			// 短信类型选择
			$("#smstemp").change(function(){
				var url = "${ctx}/dunning/tMisContantRecord/smsGetTemp";
				$.ajax({
	                    type: 'POST',
	                    url : url,
	                    data: $('#inputForm').serialize(),             //获取表单数据
	                    success : function(data) {
	                        if (data != "") {
	                        	 $('#content').val(data);
	                        } 
	                    }
	             });
			});
			
			$("#content").html("${smsContext}");
		});
		
		
		function telEmpty(){
			$("#contanttarget").empty();
			$("#contanttarget").val("");
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">

	</ul>
	<form:form id="inputForm" modelAttribute="TMisContantRecord"  class="form-horizontal">
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<div class="control-group">
			<label class="control-label">短信类型：</label>
			<div class="controls">
				<select path="" class="input-medium" id="smstemp" name="smstemp">
					<option value=""></option>
					<%-- <options items="${fns:getDictList('gen_category')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
				<option value="ST_0" ${smsT=="ST_0"?'selected':''}>未逾期</option>
				<option value="ST__1_7" ${smsT=="ST__1_7"?'selected':''}>逾期1-7</option>
				<option value="ST_8_14" ${smsT=="ST_8_14"?'selected':''}>逾期8-14</option>
				<option value="ST_15_21" ${smsT=="ST_15_21"?'selected':''}>逾期15-21</option>
				<option value="ST_22_35" ${smsT=="ST_22_35"?'selected':''}>逾期22-35</option>
				<option value="ST_15_PLUS" ${smsT=="ST_15_PLUS"?'selected':''}>逾期15+</option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">联系对象：</label>
			<div class="controls">
				<select path="" class="input-medium" id="contactstype" name="contactstype">
					<option value=""></option>
					<option value="SELF" <c:if test="${'SELF' eq contactstype}">selected</c:if>>本人</option>
					<option value="MARRIED"<c:if test="${'MARRIED' eq contactstype}">selected</c:if>>夫妻</option>
					<option value="PARENT"<c:if test="${'PARENT' eq contactstype}">selected</c:if>>父母</option>
					<option value="CHILDREN"<c:if test="${'CHILDREN' eq contactstype}">selected</c:if>>子女</option>
					<option value="RELATIVES"<c:if test="${'RELATIVES' eq contactstype}">selected</c:if>>亲戚</option>
					<option value="FRIEND"<c:if test="${'FRIEND' eq contactstype}">selected</c:if>>朋友</option>
					<option value="WORKMATE"<c:if test="${'WORKMATE' eq contactstype}">selected</c:if>>同事</option>
					<option value="WORKTEL"<c:if test="${'WORKTEL' eq contactstype}">selected</c:if>>工作电话</option>
					<option value="CANTACT"<c:if test="${'CANTACT' eq contactstype}">selected</c:if>>通讯录</option>
					<option value="COMMUNCATE"<c:if test="${'COMMUNCATE' eq contactstype}">selected</c:if>>通话记录</option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">电话号码：</label>
			<div class="controls">
				<input  value="${selfMobile}" id="contanttarget" name="contanttarget" htmlEscape="false"  class="input-xlarge required "/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">短信内容：</label>
			<div class="controls">
				<textarea id="content" value="${smsContext}" rows="4" name="content" htmlEscape="false" maxlength="500" class="input-xlarge required " /></textarea>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<input path="" id="remark" rows="4" name="remark" htmlEscape="false" class="input-xlarge" />
			</div>
		</div>
		
		<input style="display:none;" id="contanttype" name="contanttype" value="sms" />
		<input style="display:none;" id="buyerId" name="buyerId" value="${buyerId}" />
		<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		<input style="display:none;" id="dunningtaskdbid" name="dunningtaskdbid" value="${dunningtaskdbid}" />
		
		<div style= "padding:8px 180px;" >
				<input id="smsSave" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
		
		
	</form:form>
</body>
</html>


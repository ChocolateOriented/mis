<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>群发短信</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			//  获取父页面的多选框的值并保存到隐藏input
			check_val = [];
			check_taskid = [];
			var orders = window.parent.document.getElementsByName("orders");
			for(k in orders){
            	if(orders[k].checked){
            		check_val.push(orders[k].value);
            		check_taskid.push(orders[k].title);
            	}
            }
			$("#ordersStr").val(check_val);
			$("#taskidStr").val(check_taskid);
			
			// 群发短信
			$('#groupSmsSend').click(function() {
 			  if($("#inputForm").valid()){
 				 $("#groupSmsSend").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisDunningTask/groupSmsSend",
 	                	data: $('#inputForm').serialize(),             //获取表单数据
 	                    success : function(data) {
 	                        if (data == "OK") {
 	                            alert("保存成功");
 	                            window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
 	                            window.parent.window.jBox.close();            //关闭子窗体
 	                        } else {
 	                            alert("保存失败:"+data.message);
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
			
			// 取消
			$('#esc').click(function() {
				window.parent.window.jBox.close();    
			});
			
			// 短信类型选择
			$("#smsTemplate").change(function(){
				var url = "${ctx}/dunning/tMisDunningTask/getDunningSmsTemplate";
				$.ajax({
	                    type: 'POST',
	                    url : url,
	                    data: $('#inputForm').serialize(),             //获取表单数据
// 	                    data : {
// 	                 		smsTemplate : $("#smsTemplate").val(),
// 	              	  	},
// 	 	                dataType:"json",
	                    success : function(data) {
	                        if (data != "") {
	                        	 $('#remarks').val(data);
	                        } 
	                    }
	             });
				
			 });
			
			
		});
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<br/>
	<form id="inputForm"  class="form-horizontal">
		<input type="hidden" id="ordersStr"  name="ordersStr" />
		<input type="hidden" id="taskidStr"  name="taskidStr" />
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<div class="control-group">
			<label class="control-label">短信类型：</label>
			<div class="controls">
				<select id="smsTemplate" name="smsTemplate" class="input-medium" >
					<%-- <options items="${fns:getDictList('gen_category')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
					<option value=""></option>
					<c:forEach items="${smsTemplates}" var="smsTemplate" varStatus="vs">
						<option value="${smsTemplate}">${smsTemplate.templateName}</option>
					</c:forEach>
<!-- 					<option value="STAGEA">逾期1-7</option> -->
<!-- 					<option value="STAGEC">逾期15-21</option> -->
<!-- 					<option value="STAGED">逾期22-35</option> -->
<!-- 					<option value="STAGEE">逾期35+</option> -->
<!-- 					<option value="STAGEF">其他</option> -->
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" disabled="disabled"  rows="4" maxlength="500">
				</textarea>
			<span class="help-inline"><font color="red">通用模板(金额,逾期天数,订单会自动填充)</font> </span>
			</div>
		</div>
		
		<div class="form-actions">
<%--  			<shiro:hasPermission name="buyerreport:mRiskBuyerReport:edit"> --%>
			<input id="groupSmsSend" class="btn btn-primary" type="button" value="发送"/>&nbsp;
 			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
<%--  			</shiro:hasPermission> --%>
		</div>
		
	</form>
	
</body>
</html>


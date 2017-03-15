<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手动分配</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			var mes = "${mes}";
			if("" != mes){
				alert(mes);
// 				window.parent.page();
                window.parent.window.jBox.close();
			}
			
			$('#distributioncycleSave').click(function() {
 			 if($("#inputForm").valid()){
//  				 $("#distributioncycleSave").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisDunningPeople/distributioncycleSave",
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
<%-- 	<input type="text" id="sss" name="ss" value="${mes}"/> --%>
	<br/>
	<form id="inputForm"    class="form-horizontal">
	
		<input type="hidden" id="peopleids" name="peopleids" value="${peopleids}"/>
		
		<div class="control-group">
			<label class="control-label">催收队列：</label>
			<div class="controls ">
				<c:forEach var="dc" items="${fns:getDictList('dunningCycle1')}" varStatus="s">
					<input type="checkbox" name="dunningcycle" value="${dc.label}" />
						${dc.label}
				</c:forEach>
<%-- 				<form:checkboxes  name="dunningcycle" items="${fns:getDictList('dunningCycle1')}" itemLabel="description" itemValue="label" htmlEscape="false" class="required"/> --%>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
 			<shiro:hasPermission name="dunning:tMisDunningPeople:edit">
				<input id="distributioncycleSave" class="btn btn-primary" type="button" value="分配"/>&nbsp;
 				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
 			</shiro:hasPermission>
		</div>
	</form>
	
</body>
</html>


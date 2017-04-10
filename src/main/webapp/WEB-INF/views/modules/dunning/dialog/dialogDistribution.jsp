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
			
			$('#distributionSave').click(function() {
 			 if($("#inputForm").valid()){
 				 $("#distributionSave").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisDunningTask/distributionSave",
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
	<form id="inputForm"  class="form-horizontal">
		<input type="hidden" id="orders" name="orders" value="${orders}"/>
		<input type="hidden" id="dunningcycle" name="dunningcycle" value="${dunningcycle}"/>
		<div class="control-group">
			<label class="control-label">催收人员：</label>
			<div class="controls">
<!-- 				<select id="dunningpeopleid" name="dunningpeopleid"  multiple="multiple" class="input-medium"> -->
<!-- 					<option value=""></option> -->
<%-- 					<c:forEach var="dunningPeople" items="${dunningPeoples}" varStatus="vs"> --%>
<%-- 						<option value="${dunningPeople.id}"> ${dunningPeople.name}</option> --%>
<%-- 					</c:forEach> --%>
<!-- 				</select> -->
				<select id="newdunningpeopleids" name="newdunningpeopleids"  style="width: 375px ;"  multiple="multiple" class="input-medium required">
					<c:forEach var="dunningPeople" items="${dunningPeoples}" varStatus="vs">
						<option value="${dunningPeople.id}"> ${dunningPeople.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-actions">
 			<shiro:hasPermission name="dunning:tMisDunningTask:directorview">
				<input id="distributionSave" class="btn btn-primary" type="button" value="分配"/>&nbsp;
 				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
 			</shiro:hasPermission>
		</div>
	</form>
	
</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人员配置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$('#operationSave').click(function() {
 			 if($("#inputForm").valid()){
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisDunningPeople/operationSave",
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
	<br/>
	<form:form id="inputForm" modelAttribute="TMisDunningPeople"  class="form-horizontal">
		<input type="hidden" id="peopleids" name="peopleids" value="${peopleids}"/>
		<ul class="ul-form">
		<c:if test="${operateId eq 'btnDunninggroup' }">
			<li><label>催收小组：</label> <form:select id="groupList" path="group.id" class="input-medium required">
				<form:option value="">全部</form:option>
					<!-- 添加组类型为optgroup -->
					<c:forEach items="${groupTypes}" var="type">
						<optgroup label="${type.value}">
							<!-- 添加类型对应的小组 -->
							<c:forEach items="${groupList}" var="item">
								<c:if test="${item.type == type.key}">
									<option value="${item.id}" >${item.name}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</form:select></li>
		</c:if>
		<c:if test="${operateId eq 'btnDunningauto' }">
		<div class="control-group">
			<label class="control-label">是否自动分配：</label>
			<div class="controls">
				<form:select id="auto" path="auto" class="input-medium">
					<form:option value="t" label="是" />
					<form:option value="f" label="否" />
				</form:select>
			</div>
		</div>
		</c:if>
		<div class="form-actions">
 			<shiro:hasPermission name="dunning:tMisDunningPeople:edit">
				<input id="operationSave" class="btn btn-primary" type="button" value="确定"/>&nbsp;
 				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
 			</shiro:hasPermission>
		</div>
			
		</ul>
	</form:form>
</body>
</html>


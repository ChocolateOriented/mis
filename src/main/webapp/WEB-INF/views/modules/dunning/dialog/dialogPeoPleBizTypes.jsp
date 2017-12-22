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
 	                    url : "${ctx}/dunning/tMisDunningPeople/batchUpdatepeopleBizTypes",
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
			<div class="control-group">
				<label class="control-label">产品：</label>
				<div class="controls">
					<form:checkboxes path="bizTypes" items="${bizTypes}" itemLabel="desc" class="input-medium  required" />
					<span class="help-inline"><font color="red">*</font></span>
				</div>
			</div>
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


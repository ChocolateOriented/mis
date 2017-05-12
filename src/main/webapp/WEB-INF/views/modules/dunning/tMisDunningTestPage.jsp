<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理测试页面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
		function query() {
			$.get("${ctx}/dunning/tMisDunningTest/query", {type: $("#type").val(), value: $("#value").val()}, function(data) {
				$("#label").val(data);
			});
		}
		
		function queryAll() {
			$.get("${ctx}/dunning/tMisDunningTest/queryAll", {type: $("#type").val()}, function(data) {
				var all = $("#all");
				all.empty();
				all.append($("<option></option>").val("").text(""));
				for (var i = 0; i < data.length; i++) {
					all.append($("<option></option>").val(data[i].value).text(data[i].label));
				}
				$("#all").select2();
			});
		}
	</script>
</head>
<body>
	<h4>&nbsp;</h4>
	<h4>&nbsp;&nbsp; Test</h4>
	<div style="margin:10px;">
		<div class="control-group">
			<label class="control-label">type：</label>
			<div class="controls" style="padding-top:3px;">
				<input type="text" id="type" name="type"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">value：</label>
			<div class="controls" style="padding-top:3px;">
				<input type="text" id="value" name="value"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">lable：</label>
			<div class="controls" style="padding-top:3px;">
				<input type="text" id="label" name="label" readonly/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">all：</label>
			<div class="controls" style="padding-top:3px;">
				<select id="all" name="all" style="width:200px;"/>
				</select>
			</div>
		</div>
		<div style= "padding:19px 180px 20px;">
			<input class="btn btn-primary" type="button" value="查询" onclick="query();"/>
			<input class="btn btn-primary" type="button" value="查询全部" onclick="queryAll();"/>
		</div>
	</div>
</body>
</html>
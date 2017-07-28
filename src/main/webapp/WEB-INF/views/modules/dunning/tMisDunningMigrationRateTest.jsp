<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>迁徙率测试</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	$("#migration").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/getMigration",
			type:"GET",
			data:{},
			success:function(data){
				
					top.$.jBox.tip("完成");
				
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("查询失败:"+textStatus);
                }
			
		});
	});
	
	$("#caulmigration").click(function(){
		if(!$("#margTime").val()){
			$("#tip").html("时间不能为空");
			alert(1);
			return ;
		}
		$("#tip").html("");
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/caulMigration?yesterday="+$("#margTime").val(),
			type:"GET",
			data:{},
			success:function(data){
				
					top.$.jBox.tip("完成");
				
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("查询失败:"+textStatus);
                }
			
		});
	});
	
});

</script>
</head>
<body>
	<input id="migration" class="btn btn-primary" type="button" value="获取迁徙率数据" />
	<div>----------------------------------</div>
	
	<li>
		<label>获取数据的时间</label>
		<input id="margTime" name="margTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
		onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
		<span ><font color="red" id="tip"></font></span>
		
	</li>
	<input id="caulmigration" class="btn btn-primary" type="button" value="获取计算后的迁徙率数据" />
	
</body>
</html>
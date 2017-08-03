<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>迁徙率测试</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	$("#migration1").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertTmpMoveCycleDB",
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
	
	$("#migration2").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoMigrationRateGetData",
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
	
	$("#migration3").click(function(){
		if(!$("#margTime").val()){
			$("#tip").html("时间不能为空");
			return ;
		}
		$("#tip").html("");
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertMigrationRateReportDB?yesterday="+$("#margTime").val(),
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
	
	$("#migration4").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertHistoryMigrationRateReportDB",
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
	<input id="migration1" class="btn btn-primary" type="button" value="插入cycyle" />
	<div>----------------------------------</div>
	<input id="migration2" class="btn btn-primary" type="button" value="更新迁徙基础表" />
	<li>
		<label>获取数据的时间</label>
		<input id="margTime" name="margTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
		onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
		<span ><font color="red" id="tip"></font></span>
	</li>
	<input id="migration3" class="btn btn-primary" type="button" value="计算并保存迁徙率报表" />
	
	<input id="migration4" class="btn btn-primary" type="button" value="迁徙率历史数据" />
	
	
</body>
</html>
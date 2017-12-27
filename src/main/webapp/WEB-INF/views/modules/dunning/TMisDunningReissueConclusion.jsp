<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>补发电催结论</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {


	
	
	
	
});
var iscommit=false;
	function reissueCoclusion(obj){
		if($("#conclusionDate").val()){
			if(confirm("确认补发电催结论")){
				if(!iscommit){
				 	iscommit=true;
					$.ajax({
						url:"${ctx}/dunning/tMisContantRecord/reissueCoclusion?today="+$("#conclusionDate").val()+"&conclusionType="+$("#conclusionType").val(),
						type:"GET",
						data:{},
						success:function(data){
							if(data){
								
								top.$.jBox.tip("完成");
							}else{
								top.$.jBox.tip("失败");
								
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown){
			                   alert("查询失败:"+textStatus);
			                }
						
					});	
				}else{
					$.jBox.tip("手速不要太快");
					return false;
				}
			}
		}else{
			$.jBox.tip("请选择时间");
			return false;
		}
	}

</script>
</head>
<body>
	<li>
		<label>补发电催结论的类型</label>
		<select id="conclusionType" name="conclusionType">
			<option value="cycleAndTime1">电催结论(Q0,Q1)</option>
			<option value="cycleAndTime2">电催结论(Q2,Q3,Q4)中午</option>
			<option value="cycleAndTime3">电催结论(Q2,Q3,Q4)晚上</option>
		</select>
	</li>
	<li>
		<label>补发电催结论的时间</label>
		<input id="conclusionDate" name="conclusionDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
		onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
	</li>
	<input id="reissueCoclusion" class="btn btn-primary" type="button" value="补发电催结论" onclick="return reissueCoclusion(this)" />
	
	
	
</body>
</html>
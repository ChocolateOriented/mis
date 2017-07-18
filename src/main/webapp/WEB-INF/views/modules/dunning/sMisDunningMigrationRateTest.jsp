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
			url:"${ctx}/dunning/sMisDunningTaskMonthReport/testMigration",
			type:"GET",
			data:{},
			success:function(data){
				
					top.$.jBox.tip("测试完成");
				
				
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
	<input id="migration" class="btn btn-primary" type="button" value="迁徙率数据获取" />
</body>
</html>
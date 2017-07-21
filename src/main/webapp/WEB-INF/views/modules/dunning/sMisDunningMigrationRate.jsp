<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>迁徙率图表</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	
	mrgiation();
});

function mrgiation(){
	$.post("${ctx}/dunning/sMisDunningTaskMonthReport/migrationGetdata", {}, function(data) {
	 	if($.isEmptyObject(data)) return;
    	var myChart1 = echarts.init(document.getElementById('main'));
       	myChart1.setOption({
    	title : {
	        text: '迁徙率图表',
// 	        subtext: '最近'+ weekNumber_cumulative + '周',
    	},
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:data['mirg']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	     grid:{
				 x:120,
			     x2:40,
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: data['cycle']
		    },
		    yAxis: {
		    	 type : 'value',
		         boundaryGap : [0, 0.01]
		    },
		    series:data['smdList']
	    });
	});	
}

</script>
</head>
<body>
	<div id="main" style="height: 480px; border: 1px solid #fff; padding: 10px;"></div>
</body>
</html>
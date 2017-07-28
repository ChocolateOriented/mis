<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>贷后迁徙本金图表</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	
	mrgiation();
});

function mrgiation(){
	$.post("${ctx}/dunning/tMisMigrationRateReport/migrationGetAmountdata", {}, function(data) {
	 	if($.isEmptyObject(data)) return;
    	var myCharts = document.getElementsByName('main');
    	for (var i=1; i< myCharts.length+1; i++) {
    		var  myChart=echarts.init(myCharts[i-1]);
           	myChart.setOption({
        	title : {
    	        text: '本金迁徙_c-p'+i,
        	},
    	    tooltip : {
    	        trigger: 'axis'
    	         
                 
    	    },
    	    legend: {
    	       
    	        data:data['qtime'+i]
    	    
    	       
    	    },
    	    toolbox: {
    	    	
    	        show : true,
    	        feature : {
    	            mark : {show: true},
    	            dataView : {show: true, readOnly: false},
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
    		    	  axisLabel: {  
    	                  show: true,  
    	                  interval: 'auto',  
    	                  formatter: '{value} %'  
    	                },  
    	            show: true  
    		    },
    		    series:data['smdList'+i],
    		    animationEasing :'BounceIn',
    		    backgroundColor:'seashell ' 
    	    });
		}
	});	
}

</script>
<style type="text/css">
.main{height: 480px; border: 1px solid #fff; padding: 10px;}
</style>
</head>
<body>
		
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/list">贷后迁徙日报</a></li>
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/migratechart">贷后迁徙图表</a></li>
		<li class="active"><a href="${ctx}/dunning/tMisMigrationRateReport/migrateAmountchart">贷后迁徙本金图表</a></li>
	</ul>
	
		<div id="main1" name="main" class="main" ></div>	
		<div id="main2" name="main" class="main" ></div>	
		<div id="main3" name="main" class="main" ></div>	
		<div id="main4" name="main" class="main" ></div>	
	
</body>
</html>
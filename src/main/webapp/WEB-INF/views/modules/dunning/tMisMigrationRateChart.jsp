<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<c:if test="${migrate eq 'new' }">
	<title>贷后迁徙户数图表</title>
</c:if>
<c:if test="${migrate eq 'corpus' }">
	<title>贷后迁徙本金图表</title>
</c:if>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	var corpusNew;
	if("new"==$("#migrate").val()){
		$("#new").addClass("active");
		corpusNew='户数迁徙_C-P';
	}
	if("corpus"==$("#migrate").val()){
		$("#corpus").addClass("active");
		corpusNew='本金迁徙_C-P';
	}
	mrgiation(corpusNew);
});

function mrgiation(corpusNew){
	var migrate=$("#migrate").val();
	$.post("${ctx}/dunning/tMisMigrationRateReport/migrationGetdata",{"migrate":migrate}, function(data) {
	 	if($.isEmptyObject(data)) return;
    	var myCharts = document.getElementsByName('main');
    	for (var i=1; i< myCharts.length+1; i++) {
    		var  myChart=echarts.init(myCharts[i-1]);
           	myChart.setOption({
        	title : {
    	        text: corpusNew+i,
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
    		    	 name:'单位（%）',
    		    	 splitNumber:data['split'+i],
    		    	 min:data['min'+i],
    		    	 max:data['max'+i],
    		    	 axisLabel :{
    		    		 show: true,
    		    		 formatter:function(value){
    		    			 if(value%1==0){
    		    				 return value+".00";
    		    			 }else{
    		    				 return value+"0";
    		    			 }
    		    		 }
    		    	 }
    		    },
    		    series:data['smdList'+i],
    		    animationEasing :'BounceIn'
    	    });
		}
	});	
}

</script>
<style type="text/css">
.main{height: 480px; border: 1px solid black; padding: 10px;}
</style>
</head>
<body>
	<input type="hidden" value="${migrate}" id="migrate" name="migrate"/>	
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dunning/tMisMigrationRateReport/list">贷后迁徙日报</a></li>
		<li id="new"><a href="${ctx}/dunning/tMisMigrationRateReport/migratechart?migrate=new">贷后迁徙户数图表</a></li>
		<li id="corpus"><a href="${ctx}/dunning/tMisMigrationRateReport/migratechart?migrate=corpus">贷后迁徙本金图表</a></li>
	</ul>
	
		<div id="main1" name="main" class="main" ></div>	
		<div id="main2" name="main" class="main" ></div>	
		<div id="main3" name="main" class="main" ></div>	
		<div id="main4" name="main" class="main" ></div>	
	
</body>
</html>
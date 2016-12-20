<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷周报图</title>
	<meta name="decorator" content="default"/>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsCumulativeOrders">累计订单数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsAddOrders">新增订单数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsSourceOrders">订单来源分布</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsMoneyOrders">订单金额分布</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsSingleusernum">累计放款用户数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsAmountallincome">总收益</a></li>
	</ul>
		
	<div style="height: 35px; background: #4AB0EA; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
		图表—累计订单数
	</div>
	<div id="cumulative_searchtool"
			style="height: 35px; position: relative; background: #F4F4F4; border-bottom: solid 1px #ddd">
			<table>
				<tr>
					<td style="color: #000; font-size: 12px;">
						<div style="float: left; margin-left: 10px;">
							近几周：
						<input id="weekNumber_cumulative" name="weekNumber_cumulative" maxlength="2" type="number" class="input-medium"  value="5" />
						</div>
						<input onclick="bwcumulativeOrders()" class="btn btn-primary" type="button" value="查询" />
					</td>
				</tr>
			</table>
		</div>
	<div id="bwcumulativeOrders_div" style="height: 480px; border: 1px solid #fff; padding: 10px;">
	</div>
	
<script type="text/javascript">
function validate(weekNumber){
	var num = /^[1-9]+[0-9]*]*$/;
	if(!num.test(weekNumber)){
		alert("请输入整数");
		return false;
	}
	return true;
}

function bwcumulativeOrders(){
		 	var weekNumber_cumulative = $("#weekNumber_cumulative").val();
		 	if(!validate(weekNumber_cumulative)) return;
		 	$.post("${ctx}/weeklyreport/sCashLoanWeeklyReport/findCumulativeOrders", { weekNumber : weekNumber_cumulative }, function(data) {
		 	if($.isEmptyObject(data)) return;
	    	var myChart1 = echarts.init(document.getElementById('bwcumulativeOrders_div'));
	       	myChart1.setOption({
	    	title : {
		        text: '累计订单数',
		        subtext: '最近'+ weekNumber_cumulative + '周',
	    	},
		    tooltip : {
		        trigger: 'axis'
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
		    xAxis : [
		        {
		            type : 'value',
		            boundaryGap : [0, 0.01]
		        }
		    ],
		    yAxis : [
		        {
		            type : 'category',
		            data : data['intervalDatetime']
		        }
		    ],
		    series : [
		        {
		        	itemStyle:{  
		               normal:{  
		                      label:{show:true}  
		                      ,areaStyle:{color:'green'}   //设置地图背景色的颜色设置  
		                      ,color:'rgb(74, 176, 234)' //刚才说的图例颜色设置  
		               },  
		               emphasis:{label:{show:true}}  
		           	},
		            name:'订单数',
		            type:'bar',
		            data:data['ordernumincludedelay']
		        }
			]
		    });
		});	
	}
	

	
$(function() {
	bwcumulativeOrders();

});


      
</script>
</body>



</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷周报图</title>
	<meta name="decorator" content="default"/>
<%-- 		<script src="${ctxStatic}/js/echarts-all.js" type="text/javascript"></script> --%>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=sCashLoanWeeklyReportCharts">现金贷周报图</a></li>
	</ul>
<!-- 	<input id="btnqy" name="btnck" onclick="chFunction('cumulative')" class="btn btn-primary" type="button" value="累计订单数图" /> -->
<!-- 	<input id="btnqy" name="btnck" onclick="chFunction('add')" class="btn btn-primary" type="button" value="新增订单图" /> -->
<!-- 	<input id="btnqy" name="btnck" onclick="chFunction('source')" class="btn btn-primary" type="button" value="订单来源分布图" /> -->
<!-- 	<input id="btnqy" name="btnck" onclick="chFunction('money')" class="btn btn-primary" type="button" value="订单金额分布图" /> -->
		
	<div style="height: 35px; background: #B23AEE; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
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
	
	
	<div style="height: 35px; background:#B23AEE; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
		图表—新增订单
	</div>
	<div id="add_searchtool"
			style="height: 35px; position: relative;   background: #F4F4F4; border-bottom: solid 1px #ddd">
			<table>
				<tr>
					<td style="color: #000; font-size: 12px;">
						<div style="float: left; margin-left: 10px;">
							近几周：
						<input id="weekNumber_add" name="weekNumber_add" maxlength="2" type="number" class="input-medium"  value="5" />
						</div>
						<input onclick="bwaddOrders()" class="btn btn-primary" type="button" value="查询" />
					</td>
				</tr>
			</table>
		</div>
	<div id="bwaddOrders_div" style="height: 480px; border: 1px solid #fff; padding: 10px;">
	</div>
	
	
	<div style="height: 35px; background:#B23AEE; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
		图表—订单来源分布
	</div>
	<div id="source_searchtool"
			style="height: 35px; position: relative;  background: #F4F4F4; border-bottom: solid 1px #ddd">
			<table>
				<tr>
					<td style="color: #000; font-size: 12px;">
						<div style="float: left; margin-left: 10px;">
							近几周：
						<input id="weekNumber_source" name="weekNumber_source" maxlength="2" type="number" class="input-medium"  value="5" />
						</div>
						<input  onclick="bwsourceOrders()" class="btn btn-primary" type="button" value="查询" />
					</td>
				</tr>
			</table>
		</div>
		<div id="bwsourceOrders_div"
			 style="height: 480px; border: 1px solid #fff; padding: 10px;">
		</div>
	
	
	<div style="height: 35px; background:#B23AEE; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
		图表—订单金额分布
	</div>
	<div id="money_searchtool"
			style="height: 35px; position: relative;  background: #F4F4F4; border-bottom: solid 1px #ddd">
			<table>
				<tr>
					<td style="color: #000; font-size: 12px;">
						<div style="float: left; margin-left: 10px;">
							近几周：
						<input id="weekNumber_money" name="weekNumber_money" maxlength="2" type="number" class="input-medium"  value="5" />
						</div>
						<input   onclick="bwmoneyOrders()" class="btn btn-primary" type="button" value="查询" />
					</td>
				</tr>
			</table>
		</div>
		<div id="bwmoneyOrders_div"
			 style="height: 480px; border: 1px solid #fff; padding: 10px;">
		</div>
	
	
	
		<script type="text/javascript">
function validate(weekNumber){
	var num = /^[1-9]+[0-9]*]*$/;
	if(!num.test(weekNumber)){
		alert("请输入整数");
// 		 $.jBox.tip('Hello jBox');  
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
	
function bwaddOrders(){
	 	var weekNumber_add = $("#weekNumber_add").val();
	 	if(!validate(weekNumber_add)) return;
	 	$.post("${ctx}/weeklyreport/sCashLoanWeeklyReport/findaddOrders", { weekNumber : weekNumber_add }, function(data) {
	 	if($.isEmptyObject(data)) return;
		var myChart2 = echarts.init(document.getElementById('bwaddOrders_div'));
	   	myChart2.setOption({
		title : {
	        text: '新增订单数',
	        subtext: '最近'+ weekNumber_add + '周',
		},
	    tooltip : {
	        trigger: 'axis'
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            magicType: {show: true, type: ['line', 'bar','stack', 'tiled']},
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
	                      ,color:'rgb(230, 93, 70)' //刚才说的图例颜色设置  
	               },  
	               emphasis:{label:{show:true}}  
	           	},
	            name:'新用户订单数',
	            type:'bar',
	            data:data['newuser']
	        },
	        {
	        	itemStyle:{  
		               normal:{  
		                      label:{show:true}  
		                      ,areaStyle:{color:'green'}   //设置地图背景色的颜色设置  
		                      ,color:'rgb(74, 176, 234)' //刚才说的图例颜色设置  
		               },  
		               emphasis:{label:{show:true}}  
		           	},
		            name:'老用户订单数',
		            type:'bar',
		            data:data['olduser']
		     }
		  ]
	    });
	});	
}
	
function bwsourceOrders() {
	var weekNumber_source = $("#weekNumber_source").val();
	if(!validate(weekNumber_source)) return;
	$.post("${ctx}/weeklyreport/sCashLoanWeeklyReport/findsourceOrders", {  weekNumber : weekNumber_source }, function(data) {
		if($.isEmptyObject(data)) return;
		var myChart3 = echarts.init(document.getElementById('bwsourceOrders_div'));
		myChart3.setOption( {
			title : {
				text : '订单来源分布',
				subtext : 'sourcedistribution'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ 'APP订单占比', '微信订单占比' ]
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar', 'stack', 'tiled' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			xAxis : [ {
				type : 'category',
				boundaryGap : false,
				data : data["intervalDatetime"]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				name : 'APP订单占比',
				type : 'line',
				symbol : 'none',
				smooth : true,
				itemStyle : {
					normal : {
						areaStyle : {
							type : 'default'
						}
					}
				},
				data : data["app"]
			},
			{
				name : '微信订单占比',
				type : 'line',
				symbol : 'none',
				smooth : true,
				itemStyle : {
					normal : {
						areaStyle : {
							type : 'default'
						}
					}
				},
				data : data["wechat"]
			},
			]
		});
	});
}
	
	
function bwmoneyOrders() {
	var weekNumber_money = $("#weekNumber_money").val();
	if(!validate(weekNumber_money)) return;
	$.post("${ctx}/weeklyreport/sCashLoanWeeklyReport/findmoneyOrders", {  weekNumber : weekNumber_money }, function(data) {
		if($.isEmptyObject(data)) return;
		var myChart4 = echarts.init(document.getElementById('bwmoneyOrders_div'));
		myChart4.setOption( {
			title : {
				text : '订单金额分布',
				subtext : 'moneydistribution'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ '500元占比', '1000元占比' ]
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar', 'stack', 'tiled' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			xAxis : [ {
				type : 'category',
				boundaryGap : false,
				data : data["intervalDatetime"]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				name : '500元占比',
				type : 'line',
				symbol : 'none',
				smooth : true,
				itemStyle : {
					normal : {
						areaStyle : {
							type : 'default'
						}
					}
				},
				data : data["500p"]
			},
			{
				name : '1000元占比',
				type : 'line',
				symbol : 'none',
				smooth : true,
				itemStyle : {
					normal : {
						areaStyle : {
							type : 'default'
						}
					}
				},
				data : data["1000p"]
			},
			]
		});
	});
}
	
$(function() {
	bwcumulativeOrders();
	bwaddOrders();
	bwsourceOrders();
	bwmoneyOrders();
});

function chFunction(type){
	 
}
      
</script>
</body>



</html>
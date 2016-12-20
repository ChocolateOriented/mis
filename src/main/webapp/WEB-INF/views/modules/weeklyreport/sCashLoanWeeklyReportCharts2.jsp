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
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=sCashLoanWeeklyReportCharts2">现金贷周报图</a></li>
<%-- 		<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:charts"> --%>
<%-- 		<li><a href="${ctx}/buyerreport/mRiskBuyerReport/form">用户报表添加</a></li> --%>
<%-- 		</shiro:hasPermission> --%>
	</ul>
	<div style="height: 35px; background: #B23AEE; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
		图表—总收益
	</div>
	<div id="amountallincome_searchtool"
			style="height: 35px; position: relative; background: #F4F4F4; border-bottom: solid 1px #ddd">
			<table>
				<tr>
					<td style="color: #000; font-size: 12px;">
						<div style="float: left; margin-left: 10px;">
							近几周：
						<input id="weekNumber_amountallincome" name="weekNumber_amountallincome" maxlength="2" type="number" class="input-medium"  value="5" />
						</div>
						<input onclick="bwamountallincome()" class="btn btn-primary" type="button" value="查询" />
					</td>
				</tr>
			</table>
		</div>
	<div id="bwamountallincome_div" style="height: 480px; border: 1px solid #fff; padding: 10px;">
	</div>
	
	<div style="height: 35px; background: #B23AEE; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
		图表—累计放款用户数
	</div>
	<div id="singleusernum_searchtool"
			style="height: 35px; position: relative;  background: #F4F4F4; border-bottom: solid 1px #ddd">
			<table>
				<tr>
					<td style="color: #000; font-size: 12px;">
						<div style="float: left; margin-left: 10px;">
							近几周：
						<input id="weekNumber_singleusernum" name="weekNumber_singleusernum" maxlength="2" type="number" class="input-medium"  value="5" />
						</div>
						<input  onclick="bwsingleusernum()" class="btn btn-primary" type="button" value="查询" />
					</td>
				</tr>
			</table>
		</div>
		<div id="bwsingleusernum_div"
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

function bwamountallincome(){
		 	var weekNumber_amountallincome = $("#weekNumber_amountallincome").val();
		 	if(!validate(weekNumber_amountallincome)) return;
		 	$.post("${ctx}/weeklyreport/sCashLoanWeeklyReport/findAmountallincome", { weekNumber : weekNumber_amountallincome }, function(data) {
		 	if($.isEmptyObject(data)) return;
	    	var myChart1 = echarts.init(document.getElementById('bwamountallincome_div')
// 	    			,{noDataLoadingOption: {
// 	                text: '暂无数据',
// 	                effect: 'bubble',
// 	                effectOption: {
// 	                    effect: {
// 	                        n: 0
// 	                    }
// 	                }}}
	    	);
	       	myChart1.setOption({
	    	title : {
		        text: '总收益',
		        subtext: '最近'+ weekNumber_amountallincome + '周',
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
		            name:'收益',
		            type:'bar',
		            data:data['amountallincome']
		        }
			]
		    });
		});	
	}
	
function bwsingleusernum() {
	var weekNumber_singleusernum = $("#weekNumber_singleusernum").val();
	if(!validate(weekNumber_singleusernum)) return;
	$.post("${ctx}/weeklyreport/sCashLoanWeeklyReport/findSingleusernum", {  weekNumber : weekNumber_singleusernum }, function(data) {
		if($.isEmptyObject(data)) return;
		var myChart2 = echarts.init(document.getElementById('bwsingleusernum_div'));
		myChart2.setOption( {
			title : {
				text : '累计放款用户数',
				subtext : 'singleusernum'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ '用户数' ]
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
				name : '放款用户数',
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
				data : data["singleusernum"]
			},
			
			]
		});
	});
}
	
$(function() {
	bwamountallincome();
	bwsingleusernum();
});
</script>
</body>

</html>
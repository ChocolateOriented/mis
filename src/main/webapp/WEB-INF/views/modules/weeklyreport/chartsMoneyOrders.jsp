<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷周报图</title>
	<meta name="decorator" content="default"/>
</head>
<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsCumulativeOrders">累计订单数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsAddOrders">新增订单数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsSourceOrders">订单来源分布</a></li>
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsMoneyOrders">订单金额分布</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsSingleusernum">累计放款用户数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsAmountallincome">总收益</a></li>
	</ul>
	
	<div style="height: 35px; background:#4AB0EA; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
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
		return false;
	}
	return true;
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
				data : [ '500元占比', '1000元占比', '1500元占比' ]
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
						label:{show:true} ,
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
						label:{show:true} ,
						areaStyle : {
							type : 'default'
						}
					}
				},
				data : data["1000p"]
			},
			{
				name : '1500元占比',
				type : 'line',
				symbol : 'none',
				smooth : true,
				itemStyle : {
					normal : {
						label:{show:true} ,
						areaStyle : {
							type : 'default'
						}
					}
				},
				data : data["1500p"]
			}
			]
		});
	});
}
	
$(function() {
	bwmoneyOrders();
});

      
</script>
</body>



</html>
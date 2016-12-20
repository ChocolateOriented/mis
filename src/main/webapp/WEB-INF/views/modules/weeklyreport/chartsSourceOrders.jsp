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
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsSourceOrders">订单来源分布</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsMoneyOrders">订单金额分布</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsSingleusernum">累计放款用户数</a></li>
		<li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/cumulativeOrdersCharts?chartsType=chartsAmountallincome">总收益</a></li>
	</ul>
	
	<div style="height: 35px; background:#4AB0EA; line-height: 28px; padding-left: 8px; color: #fff; font-size: 13px; font-weight: bold;">
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
	
	
		<script type="text/javascript">
function validate(weekNumber){
	var num = /^[1-9]+[0-9]*]*$/;
	if(!num.test(weekNumber)){
		alert("请输入整数");
		return false;
	}
	return true;
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
						label:{show:true} ,
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
						label:{show:true} ,
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
	
	
	
$(function() {
	bwsourceOrders();
});

</script>
</body>



</html>
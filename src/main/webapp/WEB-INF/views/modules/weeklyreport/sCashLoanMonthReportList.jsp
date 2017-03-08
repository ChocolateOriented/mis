<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务日报管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			 // 导出功能
			 $("#sCashLoanmonthReportExport").click(function(){
				top.$.jBox.confirm("确认要导出财务月报数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/financdailyreport/sCashLoanDailyReport/monthReportExportFile");
						$("#searchForm").submit();
					}
				},
				{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			 });
		});
		
		function onClickMonthDetails(obj){
			var aid = $(obj).attr("aid");
			var type = $(obj).attr("type");
			var url = "${ctx}/financdailyreport/sCashLoanDailyReport/monthdetails?id="+ aid +"&type=" + type;
			$.jBox.open("iframe:" + url, "详情", 600, 350, {            
                   buttons: {"确定": "ok"},
                   loaded: function (h) {
                       $(".jbox-content", document).css("overflow-y", "hidden");
                   }
             });
		}
		
		// 查询
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/financdailyreport/sCashLoanDailyReport/monthlist");
			$("#searchForm").submit();
        	return false;
        } 
		

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:dailyview"><li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/list">财务日报列表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:weekview"><li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/weeklist">财务周报列表</a></li></shiro:hasPermission>
        <shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:monthview"><li class="active"><a href="${ctx}/financdailyreport/sCashLoanDailyReport/monthlist">财务月报列表</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanDailyReport" action="${ctx}/financdailyreport/sCashLoanDailyReport/monthlist" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>日期：</label>
				<input name="beginCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${sCashLoanMonthReport.beginCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${sCashLoanMonthReport.endCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
		    <li class="btns"><input id="sCashLoanmonthReportExport" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
				<th></th>
				<th colspan="5" style="text-align:center;">放贷</th>
				<th colspan="3" style="text-align:center;">收入</th>
				<th colspan="2" style="text-align:center;">贷前征信成本</th>
				<th colspan="6" style="text-align:center;">贷后催收成本</th>
				<th colspan="3" style="text-align:center;">渠道成本</th>
				<th colspan="2" style="text-align:center;">推广成本</th>
<!-- 				<th colspan="2" style="text-align:center;">毛利 </th> -->
				<th colspan="1" style="text-align:center;">资金成本</th>
<!-- 				<th></th> -->
			</tr>
		
			<tr>
				<th>日期</th>
				<th title="本期累计(本次统计周期内, 累几放贷金额)">放贷金额</th>
				<th title="增长率 = 本期累计交易总额 / 上期累计交易总额">月增长率</th>
				<th title="本次统计周期内, 新增的放款独立用户数">新增贷款用户</th>
				<th title="本次统计周期内, 新增的放贷笔数, 包括延期笔数">放贷笔数</th>
				<th title="">平均单笔放贷金额</th>
				
				<th title="本次统计周期内, 实际收到的 信审收益 + 延期手续费 + 逾期费 + 委外(部分还款不计入)">收入金额</th>
				<th title="收入增长率 = (本期收入 - 上期收入) / 上期收入">月增长率</th>
				<th title="单笔收入 = 本期收入 / 本期成功还清笔数	">平均单笔收入</th>
				
				<th title="本次统计周期内, 使用第三方征信数据所产生的费用总和">征信成本</th>
				<th title="人均成本 = 本期总成本 / 本期新增交易用户数">人均成本</th>
				
				<th title="本期累计 = 本期目前累计坏账 -  上期目前累计坏账">坏账金额</th>
				<th title="">坏账催回本金</th>
				<th title="">催收减免</th>
				<th title="委外回款所扣除的佣金总金额, 佣金月结">委外佣金</th>
				<th title="自产品上线之日起, 累计坏账">历史累计坏账</th>
				<th title="坏账率 = 目前累计 / 自产品上线之日起的累计交易总额">总坏账率</th>
				

				<th title="放款成本 = 本期放贷笔数(不包括延期)">放款成本</th>
				<th title="使用各支付渠道所产生的支付渠道费	">还款成本</th>
				<th title="">平均还款费率</th>
				
				<th title="本次统计周期内, 被消耗掉的免息券及其他抵扣金额的总和">优惠减免</th>
				<th title="以我方收到的发票的日期为准">活动宣传</th>
				
				
<!-- 				<th title="毛利 = 收入 - 支付渠道费 - 征信成本 - 市场推广 - 坏账本期累计 - 委外佣金">毛利</th> -->
<!-- 				<th title="毛利率 = 毛利 / 收入">毛利率</th> -->
				<th title="利息 + 手续费">利息+手续费</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${monthlist}" var="sCashLoanMonthReport">
			<tr>
				<td>
					${sCashLoanMonthReport.month}
				</td>
				
				
				
				<td><a href="javascript:void(0)"  onclick="onClickMonthDetails(this)" aid="${sCashLoanMonthReport.id}" type="Remitamount">
					${sCashLoanMonthReport.remitamountText}</a>
				</td>
				<td>
					${sCashLoanMonthReport.monthincreasedText}
				</td>
				<td><a href="javascript:void(0)" onclick="onClickMonthDetails(this)" aid="${sCashLoanMonthReport.id}" type="Newuser">
					${sCashLoanMonthReport.newuserText}</a>
				</td>
				<td><a href="javascript:void(0)"  onclick="onClickMonthDetails(this)" aid="${sCashLoanMonthReport.id}" type="Neworder">
					${sCashLoanMonthReport.neworderText}</a>
				</td>
				<td>
					${sCashLoanMonthReport.monthSingleRemitAvgAmountText}
				</td>
				
				
				
				<td><a href="javascript:void(0)"  onclick="onClickMonthDetails(this)" aid="${sCashLoanMonthReport.id}" type="Incomeamount">
					${sCashLoanMonthReport.incomeamountText}</a>
				</td>
				<td>
					${sCashLoanMonthReport.monthincomeincreasedText} 
				</td>
				<td>
					${sCashLoanMonthReport.monthincomepercentText}
				</td>
				
				
				
				<td>
					${sCashLoanMonthReport.creditsumcostText}
				</td>
				<td>
					${sCashLoanMonthReport.monthCreditAvgCostText} 
				</td>
				
				
				
				<td>
					${sCashLoanMonthReport.monthdebatamountText}
				</td>
				<td>
					${sCashLoanMonthReport.debatreturnamountText}
				</td>
				<td>
				</td>
				<td>
					${sCashLoanMonthReport.entrustcommissionText}
				</td>
				<td>
					${sCashLoanMonthReport.debatamountText}
				</td>
				<td>
					${sCashLoanMonthReport.monthdebatpercentText} 
				</td>
				
				
				
				
				<td>
					${sCashLoanMonthReport.loancostText}
				</td>
				<td><a href="javascript:void(0)"  onclick="onClickMonthDetails(this)" aid="${sCashLoanMonthReport.id}" type="Repaycost">
					${sCashLoanMonthReport.repaycostText}</a>
				</td>
				<td>
					${sCashLoanMonthReport.monthrepayavgcostText}
				</td>
				
				
				
				
				<td><a href="javascript:void(0)"  onclick="onClickMonthDetails(this)" aid="${sCashLoanMonthReport.id}" type="Couponcost">
					${sCashLoanMonthReport.couponcostText}</a>
				</td>
				<td>
					${sCashLoanMonthReport.mediacostText}
				</td>
				
				
				
				
				
<!-- 				<td> -->
<%-- 					${sCashLoanMonthReport.grossprofileText} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sCashLoanMonthReport.monthGrossProfilePercentText} --%>
<!-- 				</td> -->
				<td>
					${sCashLoanMonthReport.cashcostamountText}
				</td>
			</tr>
		</c:forEach>
		
		<tr bgcolor="#FEE7AB">
				<td>
					合计：
				</td>
				
				
				<td> 
					${sumMonthReportBean.remitamountText} 
				</td>
				<td>
					${sumMonthReportBean.monthincreasedText}
				</td>
				<td> 
					${sumMonthReportBean.newuserText} 
				</td>
				<td> 
					${sumMonthReportBean.neworderText} 
				</td>
				<td>
<%-- 					${sumMonthReportBean.monthSingleRemitAvgAmountText} --%>
				</td>
				
				
				
				<td> 
					${sumMonthReportBean.incomeamountText} 
				</td>
				<td>
					${sumMonthReportBean.monthincomeincreasedText} 
				</td>
				<td>
<%-- 					${sumMonthReportBean.monthincomepercentText} --%>
				</td>
				
				
								
				<td>
					${sumMonthReportBean.creditsumcostText}
				</td>
				<td>
<%-- 					${sumMonthReportBean.monthCreditAvgCostText}  --%>
				</td>
				
				
					
				<td>
					${sumMonthReportBean.monthdebatamountText}
				</td>
				<td>
					${sumMonthReportBean.debatreturnamountText}
				</td>
				<td>
				</td>
				<td>
					${sumMonthReportBean.entrustcommissionText}
				</td>
				<td>
					${sumMonthReportBean.debatamountText}
				</td>
				<td>
					${sumMonthReportBean.monthdebatpercentText} 
				</td>
				
				
				
				<td>
					${sumMonthReportBean.loancostText}
				</td>
				<td> 
					${sumMonthReportBean.repaycostText} 
				</td>
				<td>
					${sumMonthReportBean.monthrepayavgcostText}
				</td>
				
				
				
				
				<td> 
					${sumMonthReportBean.couponcostText} 
				</td>
				<td>
					${sumMonthReportBean.mediacostText}
				</td>
				
				
			
				
<!-- 				<td> -->
<%-- 					${sumMonthReportBean.grossprofileText} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sumMonthReportBean.monthGrossProfilePercentText} --%>
<!-- 				</td> -->
				<td>
					${sumMonthReportBean.cashcostamountText}
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
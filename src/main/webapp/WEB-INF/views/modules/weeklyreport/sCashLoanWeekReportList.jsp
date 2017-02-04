<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务周报</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			 // 导出功能
			 $("#sCashLoanweekReportExport").click(function(){
				top.$.jBox.confirm("确认要导出财务周报数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/financdailyreport/sCashLoanDailyReport/weekReportExportFile");
						$("#searchForm").submit();
					}
				},
				{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			 });
		});
		
		function onClickWeekDetails(obj){
			var aid = $(obj).attr("aid");
			var type = $(obj).attr("type");
			var url = "${ctx}/financdailyreport/sCashLoanDailyReport/weekdetails?id="+ aid +"&type=" + type;
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
			$("#searchForm").attr("action","${ctx}/financdailyreport/sCashLoanDailyReport/weeklist");
			$("#searchForm").submit();
        	return false;
        }
		
		
// 		function page(n,s){
// 			$("#searchForm").submit();
//         	return false;
//         }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:dailyview"><li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/list">财务日报列表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:weekview"><li class="active"><a href="${ctx}/financdailyreport/sCashLoanDailyReport/weeklist">财务周报列表</a></li></shiro:hasPermission>
        <shiro:hasPermission name="financdailyreport:sCashLoanDailyReport:monthview"><li><a href="${ctx}/financdailyreport/sCashLoanDailyReport/monthlist">财务月报列表</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanDailyReport" action="${ctx}/financdailyreport/sCashLoanDailyReport/weeklist" method="post" class="breadcrumb form-search">
<%-- 		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/> --%>
<%-- 		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> --%>
		<ul class="ul-form">
			<li><label>日期：</label>
				<input name="beginCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SCashLoanDailyReport.beginCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endCreatetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SCashLoanDailyReport.endCreatetime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
		    <li class="btns"><input id="sCashLoanweekReportExport" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
				<th ></th>
				<th colspan="5" style="text-align:center;">交易总额</th>
				<th colspan="3" style="text-align:center;">收入</th>
				<th colspan="2" style="text-align:center;">贷前征信成本</th>
				<th colspan="4" style="text-align:center;">贷后催收成本</th>
				<th colspan="3" style="text-align:center;">渠道成本</th>
				<th colspan="2" style="text-align:center;">推广成本</th>
<!-- 				<th colspan="2" style="text-align:center;">毛利 </th> -->
				<th colspan="1" style="text-align:center;">资金成本</th>
				<th></th>
			</tr>
		
			<tr>
				<th >日期</th>
				
				<th title="本期累计(本次统计周期内, 累几放贷金额)">本期累计</th>
				<th title="增长率 = 本期累计交易总额 / 上期累计交易总额">周增长率</th>
				<th title="本次统计周期内, 新增的放款独立用户数">新增交易用户</th>
				<th title="本次统计周期内, 新增的放贷笔数, 包括延期笔数">放贷笔数</th>
				<th title="">平均单笔放贷金额</th>
				
				<th title="本次统计周期内, 实际收到的 信审收益 + 延期手续费 + 逾期费 + 委外(部分还款不计入) ">本期收入</th>
				<th title="收入增长率 = (本期收入 - 上期收入) / 上期收入 ">周收入增长率</th>
				<th title="单笔收入 = 本期收入 / 本期成功还清笔数">周单笔收入</th>
				
				<th title="本次统计周期内, 使用第三方征信数据所产生的费用总和  ">征信成本</th>
				<th title="人均成本 = 本期总成本 / 本期新增交易用户数">人均成本</th>
				
				
				<th title="本期累计 = 本期目前累计坏账 - 上期目前累计坏账">本期累计</th>
				<th title="">催收减免</th>
<!-- 				<th title="">委外佣金</th> -->
				<th title="自产品上线之日起, 累计坏账">目前累计</th>
				<th title="坏账率 = 目前累计 / 自产品上线之日起的累计交易总额">坏账率</th>
				

				<th title="放款成本 = 本期放贷笔数(不包括延期)">放款成本</th>
				<th title="使用各支付渠道所产生的支付渠道费">还款成本</th>
				<th title="">平均还款费率</th>
				
				<th title="本次统计周期内, 被消耗掉的免息券及其他抵扣金额的总和">优惠减免</th>
				<th title="以我方收到的发票的日期为准  ">活动宣传</th>
				

<!-- 				<th title="毛利 = 收入 - 支付渠道费 - 征信成本 - 市场推广 - 坏账本期累计 - 委外佣金">毛利</th> -->
<!-- 				<th title="毛利率 = 毛利 / 收入">毛利率</th> -->
				<th title="利息 + 手续费">利息+手续费</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${weeklist}" var="sCashLoanweekReport">
			<tr>
				<td>
					${sCashLoanweekReport.weekdesc}
				</td>
				
				
				<td><a href="javascript:void(0)"  onclick="onClickWeekDetails(this)" aid="${sCashLoanweekReport.id}" type="Remitamount">
					${sCashLoanweekReport.remitamountText}</a>
				</td>
				<td>
					${sCashLoanweekReport.weekincreasedText}
				</td>
				<td><a href="javascript:void(0)" onclick="onClickWeekDetails(this)" aid="${sCashLoanweekReport.id}" type="Newuser">
					${sCashLoanweekReport.newuserText}</a>
				</td>
				<td><a href="javascript:void(0)"  onclick="onClickWeekDetails(this)" aid="${sCashLoanweekReport.id}" type="Neworder">
					${sCashLoanweekReport.neworderText}</a>
				</td>
				<td>					  
					${sCashLoanweekReport.weekSingleRemitAvgAmountText}
				</td>
				
				
				
				<td><a href="javascript:void(0)"  onclick="onClickWeekDetails(this)" aid="${sCashLoanweekReport.id}" type="Incomeamount">
					${sCashLoanweekReport.incomeamountText}</a>
				</td>
				<td>
					${sCashLoanweekReport.incomeincreasedText}
				</td>
				<td>
					${sCashLoanweekReport.incomepercentText}
				</td>
				
				
				<td>
					${sCashLoanweekReport.creditsumcostText}
				</td>
				<td>
					${sCashLoanweekReport.creditavgcostText}
				</td>
				
				
				
				<td>
					${sCashLoanweekReport.weekdebatamountText}
				</td>
				<td>
				</td>
<!-- 				<td> -->
<!-- 				</td> -->
				<td>
					${sCashLoanweekReport.debatamountText}
				</td>
				<td>
					${sCashLoanweekReport.debatpercentText}
				</td>
				
				
				
				
				<td>
					${sCashLoanweekReport.loancostText}
				</td>
				<td><a href="javascript:void(0)"  onclick="onClickWeekDetails(this)" aid="${sCashLoanweekReport.id}" type="Repaycost">
					${sCashLoanweekReport.repaycostText}</a>
				</td>
				<td>
					${sCashLoanweekReport.weekrepayavgcostText}
				</td>
				
				
				<td><a href="javascript:void(0)"  onclick="onClickWeekDetails(this)" aid="${sCashLoanweekReport.id}" type="Couponcost">
					${sCashLoanweekReport.couponcostText}</a>
				</td>
				<td>
					${sCashLoanweekReport.mediacostText}
				</td>
				
				
				
<!-- 				<td> -->
<%-- 					${sCashLoanweekReport.grossprofileText} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sCashLoanweekReport.grossprofilepercentText} --%>
<!-- 				</td> -->
				<td>
					${sCashLoanweekReport.cashcostamountText}
				</td>
			</tr>
		</c:forEach>
		
		<tr bgcolor="#FEE7AB">
				<td>
					合计：
				</td>
				
				
				<td>
					${sumWeekReportBean.remitamountText} 
				</td>
				<td>
					${sumWeekReportBean.weekincreasedText}
				</td>
				<td> 
					${sumWeekReportBean.newuserText} 
				</td>
				<td> 
					${sumWeekReportBean.neworderText} 
				</td>
				<td>					  
<%-- 					${sumWeekReportBean.weekSingleRemitAvgAmountText} --%>
				</td>
				
				
				
				<td> 
					${sumWeekReportBean.incomeamountText} 
				</td>
				<td>
					${sumWeekReportBean.incomeincreasedText}
				</td>
				<td>
<%-- 					${sumWeekReportBean.incomepercentText} --%>
				</td>
				
				
				
				<td>
					${sumWeekReportBean.creditsumcostText}
				</td>
				<td>
<%-- 					${sumWeekReportBean.creditavgcostText} --%>
				</td>
				
				
				
								
				
				<td>
					${sumWeekReportBean.weekdebatamountText}
				</td>
				<td>
				</td>
<!-- 				<td> -->
<!-- 				</td> -->
				<td>
					${sumWeekReportBean.debatamountText}
				</td>
				<td>
					${sumWeekReportBean.debatpercentText}
				</td>
				
				
				
				
				<td>
					${sumWeekReportBean.loancostText}
				</td>
				<td> 
					${sumWeekReportBean.repaycostText} 
				</td>
				<td>
					${sumWeekReportBean.weekrepayavgcostText}
				</td>
				
				
				
				
				
				<td> 
					${sumWeekReportBean.couponcostText} 
				</td>
				<td>
					${sumWeekReportBean.mediacostText}
				</td>
				
				
				
<!-- 				<td> -->
<%-- 					${sumWeekReportBean.grossprofileText} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${sumWeekReportBean.grossprofilepercentText} --%>
<!-- 				</td> -->
				<td>
					${sumWeekReportBean.cashcostamountText}
				</td>
			</tr>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>
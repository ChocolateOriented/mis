<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷款周报管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/">现金贷款周报列表</a></li>
		<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><li><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form">现金贷款周报添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="SCashLoanWeeklyReport" action="${ctx}/weeklyreport/sCashLoanWeeklyReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>时间区间 例如2016 5/27 ~ 6/2：</label>
				<form:input path="intervaldatetime" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>累积放款订单数, 不包括延期：</label>
				<form:input path="ordernum" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>累积放款订单数, 包括延期：</label>
				<form:input path="ordernumincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本周期内, 累积放 款订单笔数中, 新用户订单笔数：</label>
				<form:input path="ordernumnewuser" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本周期内, 累积放款订单笔数中, 老用户订单笔数(含延期)：</label>
				<form:input path="ordernumolduserincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本周期内, 累积放款订单笔数中, 老用户订单笔数(不含延期)：</label>
				<form:input path="ordernumolduser" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>自上线之日起, 累积还清订单笔数(含延期)：</label>
				<form:input path="ordernumpayoffincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>自上线之日起, 累积还清订单笔数(不含延期)：</label>
				<form:input path="ordernumpayoff" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本周期内, 累积已还清订单笔数(含延期)：</label>
				<form:input path="ordernumpayoffperiodincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本周期内, 累积已还清订单笔数(不含延期)：</label>
				<form:input path="ordernumpayoffperiod" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>当前处于逾期状态的订单笔数：</label>
				<form:input path="ordernumoverdue" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>当前处于委外状态的订单笔数：</label>
				<form:input path="ordernumoutsource" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增申请订单笔数：</label>
				<form:input path="ordernumneworderperiod" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增申请订单笔数中,订单来源为APP的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodappincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增申请订单笔数中,订单来源为APP的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodapppercentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodapp" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodapppercent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodwechatincludedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodwechatpercentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodwechat" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodwechatpercent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodamount500includedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodamount500percentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodamount500" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodamount500percent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodamount1000includedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodamount1000percentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodamount1000" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodamount1000percent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodamount1500includedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodamount1500percentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodamount1500" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodamount1500percent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodinterval7includedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodinterval7percentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodinterval7" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodinterval7percent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(含延期)：</label>
				<form:input path="ordernumneworderperiodinterval14includedelay" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(含延期)：</label>
				<form:input path="ordernumneworderperiodinterval14percentincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(不含延期)：</label>
				<form:input path="ordernumneworderperiodinterval14" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(不含延期)：</label>
				<form:input path="ordernumneworderperiodinterval14percent" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>自产品上线之日起，累计放款独立用户数：</label>
				<form:input path="singleusernum" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>到本次统计周期截止日次日0点之前，未收回的全部贷款金额：</label>
				<form:input path="amountnotrecovered" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>到本次统计周期截止日次日0点之前，委派第三方进行催收，并且未在本方系统中结算的全部金额：</label>
				<form:input path="amountnotrecoveredoutsource" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增贷款余额(含延期)：</label>
				<form:input path="amountperiodincreasedincludedelay" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>本次统计周期内，新增贷款余额(不含延期)：</label>
				<form:input path="amountperiodincreased" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>总收益：</label>
				<form:input path="amountallincome" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>信审收益：</label>
				<form:input path="amountcreditincome" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>延期收益：</label>
				<form:input path="amountdelayincome" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>逾期收益：</label>
				<form:input path="amountoverdueincome" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>委外收益：</label>
				<form:input path="amountoutsourceincome" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>统计周期内App订单取消率：</label>
				<form:input path="ordercancelperiodpercentapp" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>统计周期内微信订单取消率：</label>
				<form:input path="ordercancelperioadpercentwechat" htmlEscape="false" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>时间区间 例如2016 5/27 ~ 6/2</th>
				<th>累积放款订单数, 不包括延期</th>
				<th>累积放款订单数, 包括延期</th>
				<th>本周期内, 累积放 款订单笔数中, 新用户订单笔数</th>
				<th>本周期内, 累积放款订单笔数中, 老用户订单笔数(含延期)</th>
				<th>本周期内, 累积放款订单笔数中, 老用户订单笔数(不含延期)</th>
				<th>自上线之日起, 累积还清订单笔数(含延期)</th>
				<th>自上线之日起, 累积还清订单笔数(不含延期)</th>
				<th>本周期内, 累积已还清订单笔数(含延期)</th>
				<th>本周期内, 累积已还清订单笔数(不含延期)</th>
				<th>当前处于逾期状态的订单笔数</th>
				<th>当前处于委外状态的订单笔数</th>
				<th>本次统计周期内, 新增申请订单笔数</th>
				<th>本次统计周期内, 新增申请订单笔数中,订单来源为APP的笔数(含延期)</th>
				<th>本次统计周期内, 新增申请订单笔数中,订单来源为APP的占比(含延期)</th>
				<th>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的笔数(不含延期)</th>
				<th>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的占比(不含延期)</th>
				<th>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(含延期)</th>
				<th>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(含延期)</th>
				<th>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(不含延期)</th>
				<th>本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(不含延期)</th>
				<th>本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(不含延期)</th>
				<th>自产品上线之日起，累计放款独立用户数</th>
				<th>到本次统计周期截止日次日0点之前，未收回的全部贷款金额</th>
				<th>到本次统计周期截止日次日0点之前，委派第三方进行催收，并且未在本方系统中结算的全部金额</th>
				<th>本次统计周期内，新增贷款余额(含延期)</th>
				<th>本次统计周期内，新增贷款余额(不含延期)</th>
				<th>总收益</th>
				<th>信审收益</th>
				<th>延期收益</th>
				<th>逾期收益</th>
				<th>委外收益</th>
				<th>统计周期内App订单取消率</th>
				<th>统计周期内微信订单取消率</th>
				<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sCashLoanWeeklyReport">
			<tr>
				<td><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form?id=${sCashLoanWeeklyReport.id}">
					${sCashLoanWeeklyReport.intervaldatetime}
				</a></td>
				<td>
					${sCashLoanWeeklyReport.ordernum}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumnewuser}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumolduserincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumolduser}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumpayoffincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumpayoff}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumpayoffperiodincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumpayoffperiod}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumoverdue}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumoutsource}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiod}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodappincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodapppercentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodapp}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodapppercent}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodwechatincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodwechatpercentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodwechat}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodwechatpercent}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount500includedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount500percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount500}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount500percent}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1000includedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1000percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1000}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1000percent}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1500includedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1500percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1500}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodamount1500percent}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval7includedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval7percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval7}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval7percent}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval14includedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval14percentincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval14}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordernumneworderperiodinterval14percent}
				</td>
				<td>
					${sCashLoanWeeklyReport.singleusernum}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountnotrecovered}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountnotrecoveredoutsource}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountperiodincreasedincludedelay}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountperiodincreased}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountallincome}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountcreditincome}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountdelayincome}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountoverdueincome}
				</td>
				<td>
					${sCashLoanWeeklyReport.amountoutsourceincome}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordercancelperiodpercentapp}
				</td>
				<td>
					${sCashLoanWeeklyReport.ordercancelperioadpercentwechat}
				</td>
				<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><td>
    				<a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form?id=${sCashLoanWeeklyReport.id}">修改</a>
					<a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/delete?id=${sCashLoanWeeklyReport.id}" onclick="return confirmx('确认要删除该现金贷款周报吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
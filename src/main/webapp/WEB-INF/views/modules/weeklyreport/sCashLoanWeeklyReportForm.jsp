<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>现金贷款周报管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">

		<li class="active"><a href="${ctx}/weeklyreport/sCashLoanWeeklyReport/form?id=${sCashLoanWeeklyReport.id}">现金贷款周报<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit">${not empty sCashLoanWeeklyReport.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="weeklyreport:sCashLoanWeeklyReport:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="SCashLoanWeeklyReport" action="${ctx}/weeklyreport/sCashLoanWeeklyReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">时间区间 例如2016 5/27 ~ 6/2：</label>
			<div class="controls">
				<form:input path="intervaldatetime" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">累积放款订单数, 不包括延期：</label>
			<div class="controls">
				<form:input path="ordernum" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">累积放款订单数, 包括延期：</label>
			<div class="controls">
				<form:input path="ordernumincludedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本周期内, 累积放 款订单笔数中, 新用户订单笔数：</label>
			<div class="controls">
				<form:input path="ordernumnewuser" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本周期内, 累积放款订单笔数中, 老用户订单笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumolduserincludedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本周期内, 累积放款订单笔数中, 老用户订单笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumolduser" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自上线之日起, 累积还清订单笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumpayoffincludedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自上线之日起, 累积还清订单笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumpayoff" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本周期内, 累积已还清订单笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumpayoffperiodincludedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本周期内, 累积已还清订单笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumpayoffperiod" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前处于逾期状态的订单笔数：</label>
			<div class="controls">
				<form:input path="ordernumoverdue" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前处于委外状态的订单笔数：</label>
			<div class="controls">
				<form:input path="ordernumoutsource" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增申请订单笔数：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiod" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增申请订单笔数中,订单来源为APP的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodappincludedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增申请订单笔数中,订单来源为APP的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodapppercentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodapp" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodapppercent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodwechatincludedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodwechatpercentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodwechat" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodwechatpercent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount500includedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount500percentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount500" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount500percent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1000includedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1000percentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1000" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1000percent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1500includedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1500percentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1500" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodamount1500percent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval7includedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval7percentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval7" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval7percent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval14includedelay" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval14percentincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval14" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(不含延期)：</label>
			<div class="controls">
				<form:input path="ordernumneworderperiodinterval14percent" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自产品上线之日起，累计放款独立用户数：</label>
			<div class="controls">
				<form:input path="singleusernum" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">到本次统计周期截止日次日0点之前，未收回的全部贷款金额：</label>
			<div class="controls">
				<form:input path="amountnotrecovered" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">到本次统计周期截止日次日0点之前，委派第三方进行催收，并且未在本方系统中结算的全部金额：</label>
			<div class="controls">
				<form:input path="amountnotrecoveredoutsource" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增贷款余额(含延期)：</label>
			<div class="controls">
				<form:input path="amountperiodincreasedincludedelay" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">本次统计周期内，新增贷款余额(不含延期)：</label>
			<div class="controls">
				<form:input path="amountperiodincreased" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总收益：</label>
			<div class="controls">
				<form:input path="amountallincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">信审收益：</label>
			<div class="controls">
				<form:input path="amountcreditincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">延期收益：</label>
			<div class="controls">
				<form:input path="amountdelayincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">逾期收益：</label>
			<div class="controls">
				<form:input path="amountoverdueincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">委外收益：</label>
			<div class="controls">
				<form:input path="amountoutsourceincome" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">统计周期内App订单取消率：</label>
			<div class="controls">
				<form:input path="ordercancelperiodpercentapp" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">统计周期内微信订单取消率：</label>
			<div class="controls">
				<form:input path="ordercancelperioadpercentwechat" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">createtime：</label>
			<div class="controls">
				<input name="createtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${sCashLoanWeeklyReport.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="weeklyreport:sCashLoanWeeklyReport:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理父页面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
// 			var s = window.frames["iframe_text"].document.getElementById("aaa").innerHTML;
// 			$("#btnOk",document.frames("ifm").document).click();
		});

		function collectionfunction(obj, width, height){
			var method = $(obj).attr("method");
			var contactMobile = $(obj).attr("contactMobile");
			var contactstype = $(obj).attr("contactstype");
			var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactMobile=" + contactMobile + "&contactstype=" + contactstype;
			$.jBox.open("iframe:" + url, $(obj).attr("value") , width || 600, height || 430, {            
				buttons: {//"确定": "ok", "取消": true
            	},
				submit: function (v, h, f) {
				},
				loaded: function (h) {
				    $(".jbox-content", document).css("overflow-y", "hidden");
				}
			});
		}
	</script>
</head>
<body>
	<h4>&nbsp;&nbsp; </h4>
	<h4>&nbsp;&nbsp;个人信息 </h4>
	<table id="customerTable" class="table table-striped table-bordered table-condensed">
		<input id="mobile" name="mobile" type="hidden" value="${personalInfo.mobile}" />
		<tbody>
		<tr>
			<td>手机号：${personalInfo.mobile}(${personalInfo.mobileCity})&nbsp;&nbsp;
				<input id="btnTelTaskFather" class="btn btn-primary" type="button" value="电话" style="padding:0px 8px 0px 8px;font-size:13px;"
					contactMobile="${personalInfo.mobile}" contactstype="SELF" onclick="collectionfunction(this, 650)" method="Tel"/>
			</td>
			<td>姓名：${personalInfo.realName}(${personalInfo.sex})(${not empty personalInfo.marital ? personalInfo.marital eq 'Y' ? "已婚" : "未婚" : "未获取"})</td>
			<td>身份证：${personalInfo.idcard}</td>
		</tr>
		<tr>
			<td>证件地址：${not empty personalInfo.ocrAddr ? personalInfo.ocrAddr : "未获取"}</td>
			<td>常住地址：${not empty personalInfo.livingAddr ? personalInfo.livingAddr : "未获取"}</td>
			<td></td>
		</tr>
		</tbody>
	</table>
	<h4>&nbsp;&nbsp;借款信息 </h4>
	<table id="customerTable2" class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td>本金：${personalInfo.corpusAmount/100}元</td>
				<td>服务费：${personalInfo.costAmout/100}元&nbsp;(${personalInfo.days}天)</td>
				<td>优惠金额：${personalInfo.discountAmount/100}元</td>
				<td>订单金额：${personalInfo.amount/100}元</td>
				<td>到期还款日：<fmt:formatDate value="${personalInfo.repaymentTime}" pattern="yyyy-MM-dd"/></td>
			</tr>
			
			<tr>
				<td>续期次数：${personalInfo.delayCount}次</td>
				<td>逾期费：${personalInfo.overdueAmount/100}元&nbsp;(${personalInfo.overdueDays}天)</td>
				<td>减免金额：${personalInfo.modifyAmount/100}元</td>
				<td style="color:red;">当前应催金额：${personalInfo.creditAmount/100}元</td>
			</tr>
			<tr>
				<td>还款总额：${personalInfo.repaymentAmount/100  - personalInfo.creditAmount/100}元</td>
				<td colspan="4">还清日期：<fmt:formatDate value="${personalInfo.payOffTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		</tbody>
	</table>
	
	<h4>&nbsp;&nbsp;收款信息 </h4>
	<table id="customerTable3" class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td>收款时间：<fmt:formatDate value="${personalInfo.remitTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>收款银行：${not empty personalInfo.remitBankName ? personalInfo.remitBankName : "未知"}</td>
				<td>收款卡号：
					<c:choose>  
						<c:when test="${not empty personalInfo.remitBankNo}">  
							<c:out value="${fn:substring(personalInfo.remitBankNo, 0, 4)} **** **** ${fn:substring(personalInfo.remitBankNo, 12, -1)}" />
						</c:when>
						<c:otherwise>
							<c:out value="未知" />
						</c:otherwise>  
					</c:choose>
				</td>
			</tr>
		</tbody>
	</table>
<br/>
<iframe id="ifm" src="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&hasContact=${hasContact}" frameborder="0"  style="width:100%;height:600px;">
</iframe> 
</body>
</html>
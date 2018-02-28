<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代扣</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		jQuery.validator.addMethod("numberFix2", function(value, element) {
		    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
		}, "请输入小数2位以内的数字");
		
		/* if(parseInt("${personalInfo.overdueDays}") > parseInt("${fns:getDictValue('overdueday', 'overdueday', 14)}") || parseInt("${result}") > 0 || "${isDelayable}" == "false"){
			$("#delay").remove();
		} */
		
		$('#save').click(function() {
			if($("#inputForm").valid()){
				if(parseFloat($("#payamount").val())<100){
					$.jBox.tip("扣款金额必须大于100元!", "warning");
					return;
				}
				if ($("input[name='paytype']:checked").val() == "partial") {
					var payamount = $("#payamount").val();
					if (parseFloat(payamount) <= parseFloat("${personalInfo.corpusAmount}" || "0") / 100 * 0.5) {
						$.jBox.tip("扣款金额需大于借款本金的50%", "warning");
						return;
					}
					
					if (parseFloat(payamount) >= parseFloat("${personalInfo.creditAmount}" || "0") / 100) {
						$.jBox.tip("扣款金额应小于应催金额", "warning");
						return;
					}
					if (parseFloat(payamount) >= parseFloat("${personalInfo.creditAmount}" || "0") / 100) {
						$.jBox.tip("扣款金额应小于应催金额", "warning");
						return;
					}
				}
				
				$("#save").attr('disabled', "true");
				submitting();
				
				$.ajax({
					type: 'POST',
					url : "${ctx}/dunning/tMisDunningDeduct/saveRecord",
					data: $('#inputForm').serialize(),             //获取表单数据
					success : function(data) {
						if (!data) {
							alert("扣款失败");
							closeSubmitting();
							window.parent.window.jBox.close();
							return;
						}
						
						if (data.result == "OK") {
							intervalId = setInterval(queryDeductStatus.bind(null, data.deductcode), 1000);
						} else if (data.result == "WARN") {
							alert(data.msg);
							closeSubmitting();
							window.parent.window.jBox.close();
						} else {
							alert("扣款失败:" + data.msg);
							closeSubmitting();
							window.parent.window.jBox.close();
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
						//通常情况下textStatus和errorThrown只有其中一个包含信息
						alert("扣款失败:" + textStatus);
						closeSubmitting();
                    }
				});
				
			}
		});
		
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		});
		
		//扣款渠道选择事件
		$("input[name='paychannel']").change(function() {
			$("#paychannelValue").val($(this).val());
		});
		
		//扣款类型选择事件
		$("input[name='paytype']").change(function() {
			$("#payamount").val("");
			var paytype = $(this).val();
			if (paytype == "loan") {
				//$("#delayDayGroup").css("display", "none");
				//$("input[name='delaydays']:checked").prop("checked", false);
				$("#payamount").prop("readonly", true);
				$("#payamount").val(parseFloat("${personalInfo.creditAmount}" || "0") / 100);
			} else if (paytype == "partial"){
				//$("#delayDayGroup").css("display", "none");
				//$("input[name='delaydays']:checked").prop("checked", false);
				$("#payamount").prop("readonly", false);
			}/*  else {
				$("input[name='delaydays'][value='7']").prop("checked", true);
				$("#delayDayGroup").css("display", "block");
				$("#payamount").prop("readonly", true);
				$("#payamount").val("${delayAmount}");
			} */
		});
		
		$("input[name='paytype']:checked").change();

	});
	
	var intervalId = null;
	var intervalCnt = 0;

	function submitting() {
		$.jBox.tip("提交中...", "loading", {opacity: 0.2, persistent: true});
	}
	
	function closeSubmitting() {
		$.jBox.tip.mess = null;
		$.jBox.closeTip();
	}
	
	//轮询代扣记录状态
	function queryDeductStatus(deductcode) {
		intervalCnt++;
		$.get("${ctx}/dunning/tMisDunningDeduct/get", {deductcode: deductcode}, function(data) {
			if (data.status == "succeeded") {
				alert("扣款成功");
			} else if (data.status == "failed") {
				var msg = data.statusdetail || "";
				if (data.reason == "NO_BALANCE") {
					msg = "余额不足，本日请勿重复发起扣款";
				}
				alert("扣款失败:" + msg);
			} else {
				if (intervalCnt >= 60) {
					alert("系统繁忙，请稍后查询");
					clearInterval(intervalId);
					closeSubmitting();
					window.parent.window.location.reload();
					window.parent.window.jBox.close();
				}
				return;
			}
			
			clearInterval(intervalId);
			closeSubmitting();
			window.parent.window.location.reload();
			window.parent.window.jBox.close();
		});
	}

	</script>
</head>
<body>
<ul class="nav nav-tabs">

	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisDunningDeduct" class="form-horizontal">
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<div class="control-group">
			<label class="control-label">扣款姓名：</label>
			<div class="controls" style="padding-top:3px;">
				<span>${personalInfo.realName}</span>
				<input type="hidden" style="display:none;" id="buyername" name="buyername" value="${personalInfo.realName}" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">扣款银行：</label>
			<div class="controls" style="padding-top:3px;">
				<span>${not empty personalInfo.remitBankName ? personalInfo.remitBankName : "未知"}</span>
				<input type="hidden" style="display:none;" id="bankname" name="bankname" value="${personalInfo.remitBankName}" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">银行卡号：</label>
			<div class="controls" style="padding-top:3px;">
				<span>${fn:substring(personalInfo.remitBankNo, 0, 4)} **** **** ${fn:substring(personalInfo.remitBankNo, 12, 16)} ${fn:substring(personalInfo.remitBankNo, 16, -1)}</span>
				<input type="hidden" style="display:none;" id="bankcard" name="bankcard" value="${personalInfo.remitBankNo}" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">预留手机号：</label>
			<div class="controls" style="padding-top:3px;">
				<span>${personalInfo.mobile}</span>
				<input type="hidden" style="display:none;" id="mobile" name="mobile" value="${personalInfo.mobile}" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">身份证号：</label>
			<div class="controls" style="padding-top:3px;">
				<span>${personalInfo.idcard}</span>
				<input type="hidden" style="display:none;" id="idcard" name="idcard" value="${personalInfo.idcard}" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">扣款渠道：</label>
			<div class="controls" style="padding-top:3px;">
				<c:forEach items="${payChannelList}" var="payChannel" varStatus="vs">
					<c:if test="${(vs.index % 4) eq 0}">
						<div>
					</c:if>
					<input path="" type="radio" id="${payChannel.channelid}" name="paychannel" htmlEscape="false" value="${payChannel.channelid}"
						${payChannel.isusable ? '' : 'disabled'}/><label for="${payChannel.channelid}">${payChannel.channelname}</label>
					<c:if test="${vs.last || (vs.index eq 3)}">
						<input id="paychannelValue" class="required" value="" style="visibility:hidden;width:0px;"/>
					</c:if>
					<c:if test="${vs.last || (vs.index % 4) eq 3}">
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">扣款类型：</label>
			<div class="controls" style="padding-top:3px;">
				<input id="loan" type="radio" name="paytype" value="loan" checked/><label for="loan">全款扣款</label>
				<input id="partial" type="radio" name="paytype" value="partial" ${personalInfo.corpusAmount * 0.5 >= personalInfo.creditAmount ? 'disabled' : ''}/><label for="partial">部分扣款</label>
				<!-- <span id="delay">
					<input type="radio" name="paytype" value="delay"/>续期扣款
				</span> -->
			</div>
		</div>
		
		<!-- <div id="delayDayGroup" class="control-group" style="display:none;">
			<label class="control-label">续期天数：</label>
			<div class="controls" style="padding-top:3px;">
				<input type="radio" name="delaydays" value="7"/>7天
				<input type="radio" name="delaydays" value="14"/>14天
			</div>
		</div> -->
		
		<div class="control-group">
			<label class="control-label">扣款金额：</label>
			<div class="controls">
				<input path="" id="payamount" name="payamount" type="text" htmlEscape="false" class="required numberFix2"/>
			</div>
		</div>
		
		<input style="display:none;" id="buyerId" name="buyerId" value="${buyerId}" />
		<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		<input style="display:none;" id="dunningtaskdbid" name="dunningtaskdbid" value="${dunningtaskdbid}" />
		<div style= "padding:19px 180px 20px;" >
			<input id="save" class="btn btn-primary" type="button" value="确定"/>&nbsp;
			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
</body>
</html>


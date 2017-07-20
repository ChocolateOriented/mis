<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>换卡</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		jQuery.validator.addMethod("mobile", function(value, element) {
		    return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		}, "请输入正确的手机号码");
		
		jQuery.validator.addMethod("bankCard", function(value, element) {
		    return this.optional(element) || /^\d{16,19}$/.test(value);
		}, "请输入16-19位银行卡号");
		
		$('#save').click(function() {
			var bankCard = $("#bankcard").val();
			var bankName = $("#bankname").val();
			
			if($("#inputForm").valid()){
				if ("${changeType}" == "changeBankcard") {
					$.post("${ctx}/dunning/tMisChangeCardRecord/getBankByCard", {bankCard: bankCard}, function(data) {
						if (!data) {
							$.jBox.tip("该卡无法匹配对应发卡行，请返回修改", "warning");
							return;
						}
						if (data.bankname && data.bankname.indexOf(bankName) < 0 ) {
							$.jBox.tip("换卡失败，当前卡号匹配发卡行为：" + data.bankname + "，请返回修改", "warning");
							return;
						}
						if (data.cardtype && data.cardtype != "借记卡") {
							$.jBox.tip("换卡失败，当前卡号匹配为：" + data.bankname + " " + data.cardtype + "，请返回修改", "warning");
							return;
						}
						save();
					});
				} else {
					save();
				}
			}
		});
		
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		});
		
	});
	
	//输入银行卡号放大格式化展示
	function diaplayCardNo() {
		var card = $("#bankcard").val();
		var numReg = /\D/g;
		var separatorReg = /(\d{4})/g;
		//避免无非法字符时光标被定位到末尾
		if (numReg.test(card)) {
			card = card.replace(numReg, '');
			$("#bankcard").val(card);
		}
		if (card) {
			var displayCard = card.replace(separatorReg, '$1 ');
			$('#displaybankcard').css('visibility', 'visible');
			$('#displaybankcard').val(displayCard);
		} else {
			$('#displaybankcard').css('visibility', 'hidden');
			$('#displaybankcard').val("");
		}
	}
	
	function save() {
		$("#save").attr('disabled',"true");
		$.ajax({
			type: 'POST',
			url : "${ctx}/dunning/tMisChangeCardRecord/saveCard",
			data: $('#inputForm').serialize(),             //获取表单数据
			success : function(data) {
				if (data == "OK") {
					alert("保存成功");
					//window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
					window.parent.window.location.reload();
					window.parent.window.jBox.close();            //关闭子窗体
				} else {
					alert("保存失败:"+data.message);
					//window.parent.page();
					window.parent.window.jBox.close();
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
				//通常情况下textStatus和errorThrown只有其中一个包含信息
				alert("保存失败:"+textStatus);
            }
		});
	}

	</script>
</head>
<body>
<ul class="nav nav-tabs">

	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisChangeCardRecord" class="form-horizontal">
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<c:choose>
			<c:when test="${changeType eq 'changeMobile'}">
				<div class="control-group" style="margin-top:32px;">
					<label class="control-label">银行预留手机号：</label>
					<div class="controls">
						<input class="input-large required mobile" path="" id="mobile" name="mobile" htmlEscape="false" maxlength="64"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<input type="hidden" id="bankname" name="bankname" value="${bankname}" />
				<input type="hidden" id="bankcard" name="bankcard" value="${bankcard}" />
				<input type="hidden" id="idcard" name="idcard" value="${idcard}" />
			</c:when>
			<c:when test="${changeType eq 'changeIdcard'}">
				<div class="control-group" style="margin-top:32px;">
					<label class="control-label">身份证号：</label>
					<div class="controls">
						<input class="input-large required card" path="" id="idcard" name="idcard" htmlEscape="false" maxlength="50"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<input type="hidden" id="bankname" name="bankname" value="${bankname}" />
				<input type="hidden" id="bankcard" name="bankcard" value="${bankcard}" />
				<input type="hidden" id="mobile" name="mobile" value="${mobile}" />
			</c:when>
			<c:otherwise>
				<div class="control-group">
					<label class="control-label">银行名称：</label>
					<div class="controls">
						<select id="bankname" name="bankname" path="" class="input-large required">
							<option value=""></option>
							<c:forEach items="${bankList}" var="bankname">
								<option value="${bankname}">${bankname}</option>
							</c:forEach>
						</select>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				
				<div class="control-group">
					<div class="controls">
						<input type="text" id="displaybankcard" style="visibility:hidden;padding:2px;font-size:18px;width:218px;" readonly/>
					</div>
					<label class="control-label">银行卡号：</label>
					<div class="controls">
						<input class="input-large required bankCard" path="" id="bankcard" name="bankcard" htmlEscape="false" maxlength="19" oninput="diaplayCardNo();"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<input type="hidden" id="idcard" name="idcard" value="${idcard}" />
				<input type="hidden" id="mobile" name="mobile" value="${mobile}" />
			</c:otherwise>
		</c:choose>

		<input style="display:none;" id="buyerId" name="buyerId" value="${buyerId}" />
		<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		<input style="display:none;" id="dunningtaskdbid" name="dunningtaskdbid" value="${dunningtaskdbid}" />
		<div style= "padding:19px 180px 20px;" >
			<input id="save" class="btn btn-primary" type="button" value="保存"/>&nbsp;
			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
</body>
</html>


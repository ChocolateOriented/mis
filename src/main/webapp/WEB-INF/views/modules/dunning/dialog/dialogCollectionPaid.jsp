<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代付</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			//如果逾期天数大于14则催收人员代付选项没有续期
			if(parseInt(${personalInfo.overdueDays}) > parseInt(14)){
				var obj=document.getElementById('paidType');
				obj.options.remove(2); 
				$("#delaytr").hide();
			}
			
			$('#paid').click(function() {
	 			 if($("#inputForm").valid()){
	 				$("#paid").attr('disabled',"true");
	 				 $.ajax({
		                    type: 'POST',
		                    url: "${ctx}/dunning/tMisDunningTask/paidDeal",
		                    data: $('#inputForm').serialize(),             //获取表单数据
		                    success : function(data) {
		                    	data = eval(data);
		                        if (data.code == "0000") {
		                        	ow(data.redirectUrl)
		                        } else {
		                            alert(data.msg);
		                        }
		                        window.parent.window.jBox.close();            //关闭子窗体
		                    },
		                    error : function(XMLHttpRequest, textStatus, errorThrown){
		                       //通常情况下textStatus和errorThrown只有其中一个包含信息
		                       alert("保存失败:"+textStatus);
		                    }
		                });
	 			 }
			}); 
			
			$('#paidType').change(function(){
				$('#paidAmount').val("");
				if($("#paidType").val() == "loan"){
					$('#paidAmount').val(${personalInfo.creditAmount/100});
					$("#delaytr").hide();
					$("#paidAmount").attr("readonly",true);
				}else if($("#paidType").val() == "delay"){
					$("#paidAmount").val(${delayAmount});
					$("#delaytr").show();
					$("#paidAmount").attr("readonly",true);
				}else{
					$("#delaytr").hide();
					$("#paidAmount").attr("readonly",false);
				}
			});
		});
		
		function ow(owurl){
				var tmp=window.open("about:blank","","fullscreen=1")
				tmp.moveTo(0,0);
				tmp.resizeTo(screen.width+20,screen.height);
				tmp.focus();
				tmp.location=owurl;
		}
		
	</script>
</head>
<body>
<ul class="nav nav-tabs">

</ul>
		
		<table id="customerTable" class="table table-striped table-bordered table-condensed" style="padding-left : 5px;padding-right:5px;">
				<tbody>
					<tr>
						<td>订单号</td>
						<td>${dealcode}</td>
						<td>当前应催金额</td>
						<td style="color:red;">${personalInfo.creditAmount/100}元</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td>${personalInfo.realName}</td>
						<td>手机号码</td>
						<td>${personalInfo.mobile}</td>
					</tr>
					<tr>
						<td>本金</td>
						<td>${personalInfo.corpusAmount/100}元</td>
						<td>欠款金额</td>
						<td>${personalInfo.amount/100}元</td>
					</tr>
					<tr>
						<td>到期还款日期</td>
						<td><fmt:formatDate value="${personalInfo.repaymentTime}" pattern="yyyy-MM-dd"/></td>
						<td>逾期天数</td>
						<td>
							${personalInfo.overdueDays}天
							<c:if test="${personalInfo.overdueDays gt 14}" >(续期操作逾期时间不能大于14天)</c:if>
						</td>
					</tr>
				</tbody>
		</table>
		<br/>
		<form:form id="inputForm" modelAttribute="TMisPaid"  class="form-horizontal">
			<div class="control-group">
				<label class="control-label">代付类型：</label>
					<div class="controls">
						<select  class="input-medium required" id="paidType" name="paidType">
							<option value=""></option>
							<option value="loan">还款</option>
							<option value="delay">续期</option>
							<option value="partial">部分还款</option>
						</select>
					</div>
			</div>
	
			<div class="control-group">
				<label class="control-label">代付金额：</label>
				<div class="controls">
						<input  style="width:140px;" type="tel" readonly="readonly" id="paidAmount" name="paidAmount"  class="input-xlarge required "/>
						<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			
			<div class="control-group" id="delaytr" name="delaytr" style="display:none;">
				<label class="control-label">续期天数：</label>
				<div class="controls">
						<input type="radio" name="delayDay" value="7" checked="checked" />7天
						<input type="radio" name="delayDay" value="14" />14天
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">代付渠道：</label>
				<div class="controls" style="margin-top:2px;">
						<input type="radio" name="paychannel" value="alipay" checked="checked"/>支付宝
						<input type="radio" name="paychannel" value="chengfapay" />微信
				</div>
			</div>
			<input style="display:none;" id="buyerId" name="buyerId" value="${buyerId}" />
			<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		
			<div style= "padding:19px 180px 20px;" >
				<input id="paid" class="btn btn-primary" type="button" value="确认代付"/>&nbsp;
			</div>
		</form:form>
	
</body>
</html>


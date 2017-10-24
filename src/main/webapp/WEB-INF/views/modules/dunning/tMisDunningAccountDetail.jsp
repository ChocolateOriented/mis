<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>入账对公明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
      function showDetail(obj) {
        $(".suspense").css("display", "none");
        $(obj).children(".suspense").css("display", "block");
      }

      function hideDetail() {
        $(".suspense").css("display", "none");
      }

	  $(document).ready(function() {
			
		var a=/^[0-9]*(\.[0-9]{1,2})?$/;
		$("#searchForm").validate({
				submitHandler: function(form){
                    if (!a.test($("#remittanceAmount").val())){
                    	top.$.jBox.tip('金额格式不正确','warning');
                    }else{
                        form.submit();
                    }
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
			// 清空查询功能
			 $("#empty").click(function(){
       		 window.location.href="${ctx}/dunning/tMisRemittanceMessage/detail";
			 }); 
				
		});

        $(document).ready(function() {
          $("#btnExport").click(function(){
            top.$.jBox.confirm("确认要导出对公明细数据吗？","系统提示",function(v,h,f){
              if(v=="ok"){
                $("#searchForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/detailExport");
                $("#searchForm").submit();
              }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
          });

        });
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/detail");
			$("#searchForm").submit();
        	return false;
        }
	</script>
	<style type="text/css">
		.suspense {
			z-index: 10000;
			position: absolute;
			top: 10px;
			left: 10px;
			width: 250px;
			background-color: white;
			opacity: 0.9;
			border: solid red 1px;
			border-radius: 5px;
			outline: none;
			padding-left: 20px;
			padding-top: 20px;
		}

		.beautif {
			padding-bottom: 10px;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/detail">入账对公明细</a></li>
		
		<span id="successRate" style="float:right;padding:8px;"></span>
		
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm"  modelAttribute="TMisRemittanceMessage" action="${ctx}/dunning/tMisRemittanceMessage/detail" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易流水号</label>
				<form:input path="remittanceSerialNumber"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>交易时间</label>
				<input name="begindealtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisRemittanceMessage.begindealtime }" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="enddealtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisRemittanceMessage.enddealtime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>交易金额</label>
				<form:input path="remittanceAmount"  htmlEscape="false" maxlength="128" class="input-medium "/>
			</li>
			<li><label>汇款人账户</label>
				<form:input path="remittanceAccount"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>汇款人姓名</label>
				<form:input  path="remittanceName"  htmlEscape="false" maxlength="128" class="input-medium" />
			</li>
			
			<li><label>入账状态</label>
			<form:select  id="status" path="accountStatus" class="input-medium">
				<form:option selected="selected" value="" label="全部状态"/>
				 <form:option value="not_audit" label="未查账"/>
				 <form:option value="complete_audit" label="已查账"/>
				 <form:option value="finish" label="已完成"/>
			</form:select>
			</li>
			<li><label>退款状态</label>
				<form:select  id="refundStatus" path="refundStatus" class="input-medium">
					<form:option selected="selected" value="" label="全部"/>
					<form:options items="${RefundStatusList}" itemLabel="desc" />
				</form:select>
			</li>
			</br>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
		</ul>
	</form:form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				
				<th>编号</th>
				<th>交易流水号</th>
				<th>交易时间</th>
				<th>交易渠道</th>
				<th>交易金额（元）</th>
				<th>汇款人名称</th>
				<th>汇款人账户</th>
				<th>备注</th>
				<th>入账状态</th>
				<th>退款状态</th>
				<th>上传时间</th>
				<th>上传人</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tmessage" varStatus="status">
			<tr>
				<td>
				 	${status.count}
				</td>
				<td>
					 ${tmessage.remittanceSerialNumber } 
				</td>
				<td>
					 <fmt:formatDate value="${tmessage.remittanceTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tmessage.remittanceChannelText }
				</td>
				<td>
					 ${tmessage.remittanceAmount }
				</td>
				<td>
					  ${tmessage.remittanceName }
				</td>
				<td>
					 ${tmessage.remittanceAccount}
				</td>
				<td>
					<c:if test="${tmessage.remark eq '转账' }">
					  
					 </c:if>    
					<c:if test="${tmessage.remark ne '转账' }">
					  ${tmessage.remark } 
					 </c:if>    
				</td>
				<td>
						${tmessage.accountStatusText }
				</td>
				<td>
					<c:choose>
						<c:when test="${fn:length(tmessage.refunds) > 0}">
							<div name="detail" onmouseover="showDetail(this);" onmouseout="hideDetail();" style="position:relative;">
								<font color="red">${tmessage.refunds[0].refundStatusText}</font>
								<div class="suspense" style="display:none; " tabindex="0">
									<c:forEach items="${tmessage.refunds}" var="refund">
										<div class="beautif">退款审核时间:<fmt:formatDate value="${refund.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
										<div class="beautif">退款完成时间:<fmt:formatDate value="${refund.refundTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
										<div class="beautif">审核人:${refund.auditor }</div>
										<div class="beautif">退款金额:${refund.amount }</div>
										<div class="beautif">退款状态:${refund.refundStatusText}</div>
										<br/>
									</c:forEach>
								</div>
							</div>
						</c:when>
						<c:otherwise>
                            正常
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatDate value="${tmessage.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/> 
				</td>
				<td>
					  ${tmessage.financialUser }
				</td>
				
				</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
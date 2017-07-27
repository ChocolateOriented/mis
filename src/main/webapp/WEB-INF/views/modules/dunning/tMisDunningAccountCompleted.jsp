<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title id="title">已完成</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		});
		function showDetail(obj) {
			$(".suspense").css("display", "none");
			$(obj).children(".suspense").css("display", "block");
		}

		function hideDetail() {
			$(".suspense").css("display", "none");
		}

        function page(n, s) {
          if (n) window.parent.$("#pageNo").val(n);
          if (s) window.parent.$("#pageSize").val(s);
          window.location = "${ctx}/dunning/tMisRemittanceMessage/completed?" + window.parent.$("#searchForm").serialize();
          return false;
        }
	</script>
	<style type="text/css">
				.suspense {
				z-index:10000;
				position:absolute;
				top:10px;
				left:10px;
				height:200px;
				width:250px;
				background-color:white;
				opacity:0.9;
				border:solid red 1px;
				border-radius:5px;
				outline:none;
				padding-left:20px;
				padding-top:20px;
				}
				.beautif{
				padding-bottom:10px;
				}
	</style>
</head>
<body>
	<sys:message content="${message}"/>
	<table id="accountTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>借款人姓名</th>
				<th>手机号</th>
				<th>订单编号</th>
				<th>催收人</th>
				<th>欠款金额</th>
				<th>减免金额</th>
				<th>应还金额</th>
				<th>交易金额</th>
				<th>交易流水号</th>
				<th>查账人</th>
				<th>订单状态</th>
				<th>入账标签</th>
				<th>入账类型</th>
				<th>更新时间</th>
				<th>入账人</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pagecompleted.list}" var="tmessage" varStatus="status">
			<tr>

				<td>
					${status.count}
				</td>
				<td>
					${tmessage.realName }
				</td>
				<td>
					${tmessage.mobile }
				</td>
				<td>
					${tmessage.dealcode }
				</td>
				<td>
					${tmessage.nickName }
				</td>
				<td>
					${tmessage.amount }
				</td>
				<td>
					${tmessage.modifyamount/100}
				</td>
				<td>
					${tmessage.remainAmmount }
				</td>
				<td>
					${tmessage.remittanceamount }
				</td>
				<td>
					<div name="detail" onmouseover="showDetail(this);" onmouseout="hideDetail();" style="position:relative;">
						 <font  color="red">${ fn:substring(tmessage.remittanceSerialNumber,0,3)}******${ fn:substring(tmessage.remittanceSerialNumber,tmessage.remittanceSerialNumber.length()-3,-1)}</font>
					    <div class="suspense" style="display:none; " tabindex="0">
							   <div class="beautif">交易时间:<fmt:formatDate value="${tmessage.remittancetime }" pattern="yyyy-MM-dd HH:mm:ss"/></div>
							   <div class="beautif">交易金额:${tmessage.remittanceamount }</div>
							   <div class="beautif">对方名称:${tmessage.remittanceName }</div>
							   <div class="beautif">对方账户:${tmessage.remittanceaccount }</div>
							   <div class="beautif">备注:${tmessage.remark }</div>
							   <div class="beautif">上传人:${tmessage.financialUser }</div>
					    </div>
					</div>
				</td>
				<td>
				 	${tmessage.checkedPeople}
					    
				</td>
				<td>
					  <c:if test="${tmessage.orderStatus eq 'payment'}">
					  	 未还清
					  </c:if>  
					  <c:if test="${tmessage.orderStatus eq 'payoff'}">
					  	 已还清
					  </c:if>  
				</td>
				<td>
					  <c:if test="${tmessage.remittanceTag eq 'REPAYMENT_THIRD'}">
					  	第三方还款
					  </c:if>  
					  <c:if test="${tmessage.remittanceTag eq 'REPAYMENT_SELF'}">
					  	本人还款
					  </c:if>  
				</td>
				<td>
					  <c:if test="${tmessage.payType eq 'loan'}">
					  	还清
					  </c:if>  
					  <c:if test="${tmessage.payType eq 'partial'}">
					  	部分还款
					  </c:if>  
				</td>
				
				<td>
					 <fmt:formatDate value="${tmessage.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>   
				</td>
				<td>
					  ${tmessage.completePeople }
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${pagecompleted}</div>
	
</body>
</html>
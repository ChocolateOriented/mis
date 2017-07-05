<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title id="title">已查账</title>
	<meta name="decorator" content="default"/>
 
	<script type="text/javascript">
		$(document).ready(function() {
		
			
		
		});
 			
			function ss(d){
			 $(d).next().show();
			
			}
			function s(){
				var ss="nishi";
				return ss;
			}
			function dd(d){
			}
		 function page(n, s) {
		        if (n) window.parent.$("#pageNo").val(n);
		        if (s) window.parent.$("#pageSize").val(s);
		       window.parent. $("#searchForm").attr("action", "${ctx}/dunning/tMisRemittanceMessage/confirmList?childPage=checked");
		       window.parent.$("#searchForm").submit();
		        return false;
		      }
	
	</script>
<style type="text/css">
</style>
</head>
<body>


  
 
                
                
                
                
                
	<input type="hidden" id="childIfam" value="checked">
	<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/checked?child=true">已查账</a></li>
	<li ><a href="${ctx}/dunning/tMisRemittanceMessage/completed?child=true">已完成</a></li>
	</ul> 
	<sys:message content="${message}"/>
	<table id="accountTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>订单编号</th>
				<th>崔收入</th>
				<th>欠款金额</th>
				<th>减免金额</th>
				<th>应还金额</th>
				<th>交易流水号</th>
				<th>查账人</th>
				<th>订单状态</th>
				<th>入账标签</th>
				<th>入账类型</th>
				<th>更新时间</th>
				<th>入账人</th>
				<th>操作</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pagechecked.list}" var="tmessage" varStatus="status">
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
					 ${tmessage.modifyamount} 
				</td>
				<td>
					  ${tmessage.creditamount }  
				</td>
				<td >
					 <div id="serNumber"  onmouseover="ss(this)" onmouseout="dd(this)">
						 <font  color="red">${ fn:substring(tmessage.remittanceSerialNumber,0,3)}****** 
						 ${ fn:substring(tmessage.remittanceSerialNumber,tmessage.remittanceSerialNumber.length()-3,-1)}
						 </font>
					</div>
				    <div style="display:none" class="us">
						   <div><font  color="red"> ${tmessage.remittanceSerialNumber }</font></div>
						   <div>------------------------------------- </div>
						   <div>交易时间:${tmessage.remittancetime }</div>
						   <div>交易金额:${tmessage.remittanceamount }</div>
						   <div>对方名称:${tmessage.remittanceaccount }</div>
						   <div>对方账户:${tmessage.remark }</div>
						   <div>备注:${tmessage.creditamount }</div>
						   <div>上传人:${tmessage.financialUser }</div>
				    </div>
				</td>
				<td>
					  ${tmessage.checkedPeople }  
				</td>
				<td>
					  ${tmessage.orderStatus }  
				</td>
				<td>
					  ${tmessage.remittanceTag }  
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
				<input id="changeSms" onclick="recorded('${tmessage.remittanceConfirmId}')"  class="btn btn-primary" type="button" value="入账"/>
				<input id="delete" class="btn btn-primary" onclick=""  type="button" value="减免" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${pagechecked}</div>
	
</body>
</html>
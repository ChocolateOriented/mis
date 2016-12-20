<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>汇款确认信息管理</title>
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
		
		function ckFunction(ty,obj){
			var id = $(obj).attr("remittanceconfirmid");
        	if(ty=='ch'){
        		window.location.href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmForm?id="+id;
        	}else if(ty=='cw'){
        		window.location.href="${ctx}/dunning/tMisRemittanceConfirm/remittanceFinancialConfirmForm?id="+id;
        	}
        }
		
		function collectionfunction(obj){
			var method = $(obj).attr("method");
			var buyerId = $(obj).attr("buyerId");
			var dealcode = $(obj).attr("dealcode");
			var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=" + buyerId +"&dealcode=" + dealcode ;
			$.jBox.open("iframe:" + url, $(obj).attr("value") , 600, 500, {            
               buttons: {},
               submit: function (v, h, f) {},
               loaded: function (h) {
                   $(".jbox-content", document).css("overflow-y", "hidden");
               }
        	 });
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmList">汇款确认列表</a></li>
<%-- 		<shiro:hasPermission name="dunning:tMisRemittanceConfirm:edit"><li><a href="${ctx}/dunning/tMisRemittanceConfirm/form">汇款确认信息添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisRemittanceConfirm" action="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		
			<li><label>订单号：</label>
				<form:input path="dealcode" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>汇款人：</label>
				<form:input path="remittancename" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>汇款渠道：</label>
				<form:select path="remittancechannel" class="input-medium">
					<form:option selected="selected" value="" label="全部渠道"/>
					<form:options items="${fns:getDictList('remittancechannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			
			<li><label>状态：</label>
				<form:select path="confirmstatus" class="input-medium">
					<form:option selected="selected"  value="ch_submit" label="催收已提交"/>
					<form:option  value="cw_submit" label="财务已提交"/>
					<form:option  value="ch_confirm" label="催收已确认"/>
				</form:select>
			</li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>[催]汇款人</th>
				<th>[财]汇款人</th>
				<th>[催]汇款时间</th>
				<th>[财]到账时间</th>
				<th>[催]汇款金额</th>
				<th>[财]到账金额</th>
				<th>[催]汇款渠道</th>
				<th>[财]汇款渠道</th>
				<th>审核状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisRemittanceConfirm">
			<tr>
				<td><a href="${ctx}/dunning/tMisRemittanceConfirm/remittanceConfirmDetail?id=${tMisRemittanceConfirm.id}">
					${tMisRemittanceConfirm.dealcode}</a>
				</td>
				
				<td>
					${tMisRemittanceConfirm.remittancename}
				</td>
				
				<td>
					${tMisRemittanceConfirm.financialremittancename}
				</td>
				
				<td>
					<fmt:formatDate value="${tMisRemittanceConfirm.remittancetime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				
				<td>
					<fmt:formatDate value="${tMisRemittanceConfirm.accounttime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				
				<td>
					${tMisRemittanceConfirm.remittanceamount}
				</td>
				
				<td>
					${tMisRemittanceConfirm.accountamount}
				</td>
				
				<td>
					${tMisRemittanceConfirm.remittancechannel}
				</td>
				
				<td>
					${tMisRemittanceConfirm.financialremittancechannel}
				</td>
				
				<td>
					${tMisRemittanceConfirm.confirmstatus}
				</td>
				
				<td>
				
				<shiro:hasPermission name="dunning:tMisRemittanceConfirm:financialEdit">
					<input  id="btncw"  name="btnCollection" onclick="ckFunction('cw',this)" class="btn btn-primary" remittanceconfirmid="${tMisRemittanceConfirm.id}"  type="button" value="财务确认" />
   				</shiro:hasPermission>
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
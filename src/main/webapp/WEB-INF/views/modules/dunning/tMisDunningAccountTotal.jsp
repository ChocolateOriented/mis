<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>查账入账父页面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			var sss=window.frames["ifm"].document.getElementById("childIfam").value; 
			alert(sss);
			if( "checked"==sss){
				$("#ifm").attr("src","${ctx}/dunning/tMisRemittanceMessage/checked");
			$("#searchForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/confirmList");
			}
			if( "completed"==sss){
				$("#ifm").attr("src","${ctx}/dunning/tMisRemittanceMessage/completed");
			$("#searchForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/confirmList");
			}
			$("#searchForm").submit();
        	return false;
        }
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/detail">查账入账</a></li>
		
		<span id="successRate" style="float:right;padding:8px;"></span>
		
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm"  modelAttribute="TMisRemittanceMessage"  method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>手机号</label>
				<form:input path="dealcode"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>对方姓名</label>
				<form:input  path="dealcode"  htmlEscape="false" maxlength="128" class="input-medium" />
			</li>
			<li><label>订单编号</label>
				<form:input  path="dealcode"  htmlEscape="false" maxlength="128" class="input-medium" />
			</li>
			<li><label>交易流水号</label>
				<form:input path="remittanceSerialNumber"  htmlEscape="false"  class="input-medium"/>
			</li>
			
			<li><label>订单状态</label>
			<form:select  id="status" path="dealcode" class="input-medium">
				<form:option selected="selected" value="" label="全部状态"/>
<%-- 				<c:forEach items="" var=""> --%>
<%-- 				 	<form:option value="" label=""/> --%>
<%-- 				</c:forEach> --%>
			</form:select>
			</li>
			<li><label>入账标签</label>
			<form:select  id="status" path="dealcode" class="input-medium">
				<form:option selected="selected" value="" label="全部状态"/>
<%-- 				<c:forEach items="" var=""> --%>
<%-- 				 	<form:option value="" label=""/> --%>
<%-- 				</c:forEach> --%>
			</form:select>
			</li>
			</br>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
		</ul>
	</form:form>
	
	
	<br/>
<iframe id="ifm" name="ifm"  frameborder="0" src="${childPage}" style="width:100%;height:600px;">
</iframe>
</body>
</html>
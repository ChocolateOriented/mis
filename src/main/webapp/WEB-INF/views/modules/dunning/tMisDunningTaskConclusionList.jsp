<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$(".pageNo").val(n);
			$(".pageSize").val(s); 
			$(".submitForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	
	<form:form class="submitForm"  action="${ctx}/dunning/tMisDunnedConclusion/list" method="get" style="display: none;">
		<input class="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input class="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/>
		<input id ="dealcode" name="dealcode" type="hidden" value="${dealcode}"/>
		<input id = "dunningtaskdbid" name="dunningtaskdbid" type="hidden" value="${dunningtaskdbid}"/>
		<input id = "mobileSelf" name="mobileSelf" type="hidden" value="${mobileSelf}"/>
		<input id = "dunningCycle" name="dunningCycle" type="hidden" value="${dunningCycle}"/>
 		<input id = "overdueDays" name="overdueDays" type="hidden" value="${overdueDays}"/>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>订单ID</th>
				<th>还款到期日</th>
				<th>订单状态</th>
				<th>是否有效联络</th>
				<th>逾期原因</th>
				<th>结果代码</th>
				<th>用户承诺还款日</th>
				<th>下次跟进日期</th>
				<th>备注</th>
				<th>催收人</th>
				<th>累计号码数量</th>
				<th>操作时间</th>
			</tr>
		</thead>
		<tbody>
 		<c:forEach items="${page.list}" var="tMisDunnedConclusion" varStatus="vs"> 
			<tr>
				<td>
					${(vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					${tMisDunnedConclusion.dealcode}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.repaymenttime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<c:choose>
						<c:when test = "${tMisDunnedConclusion.orderstatus=='true'}">已还清</c:when>
						<c:when test = "${tMisDunnedConclusion.orderstatus=='false'}">未还清</c:when>
						<c:otherwise>${tMisContantRecord.orderstatus}</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test = "${tMisDunnedConclusion.iseffective=='true'}">是</c:when>
						<c:when test = "${tMisDunnedConclusion.iseffective=='false'}">否</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
					${tMisDunnedConclusion.overduereasonstr}
				</td>
				<td>
					${tMisDunnedConclusion.resultcodestr}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.promisepaydate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.nextfollowdate}" pattern="yyyy-MM-dd"/>
					
				</td>
				<td title="${tMisDunnedConclusion.remark}">
					<c:choose>  
						<c:when test="${fn:length(tMisDunnedConclusion.remark) > 50}">  
							<c:out value="${fn:substring(tMisDunnedConclusion.remark, 0, 50)}..." />
						</c:when>
						<c:otherwise>
							<c:out value="${tMisDunnedConclusion.remark}" />
						</c:otherwise>  
					</c:choose>
				</td>
				<td>
					${tMisDunnedConclusion.dunningpeoplename}
				</td>
				<td>
					${tMisDunnedConclusion.dunningMobileCount}
				</td>
				<td>
					<fmt:formatDate value="${tMisDunnedConclusion.dunningtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			
		</c:forEach> 
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
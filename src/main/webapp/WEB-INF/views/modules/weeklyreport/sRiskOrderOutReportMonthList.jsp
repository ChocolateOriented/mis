<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>委外月报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出委外月报表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/weeklyreport/sRiskOrderOutReportMonth/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		
		function onClickWeekDetails(obj){
			var createtime = $(obj).attr("createtime");
			var dunningpeopleid = $(obj).attr("dunningpeopleid");
			var url = "${ctx}/weeklyreport/sRiskOrderOutReportMonth/detail?createtime="+ createtime +"&dunningpeopleid=" + dunningpeopleid;
			$.jBox.open("iframe:" + url, "催收金额详情", 600, 350, {            
                   buttons: {"确定": "ok"},
                   loaded: function (h) {
                       $(".jbox-content", document).css("overflow-y", "hidden");
                   }
             });
		}
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weeklyreport/sRiskOrderOutReportMonth/">委外月报表列表</a></li>
<%-- 		<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportMonth:edit"><li><a href="${ctx}/weeklyreport/sRiskOrderOutReportMonth/form">委外月报表添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="SRiskOrderOutReportMonth" action="${ctx}/weeklyreport/sRiskOrderOutReportMonth/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		
			<li><label>催收方名称：</label>
				<form:input path="dunningpeoplename" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li>
			
			<label>月份：</label>
				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SRiskOrderOutReportMonth.datetime}" pattern="yyyy-MM"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
			</li>
			
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>年月</th>
				<th>催收方名称</th>
				<th>委外单数</th>
				<th>委外本金</th>
				<th>委外金额</th>
				<th>应催单数</th>
				<th>应催本金</th>
				<th>应催金额</th>
				<th>催回单数</th>
				<th>催回金额</th>
				<th>催回率</th>
				<th>基础佣金</th>
<%-- 				<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportMonth:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sRiskOrderOutReportMonth">
			<tr>
				<td>
<%-- 				<a href="${ctx}/weeklyreport/sRiskOrderOutReportMonth/form?id=${sRiskOrderOutReportMonth.id}"> --%>
					${sRiskOrderOutReportMonth.createtime}
<!-- 				</a> -->
				</td>
				<td>
					${sRiskOrderOutReportMonth.dunningpeoplename}
				</td>
				<td>
					${sRiskOrderOutReportMonth.dunningordernum}
				</td>
				<td>
					${sRiskOrderOutReportMonth.dunningordercapitalamount}
				</td>
				<td>
					${sRiskOrderOutReportMonth.dunningorderamount}
				</td>
				<td>
					${sRiskOrderOutReportMonth.repayordernum}
				</td>
				<td>
					${sRiskOrderOutReportMonth.repayordercapitalamount}
				</td>
				<td>
					${sRiskOrderOutReportMonth.repayorderamount}
				</td>
				<td>
					${sRiskOrderOutReportMonth.payoffordernum}
				</td>
				<td>
					<a href="javascript:void(0)"  onclick="onClickWeekDetails(this)" createtime="${sRiskOrderOutReportMonth.createtime}" dunningpeopleid="${sRiskOrderOutReportMonth.dunningpeopleid}">
<%-- 					<a href="${ctx}/weeklyreport/sRiskOrderOutReportMonth/detail?createtime=${sRiskOrderOutReportMonth.createtime}&dunningpeoplename=${sRiskOrderOutReportMonth.dunningpeoplename}"> --%>
					${sRiskOrderOutReportMonth.payofforderamount}
					</a>
				</td>
				<td>
					${sRiskOrderOutReportMonth.payofforderrate}
				</td>
				<td>
					${sRiskOrderOutReportMonth.basiccommission}
				</td>
<%-- 				<shiro:hasPermission name="weeklyreport:sRiskOrderOutReportMonth:edit"><td> --%>
<%--     				<a href="${ctx}/weeklyreport/sRiskOrderOutReportMonth/form?id=${sRiskOrderOutReportMonth.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/sRiskOrderOutReportMonth/delete?id=${sRiskOrderOutReportMonth.id}" onclick="return confirmx('确认要删除该委外月报表吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
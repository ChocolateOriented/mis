<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户周报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			// 导出功能
			 $("#userConversionExportFile").click(function(){
				top.$.jBox.confirm("确认要导出用户周报数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/weeklyreport/vUserConversionReportWeek/userConversionExportFile");
						$("#searchForm").submit();
					}
				},
				{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			 });
		});
		
		// 查询
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/weeklyreport/vUserConversionReportWeek/");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="weeklyreport:sUserConversionReport:view"><li><a href="${ctx}/weeklyreport/sUserConversionReport/">用户报表编辑列表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="weeklyreport:vUserConversionReportWeek:view"><li class="active"><a href="${ctx}/weeklyreport/vUserConversionReportWeek">用户周报表</a></li></shiro:hasPermission>
		<shiro:hasPermission name="weeklyreport:vUserConversionReportMonth:view"><li><a href="${ctx}/weeklyreport/vUserConversionReportMonth">用户月报表</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="VUserConversionReportWeek" action="${ctx}/weeklyreport/vUserConversionReportWeek/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>日期：</label>
				<form:input path="weekdesc" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
		    <li class="btns"><input id="userConversionExportFile" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th colspan="2" style="text-align:center;">装机量/关注</th>
				
				<th colspan="4" style="text-align:center;">转化率</th>
				<th colspan="2" style="text-align:center;">首单平均等待时间</th>
				<th colspan="5" style="text-align:center;">放款用户数	</th>
				<th colspan="2" style="text-align:center;">订单取消率	</th>
				<th></th>
			</tr>
			<tr>
				<th>日期</th>
				
				<th title="本期内， App 装机量 + 微信装机量。手工按天/周/月录入，并以友盟及微信后台为准">新增安装量</th>
				<th title="本期增长量 / 累计增长量">安装量增长率</th>
				
				<th title="本期注册用户数 / 本期新增用户数">新增到注册</th>
				<th title="本期首单 / 本期注册用户数">注册到订单</th>
				<th title="本期人工 / 本期首单">订单到人工</th>
				<th title="本期新增放款用户数 / 本期人工">人工通过率</th>
				
				<th title="本期新增放款用户对应的首单，从提交订单到最终放款之间的时间长度的平均值">周一至周四提交订单</th>
				<th title="本期新增放款用户对应的首单，从提交订单到最终放款之间的时间长度的平均值">周五至周日提交订单</th>
				
				<th title="放款时间在本期内的新增用户数">新增放款用户数</th>
				<th title="产品上线起，累计成功放款用户数">累计放款用户数</th>
				<th title="有效用户 / 累计放款用户数">有效用户比</th>
				<th title="优质用户 / 累计放款用户数">优质用户比</th>
				<th title="活跃用户 / 累计放款用户数">活跃用户比</th>
				
				<th title="本期首单中订单创建日期当日被取消的订单 / 本期首单">当日取消率</th>
				<th title="本期首单中订单创建日期次日被取消的订单 / 本期首单">次日取消率</th>
<%-- 				<shiro:hasPermission name="weeklyreport:vUserConversionReportWeek:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="vUserConversionReportWeek">
			<tr>
				<td>
					${vUserConversionReportWeek.weekdesc}
				</td>
				<td>
					${vUserConversionReportWeek.newinstallText}
				</td>
				<td>
					${vUserConversionReportWeek.newinstallpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.registpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.firstorderpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.artificialpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.remitpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.waitingtimeavg01}
				</td>
				<td>
					${vUserConversionReportWeek.waitingtimeavg02}
				</td>
				<td>
					${vUserConversionReportWeek.newremituserText}
				</td>
				<td>
					${vUserConversionReportWeek.allremituserText}
				</td>
				<td>
					${vUserConversionReportWeek.validuserpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.prioruserpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.activeuserpercentText}
				</td>
				<td>
					${vUserConversionReportWeek.canceltodaypercentText}
				</td>
				<td>
					${vUserConversionReportWeek.canceltomorrowpercentText}
				</td>
<%-- 				<shiro:hasPermission name="weeklyreport:vUserConversionReportWeek:edit"><td> --%>
<%--     				<a href="${ctx}/weeklyreport/vUserConversionReportWeek/form?id=${vUserConversionReportWeek.id}">修改</a> --%>
<%-- 					<a href="${ctx}/weeklyreport/vUserConversionReportWeek/delete?id=${vUserConversionReportWeek.id}" onclick="return confirmx('确认要删除该用户周报表吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
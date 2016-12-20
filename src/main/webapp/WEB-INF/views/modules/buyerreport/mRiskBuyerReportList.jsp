<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#add").click(function(){
				
				$.jBox.open("iframe:${ctx}/buyerreport/mRiskBuyerReport/dialog?a=1", "测试", 600, 300, {           //如果是修改，传个ID就行了
                    buttons: {"确定": "ok", "关闭": true},submit: function (v, h, f) {
                        if (v == "ok") {
                            var iframeName = h.children(0).attr("name");
                            var iframeHtml = window.frames[iframeName];               //获取子窗口的句柄
                            iframeHtml.saveOrUpdate();
                            return false;
                        }
                    },
                    loaded: function (h) {
                        $(".jbox-content", document).css("overflow-y", "hidden");
                    }
                });
				
// 				top.$.jBox("iframe:" + "${ctx}/buyerreport/mRiskBuyerReport/dialog", {  
// 			            title: "测试",  
// 			            width: 700,  
// 			            height: 400,  
// // 			            buttons: { '确定': 'ok' },
// 			            buttons: { '关闭': true }  
// 			        }); 
				
			});
		});
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
		<li class="active"><a href="${ctx}/buyerreport/mRiskBuyerReport/">用户报表列表</a></li>
		<shiro:hasPermission name="buyerreport:mRiskBuyerReport:edit">
		<li><a href="${ctx}/buyerreport/mRiskBuyerReport/form">用户报表添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="MRiskBuyerReport" action="${ctx}/buyerreport/mRiskBuyerReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>商户名称：</label>
				<form:input path="merchantId.creditMerchantName" htmlEscape="false" maxlength="10" class="input-medium"/>
<%-- 				<form:select path="merchantId" class="input-medium"> --%>
<%-- 					<form:option value="" label=""/> --%>
<%-- 					<form:options items="${fns:getDictList('merchant_id')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
<%-- 				</form:select> --%>
			</li>
			<li><label>数量：</label>
				<form:input path="count" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>创建日期：</label>
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mRiskBuyerReport.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="add" class="btn btn-primary" type="button" value="新增"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>merchant_id</th>
				<th>count</th>
				<th>create_date</th>
				<shiro:hasPermission name="buyerreport:mRiskBuyerReport:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mRiskBuyerReport">
			<tr>
				<td><a href="${ctx}/buyerreport/mRiskBuyerReport/form?id=${mRiskBuyerReport.id}">
					${mRiskBuyerReport.id}
				</a></td>
				<td>
					${mRiskBuyerReport.merchantId.creditMerchantName}
				</td>
				<td>
					${mRiskBuyerReport.count}
				</td>
				<td>
					<fmt:formatDate value="${mRiskBuyerReport.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="buyerreport:mRiskBuyerReport:edit"><td>
    				<a href="${ctx}/buyerreport/mRiskBuyerReport/form?id=${mRiskBuyerReport.id}">修改</a>
					<a href="${ctx}/buyerreport/mRiskBuyerReport/delete?id=${mRiskBuyerReport.id}" onclick="return confirmx('确认要删除该用户报表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
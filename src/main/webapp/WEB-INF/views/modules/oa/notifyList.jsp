<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消息通知</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function changeFeedback(obj){
            var id = $(obj).attr("feedbackId");
            var isRead = $(obj).attr("read");
            $(obj).children("span").css("color", "#999999");
            $.jBox.open("iframe:" + "${ctx}/dunning/taskIssue/detailsDialog?id=" + id+"&read="+isRead, "" , 480, 200,{
                buttons: {
                },
                submit: function (v, h, f) {
                },
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
		<li class="active">消息通知</li>
	</ul>
	<form:form id="searchForm" modelAttribute="taskIssue" action="${ctx}/dunning/taskIssue/notify" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li>状态：
				<form:select path="status" htmlEscape="false" maxlength="100" class="input-small">
					<form:option selected="selected" value="" label="全部"/>
					<form:options items="${issueStatus}" itemLabel="desc"/>
				</form:select>
			</li>
			<li><label>类别：</label>
				<form:select path="issueType" htmlEscape="false" maxlength="200" class="input-small">
					<form:option selected="selected" value="" label="不限"/>
					<form:options items="${issueType}" itemLabel="desc"/>
				</form:select>
			</li>
			<li><label>关键词：</label>
				<form:input path="keyWord"  htmlEscape="false" maxlength="200" class="input-small"/>
			</li>
			<li><label>推送时间</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${taskIssue.beginCreateDate}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${taskIssue.endCreateDate}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page()"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width: 1080px">
		<thead>
			<tr>
				<th style="width: 650px" >标题</th>
				<th>时间</th>
			</tr>
		</thead>
		<tbody>
		<c:set var="flag" value="0"/>
		<c:forEach items="${page.list}" var="taskIssue">
			<tr class="result">
				<td><a href="javascript: void 0;" feedbackId="${taskIssue.id}" onclick="changeFeedback(this);" read="${taskIssue.read}">
					<span class="solveStatus"
						  <c:if test="${taskIssue.read == true}">
							  style="color: #999999"
						  </c:if>
					>
						${taskIssue.issueChannel.desc}：订单号{${fns:abbr(taskIssue.dealcode,50)}},
                        <c:forEach items="${taskIssue.issueTypes}" var="issueTypesItem">
                            ${issueTypesItem.desc},
                        </c:forEach>
                        <c:if test="${taskIssue.status eq 'RESOLVED'}">
                            &nbsp;${fns:abbr(taskIssue.status.desc,50)}
                        </c:if>
                    </span>
				<td>
					<fmt:formatDate value="${taskIssue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: qtzhou
  Date: 2017/10/31
  Time: 17:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>问题反馈</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">

    $(document).ready(function() {

    });
    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").attr("action","${ctx}/dunning/taskIssue/feedbackList?dealcode=${dealcode}");
        $("#searchForm").submit();
        return false;
    }

    function changeResult(obj){
        var id = $(obj).attr("feedbackId");
        $.jBox.open("iframe:" + "${ctx}/dunning/taskIssue/manualResolutionDialog?id=" + id, "处理结果" , 220, 260, {
            buttons: {
            },
            submit: function (v, h, f) {
            },
            loaded: function (h) {
                $(".jbox-content", document).css("overflow-y", "hidden");
            }
        });

    }

</script>
</head>
<body>
    <form:form id="searchForm" modelAttribute="TMisCustomerServiceFeedback" action="${ctx}/dunning/taskIssue/feedbackList" method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
        <thead>
            <tr>
                <th>订单编号</th>
                <th>问题状态</th>
                <th>标签</th>
                <th>问题描述</th>
                <th>推送时间</th>
                <th>操作时间</th>
                <th>操作人</th>
                <th>推送人</th>
                <th>操作</th>
                <th>处理结果</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="taskIssue">
            <tr>
                <shiro:hasPermission name="dunning:TMisCustomerServiceFeedback:view">
                <td>
                    ${taskIssue.dealcode}
                </td>
                <td>
                    ${taskIssue.status.desc}
                </td>
                <td>
                    ${taskIssue.issueTypes2Text}
                </td>
                <td>
                    ${taskIssue.description}
                </td>
                <td>
                    <fmt:formatDate value="${taskIssue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                    <fmt:formatDate value="${taskIssue.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                    ${taskIssue.updateBy.name}
                </td>
                <td>
                    ${taskIssue.recorderName}
                </td>
                <td>
                    <c:if test="${taskIssue.status eq 'UNRESOLVED'}">
                        <c:choose>
                            <c:when test="${fns:contains(taskIssue.issueTypesToJson, 'COMPLAIN_SHAKE')}">
                                <shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
                                    <input class="btn btn-primary" type="button" feedbackId="${taskIssue.id}"
                                           value="待解决" style="padding:0px 8px 0px 8px;" onclick="changeResult(this);"/>
                                </shiro:hasPermission>
                            </c:when>
                            <c:otherwise>
                                <input class="btn btn-primary" type="button" feedbackId="${taskIssue.id}"
                                       value="待解决" style="padding:0px 8px 0px 8px;" onclick="changeResult(this);"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
                <td>
                    ${taskIssue.handlingResult}
                </td>
                </shiro:hasPermission>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>

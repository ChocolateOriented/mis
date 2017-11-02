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
        $("#searchForm").submit();
        return false;
    }

</script>
</head>
<body>
    <form:form id="searchForm" modelAttribute="TMisCustomerServiceFeedback" action="${ctx}/dunning/tMisCustomerServiceFeedback/" method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
        <thead>
            <tr>
                <th>订单编号</th>
                <th>订单类型</th>
                <th>订单状态</th>
                <th>问题状态</th>
                <th>标签</th>
                <th>问题描述</th>
                <th>操作时间</th>
                <th>操作人</th>
                <th>推送人</th>
                <th>操作</th>
                <th>处理结果</th>
                <th>主订单编号</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="tMisCustomerServiceFeedback">
            <tr>
                <shiro:hasPermission name="dunning:TMisCustomerServiceFeedback:edit">
                <td>
                    ${tMisCustomerServiceFeedback.dealcode}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.ordertype}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.orderstatus}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.problemstatus}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.hashtag}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.problemdescription}
                </td>
                <td>
                    ${fn:substring(tMisCustomerServiceFeedback.createTime, 0, 16)}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.createBy.name}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.pushpeople}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.operate}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.handlingresult}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.rootOrderId}
                </td>
                </shiro:hasPermission>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>

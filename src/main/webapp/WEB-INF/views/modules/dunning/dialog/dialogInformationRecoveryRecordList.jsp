<%--
  Created by IntelliJ IDEA.
  User: jxguo
  Date: 2017/10/24
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>记录</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<div style="margin-top: 10px">
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <th style="width: 20%">时间</th>
            <th style="width: 20%">订单ID</th>
            <th style="width: 20%">添加人</th>
            <th style="width: 40%">备注</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${historyRecordList}" var="list">
            <tr>
                <td>
                        <fmt:formatDate value="${list.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                        ${list.dealCode}
                </td>
                <td>
                        ${list.createBy.name}
                </td>
                <td>
                        ${list.historyRemark}
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>

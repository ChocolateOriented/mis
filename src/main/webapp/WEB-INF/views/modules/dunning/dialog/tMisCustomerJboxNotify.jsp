<%--
  Created by IntelliJ IDEA.
  User: qtzhou
  Date: 2017/11/7
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>Title</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
    $(document).ready(function() {
      window.parent.parent.getCustServiceNotifyNum();
    });
</script>
</head>
<body>
 <div style="background-color: #F5F5F5;height: 136px;padding: 10px">
    <div id="feedbackContainer" style="white-space:nowrap;height: 30px">
        用户${taskIssue.userName}, 订单号
        <a href="${ctx}/dunning/tMisDunningTask/pageFather?dealcode=${taskIssue.dealcode}" target="_blank"> ${taskIssue.dealcode} </a>
        ${issueTypesItem.issueTypes2Text}
    </div>
    <div id="pictureContainer" style="height: 70px">
        ${taskIssue.description}
    </div>
    <div id="peopleContainer" class="row-fluid">
        <div class="span4">
            <c:if test="${taskIssue.status eq 'RESOLVED'}">
                ${taskIssue.handlingResult}
            </c:if>
        </div>
        <div class="span4 offset3">
          ${taskIssue.updateRole}:
              <c:choose>
                  <c:when test="${taskIssue.status eq 'RESOLVED'}">
                    ${taskIssue.updateBy.name}
                  </c:when>
                  <c:otherwise>
                    ${taskIssue.recorderName}
                  </c:otherwise>
              </c:choose>
        </div>
    </div>
 </div>
</body>
</html>

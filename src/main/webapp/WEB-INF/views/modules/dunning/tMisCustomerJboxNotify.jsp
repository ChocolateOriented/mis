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
        if('${custNotify}'){
            window.parent.parent.controlNum();
        }
        function feedbackJbox(n,s){
            $("#feedbackContainer").val(n);
            $("#pictureContainer").val(n);
            $("#peopleContainer").val(n);
            $("#container").attr("action","${ctx}/dunning/tMisCustomerServiceFeedback/feedbackJbox");
            return false;
        }
    });
</script>
</head>
<body>
<form:form id="feedbackForm" modelAttribute="TMisCustomerServiceFeedback" action="${ctx}/dunning/tMisCustomerServiceFeedback/feedbackJbox" method="post" class="breadcrumb form-search">
    <div id="container" style="width:100%;height: 120px;">
        <div id="feedbackContainer" style="white-space:nowrap;">
            用户${tMisCustomerServiceFeedback.uname}:订单号{${tMisCustomerServiceFeedback.dealcode}}${tMisCustomerServiceFeedback.tagText}
        </div>
        <div id="pictureContainer">
               ${tMisCustomerServiceFeedback.problemdescription}
        </div>
        <div id="peopleContainer" style="margin-top: 70px">
            <c:if test="${tMisCustomerServiceFeedback.statusText eq '已解决'}">
                <span>问题已解决</span><span style="margin-left: 200px">解决人:${tMisCustomerServiceFeedback.updateBy.name}</span>
            </c:if>
            <c:if test="${tMisCustomerServiceFeedback.statusText eq '未解决'}">
                <span style="margin-left: 260px">客服:${fns:abbr(tMisCustomerServiceFeedback.pushpeople,50)}<span/>
            </c:if>
        </div>
    </div>
</form:form>
</body>
</html>

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
        $("#searchForm").attr("action","${ctx}/dunning/tMisCustomerServiceFeedback/feedbackList");
        $("#searchForm").submit();
        return false;
    }

    function changeResult(obj){
        var id = $(obj).attr("feedbackId");
        $.jBox.open("iframe:" + "${ctx}/dunning/tMisCustomerServiceFeedback/jboxResult?id=" + id, "处理结果" , 220, 260, {
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
    <form:form id="searchForm" modelAttribute="TMisCustomerServiceFeedback" action="${ctx}/dunning/tMisCustomerServiceFeedback/feedbackList" method="post" class="breadcrumb form-search">
        <input id = "buyerId" name="buyerId" type="hidden" value="${buyerId}"/>
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
                <th>推送时间</th>
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
                <shiro:hasPermission name="dunning:TMisCustomerServiceFeedback:view">
                <td>
                    ${tMisCustomerServiceFeedback.dealcode}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.orderTypeText}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.orderStatusText}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.statusText}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.tagText}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.problemdescription}
                </td>
                <td>
                    <fmt:formatDate value="${tMisCustomerServiceFeedback.pushTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                    <c:if test="${tMisCustomerServiceFeedback.statusText eq '已解决'}">
                      <fmt:formatDate value="${tMisCustomerServiceFeedback.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </c:if>
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.nickname}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.pushpeople}
                </td>
                <td>
                    <c:if test="${tMisCustomerServiceFeedback.statusText eq '未解决'}">
                        <c:choose>
                            <c:when test="${fns:contains(tMisCustomerServiceFeedback.tagText, '投诉催收')}">
                                <shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
                                    <input id="${tMisCustomerServiceFeedback.operate}" class="btn btn-primary" type="button" feedbackId="${tMisCustomerServiceFeedback.id}"
                                           value="待解决" style="padding:0px 8px 0px 8px;" onclick="changeResult(this);"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="dunning:tMisDunningTask:leaderview">
                                    <input id="${tMisCustomerServiceFeedback.operate}" class="btn btn-primary" type="button" feedbackId="${tMisCustomerServiceFeedback.id}"
                                           value="待解决" style="padding:0px 8px 0px 8px;"/>
                                </shiro:lacksPermission>
                            </c:when>
                            <c:otherwise>
                                <input id="${tMisCustomerServiceFeedback.operate}" class="btn btn-primary" type="button" feedbackId="${tMisCustomerServiceFeedback.id}"
                                       value="待解决" style="padding:0px 8px 0px 8px;" onclick="changeResult(this);"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.handlingresult}
                </td>
                <td>
                    ${tMisCustomerServiceFeedback.rootorderid}
                </td>
                </shiro:hasPermission>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>

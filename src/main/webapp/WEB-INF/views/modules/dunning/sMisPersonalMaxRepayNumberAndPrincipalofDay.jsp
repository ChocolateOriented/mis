<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>个人日还款最大户数&本金</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出个人日还款最大户数&本金吗？","系统提示",function(v,h,f){
                    if(v=="ok"){
                        $("#searchForm").attr("action","${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/exportPersonalMaxRepayNumberAndPrincipal");
                        $("#searchForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/personalMaxRepayNumberAndPrincipal");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/personalMaxRepayNumberAndPrincipal">个人日还款最大户数&本金</a></li>
</ul>
<form:form id="searchForm"  action="${ctx}/dunning/sMisDunningTaskMonthReport/" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>日期：</label>
            <input name="dateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${datetime}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
        </li>
        <li class="btns">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
            <input id="btnExport" class="btn btn-primary" type="button" value="导出" />
        </li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width: 900px">
    <thead>
    <tr>
        <th>日期</th>
        <th>催收队列</th>
        <th>姓名</th>
        <th>还款最大户数</th>
        <th>姓名</th>
        <th>还款最大本金</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="DunningPersonalMaxRepayNumberAndPrincipal">
        <tr>
            <td>
                    <fmt:formatDate value="${DunningPersonalMaxRepayNumberAndPrincipal.dateTime}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                    ${DunningPersonalMaxRepayNumberAndPrincipal.cycle}
            </td>
            <td>
                    ${DunningPersonalMaxRepayNumberAndPrincipal.nameDealcode}
            </td>
            <td>
                    ${DunningPersonalMaxRepayNumberAndPrincipal.maxDealcode}
            </td>
            <td>
                    ${DunningPersonalMaxRepayNumberAndPrincipal.namePrincipal}
            </td>
            <td>
                    ${DunningPersonalMaxRepayNumberAndPrincipal.principal}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
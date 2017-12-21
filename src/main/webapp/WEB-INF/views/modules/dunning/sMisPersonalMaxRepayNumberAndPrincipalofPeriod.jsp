<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>个人周期还款最大户数&本金</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出个人周期还款最大户数&本金数据吗？","系统提示",function(v,h,f){
                    if(v=="ok"){
                        $("#searchForm").attr("action","${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/exportPersonalMaxRepayNumberAndPrincipalofPeriod");
                        $("#searchForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/personalMaxRepayNumberAndPrincipalofPeriod");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/personalMaxRepayNumberAndPrincipalofPeriod">个人周期还款最大户数&本金</a></li>
</ul>
<form:form id="searchForm" modelAttribute="dunningMaxRepayNumberAndPrincipal" action="${ctx}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport/personalMaxRepayNumberAndPrincipalofPeriod" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>月份：</label>

            <input name="dateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${datetime}" pattern="yyyy-MM"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>

           <form:select id="monthdesc"  path="monthdesc" class="input-medium" >
                <form:option  value="上半月" label="上半月"/>
                <form:option  value="下半月" label="下半月"/>
            </form:select>

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
        <th>月份</th>
        <th>催收队列</th>
        <th>姓名</th>
        <th>还款最大户数</th>
        <th>姓名</th>
        <th>还款最大本金</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="dunningMaxRepayNumberAndPrincipal">
        <tr>
            <td>
                    <fmt:formatDate value="${dunningMaxRepayNumberAndPrincipal.dateTime}" pattern="yyyy-MM"/>${dunningMaxRepayNumberAndPrincipal.monthdesc}
            </td>
            <td>
                    ${dunningMaxRepayNumberAndPrincipal.cycle}
            </td>
            <td>
                    ${dunningMaxRepayNumberAndPrincipal.nameDealcode}
            </td>
            <td>
                    ${dunningMaxRepayNumberAndPrincipal.maxDealcode}
            </td>
            <td>
                    ${dunningMaxRepayNumberAndPrincipal.namePrincipal}
            </td>
            <td>
                    ${dunningMaxRepayNumberAndPrincipal.principal}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
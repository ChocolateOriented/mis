<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>质检服务支持-语音识别</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {

            var groups = [];
            if ("${groupLimit}" == "true") {
                var groupOptions = $("#groupList")[0].options;
                for (var i = 0; i < groupOptions.length; i++) {
                    if (groupOptions[i].value) {
                        groups.push(groupOptions[i].value);
                    }
                }
            }

            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出质检服务支持-语音识别数据吗？","系统提示",function(v,h,f){
                    if(v=="ok"){
                        $("#searchForm").attr("action","${ctx}/dunning/tMisCallingASR/getAsrExcel");
                        $("#searchForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });

        });
        function page(n,s){
            if(n) $("#pageNo").val(n);
            if(s) $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/dunning/tMisCallingASR/list");
            $("#searchForm").submit();
            return false;
        }

        function collectionfunction(obj, width, height, param){
            var method = $(obj).attr("method");
            // 			var aid = $(obj).attr("aid");
            var url = "${ctx}/dunning/tMisCallingASR/" + method;
            if (param) {
                for (var name in param) {
                    if (typeof param[name] != "function") {
                        url = url + "?" + name + "=" + param[name] || "";
                    }
                }
            }
 				//alert(url);
            $.jBox.open("iframe:" + url, $(obj).attr("value") , width || 600, height || 430, {
                top: '0%',
                buttons: {
// 	            	   "确定": "ok", "取消": true
                },
                submit: function (v, h, f) {
// 	                	   alert(v);
// 	                       if (v == "ok") {
// 	                           var iframeName = h.children(0).attr("name");
// 	                           var iframeHtml = window.frames[iframeName];               //获取子窗口的句柄
// 	                           iframeHtml.saveOrUpdate();
// 	                           return false;
// 	                       }
                },
                loaded: function (h) {
                    $(".jbox-content", document).css("overflow-y", "hidden");
                }
            });
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/dunning/tMisCallingASR/list">质检服务支持-语音识别</a></li>
</ul>
<form:form id="searchForm" modelAttribute="TMisCallingQualityTest" action="${ctx}/dunning/tMisCallingASR/list" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>日期时间：</label>
            <input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${TMisCallingQualityTest.startTime}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
            <input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${TMisCallingQualityTest.endTime}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
        </li>
        <li>
            <label>订单号：</label>
            <form:input id="dealcode" path="dealcode"/>
        </li>
        <li>
            <label>客户电话：</label>
            <form:input id="targetNumber" path="targetNumber"/>
        </li>
        <li class="btns">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
            <input id="btnExport" class="btn btn-primary" type="button" value="导出" />
        </li>
        <li class="clearfix"></li>
        <li>
            <label>催收小组：</label>
            <form:select id="groupList" path="groupId" class="input-medium">
                <form:option value="">全部</form:option>
                <!-- 添加组类型为optgroup -->
                <c:forEach items="${groupTypes}" var="type">
                    <optgroup label="${type.value}">
                        <!-- 添加类型对应的小组 -->
                        <c:forEach items="${groupList}" var="item">
                            <c:if test="${item.type == type.key}">
                                <option value="${item.id}" <c:if test="${TMisCallingQualityTest.groupId == item.id }">selected="selected"</c:if>>${item.name}</option>
                            </c:if>
                        </c:forEach>
                    </optgroup>
                </c:forEach>
            </form:select></li>
        <li>
        <li>
            <label>通话人：</label>
            <form:input id="peopleName" path="peopleName"/>
        </li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>编号</th>
        <th>呼叫时间</th>
        <th>客户电话</th>
        <th>通话类型</th>
        <th>通话人</th>
        <th>通话时间(单位:秒)</th>
        <th>订单编号</th>
        <th>录音</th>
        <th>敏感词</th>
        <th>质检分数</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="TMisCallingQualityTest" varStatus="vs">
        <tr>
            <td>
                    ${ (vs.index+1) +  (page.pageNo-1) * page.pageSize}
            </td>
            <td>
                <fmt:formatDate value="${TMisCallingQualityTest.startTime}" pattern="yyyy-MM-dd hh:MM:ss"/>
            </td>
            <td>
                    ${TMisCallingQualityTest.targetNumber}
            </td>
            <td>
                    ${TMisCallingQualityTest.callType.desc}
            </td>
            <td>
                    ${TMisCallingQualityTest.peopleName}
            </td>
            <td>
                    ${TMisCallingQualityTest.durationTime}
            </td>
            <td>
                    ${TMisCallingQualityTest.dealcode}
            </td>
            <td style="padding:0px;">
                <c:if test="${not empty TMisCallingQualityTest.audioUrl}">
                    <audio src="${ctiUrl}${TMisCallingQualityTest.audioUrl}" preload="none" controls="controls" controlsList="nodownload"></audio>
                </c:if>
            </td>
            <td>
                <a href="javascript:void(0)"  onclick="collectionfunction(this, 740, 340, {id : '${TMisCallingQualityTest.id}'});"  method="getCallingContent" value="通话记录" >
                ${TMisCallingQualityTest.sensitiveWordNumber}
            </a>

            </td>
            <td>
                    <%--${TMisCallingQualityTest.effectiveUser}--%>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
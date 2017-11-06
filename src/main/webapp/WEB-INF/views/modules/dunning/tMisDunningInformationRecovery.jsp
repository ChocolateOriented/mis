<%--
  Created by IntelliJ IDEA.
  User: jxguo
  Date: 2017/10/23
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>信息修复</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
    </script>
</head>
<body>

<div style="margin-bottom: 10px; margin-left: 10px">
<input id="btnAdd" onclick="window.parent.tagPopup(this)" class="btn btn-primary" method="InformationAdd" type="button" value="添加" />
</div>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
<thead>
<tr>
    <!-- 				<th>序号</th> -->
    <th>类型</th>
    <th>号码</th>
    <th>姓名</th>
    <th>关系</th>
    <th>历史记录</th>
    <th>添加人</th>
    <th>操作时间</th>
    <th>操作</th>
</tr>
</thead>
<tbody>
<c:forEach items="${entityList}" var="list">
    <tr>
        <td>
                ${list.contactType}
        </td>
        <td>
                ${list.contactNumber}
        </td>
        <td>
                ${list.contactName}
        </td>
        <td>
                ${list.contactRelationship}
        </td>
        <td>

            <a href="javascript:void(0)"  onclick="window.parent.collectionfunction(this, 740, 340, {id : '${list.id}'});"  method="HistoryRecordList"  value="记录" >
                (${list.historyRecordCount})
            </a>
            &nbsp<input id="btnHistoryRecordAdd" onclick="window.parent.collectionfunction(this, 540, 340, {id : '${list.id}'});" class="btn btn-primary" method="HistoryRecordAdd" type="button" value="添加"/>
        </td>
        <td>
                ${list.createBy.name}
        </td>
        <td>
                <fmt:formatDate value="${list.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </td>
        <td>
            <input id="btnChange" onclick="window.parent.collectionfunction(this, 540, 340,{id : '${list.id}'});" class="btn btn-primary" method="InformationChange " type="button" value="修改" />
        </td>
    </tr>
</c:forEach>


</tbody>
</table>


</body>
</html>

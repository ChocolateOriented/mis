<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>坐席配置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function(){
		
		
		
		
		
	});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		
		function bind(obj){
			if($(obj).attr("peopleId")){
				//取消绑定
				if(confirm("确定取消绑定吗?")){
					location.href='${ctx}/dunning/tMisAgentInfo/unbind?id='+$(obj).attr("id");
				}
			}else{
				//绑定
				var url = "${ctx}/dunning/tMisAgentInfo/bindPage?id="+$(obj).attr("id");
				
				$.jBox.open("iframe:"+url,"绑定页面",500,400,{
					 buttons: {} ,
					   loaded: function (h) {
		                 $(".jbox-content", document).css("overflow-y", "hidden");
		             }
					
				});
				
			}
		}		
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisAgentInfo/list">坐席管理</a></li>
		<li><a href="${ctx}/dunning/tMisAgentInfo/form">坐席添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisAgentInfo" action="${ctx}/dunning/tMisAgentInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		
			<li>
				<label>坐席名称：</label>
				<input id="agentName" name="agent" type="text" />
			</li>
		
			<li>
				<label>催收员花名：</label>
				<input id="nickName" name="nickName" type="text" />
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th >坐席名称</th>
				<th>队列</th>
				<th>分机号</th>
				<th>对外分机号</th>
				<th>催收员花名</th>
				<th>操作</th>	
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="info" varStatus="vs">
			<tr>
				<td>
					${info.agent}
				</td>
				<td>
					${info.queue}
				</td>
				<td>
					${info.extension }
				</td>
				
				<td>
					${info.direct }
				</td>
				<td>
					${info.nickName}
				</td>
				
				<td>
					<a href="${ctx}/dunning/tMisAgentInfo/form?id=${info.id}&agent=${info.agent}">修改</a>
					<a id="${info.id}" peopleId="${info.peopleId}" onclick="bind(this)" style="cursor:pointer;">${not empty info.peopleId ?'取消绑定':'绑定'}</a>
<%-- 					<a href="${ctx}/dunning/tMisAgentInfo/delete?id=${info.id}" onclick="return confirmx('确认要删除该催收人员吗？', this.href)">删除</a> --%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			
			$("#btnDunningcycle").click(function(){
				 var peopleids = new Array();
					$("[name='peopleids']").each(function() {
						if(this.checked){
							peopleids.push($(this).val());
						}
					});
					if(peopleids.length==0){
						$.jBox.tip("请勾选催收人员", 'warning');
						return;
					}

					var url = "${ctx}/dunning/tMisDunningPeople/dialogDunningcycle?peopleids=" + peopleids ;
					$.jBox.open("iframe:" + url, "手动分配" , 600, 350, {            
			               buttons: {},
			               loaded: function (h) {
			                   $(".jbox-content", document).css("overflow-y", "hidden");
			               }
			        });
			 });
			
			$("#allorder").change(function(){
			 	if($("#allorder").prop('checked')){
			 		$("[name='peopleids']").each(function(){
				        $(this).prop('checked',true);
				    });
			 	}else{
			 		$("[name='peopleids']").each(function(){
						if(this.checked){
				        	$(this).prop('checked',false);
				        }
				    });
			 	}
			 });
			 
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningPeople/">催收人员列表</a></li>
		<shiro:hasPermission name="dunning:tMisDunningPeople:edit"><li><a href="${ctx}/dunning/tMisDunningPeople/form">催收人员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisDunningPeople" action="${ctx}/dunning/tMisDunningPeople/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>催收人：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
<!-- 			<li><label>人员类型：</label> -->
<%-- 				<form:input path="dunningpeopletype" htmlEscape="false" maxlength="32" class="input-medium"/> --%>
<!-- 			</li> -->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnDunningcycle" class="btn btn-primary" type="button" value="分配队列"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder" /></th>
				<th>催收人员名称</th>
				<th>人员类型</th>
				<th title="大于1为单笔固定费率，小于1大于0为单笔百分比费率">单笔费率</th>
				<th>催收队列</th>
<!-- 				<th>逾期周期起始</th> -->
<!-- 				<th>逾期周期截至</th> -->
				<th>自动分配</th>
				<shiro:hasPermission name="dunning:tMisDunningPeople:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisDunningPeople">
			<tr>
				<td>
					<input type="checkbox" name="peopleids" value="${tMisDunningPeople.id}"/>
				</td>
				<td><a href="${ctx}/dunning/tMisDunningPeople/form?id=${tMisDunningPeople.id}">
					${tMisDunningPeople.name}
				</a></td>
				<td>
					${tMisDunningPeople.dunningpeopletypeText}
				</td>
				<td>
					${tMisDunningPeople.rate}
				</td>
				<td>
					${tMisDunningPeople.dunningcycleText}
				</td>
<!-- 				<td> -->
<%-- 					${tMisDunningPeople.begin} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${tMisDunningPeople.end} --%>
<!-- 				</td> -->
				<td>
					${'t' eq tMisDunningPeople.auto ? '启用' : '停止'} 
				</td>
<!-- 				<td> -->
<%-- 					${tMisDunningPeople.field1} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${tMisDunningPeople.createby} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					<fmt:formatDate value="${tMisDunningPeople.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${tMisDunningPeople.updateby} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					<fmt:formatDate value="${tMisDunningPeople.updatedate}" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
<!-- 				</td> -->
				<shiro:hasPermission name="dunning:tMisDunningPeople:edit"><td>
    				<a href="${ctx}/dunning/tMisDunningPeople/form?id=${tMisDunningPeople.id}">修改</a>
					<a href="${ctx}/dunning/tMisDunningPeople/delete?id=${tMisDunningPeople.id}" onclick="return confirmx('确认要删除该催收人员吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
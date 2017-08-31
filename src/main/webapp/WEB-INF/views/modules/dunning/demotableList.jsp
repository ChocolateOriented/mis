<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#demoBut").click(function(){
				$.ajax({
					url:"${ctx}/dunning/demotable/demo",
					type:"POST",
					data:{val1:"1",val2:"2"},
					success:function(data){
						top.$.jBox.tip("分案完成");
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
		                   alert("查询失败:"+textStatus);
		                }
					
				});
			});
			
// 			 $("#automatic").click(function(){
// 				 $("#automatic").attr('disabled',"true");
// 				 $.ajax({
// 	                  type: 'POST',
// 	                  url : "${ctx}/dunning/tMisDunningTask/autoAssign",
// 	                  success : function(data) {
// 	                      if (data == "OK") {
// 	                          alert("分配成功");
// // 	                    	  $.jBox.tip("ok", 'info');
// 	                      } else {
// 	                          alert("分配失败:"+data.message);
// // 	                    	  $.jBox.tip("error", 'warning');
// 	                      }
// 	                      $("#automatic").removeAttr("disabled"); 
// 	                  },
// 	                  error : function(XMLHttpRequest, textStatus, errorThrown){
// 	                     alert("保存失败:"+textStatus);
// 	                  }
// 	              });
				
// 			 });
			
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
		<li class="active"><a href="${ctx}/dunning/demotable/">列表</a></li>
		<shiro:hasPermission name="dunning:demotable:edit"><li><a href="${ctx}/dunning/demotable/form">徐盛添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="demotable" action="${ctx}/dunning/demotable/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>dealcodenum：</label>
				<form:input path="dealcodenum" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>dealcodeamount：</label>
				<form:input path="dealcodeamount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>datetime：</label>
				<input name="datetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${demotable.datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<ul class="ul-form">
		<li><label>分案日期：</label>
			<input name="day" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		</li>
		<li><label>500户数：</label>
			<input  type="text" name="dealcodenum500" value="" />
		</li>
		<li><label>1000户数：</label>
			<input  type="text" name="dealcodenum1000" value="" />
		</li>
		<li><label>1500户数：</label>
			<input  type="text" name="dealcodenum1500" value="" />
		</li>
		<li><label>2000户数：</label>
			<input  type="text" name="dealcodenum2000" value="" />
		</li>
		<li><label>周期：</label>
			<select name="cycyle">
				<option value ="Q0">Q0</option>
				<option value ="Q1">Q1</option>
				<option value ="Q2">Q2</option>
				<option value ="Q3">Q3</option>
				<option value ="Q4">Q4</option>
				<option value ="Q5">Q5</option>
			</select>
		</li>
		<li><label>周期平均形式：</label>
			<select name="dealcodetype">
				<option value ="dealcodenum">户数</option>
 					<option value ="dealcodeamount">本金</option>
			</select>
		</li>
		<input id="demoBut" class="btn btn-primary" type="button" value="分案" />
	</ul>
	
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>催收员</th>
				<th>户数</th>
				<th>金额</th>
				<th>日期</th>
				<shiro:hasPermission name="dunning:demotable:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="demotable">
			<tr>
				<td><a href="${ctx}/dunning/demotable/form?id=${demotable.id}">
					${demotable.name}
				</a></td>
				<td>
					${demotable.dealcodenum}
				</td>
				<td>
					${demotable.dealcodeamount}
				</td>
				<td>
					<fmt:formatDate value="${demotable.datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="dunning:demotable:edit"><td>
    				<a href="${ctx}/dunning/demotable/form?id=${demotable.id}">修改</a>
					<a href="${ctx}/dunning/demotable/delete?id=${demotable.id}" onclick="return confirmx('确认要删除该徐盛吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
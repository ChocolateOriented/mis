<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>催收小组管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function distributeGroup() {
		var groups = [];
		$("input[name='groups']:checked").each(function() {
			groups.push($(this).attr('value'));
		});
		
		if (groups.length == 0) {
			$.jBox.tip("请勾选需分配的小组", "warning");
			return;
		}
		
		$.jBox.open("iframe:${ctx}/dunning/tMisDunningGroup/distribution?groups=" + groups, "分配机构" , 500, 280, {
            buttons: {},
            loaded: function (h) {
                $(".jbox-content", document).css("overflow-y", "hidden");
            }
     	});
	}
	
	function allGroup() {
		var checked = $("#allGroup").prop('checked');
		$("input[name='groups']").each(function() {
			$(this).prop('checked', checked);
		});
	}
	
	function resetDistribution() {
		var groups = [];
		$("input[name='groups']:checked").each(function() {
			groups.push($(this).attr('value'));
		});
		
		if (groups.length == 0) {
			$.jBox.tip("请勾选需重置的小组", "warning");
			return;
		}
		confirmx('确定要重置已分配的机构吗？', "${ctx}/dunning/tMisDunningGroup/resetDistribution?groups=" + groups);
	}
	
	function modifyOrganization() {
		$.jBox.open("iframe:${ctx}/dunning/tMisDunningOrganization/form?opr=edit", "重配机构" , 500, 280, {
            buttons: {},
            loaded: function (h) {
                $(".jbox-content", document).css("overflow-y", "hidden");
            }
     	});
	}

    function groupFormDialog(id) {
      var url = "${ctx}/dunning/tMisDunningGroup/form";
      var diaName = "催收添加";
      if (id != null && id!=""){
        url = "${ctx}/dunning/tMisDunningGroup/edit" + "?id="+id;
        diaName = "催收小组修改"
      }
      $.jBox.open("iframe:" + url, diaName , 500, 400, {
        buttons: {},
        loaded: function (h) {
          $(".jbox-content", document).css("overflow-y", "hidden");
        }
      });
    }

    function organizationFormDialog() {
      var url = "${ctx}/dunning/tMisDunningOrganization/form?opr=add";
      var diaName = "催收机构添加";
      $.jBox.open("iframe:" + url, diaName , 500, 280, {
        buttons: {},
        loaded: function (h) {
          $(".jbox-content", document).css("overflow-y", "hidden");
        }
      });
    }
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningGroup/">催收小组列表</a></li>
		<li><a onclick="groupFormDialog()">催收小组添加</a></li>
		<shiro:hasPermission name="dunning:TMisDunningOrganization:edit">
			<li><a onclick="organizationFormDialog()" >催收机构添加</a></li>
		</shiro:hasPermission>
	</ul>
	
	<form:form id="searchForm" modelAttribute="TMisDunningGroup" action="${ctx}/dunning/tMisDunningGroup/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li>
				<label>组名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li>
				<label>组长名：</label>
				<form:input path="leader.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li>
				<label>组类别：</label>
				<form:select id="groupTypes" path="type" class="input-medium">
					<form:option value="">全部</form:option>
					<form:options items="${groupTypes}" htmlEscape="false"/>
				</form:select>
			</li>
			<li>
				<label>所属机构：</label>
				<form:select id="organization" path="organization.id" class="input-medium">
					<form:option value="">全部</form:option>
					<form:options items="${organizations}" itemLabel="name" itemValue="id"/>
				</form:select>
			</li>
			<li>
				<label>监理：</label>
				<form:select id="supervisor" path="organization.supervisor.id" class="input-medium">
					<form:option value="">全部</form:option>
					<form:options items="${supervisors}" itemLabel="name" itemValue="id"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<shiro:hasPermission name="dunning:TMisDunningGroup:edit">
				<li class="btns">
					<input class="btn btn-primary" type="button" value="分配机构" onclick="distributeGroup();"/>
					<input class="btn btn-primary" type="button" value="重置机构" onclick="resetDistribution();"/>
				<shiro:hasPermission name="dunning:TMisDunningOrganization:edit">
					<input class="btn btn-primary" type="button" value="重配机构" onclick="modifyOrganization();"/>
				</shiro:hasPermission>
				</li>
			</shiro:hasPermission>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="dunning:TMisDunningGroup:edit">
				<th width="20px"><input type="checkbox" id="allGroup" onclick="allGroup();"/></th>
				</shiro:hasPermission>
				<th>组名</th>
				<th>组长名</th>
				<th>组类型</th>
				<th>机构</th>
				<th>监理</th>
				<shiro:hasPermission name="dunning:TMisDunningGroup:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisDunningGroup">
			<tr>
				<shiro:hasPermission name="dunning:TMisDunningGroup:edit">
				<td width="20px">
					<input type="checkbox" name="groups" value="${tMisDunningGroup.id}"/>
				</td>
				</shiro:hasPermission>
				<td>
					${tMisDunningGroup.name}
				</td>
				<td>
					${tMisDunningGroup.leader.name}
				</td>
				<td>
					${groupTypes[tMisDunningGroup.type]}
				</td>
				<td>
					${tMisDunningGroup.organization.name}
				</td>
				<td>
					${tMisDunningGroup.organization.supervisor.name}
				</td>
				<shiro:hasPermission name="dunning:TMisDunningGroup:edit">
				<td>
    				<a onclick="groupFormDialog('${tMisDunningGroup.id}')">修改</a>
					<a href="${ctx}/dunning/tMisDunningGroup/delete?id=${tMisDunningGroup.id}" onclick="return confirmx('确认要删除该催收小组吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
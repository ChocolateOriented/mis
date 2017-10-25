<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通话记录</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.form-row {
			margin-top : 7px;
			margin-bottom : 7px;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			//组与花名联动查询
			$("#groupList").on("change",function(){
				$("#peopleList").select2("val", null);
			});
			$("#peopleList").select2({
			    ajax: {
			        url: "${ctx}/dunning/tMisDunningPeople/optionList",
			        dataType: 'json',
			        quietMillis: 250,
			        data: function (term, page) {//查询参数 ,term为输入字符
			        	var groupId=$("#groupList").val(); 
			        	var param = {};
			        	if ("${supervisorLimit}" == "true") {
			        		param = {'group.id': groupId, 
				            		nickname: term};
			        	} else {
			        		param = {'group.id': groupId, 
				            		nickname: term};
			        	}
		            	return param;
			        },
			        results: function (data, page) {//选择要显示的数据
			        	return {results: data};
			        },
			        cache: true
			    },
		        multiple: true,
		        initSelection: function(element, callback) {//回显
		        	var ids = $(element).val().split(",");
		            if (ids == "") {
		            	return;
		            }
	            	//根据组查询选项
	                $.ajax("${ctx}/dunning/tMisDunningPeople/optionList", {
	                    data: function(){
	                    	var groupId = $("#groupList").val();     	
                    		return {groupId:groupId}             	
	                    },
	                    dataType: "json"
	                }).done(function(data) {
	                	var backData = [];
	                	var index = 0 ;
	                	for (var item in data) {
	                		//若回显ids里包含选项则选中
							if (ids.indexOf(data[item].id) > -1 ) {
								backData[index] = data[item] ;
								index++;
							}
						}
	                	callback(backData);
	                });
		        },
			    formatResult:formatPeopleList, //选择显示字段
			    formatSelection:formatPeopleList, //选择选中后填写字段
		        width:300
			});
			
			// 清空查询功能
			$("#empty").click(function(){
				window.location.href="${ctx}/dunning/tMisCallingRecord/list";
			}); 
		});
		
		//格式化peopleList选项
		function formatPeopleList(item) {
			var nickname = item.nickname;
			if (nickname == null || nickname == '') {
				nickname = "空" ;
			}
			return nickname;
		}
		
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action", "${ctx}/dunning/tMisCallingRecord/list");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisCallingRecord/">通话记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="tMisCallingRecord" action="${ctx}/dunning/tMisCallingRecord/" method="post" class="breadcrumb form-search"
		style="padding:2px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="container-fluid">
			<div class="row-fluid form-row">
				<div class="span2">
					<span>客户电话：</span>
					<input name="targetNumber" type="text" maxlength="20" class="input-medium" value="${tMisCallingRecord.targetNumber}"/>
				</div>
				<div class="span4">
					<span>呼叫时间：</span>
					<input name="beginCallTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${tMisCallingRecord.beginCallTime}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
					<input name="endCallTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${tMisCallingRecord.endCallTime}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
			</shiro:hasPermission>
			<div class="row-fluid form-row">
				<div class="span2">
					<span>催收小组：</span>
					<form:select id="groupList" path="dunningPeople.group.id" class="input-medium">
						<form:option value="">全部</form:option>
						<!-- 添加组类型为optgroup -->
						<c:forEach items="${groupTypes}" var="type">
							<optgroup label="${type.value}">
								<!-- 添加类型对应的小组 -->
								<c:forEach items="${groupList}" var="item">
									<c:if test="${item.type == type.key}">
										<option value="${item.id}" groupType="${item.type}" <c:if test="${tMisCallingRecord.dunningPeople.group.id == item.id }">selected="selected"</c:if>>${item.name}</option>
									</c:if>
								</c:forEach>
							</optgroup>
						</c:forEach>
					</form:select>
				</div>
				<div class="span4">
					<span>催款人：</span>
					<form:input id="peopleList" path="dunningPeople.queryIds" htmlEscape="false" type="hidden"/>
				</div>
			</div>
			<div class="row-fluid form-row">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<input id="empty" class="btn btn-primary" type="button" value="清空"/>
			</div>
		</div>
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
				<th>通话状态</th>
				<th>通话时长</th>
				<th>订单编号</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisCallingRecord" varStatus="vs">
			<tr>
				<td>
					${(vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					${tMisCallingRecord.callTimeText}
				</td>
				<td>
					${tMisCallingRecord.targetNumber}
				</td>
				<td>
					${tMisCallingRecord.callType.desc}
				</td>
				<td>
					${tMisCallingRecord.peopleName}
				</td>
				<td>
					${tMisCallingRecord.callingState}
				</td>
				<td>
					${tMisCallingRecord.durationTimeText}
				</td>
				<td>
					<a href="${ctx}/dunning/tMisCallingRecord/gotoTask?dealcode=${tMisCallingRecord.dealcode}" target="_blank" >
						${tMisCallingRecord.dealcode}
					</a>
				</td>
				<td style="padding:2px;">
					<c:if test="${not empty tMisCallingRecord.audioUrl}">
						<audio src="${ctiUrl}${tMisCallingRecord.audioUrl}" preload="none" controls="controls" controlsList="nodownload"></audio>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
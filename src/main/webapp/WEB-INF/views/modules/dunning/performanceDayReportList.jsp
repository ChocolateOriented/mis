<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收员工日报</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出催收员工日报数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/report/performanceDayReportExport");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			//组与花名联动查询
			$("#groupList").on("change",function(){
				$("#dunningPeople").select2("val", null);
			});
			
			$("#dunningPeople").select2({//
			    ajax: {
			        url: "${ctx}/dunning/tMisDunningPeople/optionList",
			        dataType: 'json',
			        quietMillis: 250,
			        data: function (term, page) {//查询参数 ,term为输入字符
			        	var groupId=$("#groupList").val(); 
		            	return {'group.id': groupId , name:term};
			        },
			        results: function (data, page) {//选择要显示的数据
			        	return { results: data };
			        },
			        cache: true
			    },
			    formatResult:formatPeopleList, //选择显示字段
			    formatSelection:formatPeopleList, //选择选中后填写字段
			    initSelection: function(element, callback) {//回显
			    	var id = $(element).val();
		            if (id == "") {
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
	                	for ( var item in data) {
	                		//若回显ids里包含选项则选中
							if (id == data[item].id) {
			                	callback(data[item])
			                	return ;
							}
						}
	                });
			    },
		        width:170
			});
			
		});
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/report/findPerformanceDayReport");
			$("#searchForm").submit();
        	return false;
        }

		//格式化peopleList选项
		function formatPeopleList( item ){
			var name = item.name ;
			if(name == null || name ==''){
				name = "空" ;
			}
			return name ;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/report/findPerformanceDayReport">催收员工日报</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="performanceDayReport" action="${ctx}/dunning/report/findPerformanceDayReport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		
			<li><label>时间：</label>
				<input name="datetimestart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${performanceDayReport.datetimestart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>-
				<input name="datetimeend" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${performanceDayReport.datetimeend}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			
			<li><label>催收队列：</label>
				<form:select path="dunningCycle" class="input-medium" >
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('dunningCycle1')}" itemLabel="label" itemValue="label" htmlEscape="false"/>
				</form:select>
			</li>
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
									<option value="${item.id}" <c:if test="${performanceDayReport.groupId == item.id }">selected="selected"</c:if>>${item.name}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</form:select></li>
			<li>			
			<li>
				<label>催收人：</label>
				<form:input id="dunningPeople" path="personnel" type="hidden" />
			</li>
			
			<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="clearfix"></li>
			
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>日期</th> 
				<th>催收队列</th>
				<th>催收员</th>
				<th>还款金额</th>
				<th>还清订单</th>
				<th>电话量</th>
				<th>短信量</th>
			</tr>

		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="performanceDay">
			<tr>	 
				<td> 
					<fmt:formatDate value="${performanceDay.datetimestart}" pattern="yyyy-MM-dd"/>-
					<fmt:formatDate value="${performanceDay.datetimeend}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
<%-- 					${performanceDay.begin}-${performanceDay.end} --%>
					${performanceDay.dunningCycle}
				</td>
				<td>
					${performanceDay.personnel}
				</td>
				<td>
					${performanceDay.payamountText}
				</td>
				<td>
					${performanceDay.payorder}
				</td>
				<td>
					${performanceDay.telnum}
				</td>
				<td>
					${performanceDay.smsnum}
				</td>
			</tr>
		</c:forEach>
		
		
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
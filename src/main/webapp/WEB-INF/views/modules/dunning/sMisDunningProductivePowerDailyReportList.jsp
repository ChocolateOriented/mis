<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收员案件活动日报管理</title>
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
				top.$.jBox.confirm("确认要导出催收员案件活动日报数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/report/productivePowerDailyReportExport");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			//组与花名联动查询
			$("#groupList").on("change",function(){
				$("#dunningPeopleName").select2("val", null);
			});
			
			$("#dunningPeopleName").select2({//
			    ajax: {
			        url: "${ctx}/dunning/tMisDunningPeople/optionList",
			        quietMillis: 250,
			        data: function (term, page) {//查询参数 ,term为输入字符
					  var groupId=$("#groupList").val();
					  var param = {};
                      if ("${groupLimit}" == "true") {
                        param = {'group.id': groupId,
                          'group.groupIds': groups.toString(),
                          name:term};
                      } else {
                        param = {'group.id': groupId,
                          name:term};
                      }
					  return param;
			        },
			        results: function (data, page) {//选择要显示的数据
			        	var resultsData = [] ;
                      	resultsData[0] = {id:null,name:"全部人员"};
			        	for (var i = 0; i < data.length; i++) {
			        		resultsData[i+1] = {id:data[i].name,name:data[i].name};
						}
			        	return { results: resultsData };
			        },
			        cache: true,
			    },
			    formatResult:formatPeopleList, //选择显示字段
			    formatSelection:formatPeopleList, //选择选中后填写字段
			    initSelection: function(element, callback) {//回显
		            var name=$(element).val();
		            if (name=="") {
		            	return;
		            }
		            callback({id:name,name:name})
			    },
		        width:170
			});
			
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/report/productivePowerDailyReport");
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
		<li class="active"><a href="${ctx}/dunning/report/productivePowerDailyReport">催收员案件活动日报列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="SMisDunningProductivePowerDailyReport" action="${ctx}/dunning/report/productivePowerDailyReport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>日期时间：</label>
				<input name="beginCreateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SMisDunningProductivePowerDailyReport.beginCreateTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> - 
				<input name="endCreateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${SMisDunningProductivePowerDailyReport.endCreateTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>案件队列：</label>
				<form:select path="taskCycle" class="input-medium" >
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
									<option value="${item.id}" <c:if test="${SMisDunningProductivePowerDailyReport.groupId == item.id }">selected="selected"</c:if>>${item.name}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</form:select></li>
			<li>
			<li>
				<label>催收人：</label>
				<form:input path="dunningPeopleName" type="hidden" />
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>日期时间</th>
				<th>催收员姓名</th>
				<th>催收员队列</th>
				<th>案件队列</th>
				<th>客户数</th>
				<th>逾期总额</th>
				<th>本金总额</th>
				<th>处理账户数</th>
				<th>处理次数</th>
				<th>有效账户数</th>
				<th>承诺还款户数</th>
				<th>承诺还款</th>
				<th>承诺还款本金</th>
				<th>有效处理占比_户数</th>
				<th>有效处理占比_逾期金额</th>
				<th>有效处理占比_本金</th>
				<th>承诺还款占比_户数</th>
				<th>承诺还款占比_逾期金额</th>
				<th>承诺还款占比_本金</th>
				<th>还款户数</th>
				<th>还款的总本金</th>
				<th>还款总金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sMisDunningProductivePowerDailyReport">
			<tr>
				<td>
					<fmt:formatDate value="${sMisDunningProductivePowerDailyReport.createTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.dunningPeopleName}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.dunningCycle}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.taskCycle}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.totalUser}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.overdueAmount}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.corpusAmount}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.dealUser}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.dealNumber}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.effectiveUser}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.ptpUser}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.ptpAmount}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.ptpCorpusAmount}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.effectiveUserPercent}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.effectiveAmountPercent}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.effectiveCorpusAmountPercent}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.ptpUserPercent}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.ptpAmountPercent}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.ptpCorpusAmountPerent}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.payoffUser}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.payoffCorpusAmount}
				</td>
				<td>
					${sMisDunningProductivePowerDailyReport.payoffAmount}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
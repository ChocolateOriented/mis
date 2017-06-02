<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			 $("a").click(function(){
				$("a").css("color","");
				$(this).css("color","#FF8C00");
			 }); 
			
			// 清空查询功能
			 $("#empty").click(function(){
// 				 $('#searchForm')[0].reset();  
        		 window.location.href="${ctx}/dunning/tMisDunningOuterTask/oldfindOrderPageList";
			 }); 
				
			//组与花名联动查询
			$("#groupList").on("change",function(){
				$("#peopleList").select2("val", null);
			});
			$("#peopleList").select2({//
			    ajax: {
			        url: "${ctx}/dunning/tMisDunningPeople/optionList",
			        dataType: 'json',
			        quietMillis: 250,
			        data: function (term, page) {//查询参数 ,term为输入字符
			        	var groupId=$("#groupList").val(); 
		            	return {'group.id': groupId , nickname:term};
			        },
			        results: function (data, page) {//选择要显示的数据
			        	return { results: data };
			        },
			        cache: true
			    },
		        multiple: true,
		        initSelection: function(element, callback) {//回显
		        	var ids=$(element).val();
		            if (ids=="") {
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
	                	for ( var item in data) {
	                		//若回显ids里包含选项则选中
							if (ids.indexOf(data[item].id) > -1 ) {
								backData[index] = data[item] ;
								index++;
							}
						}
	                	callback(backData)
	                });
		        },
			    formatResult:formatPeopleList, //选择显示字段
			    formatSelection:formatPeopleList, //选择选中后填写字段
		        width:300
			});
			
			 		
			 $("#allorder").change(function(){
			 	if($("#allorder").prop('checked')){
			 		$("[name='orders']").each(function(){
				        $(this).prop('checked',true);
				    });
			 	}else{
			 		$("[name='orders']").each(function(){
						if(this.checked){
				        	$(this).prop('checked',false);
				        }
				    });
			 	}
			 });
			 
			 
			//获取代扣渠道成功率
			$.get("${ctx}/dunning/tMisDunningDeduct/getSuccessRateByChannel", {}, function(data) {
				if (data) {
					var successRateInfo = "代扣成功率：";
					for (var i = 0; i < data.length; i++) {
						var rate = (!data[i].successrate && data[i].successrate !== 0) ? '-' : data[i].successrate.toFixed(2) + "%";
						successRateInfo += data[i].channelname + "(" + rate + ") ";
					}
					$("#successRate").text(successRateInfo);
				}
			});
		});
		
		//参数单位：毫秒    输出单位：天（向上取整）
		function dateDiff(startDate, endDate){
			return Math.ceil((endDate-startDate)/1000/3600/24);
		}
		
		// 查询
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/tMisDunningOuterTask/oldfindOrderPageList");
			$("#searchForm").submit();
        	return false;
        }
		
		// 订单编号跳转
		function ckFunction(obj){
			$(obj).css("color","#FF8C00");
        }
		
		//格式化peopleList选项
		function formatPeopleList( item ){
			var nickname = item.nickname ;
			if(nickname == null || nickname ==''){
				nickname = "空" ;
			}
			return nickname ;
		}
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningOuterTask/oldfindOrderPageList">催收任务列表</a></li>
		<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
			<span id="successRate" style="float:right;padding:8px;"></span>
		</shiro:hasPermission>
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="dunningOrder" action="${ctx}/dunning/tMisDunningOuterTask/oldfindOrderPageList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
<%-- 		<shiro:lacksPermission name="dunning:tMisDunningTask:leaderview"> --%>
<%-- 		</shiro:lacksPermission> --%>

		<ul class="ul-form">
			<li><label>姓名</label>
				<form:input path="realname"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>手机号</label>
				<form:input path="mobile"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>订单号</label>
				<form:input path="dealcode"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>催收备注</label>
				<form:input path="telremark"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>订单状态</label>
			<form:select  id="status" path="status" class="input-medium">
				<form:option selected="selected" value="" label="全部状态"/>
				<form:option value="payoff" label="已还清"/>
				<form:option value="payment" label="未还清"/>
			</form:select>
			</li>
			
			<li><label>逾期天数</label>
				<form:input  path="beginOverduedays"  htmlEscape="false" maxlength="3" class="digits"  style="width:35px;"  />
				- 
				<form:input  path="endOverduedays"  htmlEscape="false" maxlength="3" class="digits" style="width:35px;"   />
			</li>
			
			<li><label>还清日期</label>
				<input name="beginPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginPayofftime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endPayofftime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>到期还款</label>
				<input name="beginRepaymenttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginRepaymenttime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endRepaymenttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endRepaymenttime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			

				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
				<li>
				<label >导出批次</label>
					<input name="beginOuterfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${dunningOrder.beginOuterfiletime}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
					<input name="endOuterfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${dunningOrder.endOuterfiletime}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</li>
				<li>
					<label>催收小组：</label>
					<form:select id="groupList" path="dunningPeople.group.id" class="input-medium">
					<form:option value="">全部</form:option>
						<!-- 添加组类型为optgroup -->
						<c:forEach items="${groupTypes}" var="type">
							<optgroup label="${type.value}">
								<!-- 添加类型对应的小组 -->
								<c:forEach items="${groupList}" var="item">
									<c:if test="${item.type == type.key}">
										<option value="${item.id}" groupType="${item.type}" <c:if test="${dunningOrder.dunningPeople.group.id == item.id }">selected="selected"</c:if>>${item.name}</option>
									</c:if>
								</c:forEach>
							</optgroup>
						</c:forEach>
					</form:select></li>
				<li>
				<li>
					<label >催款人</label>
					<form:input id="peopleList" path="dunningPeople.queryIds" htmlEscape="false" type="hidden"/>
				</li>
				</shiro:hasPermission>
				
						
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page();"/></li>
				<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>

				<shiro:hasPermission name="dunning:tMisDunningTask:exportFile"> 
					<li class="btns"><input id="outerOrders" name="outerOrders"  type="hidden" value="" />
					<input id="exportOuterFile" class="btn btn-primary" type="button" value="导出" /></li>
				</shiro:hasPermission> 
				<li class="clearfix"></li>
			</ul>
	</form:form>
	

	<!-- test start -->
	<shiro:hasPermission name="dunning:tMisDunningTest:view">
		<a href="${ctx}/dunning/tMisDunningTest/testPage" target="_blank" >test</a>
	</shiro:hasPermission>
	<!-- test end -->
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder"  /></th>
				<th>序号</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>欠款金额</th>
				<th>应催金额</th>
				<th>到期还款日期</th>
				<th>逾期天数</th>
				<th>订单状态</th>
				<th>催收备注</th>
				<th>催收人</th>
				<!-- 催收留案功能-催收截止日 Patch 0001 by GQWU at 2016-11-9 start-->
				<th>催收截止日期</th>
				<!-- 催收留案功能-催收截止日 Patch 0001 by GQWU at 2016-11-9 end-->
				<th>最近催收</th>
				<th>还清日期</th>
				<th>订单编号</th>
				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
					<th>导出批次</th>
				</shiro:hasPermission>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dunningOrder" varStatus="vs">
			<tr>
				<td>
					<input type="checkbox" id="${dunningOrder.dealcode}#${vs.index}" name="orders" dunningcycle="${dunningOrder.dunningcycle}" title="${dunningOrder.dunningtaskdbid}" repaymenttime="${dunningOrder.repaymenttime}" dunningType="${dunningOrder.dunningpeopletype}" overDuedays="${dunningOrder.overduedays}" orders="${dunningOrder.dealcode}" outerOrders="${dunningOrder.dealcode}=${dunningOrder.dunningpeoplename}" deadline="${dunningOrder.deadline}" value="${dunningOrder.dealcode}#${dunningOrder.creditamount}#${dunningOrder.overduedays}#${dunningOrder.mobile}"/>
				</td>
				<td>
					${ (vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					<%-- <a href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}" target="_blank" > --%>
					<a href="${ctx}/dunning/tMisDunningTask/pageFather?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}&status=${dunningOrder.status}" target="_blank" >
					${dunningOrder.realname}
				</td>
				
				<td>
					${dunningOrder.mobile}
				</td>
				<td>
					${dunningOrder.amountText}
				</td>
				<td>
					${dunningOrder.creditamountText}
				</td>
				<td>
					<fmt:formatDate value="${dunningOrder.repaymenttime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${dunningOrder.overduedays}
				</td>
				<td>
					${dunningOrder.statusText}
				</td>
				<td title="${dunningOrder.telremark}">
					<c:choose>  
						<c:when test="${fn:length(dunningOrder.telremark) > 15}">  
							<c:out value="${fn:substring(dunningOrder.telremark, 0, 15)}..." />
						</c:when>
						<c:otherwise>
							<c:out value="${dunningOrder.telremark}" />
						</c:otherwise>  
					</c:choose>
				</td>
				
				<td>
					${dunningOrder.dunningPeople.nickname}
				</td>
				
				<!-- 催收留案功能-催收截止日 Patch 0001 by GQWU at 2016-11-9 start-->
				<td>
					<fmt:formatDate value="${dunningOrder.deadline}" pattern="yyyy-MM-dd"/>
				</td>
				<!-- 催收留案功能-催收截止日 Patch 0001 by GQWU at 2016-11-9 end-->
				
				<td>
					<fmt:formatDate value="${dunningOrder.dunningtime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				
				<td>
					<fmt:formatDate value="${dunningOrder.payofftime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${dunningOrder.dealcode}
				</td>
				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
					<td>
						<fmt:formatDate value="${dunningOrder.outerfiletime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</shiro:hasPermission>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
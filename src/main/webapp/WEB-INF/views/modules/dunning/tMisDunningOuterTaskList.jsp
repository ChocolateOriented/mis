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
        		 window.location.href="${ctx}/dunning/tMisDunningOuterTask/findOrderPageList";
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
			
			 // 普通导出功能
			 $("#dunningExport").click(function(){
				top.$.jBox.confirm("确认要导出列表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/tMisDunningOuterTask/exportFile");
						$("#searchForm").submit();
					}
				},
				{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			 });
			 
			// 委外导出功能
			 $("#exportOuterFile").click(function(){
				var outerOrders = new Array();
				$("[name='orders']").each(function() {
					if(this.checked){
						outerOrders.push($(this).attr("outerOrders"));
					}
				});
				if(outerOrders.length==0){
					$.jBox.tip("请勾选导出订单号", 'warning');
					return;
				}
// 				alert(outerOrders);
				$("#outerOrders").val(outerOrders);
				top.$.jBox.confirm("确认要导出列表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/tMisDunningOuterTask/exportOuterFile");
		// 				loading('正在提交导出shuju');
						$("#searchForm").submit();
					}
				},
				{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
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
			 
			// 群发短信
			 $("#texting").click(function(){
					var orders = new Array();
					var overDuedays = new Array();
					$("[name='orders']").each(function() {
						if(this.checked){
// 							orders.push($(this).val());
							orders.push($(this).attr("orders"));
							overDuedays.push($(this).attr("overDuedays"));
						}
					});
					
					//群发短信必须选择处于同一逾期周期用户的判断
					for(var i = 0; i < overDuedays.length; i++){
						var num = overDuedays[i];
						if(parseInt(num)<parseInt(0) || parseInt(num) == parseInt(0)){
							overDuedays[i] = 'Z';
						}else if(parseInt(num)>parseInt(0) && parseInt(num)<parseInt(8)){
							overDuedays[i] = 'A';
						}else if(parseInt(num)>parseInt(7) && parseInt(num)<parseInt(15)){
							overDuedays[i] = 'B';
						}else if(parseInt(num)>parseInt(14) && parseInt(num)<parseInt(22)){
							overDuedays[i] = 'C';
						}else if(parseInt(num)>parseInt(21) && parseInt(num)<parseInt(36)){
							overDuedays[i] = 'D';
						}else if(parseInt(num)>parseInt(35)){
							overDuedays[i] = 'E';
						}
						if(i > 0){
							if(overDuedays[i] != overDuedays[i-1]){
								$.jBox.tip("群发短信必须选择处于同一逾期周期用户！", 'warning');
								return false;
							}
						}
					}
					
					if(orders.length==0){
						$.jBox.tip("请勾选发送短信的催收订单", 'warning');
						return;
					}
					var url = "${ctx}/dunning/tMisDunningOuterTask/collectionGroupSms";
								$.jBox.open("iframe:" + url, "群发短信" , 600, 350, {            
					               buttons: {},
//		 			                   submit: function (v, h, f) {
//				 	                       if (v == "ok") {
//				 	                           var iframeName = h.children(0).attr("name");
//				 	                           var iframeHtml = window.frames[iframeName];               //获取子窗口的句柄
//				 	                           iframeHtml.saveOrUpdate();
//				 	                           return false;
//				 	                       }
//		 			                   },
					               loaded: function (h) {
					                   $(".jbox-content", document).css("overflow-y", "hidden");
					               }
					         });
			 }); 
			 
			// 手动分配
			 $("#distribution").click(function(){
				 var orders = new Array();
				 var dunningcycle = new Array();
					$("[name='orders']").each(function() {
						if(this.checked){
							if("" == $(this).attr("orders") || "" == $(this).attr("dunningcycle")){
								$.jBox.tip("数据异常,需先将数据补充完整", 'warning');
								return;
							}
							orders.push($(this).attr("orders"));
							dunningcycle.push($(this).attr("dunningcycle"));
// 							overduedays.push($(this).attr("overDuedays"));
						}
					});
					if(orders.length==0){
						$.jBox.tip("请选择需要移动的案件", 'warning');
						return;
					}
// 					if(orders.length>300){
// 						$.jBox.tip("请选择小于300条分配订单", 'warning');
// 						return;
// 					}
					var uniqueid = unique(dunningcycle);
					if(uniqueid.length != 1 ){
						$.jBox.tip("请选择同队列的案件", 'warning');
						return;
					}
					var url = "${ctx}/dunning/tMisDunningOuterTask/dialogOutDistribution?dunningcycle=" + uniqueid;
					$.jBox.open("iframe:" + url, "手动分配" , 600, 350, {            
			               buttons: {},
			               loaded: function (h) {
			                   $(".jbox-content", document).css("overflow-y", "hidden");
			               }
			         });
					
			 });
			
			 function unique(arr) {
			    var result = [], hash = {};
			    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
			        if (!hash[elem]) {
			            result.push(elem);
			            hash[elem] = true;
			        }
			    }
			    return result;
			}
			 
			// 自动分配
			 $("#automatic").click(function(){
				 $("#automatic").attr('disabled',"true");
				 $.ajax({
	                  type: 'POST',
	                  url : "${ctx}/dunning/tMisDunningOuterTask/autoAssign",
	                  success : function(data) {
	                      if (data == "OK") {
	                          alert("分配成功");
// 	                    	  $.jBox.tip("ok", 'info');
	                      } else {
	                          alert("分配失败:"+data.message);
// 	                    	  $.jBox.tip("error", 'warning');
	                      }
	                      $("#automatic").removeAttr("disabled"); 
	                  },
	                  error : function(XMLHttpRequest, textStatus, errorThrown){
	                     alert("保存失败:"+textStatus);
	                  }
	              });
				
			 });
			
			// 新任务添加
			 $("#autoAssignNewOrder").click(function(){
// 				 $("#qqq").attr('disabled',"true");
				 $.ajax({
	                  type: 'POST',
	                  url : "${ctx}/dunning/tMisDunningOuterTask/autoAssignNewOrder",
	                  success : function(data) {
	                      if (data == "OK") {
	                          alert("新任务添加成功");
// 	                    	  $.jBox.tip("ok", 'info');
	                      } else {
	                          alert("新任务添加失败:"+data.message);
// 	                    	  $.jBox.tip("error", 'warning');
	                      }
// 	                      $("#automatic").removeAttr("disabled"); 
	                  },
	                  error : function(XMLHttpRequest, textStatus, errorThrown){
	                     alert("新任务添加失败:"+textStatus);
	                  }
	              });
				
			 });
			
			// 过期任务自动分配
			 $("#autoRepayment").click(function(){
// 				 $("#qqq").attr('disabled',"true");
				 $.ajax({
	                  type: 'POST',
	                  url : "${ctx}/dunning/tMisDunningOuterTask/autoRepayment",
	                  success : function(data) {
	                      if (data == "OK") {
	                          alert("过期任务自动分配成功");
// 	                    	  $.jBox.tip("ok", 'info');
	                      } else {
	                          alert("过期任务自动分配失败:"+data.message);
// 	                    	  $.jBox.tip("error", 'warning');
	                      }
// 	                      $("#automatic").removeAttr("disabled"); 
	                  },
	                  error : function(XMLHttpRequest, textStatus, errorThrown){
	                     alert("保存失败:"+textStatus);
	                  }
	              });
				
			 });
			 
			
			
			
			// 催收留案功能-留案自检 Patch 0001 by GQWU at 2016-11-9 start-->
			 $("#deferDunningDeadline").click(function(){
// 				 var dealcodes = new Array();
				 
// 				 $("[name='orders']").each(function() {
// 					 if(this.checked){
// 						 dealcodes.push($(this).attr("orders"));
// 					 }
// 				 });
				 
// 				 if(dealcodes.length==0){
// 						$.jBox.tip("请勾选留案订单", 'warning');
// 						return;
// 				 }
					
// 				 var url = "${ctx}/dunning/tMisDunningOuterTask/dialogDeferDunningDeadline?dealcodes=" + dealcodes;
// 				 $.jBox.open("iframe:" + url, "催收留案" , 600, 350, {            
// 			               buttons: {},
// 			               loaded: function (h) {
// 			                   $(".jbox-content", document).css("overflow-y", "hidden");
// 			               }
// 			     });
					
			 });
			 // 催收留案功能-留案触发按钮 Patch 0001 by GQWU at 2016-11-9 end-->
			 
			// 催收留案功能-委外订单截止日期修改按钮 Patch 0001 by GQWU at 2016-11-25 start-->
			 $("#setOuterOrdersDeadline").click(function(){
// 				 var dealcodes = new Array();
				 
// 				 $("[name='orders']").each(function() {
// 					 if(this.checked){
// 						 dealcodes.push($(this).attr("orders"));
// 					 }
// 				 });
				 
// 				 if(dealcodes.length==0){
// 						$.jBox.tip("请勾选委外订单", 'warning');
// 						return;
// 				 }
					
// 				 var url = "${ctx}/dunning/tMisDunningOuterTask/dialogSetOuterOrdersDeadline?dealcodes=" + dealcodes;
// 				 $.jBox.open("iframe:" + url, "委外订单截止日期修改" , 600, 350, {            
// 			               buttons: {},
// 			               loaded: function (h) {
// 			                   $(".jbox-content", document).css("overflow-y", "hidden");
// 			               }
// 			     });
					
			 });
			 // 催收留案功能-委外订单截止日期修改按钮 Patch 0001 by GQWU at 2016-11-25 end-->
				
		});
		
		//参数单位：毫秒    输出单位：天（向上取整）
		function dateDiff(startDate, endDate){
			return Math.ceil((endDate-startDate)/1000/3600/24);
		}
		
		// 查询
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/tMisDunningOuterTask/findOrderPageList");
			$("#searchForm").submit();
        	return false;
        }
		
		// 订单编号跳转
		function ckFunction(obj){
			$(obj).css("color","#FF8C00");
// 			$(obj).addClass
// 			backgroudcolor=Red;
// 			var buyerId = $(obj).attr("buyerid");
// 			var dealcode = $(obj).attr("dealcode");
//         	window.location.href="${ctx}/dunning/tMisDunningOuterTask/customerDetails?buyerId="+ buyerId + "&dealcode="+ dealcode;
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
		<li class="active"><a href="${ctx}/dunning/tMisDunningOuterTask/findOrderPageList">催收任务列表</a></li>
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="dunningOrder" action="${ctx}/dunning/tMisDunningOuterTask/findOrderPageList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

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
<!-- 			<li><label>催收备注</label> -->
<%-- 				<form:input path="telremark"  htmlEscape="false" maxlength="128" class="input-medium"/> --%>
<!-- 			</li> -->
			
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
			
<!-- 			<li><label>任务状态</label> -->
<%-- 			<form:select id="dunningtaskstatus"   path="dunningtaskstatus"  class="input-medium"> --%>
<%-- 				<form:option selected="selected" value="" label="全部状态"/> --%>
<%-- 				<form:option value="dunning" label="正在催收"/> --%>
<%-- 				<form:option value="expired" label="超出催收周期未催回"/> --%>
<%-- 				<form:option value="finished" label="催收周期内已还清"/> --%>
<%-- 				<form:option value="transfer" label="同期转移"/> --%>
<%-- 			</form:select></li> --%>
			
<!-- 			</ul> -->
<!-- 			<ul class="ul-form"> -->
			<li><label>分案日期</label>
				<input name="beginOutsourcingBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginOutsourcingBeginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endOutsourcingBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endOutsourcingBeginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>催收截止</label>
				<input name="beginOutsourcingEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginOutsourcingEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endOutsourcingEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endOutsourcingEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>还清日期</label>
				<input name="beginPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginPayofftime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endPayofftime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
<!-- 			<li><label>到期还款</label> -->
<!-- 				<input name="beginRepaymenttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${dunningOrder.beginRepaymenttime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至  -->
<!-- 				<input name="endRepaymenttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${dunningOrder.endRepaymenttime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -->
<!-- 			</li> -->
			
<!-- 				<li> -->
<!-- 				<label >导出批次</label> -->
<!-- 				<input name="outerfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${dunningOrder.outerfiletime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -->
<!-- 					<input name="beginOuterfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 						value="<fmt:formatDate value="${dunningOrder.beginOuterfiletime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至  -->
<!-- 					<input name="endOuterfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 						value="<fmt:formatDate value="${dunningOrder.endOuterfiletime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -->
<!-- 				</li> -->
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
					<label>催收队列：</label>
					<form:select path="dunningcycle" class="input-medium">
						<form:option value="">全部</form:option>
						<form:option value="Q4">Q4</form:option>
						<form:option value="Q5">Q5</form:option>
					</form:select></li>
				<li>
				<li>
					<label >催款人</label>
					<input id="peopleList" name="dunningPeople.queryIds" 
					<c:if test="${fn:length(dunningOrder.dunningPeople.queryIds)>0}">
						value="${dunningOrder.dunningPeople.queryIds}"
					</c:if>
					type="hidden" />
				</li>
				
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page();"/></li>
				<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
<!-- 					<li class="btns"><input id="outerOrders" name="outerOrders"  type="hidden" value="" /> -->
<!-- 					<input id="exportOuterFile" class="btn btn-primary" type="button" value="导出" /></li> -->
<!-- 				<li class="clearfix"></li> -->
			</ul>
	</form:form>
	<input id="distribution"  class="btn btn-primary" type="button" value="手动分配" />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder"  /></th>
				<th>序号</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>欠款金额</th>
				<th>应催金额</th>
				<th>还款总额</th>
				<th>到期还款日期</th>
				<th>逾期天数</th>
				<th>订单状态</th>
<!-- 				<th>催收备注</th> -->
				<th>催收人</th>
				<th>分案日期</th>
				<th>催收截止日期</th>
				<th>最近催收时间</th>
				<th>还清日期</th>
				<th>订单编号</th>
<!-- 				<th>导出批次</th> -->
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
					<a href="${ctx}/dunning/tMisDunningOuterTask/pageFather?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}" target="_blank" >
					${dunningOrder.realname}
					</a>
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
					${dunningOrder.balance}
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
<%-- 				<td title="${dunningOrder.telremark}"> --%>
<%-- 					<c:choose>   --%>
<%-- 						<c:when test="${fn:length(dunningOrder.telremark) > 15}">   --%>
<%-- 							<c:out value="${fn:substring(dunningOrder.telremark, 0, 15)}..." /> --%>
<%-- 						</c:when> --%>
<%-- 						<c:otherwise> --%>
<%-- 							<c:out value="${dunningOrder.telremark}" /> --%>
<%-- 						</c:otherwise>   --%>
<%-- 					</c:choose> --%>
<!-- 				</td> -->
				
				<td>
					${dunningOrder.dunningPeople.nickname}
				</td>
				<td>
					<fmt:formatDate value="${dunningOrder.outsourcingBeginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${dunningOrder.outsourcingEndDate}" pattern="yyyy-MM-dd"/>
				</td>
				
				<td>
					<fmt:formatDate value="${dunningOrder.dunningtime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				
				<td>
					<fmt:formatDate value="${dunningOrder.payofftime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${dunningOrder.dealcode}
				</td>
<!-- 				<td> -->
<%-- 					<fmt:formatDate value="${dunningOrder.outerfiletime}" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
<!-- 				</td> -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
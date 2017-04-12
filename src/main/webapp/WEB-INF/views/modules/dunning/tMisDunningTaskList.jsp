<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
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
        		 window.location.href="${ctx}/dunning/tMisDunningTask/findOrderPageList";
			 }); 
			 
			 // 普通导出功能
			 $("#dunningExport").click(function(){
				top.$.jBox.confirm("确认要导出列表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/tMisDunningTask/exportFile");
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
						$("#searchForm").attr("action","${ctx}/dunning/tMisDunningTask/exportOuterFile");
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
					var url = "${ctx}/dunning/tMisDunningTask/collectionGroupSms";
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
						$.jBox.tip("请勾选分配订单", 'warning');
						return;
					}
					var uniqueid = unique(dunningcycle);
					if(uniqueid.length != 1 ){
						$.jBox.tip("请勾选同一催收队列订单", 'warning');
						return;
					}
					alert(orders);
					alert(uniqueid);
					var url = "${ctx}/dunning/tMisDunningTask/dialogDistribution?orders=" + orders + "&dunningcycle=" + uniqueid;
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
	                  url : "${ctx}/dunning/tMisDunningTask/autoAssign",
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
	                  url : "${ctx}/dunning/tMisDunningTask/autoAssignNewOrder",
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
	                  url : "${ctx}/dunning/tMisDunningTask/autoRepayment",
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
					
// 				 var url = "${ctx}/dunning/tMisDunningTask/dialogDeferDunningDeadline?dealcodes=" + dealcodes;
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
					
// 				 var url = "${ctx}/dunning/tMisDunningTask/dialogSetOuterOrdersDeadline?dealcodes=" + dealcodes;
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
			$("#searchForm").attr("action","${ctx}/dunning/tMisDunningTask/findOrderPageList");
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
//         	window.location.href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId="+ buyerId + "&dealcode="+ dealcode;
        }
		
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningTask/findOrderPageList">催收任务列表</a></li>
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="dunningOrder" action="${ctx}/dunning/tMisDunningTask/findOrderPageList" method="post" class="breadcrumb form-search">
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
			
<!-- 			</ul> -->
<!-- 			<ul class="ul-form"> -->
				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
				<li>
				<label >导出批次</label>
<!-- 				<input name="outerfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" -->
<%-- 					value="<fmt:formatDate value="${dunningOrder.outerfiletime}" pattern="yyyy-MM-dd"/>" --%>
<!-- 					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -->
					<input name="beginOuterfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${dunningOrder.beginOuterfiletime}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
					<input name="endOuterfiletime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${dunningOrder.endOuterfiletime}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</li>
				<li>
					<label >催款人</label>
					<form:select id="names"  path="names" multiple="multiple"   style="width: 375px ;">
						<form:options items="${dunningPeoples}" itemLabel="name" itemValue="name" htmlEscape="false" />
					</form:select>
	<!-- 				<div class='multi_select' style="width:180px !important;"></div> -->
<%-- 					<form:select id="dunningpeoplename"  path="dunningpeoplename" class="input-medium"> --%>
<%-- 						<form:option selected="selected" value="" label="全部人员"/> --%>
<%-- 						<form:options items="${dunningPeoples}" itemLabel="name" itemValue="name" htmlEscape="false"/> --%>
<%-- 					</form:select> --%>
				</li>
				</shiro:hasPermission>
				
						
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page();"/></li>
				<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
<%-- 				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">  --%>
<!-- 					<li class="btns"><input id="dunningExport" class="btn btn-primary" type="button" value="导出列表" /></li> -->
<%-- 				</shiro:hasPermission>  --%>
				<shiro:hasPermission name="dunning:tMisDunningTask:exportFile"> 
					<li class="btns"><input id="outerOrders" name="outerOrders"  type="hidden" value="" />
					<input id="exportOuterFile" class="btn btn-primary" type="button" value="导出" /></li>
				</shiro:hasPermission> 
				<li class="clearfix"></li>
			</ul>
	</form:form>
	
	<shiro:hasPermission name="dunning:tMisDunningTask:Commissionerview">
		<input id="texting"   class="btn btn-primary" type="button" value="群发短信"/>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:directorview">
		<input id="distribution"  class="btn btn-primary" type="button" value="手动分配" />
	</shiro:hasPermission>
	
	<!-- 催收留案功能-留案触发按钮 Patch 0001 by GQWU at 2016-11-9 start-->
	<shiro:hasPermission name="dunning:tMisDunningTask:directorview">
		<input id="deferDunningDeadline"  class="btn btn-primary" type="button" value="催收留案"  disabled="disabled"/>
	</shiro:hasPermission>
	<!-- 催收留案功能-留案触发按钮 Patch 0001 by GQWU at 2016-11-9 end-->
	
	<!-- 催收留案功能-委外订单截止日期设置按钮 Patch 0001 by GQWU at 2016-11-9 start-->
	<shiro:hasPermission name="dunning:tMisDunningTask:directorview">
		<input id="setOuterOrdersDeadline"  class="btn btn-primary" type="button" value="委外订单截止日期设置"  disabled="disabled"/>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="dunning:tMisDunningTask:adminview">
		<input id="automatic"  class="btn btn-primary" type="button" value="自动分配"/>
		<input id="autoAssignNewOrder"  class="btn btn-primary" type="button" value="新订单任务"/>
		<input id="autoRepayment"  class="btn btn-primary" type="button" value="扫描还款"/>
	</shiro:hasPermission>
	<!-- 催收留案功能-委外订单截止日期设置按钮 Patch 0001 by GQWU at 2016-11-9 end-->

<%-- 		<form id="searchForm"  action="${ctx}/dunning/tMisDunningTask/exportOuterFile" method="post"> --%>
<%-- 			<shiro:hasPermission name="dunning:tMisDunningTask:exportFile">  --%>
<!-- 					<input id="exportOuterFile" class="btn btn-primary" type="button" value="委外导出" /> -->
<%-- 			</shiro:hasPermission>  --%>
<!-- 		</form> -->
		
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
<!-- 				<th>任务状态</th> -->
<!-- 				<th>操作</th> -->
<%-- 				<shiro:hasPermission name="dunning:tMisDunningTask:edit"><th>操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dunningOrder" varStatus="vs">
			<tr>
				<td>
					<input type="checkbox" name="orders" dunningcycle="${dunningOrder.dunningcycle}" title="${dunningOrder.dunningtaskdbid}" repaymenttime="${dunningOrder.repaymenttime}" dunningType="${dunningOrder.dunningpeopletype}" overDuedays="${dunningOrder.overduedays}" orders="${dunningOrder.dealcode}" outerOrders="${dunningOrder.dealcode}=${dunningOrder.dunningpeoplename}" deadline="${dunningOrder.deadline}" value="${dunningOrder.dealcode}#${dunningOrder.creditamount}#${dunningOrder.overduedays}#${dunningOrder.mobile}"/>
				</td>
				<td>
					${ (vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					<%-- <a href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}" target="_blank" > --%>
					<a href="${ctx}/dunning/tMisDunningTask/pageFather?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}" target="_blank" >
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
					${dunningOrder.dunningpeoplename}
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
<%-- 					<a href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}"   > --%>
					${dunningOrder.dealcode}
<!-- 					</a> -->
				</td>
				<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
					<td>
						<fmt:formatDate value="${dunningOrder.outerfiletime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</shiro:hasPermission>
				
<!-- 				<td> -->
<%-- 					${dunningOrder.dunningtaskstatusText} --%>
<!-- 				</td> -->
<%-- 				<shiro:hasPermission name="dunning:tMisDunningTask:edit"><td> --%>
<%--     				<a href="${ctx}/dunning/tMisDunningTask/form?id=${tMisDunningTask.id}">修改</a> --%>
<%-- 					<a href="${ctx}/dunning/tMisDunningTask/delete?id=${tMisDunningTask.id}" onclick="return confirmx('确认要删除该催收任务吗？', this.href)">删除</a> --%>
<%-- 				</td></shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
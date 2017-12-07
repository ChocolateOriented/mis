<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>催收任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var groups = [];
			if ("${supervisorLimit}" == "true") {
				var groupOptions = $("#groupList")[0].options;
				for (var i = 0; i < groupOptions.length; i++) {
					if (groupOptions[i].value) {
						groups.push(groupOptions[i].value);
					}
				}
			}
			
			 $("a").click(function(){
				$("a").css("color","");
				$(this).css("color","#FF8C00");
			 }); 
			
			// 清空查询功能
			 $("#empty").click(function(){
// 				 $('#searchForm')[0].reset();  
        		 window.location.href="${ctx}/dunning/tMisDunningTask/findOrderPageList";
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
			        	var param = {};
			        	if ("${supervisorLimit}" == "true") {
			        		param = {'group.id': groupId, 
				            		'group.groupIds': groups.toString(),
				            		nickname:term};
			        	} else {
			        		param = {'group.id': groupId, 
				            		nickname:term};
			        	}
		            	return param;
			        },
			        results: function (data, page) {//选择要显示的数据
			        	return { results: data };
			        },
			        cache: true
			    },
		        multiple: true,
		        initSelection: function(element, callback) {//回显
		        	var ids=$(element).val().split(",");
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
						outerOrders.push($(this).attr("orders"));
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
			 
			// 手动分案
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
						}
					});
					if(orders.length==0){
						$.jBox.tip("请选择需要移动的案件", 'warning');
						return;
					}

					var uniqueid = unique(dunningcycle);
					if(uniqueid.length != 1 ){
						$.jBox.tip("请选择同队列的案件", 'warning');
						return;
					}
					var url = "${ctx}/dunning/tMisDunningTask/dialogDistribution?dunningcycle=" + uniqueid;
					$.jBox.open("iframe:" + url, "手动分案" , 600, 450, {
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

		  //获取黑名单联系人信息
          $("#acquireBlackRelationNum").click(function(){
            $.ajax({
              type: 'POST',
              url : "${ctx}/dunning/tMisDunningTask/acquireBlackRelationNum",
              success : function(data) {
              },
              error : function(XMLHttpRequest, textStatus, errorThrown){
              }
            });
            alert("正在获取黑名单信息");
          });
			
			
			// 催收留案功能-留案自检 Patch 0001 by GQWU at 2016-11-9 start-->
			 $("#deferDunningDeadline").click(function(){
					
			 });
			 // 催收留案功能-留案触发按钮 Patch 0001 by GQWU at 2016-11-9 end-->
			 
			// 催收留案功能-委外订单截止日期修改按钮 Patch 0001 by GQWU at 2016-11-25 start-->
			 $("#setOuterOrdersDeadline").click(function(){
					
			 });
			 // 催收留案功能-委外订单截止日期修改按钮 Patch 0001 by GQWU at 2016-11-25 end-->
			 
			//获取代扣渠道成功率
			if ($("#successRate")[0]) {
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
			}
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
        }
		
		//格式化peopleList选项
		function formatPeopleList( item ){
			var nickname = item.nickname ;
			if(nickname == null || nickname ==''){
				nickname = "空" ;
			}
			return nickname ;
		}
		
		//批量代扣
		function batchDeduct() {
			$.get('${ctx}/dunning/tMisDunningDeduct/batchDeduct', {}, function(data){
				alert(data);
			});
		}

        function changeStatus( dealcode) {

                $.ajax({
                    type: 'POST',
                    url : "${ctx}/dunning/tMisDunningTask/orderStatus?dealcode="+dealcode,
                    success : function(data) {
                        if (data == "OK") {
                            alert("同步成功");
                            window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
                            window.parent.window.jBox.close();            //关闭子窗体
                        } else  if (data == "NO") {
                            alert("该订单未还清");
                        }else{
                            alert("请联系管理员");
						}
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown){
                        //通常情况下textStatus和errorThrown只有其中一个包含信息
                        alert("保存失败:"+statusText);
                    }
                });

        }

	</script>
	<style type="text/css">
		.showSuspense{
			position: relative;
		}
		.showSuspense .suspense {
			display: none;
		}
		.showSuspense:hover .suspense{
			z-index: 10000;
			position: absolute;
			top: 10px;
			left: 10px;
			width: 100px;
			background-color: white;
			opacity: 0.9;
			border: solid red 1px;
			border-radius: 5px;
			outline: none;
			padding-left: 20px;
			padding-top: 20px;
			display: block;
		}

		.beautif {
			padding-bottom: 10px;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningTask/findOrderPageList">催收任务列表</a></li>
		<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
			<span id="successRate" style="float:right;padding:8px;"></span>
		</shiro:hasPermission>
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="dunningOrder" action="${ctx}/dunning/tMisDunningTask/findOrderPageList" method="post" class="breadcrumb form-search">
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

			<c:if test="${tmiscycle eq 'numberClean' }">
			
				<li><label>号码清洗</label>
					<form:select  id="numberCleanResult" path="numberCleanResult" class="input-medium">
						<form:option selected="selected" value="" label="全部"/>
						<c:forEach items="${numberList}" var="number">
							<form:option value="${number}" label="${number.numberResult }"/>
						</c:forEach>
					</form:select>
				</li>
			</c:if>
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
			
			<li><label>PTP时间</label>
				<input name="beginpromisepaydate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginpromisepaydate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endpromisepaydate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endpromisepaydate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			
			<li><label>下次跟进日期</label>
				<input name="beginnextfollowdate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginnextfollowdate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endnextfollowdate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endnextfollowdate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			
			<li>
				<input type="checkbox" id="taskOverdue" name="taskOverdue" style="margin-left:30px;"
					${dunningOrder.taskOverdue ? 'checked' : ''}/>
				<label for="taskOverdue" style="margin:0px;">过期未跟案件</label>
			</li>
			
			<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
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

			<li><label>最近催收</label>
				<input name="beginDunningtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${dunningOrder.beginDunningtime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至
				<input name="endDunningtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${dunningOrder.endDunningtime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>

			<li><label>最近登录时间</label>
				<input name="beginlatestlogintime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.beginlatestlogintime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="endlatestlogintime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningOrder.endlatestlogintime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
					
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page();"/></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>

			<shiro:hasPermission name="dunning:tMisDunningTask:exportFile"> 
				<li class="btns"><input id="outerOrders" name="outerOrders"  type="hidden" value="" />
				<input id="exportOuterFile" class="btn btn-primary" type="button" value="导出" /></li>
			</shiro:hasPermission> 
			<li class="clearfix"></li>
		</ul>
	</form:form>
	
	<shiro:hasPermission name="dunning:tMisDunningTask:directorview">
		<input id="distribution"  class="btn btn-primary" type="button" value="手动分案" />
	</shiro:hasPermission>
	
	<shiro:hasPermission name="dunning:tMisDunningTask:adminview">
		<input id="automatic"  class="btn btn-primary" type="button" value="自动分配"/>
		<input id="autoAssignNewOrder"  class="btn btn-primary" type="button" value="新订单任务"/>
		<input id="autoRepayment"  class="btn btn-primary" type="button" value="扫描还款"/>
		<input id="acquireBlackRelationNum"  class="btn btn-primary" type="button" value="黑名单信息获取"/>
	</shiro:hasPermission>
	<!-- 催收留案功能-委外订单截止日期设置按钮 Patch 0001 by GQWU at 2016-11-9 end-->

	<shiro:hasPermission name="dunning:tMisDunningDeduct:batch">
		<input id="batchDeduct" class="btn btn-primary" type="button" value="批量代扣" onclick="return confirmx('确认要批量代扣吗？', batchDeduct);"/>
	</shiro:hasPermission>

	<shiro:hasPermission name="dunning:tMisDunningTest:view">
		<a href="${ctx}/dunning/tMisDunningTest/testPage" target="_blank" >test</a>
	</shiro:hasPermission>
	<!-- test end -->
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder"  /></th>
				<th>序号</th>
				<th>提醒</th>
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
				<!-- <th>催收截止日期</th> -->
				<!-- 催收留案功能-催收截止日 Patch 0001 by GQWU at 2016-11-9 end-->
				<th>最近催收</th>
				<th>还清日期</th>
				<th>订单编号</th>
				<th>下次跟进日期</th>
				<th>PTP时间</th>
                <th>黑名单联系人数</th>
				<c:if test="${tmiscycle eq 'numberClean' }">
				<th>号码清洗</th>
				</c:if>
				<th>最近登录时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dunningOrder" varStatus="vs">
			<tr>
				<td>
					<input type="checkbox" id="${dunningOrder.dealcode}#${vs.index}" name="orders" dunningcycle="${dunningOrder.dunningcycle}" title="${dunningOrder.dunningtaskdbid}" repaymenttime="${dunningOrder.repaymenttime}" dunningType="${dunningOrder.dunningpeopletype}" overDuedays="${dunningOrder.overduedays}" orders="${dunningOrder.dealcode}" deadline="${dunningOrder.deadline}" value="${dunningOrder.dealcode}#${dunningOrder.creditamount}#${dunningOrder.overduedays}#${dunningOrder.mobile}"/>
				</td>
				<td>
					${ (vs.index+1) + (page.pageNo-1) * page.pageSize} 
				</td>
				<td>
					<c:if test="${dunningOrder.taskOverdue}">
						<i class="icon-flag" style="color:#c71c22;"></i>
					</c:if>
				</td>
				<td>
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

					<c:if test="${dunningOrder.statusText eq '未还清'}">
						<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
							<a href="javascript:void 0;onclick=changeStatus('${dunningOrder.dealcode}');"> ${dunningOrder.statusText}</a>
						</shiro:hasPermission>
						<shiro:lacksPermission name="dunning:tMisDunningTask:leaderview">
							${dunningOrder.statusText}
						</shiro:lacksPermission>
					</c:if>

					<c:if test="${dunningOrder.statusText eq '已还清'}">
						${dunningOrder.statusText}
					</c:if>
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
				
				
				<td>
					<fmt:formatDate value="${dunningOrder.dunningtime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				
				<td>
					<fmt:formatDate value="${dunningOrder.payofftime}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>

					${dunningOrder.dealcode}
				</td>
				<td>
					<fmt:formatDate value="${dunningOrder.nextfollowdate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${dunningOrder.promisepaydate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<c:choose>
						<c:when test="${dunningOrder.blackListRelaNum eq null}">
							获取失败
						</c:when>
						<c:otherwise>
							<div name="detail" class="showSuspense">
								<font color="red">${dunningOrder.blackListRelaNum}</font>
								<div class="suspense" tabindex="0">
									<div class="beautif">mo9黑名单: ${dunningOrder.blackListNumFromMo9}</div>
									<div class="beautif">第三方黑名单: ${dunningOrder.blackListNumFromThird}</div>
									<div class="beautif">未知: ${dunningOrder.blackListNumUnknow}</div>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
						<%--,${dunningOrder.blackListNumFromMo9},${dunningOrder.blackListNumFromThird}--%>
				</td>
				<c:if test="${tmiscycle eq 'numberClean' }">
					<td>
					  <c:if test="${dunningOrder.status eq 'payment'}">
						<c:choose>
							<c:when test="${dunningOrder.numberCleanResult  eq 'YXHM'}">
								<c:out value="有效号码" />
							</c:when>
							<c:when test="${dunningOrder.numberCleanResult  eq 'BZFWQ'}">
								<c:out value="不在服务区" />
							</c:when>
							<c:when test="${dunningOrder.numberCleanResult  eq 'KH'}">
								<c:out value="空号" />
							</c:when>
							<c:when test="${dunningOrder.numberCleanResult  eq 'HMCW'}">
								<c:out value="号码错误" />
							</c:when>
							<c:when test="${dunningOrder.numberCleanResult  eq 'GJ'}">
								<c:out value="关机" />
							</c:when>
							<c:when test="${dunningOrder.numberCleanResult  eq 'TJ'}">
								<c:out value="停机" />
							</c:when>
							<c:when test="${dunningOrder.numberCleanResult  eq 'WZ'}">
								<c:out value="未知" />
							</c:when>
							<c:otherwise>
								<c:out value="" />
							</c:otherwise>
						</c:choose>
					 </c:if>
					</td>
				</c:if>
				<td>
					<fmt:formatDate value="${dunningOrder.latestlogintime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
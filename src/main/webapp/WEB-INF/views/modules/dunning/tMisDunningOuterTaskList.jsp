<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<title>催收任务管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	$("a").click(function(){
		$("a").css("color","");
		$(this).css("color","#FF8C00");
	}); 

	$("#searchForm").validate({
		submitHandler: function(form){
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				error.appendTo(element.parent().parent());
			} else {
				error.insertAfter(element);
			}
		}
	});
	
	// 清空查询功能
	 $("#empty").click(function(){
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
			var url = "${ctx}/dunning/tMisDunningOuterTask/dialogOutDistribution?dunningcycle=" + uniqueid;
			$.jBox.open("iframe:" + url, "手动分案" , 600, 500, {
	               buttons: {},
	               loaded: function (h) {
	                   $(".jbox-content", document).css("overflow-y", "hidden");
	               }
	         });
			
	 });


    // 手动留案
    $("#extensionbutton").click(function(){
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
        var url = "${ctx}/dunning/tMisDunningOuterTask/dialogOutExtension?dunningcycle=" + uniqueid;
        $.jBox.open("iframe:" + url, "手动留案" , 490, 250, {
            buttons: {},
            loaded: function (h) {
                $(".jbox-content", document).css("overflow-y", "hidden");
            }
        });

    });

	//系统短信发送
	$("#autosend").click(function(){
		
			$.ajax({
				url:"${ctx}/dunning/tMisDunningOuterTask/autoSend",
				type:"GET",
				data:{},
				success:function(data){
					if(data=="OK"){
						top.$.jBox.tip("系统短信定时任务分配成功");
					}
					
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
                       alert("查询失败:"+textStatus);
                    }
				
			});
			
	 });
	//电催结论1
	$("#diancui1").click(function(){
		
			$.ajax({
				url:"${ctx}/dunning/tMisDunningOuterTask/diancui1",
				type:"GET",
				data:{},
				success:function(data){
					if(data=="OK"){
						top.$.jBox.tip("成功");
					}
					
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
                       alert("查询失败:"+textStatus);
                    }
				
			});
			
	 });
	//电催结论2
	$("#diancui2").click(function(){
		
			$.ajax({
				url:"${ctx}/dunning/tMisDunningOuterTask/diancui2",
				type:"GET",
				data:{},
				success:function(data){
					if(data=="OK"){
						top.$.jBox.tip("成功");
					}
					
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
                       alert("查询失败:"+textStatus);
                    }
				
			});
			
	 });
	//电催结论3
	$("#diancui3").click(function(){
		
			$.ajax({
				url:"${ctx}/dunning/tMisDunningOuterTask/diancui3",
				type:"GET",
				data:{},
				success:function(data){
					if(data=="OK"){
						top.$.jBox.tip("成功");
					}
					
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
                       alert("查询失败:"+textStatus);
                    }
				
			});
			
	 });

  // 委外导出功能
  $("#exportOuterFile").click(function () {
    var outerOrders = new Array();
    $("[name='orders']").each(function () {
      if (this.checked) {
        outerOrders.push($(this).attr("orders"));
      }
    });
    if (outerOrders.length == 0) {
      $.jBox.tip("请勾选导出订单号", 'warning');
      return;
    }
    $("#outerOrders").val(outerOrders);
    top.$.jBox.confirm("确认要导出列表数据吗？", "系统提示", function (v, h, f) {
          if (v == "ok") {
            $("#searchForm").attr("action", "${ctx}/dunning/tMisDunningOuterTask/exportOuterFile");
            $("#searchForm").submit();
          }
        },
        {buttonsFocus: 1});
    top.$('.jbox-body .jbox-icon').css('top', '55px');
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

// 查询
function page(n,s){
	if(n) $("#pageNo").val(n);
	if(s) $("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/dunning/tMisDunningOuterTask/findOrderPageList");
	$("#searchForm").submit();
      	return false;
      }

//格式化peopleList选项
function formatPeopleList( item ){
	var nickname = item.nickname ;
	if(nickname == null || nickname ==''){
		nickname = "空" ;
	}
	return nickname ;
}

function changeStatus( dealcode) {

    $.ajax({
        type: 'POST',
        url : "${ctx}/dunning/tMisDunningOuterTask/orderStatus?dealcode="+dealcode,
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

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningOuterTask/findOrderPageList">催收任务列表</a></li>
	</ul>

	<sys:message content="${message}" />
	<form:form id="searchForm" modelAttribute="dunningOrder" action="${ctx}/dunning/tMisDunningOuterTask/findOrderPageList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />

		<ul class="ul-form">
			<li><label>姓名</label> <form:input path="realname" htmlEscape="false" class="input-medium" /></li>
			<li><label>手机号</label> <form:input path="mobile" htmlEscape="false" maxlength="128" class="input-medium" /></li>
			<li><label>订单号</label> <form:input path="dealcode" htmlEscape="false" maxlength="128" class="input-medium" /></li>
			<li><label>产品</label>
				<form:select id="status" path="platformExt" class="input-medium">
					<form:option selected="selected" value="" label="全部"/>
					<form:options items="${bizTypes}" itemLabel="desc"/>
				</form:select>
			</li>
			<li><label>订单状态</label> <form:select id="status" path="status" class="input-medium">
					<form:option selected="selected" value="" label="全部状态" />
					<form:option value="payoff" label="已还清" />
					<form:option value="payment" label="未还清" />
				</form:select></li>

			<li><label>逾期天数</label> <form:input path="beginOverduedays" htmlEscape="false" maxlength="3" style="width:35px;" /> - <form:input path="endOverduedays" htmlEscape="false" maxlength="3" style="width:35px;" /></li>
			<li><label>欠款金额</label> <form:input path="beginAmount" htmlEscape="false" maxlength="4" class="digits" style="width:35px;" /> - <form:input path="endAmount" htmlEscape="false" maxlength="4" class="digits" style="width:35px;" /></li>
			<li><label>分案日期</label> <input name="beginOutsourcingBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="<fmt:formatDate value="${dunningOrder.beginOutsourcingBeginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /> 至 <input name="endOutsourcingBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${dunningOrder.endOutsourcingBeginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /></li>
			<li><label>催收截止</label> <input name="beginOutsourcingEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="<fmt:formatDate value="${dunningOrder.beginOutsourcingEndDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /> 至 <input name="endOutsourcingEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${dunningOrder.endOutsourcingEndDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /></li>
			<li><label>还清日期</label> <input name="beginPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="<fmt:formatDate value="${dunningOrder.beginPayofftime}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /> 至 <input name="endPayofftime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="<fmt:formatDate value="${dunningOrder.endPayofftime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /></li>
			<li><label>催收小组：</label> <form:select id="groupList" path="dunningPeople.group.id" class="input-medium">
					<form:option value="">全部</form:option>
					<!-- 添加组类型为optgroup -->
					<c:forEach items="${groupTypes}" var="type">
						<optgroup label="${type.value}">
							<!-- 添加类型对应的小组 -->
							<c:forEach items="${groupList}" var="item">
								<c:if test="${item.type == type.key}">
									<option value="${item.id}" <c:if test="${dunningOrder.dunningPeople.group.id == item.id }">selected="selected"</c:if>>${item.name}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</form:select></li>
			<li>
			<li><label>催收队列：</label> <form:select path="dunningcycle" class="input-medium">
					<form:option value="">全部</form:option>
					<form:option value="Q4">Q4</form:option>
					<form:option value="Q5">Q5</form:option>
				</form:select></li>
			<li>
			<li><label>留案：</label> <form:select path="extension" class="input-medium">
				<form:option value="">全部</form:option>
				<form:option value="on">是</form:option>
				<form:option value="off">否</form:option>
			</form:select></li>
			<li>
			<li><label>催款人</label> <form:input id="peopleList" path="dunningPeople.queryIds" htmlEscape="false" type="hidden" /></li>
			<li><label>最近催收</label>
				<input name="beginDunningtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${dunningOrder.beginDunningtime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至
				<input name="endDunningtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${dunningOrder.endDunningtime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>行动码</label>
			<form:select path="actionCode" class="input-medium">
				<form:option selected="selected" value="" label="全部"/>
				<c:forEach items="${mobileResultMap}" var="mobileResult">
					<form:option value="${mobileResult.key}" label="${mobileResult.value}"/>
				</c:forEach>
				<form:option value="empty" label="空"/>
			</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" /></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空" /></li>
			<li class="btns">
				<input id="outerOrders" name="outerOrders" type="hidden" value=""/>
				<input id="exportOuterFile" class="btn btn-primary" type="button" value="导出"/>
			</li>
		</ul>
	</form:form>
	<input id="distribution" class="btn btn-primary" type="button" value="手动分案" />
	<input id="extensionbutton" class="btn btn-primary" type="button" value="手动留案" />
	<shiro:hasPermission name="dunning:tMisDunningTask:adminview">
		<input id="autosend" class="btn btn-primary" type="button" value="系统短信发送" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:adminview">
		<input id="diancui1" class="btn btn-primary" type="button" value="电催结论(Q0,Q1)" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:adminview">
		<input id="diancui2" class="btn btn-primary" type="button" value="电催结论(Q2,Q3,Q4)上午" />
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:adminview">
		<input id="diancui3" class="btn btn-primary" type="button" value="电催结论(Q2,Q3,Q4)下午" />
	</shiro:hasPermission>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder" /></th>
				<th>序号</th>
				<th>产品</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>欠款金额</th>
				<th>应催金额</th>
				<th>还款总额</th>
				<th>到期还款日期</th>
				<th>逾期天数</th>
				<th>订单状态</th>
				<th>催收人</th>
				<th>分案日期</th>
				<th>催收截止日期</th>
				<th>最近催收时间</th>
				<th>还清日期</th>
				<th>订单编号</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="dunningOrder" varStatus="vs">
				<tr>
					<td><input type="checkbox" id="${dunningOrder.dealcode}#${vs.index}" name="orders" dunningcycle="${dunningOrder.dunningcycle}" title="${dunningOrder.dunningtaskdbid}" repaymenttime="${dunningOrder.repaymenttime}" dunningType="${dunningOrder.dunningpeopletype}" overDuedays="${dunningOrder.overduedays}" orders="${dunningOrder.dealcode}" deadline="${dunningOrder.deadline}"
						value="${dunningOrder.dealcode}#${dunningOrder.creditamount}#${dunningOrder.overduedays}#${dunningOrder.mobile}" /></td>
					<td>${ (vs.index+1) + (page.pageNo-1) * page.pageSize}</td>
					<td>
						<c:choose>
							<c:when test="${fn:contains(dunningOrder.platformExt, 'jinRongZhongXin')}">
								weixin36
							</c:when>
							<c:otherwise>
								mo9
							</c:otherwise>
						</c:choose>
					</td>
					<td><a href="${ctx}/dunning/tMisDunningOuterTask/pageFather?buyerId=${dunningOrder.buyerid}&dealcode=${dunningOrder.dealcode}&dunningtaskdbid=${dunningOrder.dunningtaskdbid}" target="_blank"> ${dunningOrder.realname} </a></td>

					<td>${dunningOrder.mobile}</td>
					<td>${dunningOrder.amountText}</td>
					<td>${dunningOrder.creditamountText}</td>
					<td>${dunningOrder.balance}</td>
					<td><fmt:formatDate value="${dunningOrder.repaymenttime}" pattern="yyyy-MM-dd" /></td>
					<td>${dunningOrder.overduedays}</td>
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
					<td>${dunningOrder.dunningPeople.nickname}</td>
					<td><fmt:formatDate value="${dunningOrder.outsourcingBeginDate}" pattern="yyyy-MM-dd" /></td>
					<td><fmt:formatDate value="${dunningOrder.outsourcingEndDate}" pattern="yyyy-MM-dd" /></td>

					<td><fmt:formatDate value="${dunningOrder.dunningtime}" pattern="yyyy-MM-dd HH:mm" /></td>

					<td><fmt:formatDate value="${dunningOrder.payofftime}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td>${dunningOrder.dealcode}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>信函管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//初始化message显示
			resetTip();
			//清空
			 $("#empty").click(function(){
        		 window.location.href="${ctx}/dunning/tMisDunningLetter";
			 }); 	
			//同步历史按钮
			$("#synButton").click(function(){
				$.ajax({
					url:"${ctx}/dunning/tMisDunningLetter/synDealcode",
					type:"GET",
					data:{},
					success:function(data){
							top.$.jBox.tip("同步完成");
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
		                   alert("同步失败:"+textStatus);
		                }
					
				});
			});
			//同步当天按钮
			$("#synButtonToday").click(function(){
				$.ajax({
					url:"${ctx}/dunning/tMisDunningLetter/synDealcodeToday",
					type:"GET",
					data:{},
					success:function(data){
							top.$.jBox.tip("同步完成");
					},
					error : function(XMLHttpRequest, textStatus, errorThrown){
		                   alert("同步失败:"+textStatus);
		                }
					
				});
			});
		
			//多选
			 $("#allorder").change(function(){
				 	if($("#allorder").prop('checked')){
				 		$("[name='letterIds']").each(function(){
					        $(this).prop('checked',true);
					    	$(this).attr("control","true");
					    });
				 	}else{
				 		$("[name='letterIds']").each(function(){
							if(this.checked){
					        	$(this).prop('checked',false);
					        	$(this).attr("control","false");
					        }
					    });
				 	}
			 });
			 $("[name='letterIds']").change(function(){
			        if($(this).prop('checked')){
			        	$(this).attr("control","true");
			        }else{
			        	$(this).attr("control","false");
			        }
			    });
		
			   
			 //发送信函
			 $("#sendLetters").click(function(){
					var sendLetterDealcodes = new Array();
					var controls=false;
					$("[name='letterIds'][control='true']").each(function(){
						if($(this).attr('status')=='BESEND' && $(this).attr('orderstatus')=='payment'){
							sendLetterDealcodes.push($(this).attr("orders"));
						}else{
							controls=true;
							return false;
						}
					});
					if(controls){
						$.jBox.tip("请勾选状态待发送的未还清的订单!", 'warning');
						return ;
					}
					if(sendLetterDealcodes.length==0){
						$.jBox.tip("请勾选订单", 'warning');
						return;
					}
					$("#sendLetterDealcodes").val(sendLetterDealcodes);
					top.$.jBox.confirm("确认要发送信函吗？","系统提示",function(v,h,f){
						if(v=="ok"){
							$("#searchForm").attr("action","${ctx}/dunning/tMisDunningLetter/sendLetterDealcodes");
							$("#searchForm").submit();
						}
					},
					{buttonsFocus:1});
					top.$('.jbox-body .jbox-icon').css('top','55px');
			 });
				 
			//导出
			 $("#exportOuterFile").click(function(){
					top.$.jBox.confirm("确认要导出吗？","系统提示",function(v,h,f){
						if(v=="ok"){
							$("#searchForm").attr("action","${ctx}/dunning/tMisDunningLetter/exportFile");
							$("#searchForm").submit();
						}
					},{buttonsFocus:1});
					top.$('.jbox-body .jbox-icon').css('top','55px');
			 });
			
			//导入
			 $("#batchAdd").click(function(){
				 var e = $("#importBox").clone();
				 e.children("form").prop("id", "importExcel");
					$.jBox(e.html(), {title:"导入数据", width: 500, 
					    height:450,  buttons:{"关闭":true}} );
		     });
		});
		
		function page(n,s){
			if(n)
			$("#pageNo").val(n);
			if(s)
			$("#pageSize").val(s);
			$("#pageSize").val($("#sizes").val());
			$("#searchForm").attr("action","${ctx}/dunning/tMisDunningLetter/list");
			$("#searchForm").submit();
			return false;
		}
		
		function disableBUtton(obj){
			$(obj).attr("disabled",true);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisDunningLetter">信函管理</a></li>
		<shiro:hasPermission name="dunning:tMisDunningDeduct:edit">
			<span id="successRate" style="float:right;padding:8px;"></span>
		</shiro:hasPermission>
	</ul>
	<div id="importBox" class="hide">
		<form action="${ctx}/dunning/tMisDunningLetter/fileUpload" method="post" enctype="multipart/form-data"  onsubmit="loading('正在导入，请稍等...');" >
			<div style="border:1px dashed ;width:300px;height:260px;margin:10px 0px 0px 100px;text-align:center;">
			
				<div  style="margin-top: 10px ;">
	            <input   id="file" type="file" accept=".xls,.xlsx,.csv" name="file" />
				</div>	
				<div  style="margin-top: 180px ;text-align:left; "> 
					<input type="submit" class="btn btn-primary" id="save"  value="确定上传" onclick="disableBUtton(this)" />
				</div>
			</div>
			
		</form>
	</div>
	
	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="TMisDunningLetter" action="${ctx}/dunning/tMisDunningLetter/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="sendLetterDealcodes" name="sendLetterDealcodes" type="hidden" value=""/>

		<ul class="ul-form">
			<li><label>姓名</label>
				<form:input path="realname"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>订单号</label>
				<form:input path="dealcode"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>发函结果</label>
			<form:select  id="sendResult" path="sendResult" class="input-medium">
					<form:option value="">全部</form:option>
				<c:forEach items="${sendResults}" var="result" >
					<form:option value="${result}">${result.desc}</form:option>
				</c:forEach>
			</form:select>
			</li>
			<li><label>订单状态</label>
			<form:select  id="orderStatus" path="orderStatus" class="input-medium">
					<form:option value="">全部</form:option>
					<form:option value="payment">未还清</form:option>
					<form:option value="payoff">已还清</form:option>
			</form:select>
			</li>
			
			<li><label>逾期天数</label>
				<form:input  path="beginOverduedays"  htmlEscape="false" maxlength="3" class="digits"  style="width:35px;"  />
				- 
				<form:input  path="endOverduedays"  htmlEscape="false" maxlength="3" class="digits" style="width:35px;"   />
			</li>
			
			<li><label>订单还清时间</label>
				<input name="payoffBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisDunningLetter.payoffBeginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="payoffEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisDunningLetter.payoffEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
			</li>
			<li><label>信函发送时间</label>
				<input name="sendBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisDunningLetter.sendBeginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="sendEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisDunningLetter.sendEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
			</li>
			<li><label>结果更新时间</label>
				<input name="resultBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisDunningLetter.resultBeginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="resultEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${TMisDunningLetter.resultEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="button" value="查询" onclick=" return page()" /></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
			<li class="btns"><input id="sendLetters" class="btn btn-primary" type="button" value="发送信函" /></li>
			<li class="btns"><input id="exportOuterFile" class="btn btn-primary" type="button" value="导出" /></li>
			<li class="btns"><input type="button"  class="btn btn-primary" id="batchAdd" value="导入" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
<%-- 	<shiro:hasPermission name="dunning:tMisDunningTask:adminview"> --%>
<!-- 		<input id="synButton"  class="btn btn-primary" type="button" value="同步历史案件"/> -->
<!-- 		<input id="synButtonToday"  class="btn btn-primary" type="button" value="同步当天案件"/> -->
<%-- 	</shiro:hasPermission> --%>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder"  /></th>
				<th>序号</th>
				<th>订单号</th>
				<th>姓名</th>
				<th>发函结果</th>
				<th>邮编</th>
				<th>欠款金额</th>
				<th>逾期天数</th>
				<th>户籍地址</th>
				<th>订单状态</th>
				<th>备注</th>
				<th>订单还清日期</th>
				<th>信函发送时间</th>
				<th>结果更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="letters" varStatus="vs">
			<tr>
				<td><input type="checkbox" name="letterIds" orderstatus="${letters.orderStatus}" orders="${letters.dealcode }" value="${letters.id}" status="${letters.sendResult}" control="" /></td>
				<td>${ (vs.index+1) + (page.pageNo-1) * page.pageSize}</td>
				<td>${letters.dealcode }</td>
				<td>${letters.realname }</td>
				<td>${letters.sendResult.desc }</td>
				<td>${letters.postCode }</td>
				<td>${letters.creditamount }</td>
				<td>${letters.overduedays }</td>
				<td>${letters.ocrAddr }</td>
				<td>${letters.orderStatusText}</td>
				<td>${letters.remark}</td>
				<td><fmt:formatDate value="${letters.payoffDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${letters.sendDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${letters.resultDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
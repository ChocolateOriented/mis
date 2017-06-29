<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
	<title>入账对公明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
      var content = null;
		$(document).ready(function() {
		  var queryOrderTemplate = $("#queryOrderTemplate").html();
          content = {
            state1: {
              content: queryOrderTemplate,
              buttons: { '下一步': 1, '清空': 0 },
              buttonsFocus: 0,
              submit: function (v, h, f) {
                if (v == 0) {
                  return true; // close the window
                }
                else {
                  $.jBox.nextState(); //go forward
                  // 或 $.jBox.goToState('state2')
                }
                return false;
              }
            },
            state2: {
              content: '状态二，请关闭窗口哇：）',
              buttons: { '上一步': 1, '取消': 0 },
              buttonsFocus: 0,
              submit: function (v, h, f) {
                if (v == 0) {
                  return true; // close the window
                } else {
                  $.jBox.prevState() //go back
                  // 或 $.jBox.goToState('state1');
                }

                return false;
              }
            }
          };
		});
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/dunning/tMisRemittanceMessage/detail");
			$("#searchForm").submit();
			return false;
		}

//===================手工查账======================
		function openHandAudit() {
		  //TODO 查账表单重置
          $.jBox(content,{
            title: "查账",
            width: 600,
            height: 450});
		}

		function findOrderByMobile() {
		  //重置已查到的订单
		  restOrder();
		  var mobile = $("#jbox-content").find("#inputMobile").val();
		  if (mobile == ""){
            jBox.tip("请输入手机号");
		    return ;
		  }
		  $.post("${ctx}/dunning/tMisRemittanceMessage/findOrderByMobile",{mobile:mobile},function(orderMsg){
		    //没查到给提示
			if (orderMsg == null){
              jBox.tip("未查询到订单");
              return ;
			}
		    //查到了填充值
			console.info(orderMsg);
		  },"json");
        }
        function restOrder() {
		  $("#auditDealcode").val(null);
		  $("#jbox-content").find("#orderData").hide();
        }
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/detail">催收任务列表</a></li>
		
		<span id="successRate" style="float:right;padding:8px;"></span>
		
	</ul>

	<sys:message content="${message}"/>
	<form:form id="searchForm"  modelAttribute="TMisRemittanceMessage" action="${ctx}/dunning/tMisRemittanceMessage/detail" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易流水号</label>
				<form:input path="remittanceSerialNumber"  htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>交易时间</label>
				<input name="begindealtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
<%-- 					value="<fmt:formatDate value="1997-12-12" pattern="yyyy-MM-dd"/>" --%>
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至 
				<input name="enddealtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
<%-- 					value="<fmt:formatDate value="1997-12-18" pattern="yyyy-MM-dd"/>" --%>
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>交易金额</label>
				<form:input path="remittanceAmount"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>对方账户</label>
				<form:input path="remittanceAccount"  htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			
			<li><label>对方姓名</label>
				<form:input  path="remittanceName"  htmlEscape="false" maxlength="128" class="input-medium" />
			</li>
			
			<li><label>入账状态</label>
			<form:select  id="status" path="accountStatus" class="input-medium">
				<form:option selected="selected" value="" label="全部状态"/>
				<c:forEach items="${statusList}" var="status">
				 	<form:option value="${status}" label="${status.desc}"/>
				</c:forEach>
			</form:select>
			</li>
			</br>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
		</ul>
	</form:form>
	<div><a onclick="openHandAudit()" class="btn btn-primary btn-large">+查账申请</a></div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>交易流水号</th>
				<th>交易时间</th>
				<th>交易渠道</th>
				<th>交易金额（元）</th>
				<th>对方名称</th>
				<th>对方账户</th>
				<th>备注</th>
				<th>入账状态</th>
				<th>上传时间</th>
				<th>上传人</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tmessage" varStatus="status">
			<tr>
				<td>
				 	${status.count}
				</td>
				<td>
					 ${tmessage.remittanceSerialNumber } 
				</td>
				<td>
					 <fmt:formatDate value="${tmessage.remittanceTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					 ${tmessage.remittanceChannel }
				</td>
				<td>
					 ${tmessage.remittanceAmount }
				</td>
				<td>
					  ${tmessage.remittanceName }
				</td>
				<td>
					 ${tmessage.remittanceAccount}
				</td>
				<td>
					  ${tmessage.remark }  
				</td>
				<td>
					  ${tmessage.accountStatus }  
				</td>
				<td>
					  ${tmessage.createDate }  
				</td>
				<td>
					  ${tmessage.financialUser }
				</td>
				
				</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>

<%--查账信息--%>
	<form>
		<input type="hidden" id="auditDealcode" name="dealcode">
		<input type="hidden" id="auditRemittanceId" name="remittanceId">
		<input type="hidden" id="auditRemittanceTag" name="remittanceTag">
	</form>
	<%--=========查账申请jbox模板==============--%>
	<div id="htmlTemplate" style="display: none">
		<%--订单查询模板--%>
		<div id="queryOrderTemplate">
			<h3>
				<div class="row" style="text-align: center; color: #999; margin-bottom: 20px">
					<div class="span3" style="color: #2fa4e7">① 订单查询</div>
					<div class="span2">② 交易查询</div>
					<div class="span2">③ 查账</div>
				</div>
			</h3>
			<form class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="inputMobile">手机号</label>
					<div class="controls">
						<input type="text" id="inputMobile"/>
						<a class="btn btn-primary" onclick="findOrderByMobile()">查询</a>
					</div>
				</div>
				<div id="orderData" style="display: none">
                    <div class="control-group">
                        <label class="control-label">订单号</label>
                        <div class="controls" name="dealcode"></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">姓名</label>
                        <div class="controls" name="realname"></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">借款金额</label>
                        <div class="controls" name="amount"></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">应催金额</label>
                        <div class="controls" name="creditAmount"></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">逾期天数</label>
                        <div class="controls" name="overduedays"></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">催收人</label>
                        <div class="controls" name="dunningpeoplename"></div>
                    </div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>查账入账父页面</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
      var content = null;
      $(document).ready(function () {
    	
        var queryOrderTemplate = $("#queryOrderTemplate").html();
        content = {
          state1: {
            content: queryOrderTemplate,
            buttons: {'下一步': 1, '清空': 0},
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
            buttons: {'上一步': 1, '取消': 0},
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
        
    	// 清空查询功能
		 $("#empty").click(function(){
			var sss = window.frames["ifm"].document.getElementById("childIfam").value;
  		 	window.location.href="${ctx}/dunning/tMisRemittanceMessage/confirmList?childPage="+sss;
		 }); 
			
      });
      function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        var sss = window.frames["ifm"].document.getElementById("childIfam").value;
        $("#searchForm").attr("action", "${ctx}/dunning/tMisRemittanceMessage/confirmList?childPage="+sss);
        $("#searchForm").submit();
        return false;
      }
      //===================手工查账======================
      function openHandAudit() {
        //TODO 查账表单重置
        $.jBox(content, {
          title: "查账",
          width: 600,
          height: 450
        });
      }

      function findOrderByMobile() {
        //重置已查到的订单
        restOrder();
        var mobile = $("#jbox-content").find("#inputMobile").val();
        if (mobile == "") {
          jBox.tip("请输入手机号");
          return;
        }
        $.post("${ctx}/dunning/tMisRemittanceMessage/findOrderByMobile", {mobile: mobile},
            function (orderMsg) {
              //没查到给提示
              if (orderMsg == null) {
                jBox.tip("未查询到订单");
                return;
              }
              //查到了填充值
              console.info(orderMsg);
            }, "json");
      }
      function restOrder() {
        $("#auditDealcode").val(null);
        $("#jbox-content").find("#orderData").hide();
      }
    </script>

</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/confirmList">查账入账</a></li>

    <span id="successRate" style="float:right;padding:8px;"></span>

</ul>

<sys:message content="${message}"/>
<form:form id="searchForm" modelAttribute="TMisRemittanceMessagChecked" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>手机号</label>
            <form:input path="mobile" htmlEscape="false" class="input-medium"/>
        </li>
        <li><label>对方姓名</label>
            <form:input path="realName" htmlEscape="false" maxlength="128" class="input-medium"/>
        </li>
        <li><label>订单编号</label>
            <form:input path="dealcode" htmlEscape="false" maxlength="128" class="input-medium"/>
        </li>
        <li><label>交易流水号</label>
            <form:input path="remittanceSerialNumber" htmlEscape="false" class="input-medium"/>
        </li>

        <li><label>订单状态</label>
            <form:select id="status" path="orderStatus" class="input-medium">
                <form:option selected="selected" value="" label="全部状态"/>
                <form:option value="payment" label="未还清"/>
                <form:option value="payoff" label="已还清"/>
           	</form:select>
        </li>
        <li><label>入账标签</label>
            <form:select id="status" path="remittanceTag" class="input-medium">
                <form:option selected="selected" value="" label="全部状态"/>
                <%-- 				<c:forEach items="" var=""> --%>
                <%-- 				 	<form:option value="" label=""/> --%>
                <%-- 				</c:forEach> --%>
            </form:select>
        </li>
      
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
        <li class="btns"><input id="empty" class="btn btn-primary" type="button" value="清空"/></li>
    </ul>
</form:form>

<div><a onclick="openHandAudit()" class="btn btn-primary btn-large">+查账申请</a></div>

<br/>
<iframe id="ifm" name="ifm" frameborder="0"
 <c:if test="${childPage eq 'completed'}">
  src="${ctx}/dunning/tMisRemittanceMessage/completed"
</c:if> 
 <c:if test="${childPage ne 'completed'}">
  src="${ctx}/dunning/tMisRemittanceMessage/checked"
</c:if> 
style="width:100%;height:600px;">
</iframe>
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
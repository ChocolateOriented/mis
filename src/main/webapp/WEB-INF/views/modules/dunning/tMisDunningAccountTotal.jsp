<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>查账入账父页面</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
      $(document).ready(function () {
        //加载子页面
        if (null == currentPageUrl){
          checkedPage();
        }

    	// 清空查询功能
		 $("#empty").click(function(){
			var sss = window.frames["ifm"].document.getElementById("childIfam").value;
  		 	window.location.href="${ctx}/dunning/tMisRemittanceMessage/confirmList?childPage="+sss;
		 });

        //组与花名联动查询
        $("#groupList").on("change", function () {
          $("#dunningPeople").select2("val", null);
        });
        $("#dunningPeople").select2({//
          ajax: {
            url: "${ctx}/dunning/tMisDunningPeople/optionList",
            dataType: 'json',
            quietMillis: 250,
            data: function (term, page) {//查询参数 ,term为输入字符
              var groupId = $("#groupList").val();
              return {'group.id': groupId, nickname: term};
            },
            results: function (data, page) {//选择要显示的数据`
              var resultsData = [] ;
              resultsData[0] = {id:null,nickname:"全部人员"};
              for (var i = 0; i < data.length; i++) {
                resultsData[i+1] = {id:data[i].id,nickname:data[i].nickname};
              }
              return { results: resultsData };
            },
            cache: true
          },
          initSelection: function (element, callback) {//回显
            var id = $(element).val();
            if (id == "") {
              return;
            }
            //根据组查询选项
            $.ajax("${ctx}/dunning/tMisDunningPeople/optionList", {
              data: function () {
                var groupId = $("#groupList").val();
                return {groupId: groupId}
              },
              dataType: "json"
            }).done(function (data) {
              for (var item in data) {
                //若回显ids里包含选项则选中
                if (id == data[item].id) {
                  callback(data[item]);
                }
              }
            });
          },
          formatResult: formatPeopleList, //选择显示字段
          formatSelection: formatPeopleList, //选择选中后填写字段
          width:170
        });
      });
      //格式化peopleList选项
      function formatPeopleList(item) {
        var nickname = item.nickname;
        if (nickname == null || nickname == '') {
          nickname = "空";
        }
        return nickname;
      }

      var currentPageUrl = null;
      //选则已查账
      function checkedPage(){
        $("#checkedPageLab").addClass("active");
        $("#completedPageLab").removeClass("active");

        currentPageUrl = "${ctx}/dunning/tMisRemittanceMessage/checked?";
        page(1);
      }
      //选则已完成
      function completedPage(){
        $("#completedPageLab").addClass("active");
        $("#checkedPageLab").removeClass("active");

        currentPageUrl = "${ctx}/dunning/tMisRemittanceMessage/completed?" ;
        page(1);
      }
      function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#ifm").attr("src", currentPageUrl + $("#searchForm").serialize());
        return false;
      }

      /**
       * ===================手工查账======================
       */
      var auditComiting = false;

      //获取手工查账弹窗
      var content = null;
      $(document).ready(function () {
        var queryOrderTemplate = $("#queryOrderTemplate").html();
        var queryRemittanceMessage = $("#queryRemittanceMessage").html();
        var auditComfirm = $("#auditComfirm").html();
        content = {
          state1: {
            content: queryOrderTemplate,
            buttons: {'下一步': 1, '清空': 0},
            buttonsFocus: 0,
            submit: function (v, h, f) {
              if (v == 0) {//清空
                restOrder();
                $(".jbox-container").find("#inputMobile").val(null);
                return false; // close the window
              }
              else {//下一步
                var auditDealcode = $("#auditDealcode").val();
                if (auditDealcode == ""){
                  jBox.tip("请先查找订单");
                  return false;
                }
                $.jBox.nextState(); //go forward
                // 或 $.jBox.goToState('state2')
              }
              return false;
            }
          },
          state2: {
            content: queryRemittanceMessage,
            buttons: {'下一步': 1, '清空': 0},
            buttonsFocus: 0,
            submit: function (v, h, f) {
              if (v == 0) {//清空
                restRemittance();
                $(".jbox-container").find("#inputRemittanceSerialNumber").val(null);
                return false; // close the window
              }
              else {//下一步
                var serialNumber = $("#auditRemittanceSerialNumber").val();
                if (serialNumber == ""){
                  jBox.tip("请先查寻交易信息");
                  return false;
                }
                $.jBox.nextState();
                var remittanceConfirm = $("#auditRemittanceId").data("target");
                var orderMsg = $("#auditDealcode").data("target");
                formLoad($(".jbox-container").find("#orderMsgComfirm"),orderMsg);
                formLoad($(".jbox-container").find("#remittanceMsgComfirm"),remittanceConfirm);
              }
              return false;
            }
          },
          state3: {
            content: auditComfirm,
            buttons: {'查账': 1, '取消': 0},
            buttonsFocus: 0,
            submit: function (v, h, f) {
              if (v == 0) {
                return true; // close the window
              }
              if (auditComiting){
                jBox.tip("正在查账请稍后");
                return false;
              } else {//查账
                var auditRemittanceTag = $("#auditRemittanceTag").val();
                if (auditRemittanceTag == ""){
                  jBox.tip("请先选择入账标签");
                  return false;
                }
                auditComiting = true ;
                //发送查账请求
                $.post("${ctx}/dunning/tMisRemittanceMessage/handleAudit",$("#auditForm").serialize(),function (msg) {
                  auditComiting = false ;
                  if ("success" == msg){
                    $.jBox.close();
                    jBox.tip("查账成功");
                    page();
                    return ;
                  }
                  $.jBox.tip(msg);
                });
              }
              return false;
            }
          }
        };
      });

      function openHandAudit() {
        auditComiting = false;
        restOrder();
        restRemittance();
        resetTag();

        $.jBox(content, {
          title: "查账",
          width: 900,
          height: 500
        });
      }

      //订单查询
      function findOrderByMobile() {
        //重置已查到的订单
        restOrder();
        var mobile = $(".jbox-container").find("#inputMobile").val();
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
              $("#auditDealcode").val(orderMsg.dealcode);
              $("#auditDealcode").data("target",orderMsg);
              var orderMsgForm = $(".jbox-container").find("#orderMsg");
              formLoad(orderMsgForm,orderMsg);
              if (orderMsg.dunningPeople != null){
                orderMsgForm.find("[name='dunningPeople_nickname']").val(orderMsg.dunningPeople.nickname);
              }
              orderMsgForm.find("#orderData").show(300);
            }, "json");
      }
      //重置订单数据
      function restOrder() {
        $("#auditDealcode").val(null);
        $("#jbox-content").find("#orderData").hide(300);
      }

      //汇款信息查询
      function findRemittance(){
        //重置已查到的订单
        restRemittance();
        var serialNumber = $(".jbox-container").find("#inputRemittanceSerialNumber").val();
        if (null == serialNumber || serialNumber == "") {
          jBox.tip("请输入流水号");
          return;
        }
        $.post("${ctx}/dunning/tMisRemittanceMessage/findRemittance",{remittanceSerialNumber:serialNumber,remittanceChannel: "alipay"},
            function (remittanceConfirm) {
              //没查到给提示
              if (remittanceConfirm == null) {
                jBox.tip("未查询到汇款信息");
                return;
              }

              var id = remittanceConfirm.id ;
              if (null == id || "" == id){//填充值
                setRemittance(remittanceConfirm);
                return ;
              }
              //查到了若汇款信息以匹配给提示
              jBox.confirm("该交易流水已查账，是否重新关联，确定后将解除与前一笔订单的关联","提示",function (v, h, f) {
                if (v=="ok"){
                  setRemittance(remittanceConfirm);
                }
                return true ;
              })
            }, "json");
      }
      //重置汇款信息数据
      function restRemittance() {
        $("#auditRemittanceId").val(null);
        $("#auditRemittanceSerialNumber").val(null);
        $(".jbox-container").find("#remittanceData").hide(300);
      }
      //填充汇款信息
      function setRemittance(remittanceConfirm){
        $("#auditRemittanceId").val(remittanceConfirm.id);
        $("#auditRemittanceId").data("target",remittanceConfirm)
        $("#auditRemittanceSerialNumber").val(remittanceConfirm.serialnumber);
        var remittanceMsgForm = $(".jbox-container").find("#remittanceMsg");
        formLoad(remittanceMsgForm,remittanceConfirm);
        remittanceMsgForm.find("#remittanceData").show(300);
      }

      //查账
      function addTag(tag){
        resetTag();
        $(".jbox-container").find("#"+tag).addClass("btn-primary");
        $("#auditRemittanceTag").val(tag);
      }
      //重置入账标签
      function resetTag(){
        $("#auditRemittanceTag").val(null);
        $(".jbox-container").find("#btnGroup a").removeClass("btn-primary");
      }

      //将数据值与inputname对应的表单回显
      function formLoad(form,data) {
        for(var i in data){
          var name=i;
          var value=data[i];
          if(name!=""&&value!=""){
            valuAtion(form,name,value);
          }
        }
      }
      function valuAtion(form,name,value){
        if(form.length<1){
          return;
        }
        if(form.find("[name='"+name+"']").length<1){
          return;
        }
        var input=form.find("[name='"+name+"']")[0];
        if($.inArray(input.type,["text","password","hidden","select-one","textarea"])>-1){
          $(input).val(value);
        }else if(input.type==" "||input.type=="checkbox"){
          form.find("[name='"+name+"'][value='"+value+"']").attr("checked",true);
        }
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
        <li><label>借款人姓名</label>
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
                <form:options items="${remittanceTagList}" itemLabel="desc" />
            </form:select>
        </li>

        <li><label>催收小组：</label>
            <form:select id="groupList" path="dunningGroupId" class="input-medium">
                <form:option value="">全部</form:option>
                <!-- 添加组类型为optgroup -->
                <c:forEach items="${groupTypes}" var="type">
                    <optgroup label="${type.value}">
                        <!-- 添加类型对应的小组 -->
                        <c:forEach items="${groupList}" var="item">
                            <c:if test="${item.type == type.key}">
                                <option value="${item.id}"
                                        <c:if test="${TMisRemittanceMessagChecked.dunningGroupId == item.id }">selected="selected"</c:if>>${item.name}</option>
                            </c:if>
                        </c:forEach>
                    </optgroup>
                </c:forEach>
            </form:select></li>

        <li><label>催收员</label>
            <form:input id="dunningPeople" path="dunningPeopleId" type="hidden" />
        </li>

        <li class="btns">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page(1);"/>
            <input id="empty" class="btn btn-primary" type="button" value="清空"/>
        </li>
        <li class="clearfix"></li>
    </ul>
</form:form>

<shiro:hasPermission name="dunning:TMisRemittanceMessage:handleAudit">
    <div><a onclick="openHandAudit()" class="btn btn-primary btn-large">+查账申请</a></div>
</shiro:hasPermission>
<br/>

<ul class="nav nav-tabs">
    <li id="checkedPageLab" class="active"><a onclick="checkedPage()">已查账</a></li>
    <li id="completedPageLab" ><a onclick="completedPage()">已完成</a></li>
</ul>

<iframe id="ifm" name="ifm" frameborder="0" style="width:100%;height: 560px;"></iframe>

<%--查账信息--%>
<form id="auditForm">
    <input type="hidden" id="auditDealcode" name="dealcode">
    <input type="hidden" name="remittancechannel" value="alipay">
    <input type="hidden" id="auditRemittanceId" name="id">
    <input type="hidden" id="auditRemittanceSerialNumber" name="serialnumber">
    <input type="hidden" id="auditRemittanceTag" name="remittanceTag">
</form>
<%--=========查账申请jbox模板==============--%>
<div id="htmlTemplate" style="display: none">
    <%--订单查询模板--%>
    <div id="queryOrderTemplate">
        <h3>
            <div class="row-fluid" style="text-align: center; color: #999; margin-bottom: 40px">
                <div class="span4" style="color: #2fa4e7">① 订单查询</div>
                <div class="span4">② 交易查询</div>
                <div class="span4">③ 查账</div>
            </div>
        </h3>
        <form class="form-horizontal" id="orderMsg" style="padding: 0px 100px 0px 100px">
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
                    <div class="controls">
                        <input name="dealcode" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">姓名</label>
                    <div class="controls">
                        <input name="realname" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">借款金额</label>
                    <div class="controls">
                        <input name="amount" style="width: 4em;border: none;background-color: inherit" readonly>（元）
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">应催金额</label>
                    <div class="controls">
                        <input name="remainAmmount" style="width: 4em;border: none;background-color: inherit" readonly>（元）
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">逾期天数</label>
                    <div class="controls">
                        <input name="overduedays" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">催收人</label>
                    <div class="controls">
                        <input name="dunningPeople_nickname" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <%--交易查询模板--%>
    <div id="queryRemittanceMessage">
        <h3>
            <div class="row-fluid" style="text-align: center; color: #999; margin-bottom: 40px">
                <div class="span4" style="color: #2fa4e7"><i class="icon-ok"></i> 订单查询</div>
                <div class="span4" style="color: #2fa4e7">② 交易查询</div>
                <div class="span4">③ 查账</div>
            </div>
        </h3>
        <form class="form-horizontal" id="remittanceMsg" style="padding: 0px 100px 0px 100px">
            <div class="control-group">
                <label class="control-label" for="inputRemittanceSerialNumber">支付宝交易流水号</label>
                <div class="controls">
                    <input type="text" id="inputRemittanceSerialNumber" style="width: 350px"/>
                    <a class="btn btn-primary" onclick="findRemittance()">查询</a>
                </div>
            </div>
            <div id="remittanceData" style="display: none">
                <div class="control-group">
                    <label class="control-label">对方账号</label>
                    <div class="controls">
                        <input name="financialremittancename" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">对方姓名</label>
                    <div class="controls">
                        <input name="remittancename" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">交易金额</label>
                    <div class="controls">
                        <input name="remittanceamount" style="width: 4em;border: none;background-color: inherit" readonly>（元）
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">交易时间</label>
                    <div class="controls">
                        <input name="remittancetime" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">备注</label>
                    <div class="controls">
                        <input name="remark" style="border: none;background-color: inherit" readonly>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <%--查账模板--%>
    <div id="auditComfirm">
        <h3>
            <div class="row-fluid" style="text-align: center; color: #999; margin-bottom: 40px">
                <div class="span4" style="color: #2fa4e7"><i class="icon-ok"></i> 订单查询</div>
                <div class="span4" style="color: #2fa4e7"> <i class="icon-ok"></i>交易查询</div>
                <div class="span4" style="color: #2fa4e7">③ 查账</div>
            </div>
        </h3>
        <div class="row-fluid" id="btnGroup">
            <div class="span2 offset1 text-center" style="line-height: 45px">入账标签</div>
            <div class="span4"><a class="btn btn-large" id="REPAYMENT_SELF" onclick="addTag('REPAYMENT_SELF')">本人还款</a></div>
            <div class="span4"><a class="btn btn-large" id="REPAYMENT_THIRD" onclick="addTag('REPAYMENT_THIRD')">第三方还款</a></div>
        </div>
        <div class="row-fluid text-right" style="margin-top: 30px">
            <div id="auditRemittanceData" class="span5 offset1" style="border: 2px solid grey;padding-top: 20px">
                <h4>
                    <div class="row-fluid">
                        <div class="span4" style="color: #2fa4e7">订单信息</div>
                    </div>
                </h4>
                <form class="form-horizontal" id="orderMsgComfirm">
                    <div class="row-fluid">
                        <div class="span4">订单号</div>
                        <div class="span8">
                            <input name="dealcode" style="border: none;background-color: inherit;" readonly>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">姓名</div>
                        <div class="span8">
                            <input name="realname" style="border: none;background-color: inherit" readonly>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">手机号</div>
                        <div class="span8">
                            <input name="mobile" style="border: none;background-color: inherit" readonly>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">应催金额</div>
                        <div class="span8" style="text-align: left">
                            <input name="remainAmmount" style="width: 4em;border: none;background-color: inherit;" readonly>（元）
                        </div>
                    </div>
                </form>
            </div>
            <div id="auditOrderData" class="span5" style="border: 2px solid grey;padding-top: 20px">
                <h4>
                    <div class="row-fluid">
                        <div class="span4" style="color: #2fa4e7">交易信息</div>
                    </div>
                </h4>
                <form class="form-horizontal" id="remittanceMsgComfirm">
                    <div class="row-fluid">
                        <div class="span4">交易流水号</div>
                        <div class="span8">
                            <input name="serialnumber" style="border: none;background-color: inherit" readonly>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">对方姓名</div>
                        <div class="span8">
                            <input name="remittancename" style="border: none;background-color: inherit" readonly>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">对方账号</div>
                        <div class="span8">
                            <input name="financialremittancename" style="border: none;background-color: inherit" readonly>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">交易金额</div>
                        <div class="span8" style="text-align: left">
                            <input name="remittanceamount" style="width: 4em;border: none;background-color: inherit;" readonly>（元）
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
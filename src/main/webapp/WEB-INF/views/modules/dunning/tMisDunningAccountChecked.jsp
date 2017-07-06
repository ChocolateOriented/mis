<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title id="title">已查账</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
      var content; //jbox内容
      var comiting = false ;//提交中标识

      $(document).ready(function () {
        var recordedDetailTemplate = $("#recordedDetailTemplate").html();
        var recordedMarkTemplate = $("#recordedMarkTemplate").html();
        content = {
          state1: {
            content: recordedMarkTemplate,
            buttons: {'下一步': 1},
            buttonsFocus: 0,
            submit: function (v, h, f) {
              var recordedPaytype = $("#recordedPaytype").val();
              if (recordedPaytype == "") {
                jBox.tip("请选择还款类型");
                return false;
              }
              if (needTag) {
                if ($("#recordedRemittanceTag").val() == "") {
                  jBox.tip("请选择还款标签");
                  return false;
                }
              }

              //显示信息
              var remittanceConfirm = $("#recordedForm").data("target");
              var remittanceMsgForm = $(".jbox-container #remittanceMsg");
              var paytypeText = recordedPaytype == "loan" ? "还清" : "部分还款";

              remittanceMsgForm.find("[name=payType]").val(paytypeText);
              remittanceMsgForm.find("[name=remainAmmount]").val(remittanceConfirm.remainAmmount);

              var remittanceamount = remittanceMsgForm.find("[name=remittanceamount]");
              remittanceamount.val(remittanceConfirm.remittanceamount);
              if (remittanceConfirm.remittanceamount != remittanceConfirm.remainAmmount) {
                remittanceamount.css("color", "red");
              }

              $.jBox.nextState();
              return false;
            }
          },
          state2: {
            content: recordedDetailTemplate,
            buttons: {'入账': 1, '上一步': 0},
            buttonsFocus: 0,
            submit: function (v, h, f) {
              if (v == 0) {//上一步
                $.jBox.prevState();
                return false;

              }
              if (comiting) {
                jBox.tip("正在入账请稍后");
                return false;
              }
              //入账
			  var remittanceConfirmId = $("#recordedForm").data("target").remittanceConfirmId ;
              var paytype = $("#recordedPaytype").val();
              var param = {paytype:paytype,remittanceConfirmId:remittanceConfirmId};
              if (needTag){
                var remittanceTag = $("#recordedRemittanceTag").val();
                param.remittanceTag = remittanceTag ;
			  }

			  comiting = true;
              $.post("${ctx}/dunning/tMisRemittanceConfirm/auditConfrim", param, function (msg) {
                comiting = false;
                if (msg == "success") {
                  $.jBox.close();
                  jBox.tip("入账成功");
                  return ;
                }
                $.jBox.tip(msg);
              })
              return false;
            }
          }
        };
      });

      function showDetail(obj) {
        $(".suspense").css("display", "none");
        $(obj).children(".suspense").css("display", "block");
      }

      function hideDetail() {
        var e = window.event || arguments.callee.caller.arguments[0];
        var toElem = e.toElement || e.relatedTarget;
        if (toElem.className != "suspense");
        $(".suspense").css("display", "none");
      }

      function page(n, s) {
        if (n) window.parent.$("#pageNo").val(n);
        if (s) window.parent.$("#pageSize").val(s);
        window.parent.$("#searchForm").attr("action", "${ctx}/dunning/tMisRemittanceMessage/confirmList?childPage=checked");
        window.parent.$("#searchForm").submit();
        return false;
      }

      function collectionfunction(obj, width, height) {
        var dealcode = $(obj).attr("dealcode");
        var buyerId = $(obj).attr("buyerId");
        var dunningtaskdbid = $(obj).attr("dunningtaskdbid");
        var url = "${ctx}/dunning/tMisDunningTask/collectionAmount?&dealcode=" + dealcode + "&buyerId=" + buyerId + "&dunningtaskdbid="
            + dunningtaskdbid;
        $.jBox.open("iframe:" + url, $(obj).attr("value"), width || 600,
            height || 430, {
              buttons: {},
              submit: function (v, h, f) {

              },
              loaded: function (h) {
                $(".jbox-content", document).css("overflow-y",
                    "hidden");
              }
            });
      }

      /**
       * ===================入账======================
       */
      var needTag;//否需要打标签
      function recorded(remittanceConfirmId) {
        //重置表单
        needTag = false;
        comiting = false ;
        resetTag();
        resetType();
        $("#recordedForm").data("target",null);

        //获取remittanceConfirm
        $.post("${ctx}/dunning/tMisRemittanceMessage/findRemittanceMessagChecked", {remittanceConfirmId: remittanceConfirmId},
            function (remittanceConfirm) {
              console.info(remittanceConfirm);
              $("#recordedForm").data("target", remittanceConfirm);
              if (remittanceConfirm.remittanceTag == null || "" == remittanceConfirm.remittanceTag) {
                needTag = true;
              }

              $.jBox(content, {
                title: "查账",
                width: 500,
                height: 350
              });

              if (needTag) {
                $(".jbox-container #tagBtnGroup").show();
              }
            }
        );
      }

      //还款标签
      function addTag(tag) {
        resetTag();
        $(".jbox-container #" + tag).addClass("btn-primary");
        $("#recordedRemittanceTag").val(tag);
      }
      //重置入账标签
      function resetTag() {
        $("#recordedRemittanceTag").val(null);
        $(".jbox-container #tagBtnGroup a").removeClass("btn-primary");
      }
      //还款标签
      function addType(type) {
        resetType();
        $(".jbox-container #" + type).addClass("btn-primary");
        $("#recordedPaytype").val(type);
      }
      //重置入账标签
      function resetType() {
        $("#recordedPaytype").val(null);
        $(".jbox-container #typeBtnGroup a").removeClass("btn-primary");
      }
	</script>
	<style type="text/css">
		.suspense {
			z-index: 10000;
			position: absolute;
			top: 10px;
			left: 10px;
			height: 200px;
			width: 300px;
			background-color: white;
			opacity: 0.9;
			border: solid red 1px;
			border-radius: 5px;
			outline: none;
		}
	</style>
</head>
<body>
	<input type="hidden" id="childIfam" value="checked">
	<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/dunning/tMisRemittanceMessage/checked?child=true">已查账</a></li>
	<li ><a href="${ctx}/dunning/tMisRemittanceMessage/completed?child=true">已完成</a></li>
	</ul> 
	<sys:message content="${message}"/>
	<table id="accountTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>订单编号</th>
				<th>崔收入</th>
				<th>欠款金额</th>
				<th>减免金额</th>
				<th>应还金额</th>
				<th>交易流水号</th>
				<th>查账人</th>
				<th>订单状态</th>
				<th>入账标签</th>
				<th>入账类型</th>
				<th>更新时间</th>
				<th>入账人</th>
				<th>操作</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pagechecked.list}" var="tmessage" varStatus="status">
			<tr>
				
				<td>
				 	${status.count}
				</td>
				<td>
					 ${tmessage.realName } 
				</td>
				<td>
				 	${tmessage.mobile } 
				</td>
				<td>
					 ${tmessage.dealcode } 
				</td>
				<td>
					 ${tmessage.nickName } 
				</td>
				<td>
					  ${tmessage.amount }  
				</td>
				<td>
					 ${tmessage.modifyamount} 
				</td>
				<td>
					  ${tmessage.remainAmmount }
				</td>
				<td >
					 <div name="detail" onmouseover="showDetail(this);" onmouseout="hideDetail();" style="position:relative;">
						 <font  color="red">${ fn:substring(tmessage.remittanceSerialNumber,0,3)}****** 
						 ${ fn:substring(tmessage.remittanceSerialNumber,tmessage.remittanceSerialNumber.length()-3,-1)}
						 </font>
					    <div class="suspense" style="display:none;" tabindex="0">
<%-- 							   <div><font  color="red"> ${tmessage.remittanceSerialNumber }</font></div> --%>
							   <div><font  color="red">------------------------------------------------</font></div>
							   <div>交易时间:<fmt:formatDate value="${tmessage.remittancetime }" pattern="yyyy-MM-dd HH:mm:ss"/></div>
							   <div>交易金额:${tmessage.remittanceamount }</div>
							   <div>对方名称:${tmessage.realName }</div>
							   <div>对方账户:${tmessage.remittanceaccount }</div>
							   <div>备注:${tmessage.remark }</div>
							   <div>上传人:${tmessage.financialUser }</div>
					    </div>
					</div>
				</td>
				<td>
					  ${tmessage.checkedPeople }  
				</td>
				<td>
					  ${tmessage.orderStatus }  
				</td>
				<td>
					  ${tmessage.remittanceTag }  
				</td>

				<td>
					  ${tmessage.payType }
				</td>

				<td>
					 <fmt:formatDate value="${tmessage.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				</td>

				<td>
				<input id="changeSms" onclick="recorded('${tmessage.remittanceConfirmId}')"  class="btn btn-primary" type="button" value="入账"/>
				<input id="derate" class="btn btn-primary" onclick="collectionfunction(this)" buyerId="${tmessage.buyerId}" dunningtaskdbid="${tmessage.dunningtaskdbid}" dealcode="${tmessage.dealcode }"  type="button" value="减免" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${pagechecked}</div>

	<%--入账信息--%>
	<form id="recordedForm">
		<input type="hidden" id="recordedPaytype" name="paytype">
		<input type="hidden" id="recordedRemittanceTag" name="remittanceTag">
	</form>
	<%--=========入账jbox模板==============--%>
	<div id="htmlTemplate" style="display: none">
		<%--还款标记模板--%>
		<div id="recordedMarkTemplate">
			<h3>
				<div class="row-fluid" style="text-align: center; color: #999; margin-bottom: 50px">
					<div class="span6" style="color: #2fa4e7">① 还款标记</div>
					<div class="span6">② 还款明细</div>
				</div>
			</h3>
			<form class="form-horizontal" id="orderMsg">
				<div class="row-fluid" id="tagBtnGroup" style="margin-bottom: 30px;display: none">
					<div class="span2 offset1 text-center" style="line-height: 45px">入账标签</div>
					<div class="span3"><a class="btn btn-large" id="REPAYMENT_SELF" onclick="addTag('REPAYMENT_SELF')">本人还款</a></div>
					<div class="span4"><a class="btn btn-large" id="REPAYMENT_THIRD" onclick="addTag('REPAYMENT_THIRD')">第三方还款</a></div>
				</div>
				<div class="row-fluid" id="typeBtnGroup">
					<div class="span2 offset1 text-center" style="line-height: 45px">还款类型</div>
					<div class="span3"><a class="btn btn-large" id="loan" onclick="addType('loan')">还清</a></div>
					<div class="span4"><a class="btn btn-large" id="partial" onclick="addType('partial')">部分还款</a></div>
				</div>
			</form>
		</div>
		<%--还款明细模板--%>
		<div id="recordedDetailTemplate">
			<h3>
				<div class="row-fluid" style="text-align: center; color: #999; margin-bottom: 20px">
					<div class="span6"><i class="icon-ok"></i> 还款标记</div>
					<div class="span6" style="color: #2fa4e7">② 还款明细</div>
				</div>
			</h3>
			<form class="form-horizontal" id="remittanceMsg">
				<div id="recordedData">
					<div class="control-group">
						<label class="control-label">还款类型</label>
						<div class="controls">
							<input name="payType" style="border: none;background-color: inherit" readonly>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">应催金额</label>
						<div class="controls">
							<input name="remainAmmount" style="border: none;background-color: inherit" readonly>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">还款金额</label>
						<div class="controls">
							<input name="remittanceamount" style="border: none;background-color: inherit;" readonly>（元）
						</div>
					</div>

				</div>
			</form>
		</div>
	</div>
</body>
</html>
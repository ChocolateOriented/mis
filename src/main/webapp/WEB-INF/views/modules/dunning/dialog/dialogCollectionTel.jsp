<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收短信</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		var effective = [{value:"PTP", text:"承诺还款（PTP）"},
		                 {value:"RTP", text:"拒绝还款（RTP）"},
		                 {value:"WTP", text:"有还款意愿"},
		                 {value:"WTR", text:"有代偿意愿"},
		                 {value:"CAIN", text:"客户回电"},
		                 {value:"CMIN", text:"沟通中"},
		                 {value:"PYD", text:"已还款"},
		                 {value:"OTHER", text:"其他"}
		                 ];
		var uneffective = [{value:"NSA", text:"非本人接听"},
		                   {value:"NSN", text:"非本人号码"},
		                   {value:"OOC", text:"完全失联"},
		                   {value:"OTHER", text:"其他"}
		                   ];
		
		$('#smsSave').click(function() {
			 if($("#inputForm").valid()){
				 $("#smsSave").attr('disabled',"true");
	                $.ajax({
	                    type: 'POST',
	                    url : "${ctx}/dunning/tMisContantRecord/saveRecord",
	                    data: $('#inputForm').serialize(),             //获取表单数据
	                    success : function(data) {
	                        if (data == "OK") {
	                            alert("保存成功");
	                            //window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
	                            window.parent.window.jBox.close();            //关闭子窗体
	                        } else {
	                            alert("保存失败:"+data.message);
	                            //window.parent.page();
	                            window.parent.window.jBox.close();
	                        }
	                    },
	                    error : function(XMLHttpRequest, textStatus, errorThrown){
	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
	                       alert("保存失败:"+textStatus);
	                    }
	                });
		          }
		}); 
		
		$("#contactstype").click(function(){
			telEmpty();
			if($("#contactstype").val().toLowerCase()=="self" || $("#contactstype").val().toLowerCase()=="married" || $("#contactstype").val().toLowerCase()=="parent"
				|| $("#contactstype").val().toLowerCase()=="children" || $("#contactstype").val().toLowerCase()=="relatives"
				|| $("#contactstype").val().toLowerCase()=="workmate" || $("#contactstype").val().toLowerCase()=="worktel"  || $("#contactstype").val().toLowerCase()=="friend"){
				$.ajax({
	                    type: 'POST',
	                    url : "${ctx}/dunning/tMisContantRecord/getTelInfos",
	                    data: {buyerId:$("#buyerId").val(),type:$("#contactstype").val()},             
	                    success : function(data) {
	                    	console.log(data);
	                    	if(data.length>0){
	                    		$("#contanttarget").val(data[0].tel);
	                    	}
	                    	
	                    }
				})
			}else{
				var url = "${ctx}/dunning/tMisContantRecord/dialogTelInfos?buyerId=" + $("#buyerId").val() + "&type=" + $("#contactstype").val() +"&dialogType=tel&dealcode=${dealcode}";
				$.jBox.open("iframe:" + url , $("#contactstype option:selected").text() + "手机号码" , 530, 250, {
	               buttons: { "确定": "ok", "取消": true },
	                   submit: function (v, h, f) {
	                       if (v == "ok") {
	                    	   telEmpty();
	                           var iframeName = h.children(0).attr("name");
	                           var iframeHtml = window.frames[iframeName];
	                           check_val = [];
	                           var check_name = [];
	                           var sendMsgs = iframeHtml.document.getElementsByName("sendMsgInfo");
	                           for(k in sendMsgs){
		                       		if(sendMsgs[k].checked) {
		                       			check_val.push(sendMsgs[k].value);
		                       			check_name.push(sendMsgs[k].attributes["namevalue"].value);
		                       		}
		                       }
	                           $("#contanttarget").val(check_val);
	                           $("#contactsname").val(check_name.toString() || "未知");
	                           
	                           return true;
	                       }
	                   },
	               loaded: function (h) {
	                   $(".jbox-content", document).css("overflow-y", "hidden");
	               }
	         	});
			}
		 });
			
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		}); 
		
		//是否有效联络选择事件
		$("input[name=iseffective]").change(function() {
			var checked = $("input[name=iseffective]:checked").val();
			var resultCode = $("#telstatus");
			resultCode.empty();
			$("#s2id_telstatus span.select2-chosen").text("");
			$("div[name='promisepaydateGroup']").css("display", "none");
			$("#promisepaydate").val("");
			var options = checked == "1" ? effective : uneffective;
			resultCode.append($("<option></option>").val("").text(""));
			for (var i = 0; i < options.length; i++) {
				resultCode.append($("<option></option>").val(options[i].value).text(options[i].text));
			}
		});
		
		$("input[name=iseffective]").change();
		
		//结果代码选择事件
		$("#telstatus").change(function() {
			var selected = $("#telstatus").val();
			var show = selected == "PTP" ? "block" : "none";
			$("div[name='promisepaydateGroup']").css("display", show);
			$("#promisepaydate").val("");
		});
	});
	
	function telEmpty(){
		$("#contanttarget").empty();
		$("#contanttarget").val("");
		$("#contactsname").val("");
	}
	</script>
</head>
<body>
<ul class="nav nav-tabs">

	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisContantRecord"  class="form-horizontal">
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<div class="control-group">
			<div style="width:20%;display:inline-block;text-align:right;">
				<label>联系对象：</label>
			</div>
			<div style="width:25%;display:inline-block;">
				<select class="input-small" path="" id="contactstype" name="contactstype">
					<option value=""></option>
					<option value="SELF" <c:if test="${'SELF' eq contactstype}">selected</c:if>>本人</option>
					<option value="MARRIED" <c:if test="${'MARRIED' eq contactstype}">selected</c:if> >夫妻 </option>
					<option value="PARENT"<c:if test="${'PARENT' eq contactstype}">selected</c:if>>父母</option>
					<option value="CHILDREN"<c:if test="${'CHILDREN' eq contactstype}">selected</c:if>>子女</option>
					<option value="RELATIVES"<c:if test="${'RELATIVES' eq contactstype}">selected</c:if>>亲戚</option>
					<option value="FRIEND"<c:if test="${'FRIEND' eq contactstype}">selected</c:if>>朋友</option>
					<option value="WORKMATE"<c:if test="${'WORKMATE' eq contactstype}">selected</c:if>>同事</option>
					<option value="WORKTEL"<c:if test="${'WORKTEL' eq contactstype}">selected</c:if>>工作电话</option>
					<option value="CANTACT"<c:if test="${'CANTACT' eq contactstype}">selected</c:if>>通讯录</option>
					<option value="COMMUNCATE"<c:if test="${'COMMUNCATE' eq contactstype}">selected</c:if>>通话记录</option>
				</select>
			</div>
			<div style="width:18%;display:inline-block;text-align:right;">
				<label>联系人姓名：</label>
			</div>
			<div style="width:35%;display:inline-block;">
				<input class="input-small" path="" id="contactsname" name="contactsname" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<div style="width:20%;display:inline-block;text-align:right;">
				<label>电话号码：</label>
			</div>
			<div style="width:50%;display:inline-block;">
<%-- 				<form:input path="" htmlEscape="false" maxlength="10" class="input-xlarge required digits"/> --%>
				<input path="" id="contanttarget" name="contanttarget" htmlEscape="false"  class="input-small required" value="${contactMobile}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<div style="width:20%;display:inline-block;text-align:right;">
				<label>是否有效联络：</label>
			</div>
			<div style="width:25%;display:inline-block;">
				<div style="display:inline-block;">
					<input path="" type="radio" name="iseffective" htmlEscape="false" value="1" checked/>是
					<input path="" type="radio" name="iseffective" htmlEscape="false" value="0"/>否
				</div>
			</div>
			<div style="width:18%;display:inline-block;text-align:right;">
				<label>结果代码：</label>
			</div>
			<div style="width:35%;display:inline-block;">
				<select path="" class="input-small required" id="telstatus" name="telstatus">
					<!-- <option value=""></option>
					<option value="TNIS">号码停机</option>
					<option value="NOPK">手机拒接</option>
					<option value="BUSY">电话忙音</option>
					<option value="NOBD">查无此人</option>
					<option value="NOEX">号码不存在</option>
					<option value="MEMO">一般纪要</option>
					<option value="BDQJ">客户离职</option>
					<option value="NOAS">无人应答</option>
					<option value="LMS">留口信</option>
					<option value="PTP">承诺还款</option>
					<option value="OPTP">他人代偿</option>
					<option value="CAIN">客户回电</option>
					<option value="STOP">暂缓催收</option>
					<option value="FEAD">费用调整</option>
					<option value="OFF">电话关机</option>
					<option value="LDTX">来电提醒</option>
					<option value="OTHER">其他</option> -->
				</select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div name="promisepaydateGroup" class="control-group" style="display:none;">
			<div style="width:20%;display:inline-block;text-align:right;">
				<label>用户承诺还款日：</label>
			</div>
			<div style="width:50%;display:inline-block;">
				<input id="promisepaydate" name="promisepaydate" type="text" readonly="readonly" maxlength="20" class="Wdate required" style="width:150px"
					value="<fmt:formatDate value="${TMisContantRecord.promisepaydate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'%y-%M-%d'});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<div style="width:20%;display:inline-block;text-align:right;">
				<label>备注：</label>
			</div>
			<div style="width:75%;display:inline-block;">
<%-- 				<form:input path="" htmlEscape="false" maxlength="10" class="input-xlarge required digits"/> --%>
				<textarea style="height:80px;width:80%;" path="" id="remark" name="remark" htmlEscape="false" maxlength="255"></textarea>
			</div>
		</div>
		<input style="display:none;" id="contanttype" name="contanttype" value="tel" />
		<input style="display:none;" id="buyerId" name="buyerId" value="${buyerId}" />
		<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		<input style="display:none;" id="dunningtaskdbid" name="dunningtaskdbid" value="${dunningtaskdbid}" />
		<div style= "padding:19px 180px 20px;" >
			<input id="smsSave" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
</body>
</html>


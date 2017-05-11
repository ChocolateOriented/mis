<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电催结论</title>
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
		                   {value:"HOOC", text:"半失联"},
		                   {value:"OTHER", text:"其他"}
		                   ];
		
		var nextDate = {};
		
		function initRemark() {
			var targetArr = window.parent.window.target;
			var cnt = 0;
			var targetMap = {};
			//根据电话号码去重
			for (var i = 0; i < targetArr.length; i++) {
				var o = targetArr[i];
				if (targetMap[o.contanttarget]) {
					if (o.dunningtime > targetMap[o.contanttarget].dunningtime) {
						targetMap[o.contanttarget] = o;
					}
				} else {
					if (++cnt > 20) {
						break;
					}
					targetMap[o.contanttarget] = o;
				}
			}
			
			var remark = "";
			for (var k in targetMap) {
				remark = remark + targetMap[k].contactstype + (targetMap[k].contactsname ? "-" + targetMap[k].contactsname : "") + " " + targetMap[k].contanttarget + " " + targetMap[k].resultcode + "/";
			}
			remark = remark.substring(0, remark.length - 1);
			if (cnt > 20) {
				remark += "...";
			}
			$("#remark").val(remark);
		}
		
		initRemark();
		
		$('#save').click(function() {
			if($("#inputForm").valid()){
				$("#save").attr('disabled',"true");
					$.ajax({
					type: 'POST',
					url : "${ctx}/dunning/tMisDunnedConclusion/saveRecord",
					data: $('#inputForm').serialize(),             //获取表单数据
					success : function(data) {
						if (data == "OK") {
							alert("保存成功");
							//window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
							window.parent.window.location.reload();
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
		
		$('#esc').click(function() {
			window.parent.window.jBox.close();    
		});

		//是否有效联络选择事件
		$("input[name=iseffective]").change(function() {
			var checked = $("input[name=iseffective]:checked").val();
			var resultCode = $("#resultcode");
			resultCode.empty();
			$("#s2id_resultcode span.select2-chosen").text("");
			$("div[name='promisepaydateGroup']").css("display", "none");
			
			var options;
			var show;
			if (checked == "1") {
				options = effective;
				show = "inline-block";
			} else {
				options = uneffective;
				show = "none";
				$("#promisepaydate").val("");
				$("#overduereason").val("");
				$("#s2id_overduereason span.select2-chosen").text("");
				$("#nextfollowdate").val("");
			}
			
			resultCode.append($("<option></option>").val("").text(""));
			for (var i = 0; i < options.length; i++) {
				resultCode.append($("<option></option>").val(options[i].value).text(options[i].text));
			}
			$("div[name='overduereasonGruop']").css("display", show);
		});

		//结果代码选择事件
		$("#resultcode").change(function() {
			var selected = $("#resultcode").val();
			var show = selected == "PTP" ? "inline-block" : "none";
			$("div[name='promisepaydateGroup']").css("display", show);
			$("#promisepaydate").val("");
			var day = nextDate[selected] || 0;
			var followDate = addDate(new Date(), day);
			var month = followDate.getMonth() + 1;
			if (month < 10) {
				month = "0" + month.toString();
			}
			var date = followDate.getDate();
			if (date < 10) {
				date = "0" + date.toString();
			}
			$("#nextfollowdate").val(followDate.getFullYear() + "-" + month + "-" + date);
		});
		
        $.ajax({
            type: 'POST',
            url : "${ctx}/dunning/tMisDunnedConclusion/nextfollowdate",
            success : function(data) {
            	nextDate = data;
            	
        		//根据勾选actions初始化是否有效联络
        		$("input[name=iseffective][value='" + window.parent.iseffective + "']").prop("checked", true);
        		$("input[name=iseffective]:checked").change();
            	
        		//根据勾选actions初始化结果代码
        		if (window.parent.resultcode == "PTP") {
        			var resultcode = $("#resultcode");
        			resultcode.val(window.parent.resultcode);
        			$("#s2id_resultcode span.select2-chosen").text(resultcode.text());
        			resultcode.change();
        		}
            }
        });
	});

	</script>
</head>
<body>
<ul class="nav nav-tabs">

	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="TMisDunnedConclusion" class="form-horizontal">
<%-- 		<form:hidden path="id"/> --%>
<%-- 		<sys:message content="${message}"/>		 --%>
		<div class="control-group">
			<div style="width:15%;display:inline-block;text-align:right;">
				<label>是否有效联络：</label>
			</div>
			<div style="padding:3px 0px 5px 0px;width:30%;display:inline-block;">
				<div style="display:inline-block;">
					<input path="" type="radio" name="iseffective" htmlEscape="false" value="1"/>是
					<input path="" type="radio" name="iseffective" htmlEscape="false" value="0"/>否
				</div>
			</div>
			<div name="overduereasonGruop" style="width:16%;display:inline-block;text-align:right;">
				<label>逾期原因：</label>
			</div>
			<div name="overduereasonGruop" style="width:36%;display:inline-block;">
				<select id="overduereason" name="overduereason" path="" class="input-small required">
					<option value=""></option>
					<option value="FTP">忘记还款</option>
					<option value="ECOH">经济困难</option>
					<option value="MULO">多头共债</option>
					<option value="RODM">扣款原因</option>
					<option value="NAL">不在本地</option>
					<option value="ARGU">争议</option>
					<option value="BKR">破产</option>
					<option value="UNEM">失业</option>
					<option value="DIS">疾病</option>
					<option value="ARTD">被公安逮捕或入狱</option>
					<option value="DEAD">死亡</option>
					<option value="OTHER">其他原因</option>
				</select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<div style="width:15%;display:inline-block;text-align:right;">
				<label>结果代码：</label>
			</div>
			<div style="padding:1px 0px 1px 0px;width:30%;display:inline-block;">
				<select path="" class="input-small required" id="resultcode" name="resultcode">
				</select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
			<div name="promisepaydateGroup" style="width:16%;display:none;text-align:right;">
				<label>用户承诺还款日：</label>
			</div>
			<div name="promisepaydateGroup" style="width:36%;display:none;">
				<input id="promisepaydate" name="promisepaydate" type="text" readonly="readonly" maxlength="20" class="Wdate required" style="width:150px"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, minDate:'%y-%M-%d'});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<div style="width:15%;display:inline-block;text-align:right;">
				<label>下次跟进时间：</label>
			</div>
			<div style="width:30%;display:inline-block;">
				<input class="input-small" path="" type="text" id="nextfollowdate" name="nextfollowdate" htmlEscape="false" readonly="readonly"/>
			</div>
		</div>
		
		<div class="control-group">
			<div style="width:15%;display:inline-block;text-align:right;">
				<label>备注：</label>
			</div>
			<div style="width:75%;display:inline-block;">
				<textarea style="height:80px;width:85%;" path="" id="remark" name="remark" htmlEscape="false" maxlength="1000"></textarea>
			</div>
		</div>
		
		<input style="display:none;" id="buyerId" name="buyerId" value="${buyerId}" />
		<input style="display:none;" id="dealcode" name="dealcode" value="${dealcode}" />
		<input style="display:none;" id="dunningtaskdbid" name="dunningtaskdbid" value="${dunningtaskdbid}" />
 		<c:forEach items="${actions}" var="action" varStatus="vs">
 			<input style="display:none;" name="actions" value="${action}" />
 		</c:forEach>
		<div style= "padding:19px 180px 20px;" >
			<input id="save" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form:form>
</body>
</html>


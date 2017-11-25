<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>电话模板</title>
<meta name="decorator" content="default"/>
<style type="text/css">
	.numbers-container {
		width: 50px;
		height: 50px;
		background-color: white;
		border-radius: 25px;
		border: solid rgb(100, 100, 100) 1px;
		float: left;
		margin: 10px 15px;
		cursor: pointer;
		overflow: hidden;
		position: relative;
	}
	
	.number-content {
		height: 50px;
		line-height: 50px;
		display: block;
		color: black;
		text-align: center;
	}
	
	.numbers-container  :active {
		background: rgba(104, 208, 249, .4);
	}
	
	a :active {
		color: pink;
	}
	
	.delete-btn {
		float: right;
		margin-right: 10px;
		cursor: pointer;
		font-size: 22px;
		color: gray;
	}
	
	.calling {
		text-align: center;
		font-size: 80px;
		margin: 30px 0 30px 75px;
		border-radius: 150px;
		border: solid rgb(100, 100, 100) 1px;
		width: 100px;
		height: 100px;
	}
	
	.select2-arrow {
		background: white;
	}
	
	.status-display {
		white-space: nowrap;
		background-color: #fff;
		font-weight: bold;
	}
	
	.status-li-container {
		background-color: #fff;
		border: solid #5897fb 1px;
		border-radius: 3px;
		padding: 1px;
		box-shadow: 2px 2px 5px #999999;
		position: absolute;
		top: 20px;
		left: -1px;
		z-index: 100000;
	}
	
	.status-li {
		list-style-type: none;
	}
	
	.status-li-highlight:hover {
		background: #3875d7;
		color: #fff;
	}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		//加载子页面
		 if (null == currentPageUrl){
			callinfo();
        }
		//第一次打开该页面判断用户是否有坐席没有不让操作软电话页面
		if(!"${agent}"){
			$("#empty").css("display","none");
			alert("请绑定坐席!");
		}
		
		//删除号码值
		$('.delete-btn').click(function() {
			var numbers = $('#numberValue').val();
			var length = $('#numberValue').val().length;
			$("#numberValue").val(numbers.substr(0, length - 1));
		});
	});
	
	//添加号码值
	function addNumber(value) {
		$("#numberValue").val($("#numberValue").val() + value);
	}
	
	//点击呼叫
	function clickCall(obj) {
		var number = $("#numberValue").val();
		if(!$("#numberValue").val()){
			return;
		}

		var operation = $(obj).attr("operation");
		var msg = {operation: operation, target: number};
		webSocket.send(JSON.stringify(msg));
		
	}
	
	//点击接听电话
	function callingPhone(obj) {
		var operation = $(obj).attr("operation");
		var msg = {operation: operation};
		webSocket.send(JSON.stringify(msg));
	}
	
	//点击保持通话
	function continueCalling() {
		var msg = {operation: $("#holdText").attr("operation"), target: $("#numberValue").val() || ""};
		webSocket.send(JSON.stringify(msg));
	}
	
	//点击中断通话或中断呼叫
	function refuseCalling(obj) {
		stopTime();
		$("#holdText").html("保持");
		var msg = {operation: $(obj).attr("operation"), target: $("#numberValue").val() || ""};
		webSocket.send(JSON.stringify(msg));
	}
	
	function changePhone() {
		$("#numberValue").attr("readonly", true);
		$("#deleteNumber").hide();
	}
	
	//计时器(通话时间)
	var timeTask = null;
	function time_fun() {
		var sec = 0;
		stopTime();
		$("#callingTime").html("00:00:00");
		timeTask = setInterval(function() {
			sec++;
			var date = new Date(0, 0)
			date.setSeconds(sec);
			var h = date.getHours(), m = date.getMinutes(), s = date.getSeconds();
			$("#callingTime").html(callingtime(h) + ":" + callingtime(m) + ":" + callingtime(s));
	    }, 1000);
	}
	
	function callingtime(n) {
		return n >= 10 ? n : "0" + n;
	}
	
  	//关闭计时器
	function stopTime() {
		if(timeTask) {
			clearInterval(timeTask);
		}
	}
  	
	//桌面通知
	function showNotice(title, msg, icon) {
		var Notification = window.Notification || window.mozNotification || window.webkitNotification;
		if (!Notification) {
			alert("您的浏览器不支持桌面消息,谢谢!");
		}
		Notification.requestPermission(function(status) {
            //status默认值'default'等同于拒绝 'denied' 意味着用户不想要通知 'granted' 意味着用户同意启用通知
			if ("granted" != status) {
				return;
			}
			var tag = "sds" + Math.random();
			var notify = new Notification(
				title,
				{
					dir: 'auto',
					lang: 'zh-CN',
// 					tag: tag,//实例化的notification的id
					icon: icon,//通知的缩略图,//icon 支持ico、png、jpg、jpeg格式
					body: msg //通知的具体内容
				}
			);
			//消息显示、被点击、被关闭和出错的时候被触发
			notify.onclick = function() {
				window.focus();
				notify.close();
			}
			notify.onerror = function() {
				//alert("桌面消息出错！！！联系管理员");
			}
			notify.onshow = function() {
				setTimeout(function() {
					notify.close();
				}, 1000*120);
			}
			notify.onclose = function() {
			}
		});
	}
	
	//收到服务端的消息
	function onMessage(event) {
		data = JSON.parse(event.data);
		
		if (data.result == "error") {
			alert(data.msg || "操作失败");
			return;
		}
		
 		//判断是离线状态
  		if (data.operation == "Logged Out") {
  			hidePhoneDial();
			//离线时通道关闭
			closeWebSocket();
			return;
		}
 		
 		//判断是在线或小休状态
  		if (data.operation == "Available" || data.operation == "On Break") {
  			showPhoneDial(data.operation);
			return;
		}
		
		//判断是来电
		if (data.operation == "ringing") {
			//呼叫中或通话中不处理ringing消息
			if($("#calling").css("display") == "block" || $("#callingPhone").css("display") == "block"){
				return;
			}
			changePhone();
			$("#statusPhones").attr("readonly", true);
			
			$(".showName").html(data.name || '未知');
			$(".phoneStatus").css("display", "none");
			$("#backCall").css("display", "block");
			$(".numberSave").css("display", "block");
			$("#numberValue").val(data.target);
			$("#continues").removeClass("icon-play");
			$("#continues").addClass("icon-pause");
			var icon = "${ctxStatic}/images/userinfo.jpg";
			showNotice('来电提醒', '点击去接听电话', icon);  
			return;
		}
		//空闲状态
		if (data.operation == "available") {
			stopTime();
			$("#holdText").html("保持")
			var msg = {operation: $(obj).attr("operation"), target: $("#numberValue").val() || ""};
			$("#statusPhones").attr("readonly", false);
			$("#numberValue").attr("readonly", false);
			$(".showName").html("");
			$("#numberValue").val("");
			$(".phoneStatus").css("display", "none");
			$("#initPhone").css("display", "block");
			$(".numberSave").css("display", "block");
			$("#deleteNumber").show();
			return;
		}
		//通话中状态
		if (data.operation == "oncall") {
			changePhone();
			$(".phoneStatus").css("display", "none");
			$("#callingPhone").css("display", "block");
			$("#holdText").html("保持");
			$("#holdText").attr("operation", "hold");
			time_fun();
			return;
		}
		
		//呼叫成功
		if (data.operation == "originate") {
			changePhone();
			if(data.target)
			$("#numberValue").val(data.target);
			$(".showName").html(data.name);
			$(".phoneStatus").css("display", "none");
			$("#calling").css("display", "block");
			$("#statusPhones").attr("readonly", true);
			
			return;
		}
		

		//保持成功
		if (data.operation == "hold") {
			$("#holdText").html("取消保持");
			$("#continues").removeClass("icon-pause");
			$("#continues").addClass("icon-play");
			$("#holdText").attr("operation", "hold_off");
			return;
		}
		
		//取消保持成功
		if (data.operation == "hold_off") {
			$("#holdText").html("保持");
			$("#holdText").attr("operation", "hold");
			$("#continues").removeClass("icon-play");
			$("#continues").addClass("icon-pause");
			return;
		}

	}
    	    
	//建立通道
	var webSocket = null;
	function callConect(status) {
  	    webSocket = new WebSocket('${url}/ws/phone/${userId}/${agent}/' + status);

		webSocket.onerror = function(event) {
		};
		webSocket.onopen = function(event) {
		};
		webSocket.onmessage = function(event) {
			onMessage(event);
		};
		webSocket.onclose = function(event) {
			webSocket = null;
			hidePhoneDial();
		};
	}
	
	//关闭通道
	function closeWebSocket() {
		if (webSocket) {
			webSocket.close();
			webSocket = null;
		}
	}
	
	//显示电话键盘
	function showPhoneDial(operation) {
		$("#statusHolder").text(operation == "On Break" ? "小休" : "在线");
  		$("#statusHolder").attr("value", operation);
		$(".phoneStatus").css("display", "none");
		$("#initPhone").css("display", "block");
		$(".numberSave").css("display", "block");
		$("#numberValue").attr("readonly",false );
		$("#deleteNumber").show();
		
		
	}
	
	//隐藏电话键盘
	function hidePhoneDial() {
		$("#statusHolder").text("离线");
		$("#statusHolder").attr("value", "Logged Out");
		$(".phoneStatus").css("display","none");
		$(".numberSave").css("display","none");
		$("#numberValue").val("");
		$("#empty").css("display","block");
	}
	
	//显示坐席状态列表
	function showList() {
		var cssDiv1=$("#initPhone").css("display");
		var cssDiv2=$("#empty").css("display");
		if(cssDiv1 != 'block'&&cssDiv2 != 'block'){
			return;
		}
		$("#statusList").css("display", "block");
		$("#statusList").focus();
	}
	
	//隐藏坐席状态列表
	function hideList() {
		$("#statusList").css("display", "none");
	}
	
	//变更坐席状态
	function chooseStatus(obj) {
		hideList();
		var curVal = $("#statusHolder").attr("value");
		var operation = $(obj).attr("operation");
		if (curVal == operation) {
			return;
		}
		
		if (operation == "Logged Out") {
			hidePhoneDial();
			//离线时通道关闭
			closeWebSocket();
			return;
		} 
		
   		//小休或在线
		if(operation != "Available" && (operation != "On Break")) {
			return;
		}
		
		if (!webSocket) {
			callConect(operation);
		} else {
			var msg = {operation: operation};
			webSocket.send(JSON.stringify(msg));
		}
	}
	
	var currentPageUrl = null;
	//呼叫信息
	function callinfo(obj) {
		currentPageUrl = "${pageContext.request.contextPath}/f/numberPhone/" + (obj ? ($(obj).attr("typecall") || "callAll") : "callAll") + "?";
		var type = obj ? $(obj).attr("typecall") : "callAll";
		$("#callinfoTab li").removeClass("active");
		$("#" + type).addClass("active");
		if (type == "callBusy") {
			$("#agent").attr("disabled", false);
			$("#queue").attr("disabled", true);
		} else if (type == "callQueueOff") {
			$("#agent").attr("disabled", true);
			$("#queue").attr("disabled", false);
		} else {
			$("#agent").attr("disabled", false);
			$("#queue").attr("disabled", false);
		}
		$("#page").val("");
		$("#pageSize").val("");
		page();
	}
	function page(n, s) {
		if (n) $("#pageNo").val(n);
		if (s) $("#pageSize").val(s);
		$("#ifm").attr("src", currentPageUrl + $("#searchForm").serialize());
		return false;
	}
	
	function setCallNumber(number) {
		if (("#statusHolder").attr("value") && ("#statusHolder").attr("value") != "Logged Out") {
			$("#numberValue").val(number);
		}
	}
	
	function checkNumber(obj) {
		var value = $(obj).val();
		var reg = /\D/g;
		var newValue = value.replace(reg, "");
		if (value != newValue) {
			$(obj).val(newValue);
		}
	}
</script>

</head>
<body >
<div id="messages"></div>
 <div style="width:270px; height:620px;position:absolute; border:1px solid gold;border-radius: 25px;background-color:#F8F8F8; left:2%;" >
 	<div style="height:60px;width:280px;position:absolute;">
 		<div style="top:20px;left:120px;position:absolute;margin:0 auto;">
<!-- 			<i class="icon-legal" style="font-size:15px;color:gray;"></i> -->
			<font size="4" color="orange">mo</font><font color="blue">9</font>
 		</div>
 	</div>
	<div style="width:250px; height:500px;margin:0px;top:60px;left:10px; position:absolute; border:1px solid gold;background-color:white;">
		<div style="height:10%;">
			<div style="height:50%;margin: 10px;">
				<i><font face="微软雅黑" size="4" color="black" style="float: left;">电话服务</font></i>
				<div style="float:right;position:relative;" id="statusPhones">
					<div style="cursor: pointer;" onclick="showList();" >
						<span id="statusHolder" class="status-display" value="Logged Out">离线</span>
						&nbsp;<i class="icon-double-angle-down"></i>
					</div>
					<div id="statusList" class="status-li-container" style="display:none;" tabindex="0" onblur="hideList();">
						<ul style="margin:4px;">
							<li class="status-li status-li-highlight">
								<div style="white-space:nowrap;padding:3px;" operation="Logged Out" onclick="chooseStatus(this);">离线</div>
							</li>
							<li class="status-li status-li-highlight">
								<div style="white-space:nowrap;padding:3px;" operation="Available" onclick="chooseStatus(this);">在线</div>
							</li>
							<li class="status-li status-li-highlight">
								<div style="white-space:nowrap;padding:3px;" operation="On Break" onclick="chooseStatus(this);">小休</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div style="height:50%;">
			<em><font color="gray" size="1" style="float: left;">本机号码:${direct}</font></em>
			</div>
		</div>
		<hr/>
		<div style="height:8%;display:none" class="numberSave">
			<input id="numberValue" style="margin:0 0 30px 50px ;height:30px;width:150px;border:none;font-size:20px" placeholder="点此输入号码" oninput="checkNumber(this);"/>
			<a id="deleteNumber" class="delete-btn" style="text-decoration:none;"><i class="icon-remove"></i></a>
		</div>
		<%--电话键盘初始化 --%>
		<div class="phoneStatus" id="empty" style="display:block"></div>
		<div  class="phoneStatus" id="initPhone"style="left:10px;display:none">
			 <div style="height:60%;">
				<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('1')" >1</span>
			   	</div>
				<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('2')">2</span>
			   	</div>
				<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('3')">3</span>
			   	</div>
				<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('4')">4</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('5')">5</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('6')">6</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('7')">7</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('8')">8</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('9')">9</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content " onclick="addNumber('*')" style="margin: 5px;font-size: 30px;">*</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('0')">0</span>
			   	</div>
			   	<div class="numbers-container">
			        <span class="number-content" onclick="addNumber('#')">#</span>
			   	</div>
			 </div>
			 <div onclick="clickCall(this)" class="numbers-container" style="background-color:green;border:0px; margin: 0px 0px 0px 100px;" operation="originate">
			        <i class="number-content  icon-phone" style="color:white;font-size:20px; "></i>
			 </div>
		</div>
		<%--呼叫中状态 --%>
		<div class="phoneStatus" id="calling" style="height:60%;display:none">
			<div style="text-align: center;margin:20px">
				正在呼叫...
			</div>
			<div class="calling">
				<i class="icon-user" ></i>
			</div>
			<div class="showName" style="text-align: center;margin-bottom:20px; "></div>
			<div class="numbers-container" style="background-color:red;border:0px; margin: 30px 0px 10px 100px;transform:rotate(140deg);">
		        <i class="number-content icon-phone" style="color:white;font-size:20px; " onclick="refuseCalling(this)" operation="hangup"></i>
		 	</div>
		</div>
		<%--通话中状态 --%>
		<div class="phoneStatus" id="callingPhone" style="height:60%;display:none">
			<div style="text-align: center;margin:20px">
				<h4 id="callingTime">00:00:00</h4>
			</div>
			<div class="calling">
				<i class="icon-user"></i>
			</div>
			<div class="showName" style="text-align: center;margin-bottom:20px; "></div>
			<div class="numbers-container" style="background-color:red;border:0px;margin: 30px 0px 10px 30px;transform:rotate(140deg);">
		        <i class="number-content icon-phone" style="color:white;font-size:20px; "onclick="refuseCalling(this)" operation="hangup"></i>
		 	</div>
			<div class="numbers-container" style="background-color:orange;border:0px; margin: 30px 0px 10px 100px;">
		        <i id="continues" class="number-content icon-pause" style="color:white;font-size:20px; " onclick="continueCalling(this)"></i>
		 	</div>
		 	<div >
			 	<i style="float:left;margin-left:35px;">拒绝</i>
			 	<i style="float:right;margin-right:35px;" id="holdText" operation="hold">保持</i>
		 	</div>
		</div>
		<%--来电中状态 --%>
		<div class="phoneStatus" id="backCall" style="height:60%;display:none;">
			<div class="calling">
				<i class="icon-user" ></i>
			</div>
			<div class="showName" style="text-align: center;margin-bottom:20px; "></div>
			<div class="numbers-container" style="background-color:red; margin: 30px 0px 10px 30px;transform:rotate(140deg);">
		        <i class="number-content icon-phone" style="color:white;font-size:20px;" onclick="refuseCalling(this)" operation="hangup"></i>
		 	</div>
		 	<div class="numbers-container" style="background-color:green; margin: 30px 0px 10px 100px;">
		        <i class="number-content icon-phone" style="color:white;font-size:20px;" onclick="callingPhone(this)" operation="answer"></i>
			 </div>
		 	<div >
			 	<i style="float:left;margin-left:35px;">拒绝</i>
			 	<i style="float:right;margin-right:35px;">接听</i>
		 	</div>
		</div>
		
	</div>
	<div style=" height:60px;width:280px;top:560px;position:absolute;">
		<div class="numbers-container" style="top:5px;left:110px;position:absolute;margin:0px;">
		<i  class=" icon-circle-blank" style="font-size:50px;color:#E0E0E0;margin-left:4px;"></i>
		</div>
	</div>
 </div>
 <div style="width:1200px;height:800px;position:absolute;background-color:white;left:30%;" >
 <ul id="callinfoTab" class="nav nav-tabs">
    <li id="callAll" class="active" style="cursor: pointer;"><a typecall="callAll" onclick="callinfo(this)">全部</a></li>
    <li id="callBusy" style="cursor: pointer;"><a typecall="callBusy" onclick="callinfo(this)">未接</a></li>
    <li id="callQueueOff" style="cursor: pointer;"><a typecall="callQueueOff" onclick="callinfo(this)">队列中放弃</a></li>
</ul>
<iframe id="ifm" name="ifm" frameborder="0" style="width:1200px;height:800px;"></iframe>
<form id="searchForm" method="post" >
    <input id="page" name="page" type="hidden" value=""/>
    <input id="pageSize" name="pagesize" type="hidden" value=""/>
    <input id="queue" name="queue" type="hidden" value="${queue}"/>
    <input id="agent" name="agent" type="hidden" value="${agent}"/>
</form>
 
 </div>
</body>
</html>
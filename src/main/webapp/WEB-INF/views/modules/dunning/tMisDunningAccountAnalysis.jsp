<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>账目解析</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	closeSubmitting();
//    $("#inputForm").validate({
	   
// 	   submitHandler: function(form){
// 			loading('loading');
// 			form.submit();
// 		}
//    });
// 	   closeLoading();
	
	
	$("#remove").click(function(){
		
		$("#file").val("");
	});
	$("#save").click(function(){
		
		$("#save").attr("disabled" ,true);
		
		submitting() ;
	});
	
	if($("#message").val()){
		$.jBox.tip($("#message").val());
		$("#message").val("");
	}
	
});

function submitting() {
	$.jBox.tip("提交中...", "loading", {opacity: 0.2, persistent: true});
}

function closeSubmitting() {
	$.jBox.tip.mess = null;
	$.jBox.closeTip();
}
</script>

</head>
<body>
     <input type="hidden" id="message" value="${message}" />
	<form id="inputForm" action="${ctx}/dunning/tMisDunningAccountAnalysis/fileUpload" method="post" enctype="multipart/form-data"  >
		<div>
		
	
			对账渠道 <input type="radio" value="aliPay" name="aliPay"
				checked="checked" />支付宝
		</div>
		<div >

			对账文件 ：<input id="file" type="file" name="file"  class="input-xlarge required " />
		</div>

		<div>

			<input type="submit"  id="save"  value="确定上传" />
			 <input type="button" id="remove" value="取消" />
		</div>
	</form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>账目解析</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	closeSubmitting();
   $("#inputForm").submit(function(){
	 
		 if(!$("#inputForm").valid()){
			 return false;
		 }else{
				
			submitting() ;
		 }
   });
	
	
	$("#remove").click(function(){
		
		$("#file").val("");
	});
	
	
	if($("#message").val()){
		$.jBox.alert($("#message").val(),"上传结果");
		
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
<style type="text/css" >
.div{ margin:0 auto; width:400px; height:100px; }
input.bor {border:1px dashed #444444;width:300px;height:160px;margin-top:10px}  
.divp{padding:20px 180px;}
</style>
</head>
<body>
     <input type="hidden" id="message" value="${message}" />
	<form  id="inputForm" action="${ctx}/dunning/tMisRemittanceMessage/fileUpload" method="post" enctype="multipart/form-data"  >

		<div class="control-group divp" >
			<label class="control-label">对账渠道 	</label>
			<input type="radio" value="aliPay" name="channel" checked="checked" />支付宝
		</div>
		<div class="control-group divp">
			<label class="control-label">对账文件</label>
            <input   id="file" type="file" accept=".xls,.xlsx" name="file"  class="input-xlarge required bor divp " />
            <div class=" divp" >
               <font color="red">可拖拽至虚线框上传</font>    
            </div>
			
		</div>

		<div class="control-group  divp"> 
			<input type="submit" class="btn btn-primary" id="save"  value="确定上传" />
			<input type="button" class="btn btn-primary" id="remove" value="取消" />
		</div>
		
	</form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>账目解析</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	
</script>
<style type="text/css">
</style>
<style type="text/css">
</style>
</head>
<body>

	<form action="${ctx}/dunning/tMisDunningAccountAnalysis/fileUpload"
		method="post" enctype="multipart/form-data">
		<div>
			对账渠道 <input type="radio" value="zhifuPay" name="zhifuPay"
				checked="checked" />支付宝
		</div>
		<div class="demo">

			对账文件 ：<input type="file" name="file" />
		</div>

		<div>

			<input type="submit" value="确定上传" /> <input type="button"
				id="fileButton" value="取消" />
		</div>
	</form>
</body>
</html>
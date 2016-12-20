<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/buyer/tRiskMerchant/">用户报表列表</a></li>
		<li class="active"><a href="${ctx}/buyer/tRiskMerchant/form?id=${tRiskMerchant.id}">用户报表<shiro:hasPermission name="buyer:tRiskMerchant:edit">${not empty tRiskMerchant.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="buyer:tRiskMerchant:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="TRiskMerchant" action="${ctx}/buyer/tRiskMerchant/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">商户名：</label>
			<div class="controls">
				<form:input path="creditMerchantName" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户唯一key：</label>
			<div class="controls">
				<form:input path="creditMerchantKey" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">logo文件id：</label>
			<div class="controls">
				<form:input path="logoFileId" htmlEscape="false" maxlength="10" class="input-xlarge  digits"/>
			</div>
		</div>
			<div class="control-group">
				<label class="control-label">用户报表：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>count</th>
								<shiro:hasPermission name="buyer:tRiskMerchant:edit"><th width="10">&nbsp;</th></shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="mRiskBuyerReportList">
						</tbody>
						<shiro:hasPermission name="buyer:tRiskMerchant:edit"><tfoot>
							<tr><td colspan="3"><a href="javascript:" onclick="addRow('#mRiskBuyerReportList', mRiskBuyerReportRowIdx, mRiskBuyerReportTpl);mRiskBuyerReportRowIdx = mRiskBuyerReportRowIdx + 1;" class="btn">新增</a></td></tr>
						</tfoot></shiro:hasPermission>
					</table>
					<script type="text/template" id="mRiskBuyerReportTpl">//<!--
						<tr id="mRiskBuyerReportList{{idx}}">
							<td class="hide">
								<input id="mRiskBuyerReportList{{idx}}_id" name="mRiskBuyerReportList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="mRiskBuyerReportList{{idx}}_delFlag" name="mRiskBuyerReportList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<input id="mRiskBuyerReportList{{idx}}_count" name="mRiskBuyerReportList[{{idx}}].count" type="text" value="{{row.count}}" maxlength="10" class="input-small required digits"/>
							</td>
							<shiro:hasPermission name="buyer:tRiskMerchant:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#mRiskBuyerReportList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var mRiskBuyerReportRowIdx = 0, mRiskBuyerReportTpl = $("#mRiskBuyerReportTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(tRiskMerchant.mRiskBuyerReportList)};
							for (var i=0; i<data.length; i++){
								addRow('#mRiskBuyerReportList', mRiskBuyerReportRowIdx, mRiskBuyerReportTpl, data[i]);
								mRiskBuyerReportRowIdx = mRiskBuyerReportRowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
		<div class="form-actions">
			<shiro:hasPermission name="buyer:tRiskMerchant:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
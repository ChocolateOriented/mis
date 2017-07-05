<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>调整金额</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#savefreeCreditAmount').click(function() { 
				if($("#inputForm").valid()){
					$("#savefreeCreditAmount").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/tMisDunningTask/savefreeCreditAmount",
 	                    data: $('#inputForm').serialize(),             //获取表单数据
 	                    success : function(data) {
 	                        if (data == "OK") {
 	                            alert("保存成功");
 	                            window.parent.location.href="${ctx}/dunning/tMisDunningTask/customerDetails?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}";
 	                            window.parent.window.jBox.close();            //关闭子窗体
 	                        } else {
 	                            alert("减免失败"+data);
 	                        }
 	                    },
 	                    error : function(XMLHttpRequest, textStatus, errorThrown){
 	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
 	                       alert("保存失败:"+textStatus);
 	                    }
 	                });
 		          }
			}); 
			
			
			// 取消
			$('#esc').click(function() {
				window.parent.window.jBox.close();    
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<br/>
	<form id="inputForm"  class="form-horizontal">
		<div class="control-group">
			<label class="control-label">减免金额：</label>
			<div class="controls">
<%-- 				<form:input path="" htmlEscape="false" maxlength="10" class="input-xlarge required digits"/>元 --%>
<!-- onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" -->
				<input id="amount" name="amount"  maxlength="10" class="input-xlarge required number"/>&nbsp;&nbsp;元
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">减免原因：</label>
			<div class="controls">
			<select class="input-medium required" id="derateReason" name="derateReason">
					<option value="">选择</option>
					<c:forEach  items="${derateReasonList}" var="drList" >
					<option value="${drList}">${drList.derateReasonName}</option>
					</c:forEach>
			</select>
		</div>
		<br/>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
			<textarea id="remarks" rows="4" name="remarks"  maxlength="500"  ></textarea>
		</div>
		<div style= "padding:19px 180px 20px;" >
			<input id="savefreeCreditAmount" class="btn btn-primary" type="button" value="减免"/>&nbsp;
			<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
		<input type="hidden" id="buyerId" name="buyerId" value="${buyerId}" />
		<input type="hidden" id="dealcode" name="dealcode" value="${dealcode}" />
		<input type="hidden" id="id" name="id" value="${dunningtaskdbid}" />
	</form>
		<label class="control-group">&nbsp;<font color="red">*历史记录(金额不累加,只作为历史减免记录)</font></label>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>减免金额</th>
				<th>减免人</th>
				<th>时间</th>
				<th>减免原因</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="tMisReliefamountHistory">
			<tr>
				<td>
					${tMisReliefamountHistory.dealcode}
				</td>
				<td>
					${tMisReliefamountHistory.reliefamount}
				</td>
				<td>
					${tMisReliefamountHistory.createBy.name}
				</td>
				<td>
					<fmt:formatDate value="${tMisReliefamountHistory.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tMisReliefamountHistory.derateReason.derateReasonName}
				</td>
				<td>
					<label name="remark"  title="<c:if test='${fn:length(tMisReliefamountHistory.remarks)>6}'> ${tMisReliefamountHistory.remarks}</c:if>">
<!-- 					style="width:100px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;"> -->
						 <c:if test='${fn:length(tMisReliefamountHistory.remarks)>6}'>${ fn:substring(tMisReliefamountHistory.remarks,0,6)}...</c:if>
						 <c:if test='${fn:length(tMisReliefamountHistory.remarks)<=6}'>${tMisReliefamountHistory.remarks}</c:if>
						 
					</label>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
</body>
</html>


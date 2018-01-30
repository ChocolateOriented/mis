<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>调整金额</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            if("${ispayoff}" == "true"){
                $("#inputForm,#history").hide();
            }
			$('#savefreeCreditAmount').click(function() { 
				if($("#inputForm").valid()){
					$("#savefreeCreditAmount").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/reliefamount/savefreeCreditAmount",
 	                    data: $('#inputForm').serialize(),             //获取表单数据
 	                    success : function(data) {
 	                        if (data == "OK") {
 	                            alert("保存成功");
							  	window.parent.location.reload();
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

		  $('#applyfreeCreditAmount').click(function() {
				if($("#inputForm").valid()){
					$("#applyfreeCreditAmount").attr('disabled',"true");
 	                $.ajax({
 	                    type: 'POST',
 	                    url : "${ctx}/dunning/reliefamount/applyFreeCreditAmount",
 	                    data: $('#inputForm').serialize(),             //获取表单数据
 	                    success : function(data) {
 	                        if (data == "OK") {
 	                            alert("申请成功, 等待审批");
							  	window.parent.location.reload();
 	                            window.parent.window.jBox.close();            //关闭子窗体
 	                        } else {
 	                            alert("申请失败"+data);
 	                        }
 	                    },
 	                    error : function(XMLHttpRequest, textStatus, errorThrown){
 	                       //通常情况下textStatus和errorThrown只有其中一个包含信息
 	                       alert("申请失败:"+textStatus);
 	                    }
 	                });
 		          }
			});

          $('#refuse').click(function() {
            if($("#inputForm").valid()){
              $("#refuse").attr('disabled',"true");
              $.ajax({
                type: 'POST',
                url : "${ctx}/dunning/reliefamount/refuseFreeCreditAmount",
                data: $('#inputForm').serialize(),             //获取表单数据
                success : function(data) {
                  if (data == "OK") {
                    window.parent.location.reload();
                    window.parent.window.jBox.close();            //关闭子窗体
                  } else {
                    alert("拒绝失败"+data);
                  }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown){
                  //通常情况下textStatus和errorThrown只有其中一个包含信息
                  alert("拒绝失败:"+textStatus);
                }
              });
            }
          });

          // 取消
			$('#esc').click(function() {
				window.parent.window.jBox.close();    
			});

		});

        function showMessenger() {

            //alert(${thisCreditAmount});
            //var v= $("#amount").val();
            //var v2 =;
			var v = ${thisCreditAmount} - $("#amount").val();
            var x = Math.round(v*100)/100;
            if(x){
                $("#showContent").text("减免后应还金额:" +x);
            }
//            if(v){
//                $("#showContent").text("减免后应还金额:" +v);
//            }

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<%--<br/>--%>
	<form:form modelAttribute="tfHistory" id="inputForm" cssClass="form-horizontal">
		<%--<form  class="form-horizontal">--%>
		<div class="control-group">
			<label class="control-label">减免金额：</label>
			<div class="controls">
				<form:input path="reliefamount" id="amount" maxlength="10" class="input-xlarge required number" onchange="showMessenger()"/>&nbsp;&nbsp;元
				<span class="help-inline"><font color="red">*</font> </span>
				<div id="showContent"></div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">减免原因：</label>
			<div class="controls">
				<form:select path="derateReason" class="input-medium required" >
					<option value="">选择</option>
					<form:options items="${derateReasonList}" itemLabel="derateReasonName"></form:options>
				</form:select>
			</div>
		</div>
		<br/>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" rows="4" maxlength="500"></form:textarea>
			</div>
		</div>
		<div style="padding:19px 180px 20px;">
				<%--减免--%>
			<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
				<input id="savefreeCreditAmount" class="btn btn-primary" type="button" value="减免"/>
				<%--有申请使用拒绝, 无申请使用取消--%>
				<c:choose>
					<c:when test="${tfHistory.id == null}">
						<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
					</c:when>
					<c:otherwise>
						<input id="refuse" class="btn btn-primary" type="button" value="拒绝"/>&nbsp;
					</c:otherwise>
				</c:choose>
			</shiro:hasPermission>
				<%--减免申请--%>
			<shiro:lacksPermission name="dunning:tMisDunningTask:leaderview">
				<input id="applyfreeCreditAmount" class="btn btn-primary" type="button" value="申请"/>
				<input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
			</shiro:lacksPermission>
			&nbsp;
		</div>
		<form:hidden path="dealcode"/>
		<form:hidden path="id"/>
	</form:form>
		<label id="history" class="control-group">&nbsp;<font color="red">*历史记录(金额不累加,只作为历史减免记录)</font></label>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>减免金额</th>
				<th>申请人</th>
				<th>申请时间</th>
				<th>减免原因</th>
				<th>备注</th>
				<th>审批结果</th>
				<th>审批人</th>
				<th>审批时间</th>
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
					${tMisReliefamountHistory.applyUserName}
				</td>
				<td>
					<fmt:formatDate value="${tMisReliefamountHistory.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
				<td>
					${tMisReliefamountHistory.status.desc}
				</td>
				<td>
					${tMisReliefamountHistory.checkUserName}
				</td>
				<td>
					<fmt:formatDate value="${tMisReliefamountHistory.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
</body>
</html>


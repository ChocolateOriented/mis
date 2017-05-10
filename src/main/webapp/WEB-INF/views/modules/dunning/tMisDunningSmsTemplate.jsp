<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信模板</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		
	//添加短信模板
	$("#doTemplate").click(function(){
		
		var url = "${ctx}/dunning/TmisDunningSmsTemplate/addTemplate";
		
		$.jBox.open("iframe:"+url,"添加模板",800,700,{
			 buttons: {} ,
			   loaded: function (h) {
                 $(".jbox-content", document).css("overflow-y", "hidden");
             }
			
		});
		
	});
	});
	
	//删除短信模板
	function deleteTemplate(obj){
		var sid=$(obj).attr("sid");
		$.jBox.confirm("确认删除吗?","提示",function(v,h,f){
			if(v=="ok"){
			$.ajax({
				url:"${ctx}/dunning/TmisDunningSmsTemplate/deleteTemplate?id="+sid,
				type:"GET",
				data:{},
				success:function(data){
					if("OK"==data){
						$.jBox.tip("删除成功");
						  window.location.reload(); 
						return;
					}else{
						$.jBox.tip("删除失败");
						return;
					}
					
				},
				 error : function(XMLHttpRequest, textStatus, errorThrown){
                     //通常情况下textStatus和errorThrown只有其中一个包含信息
                     alert("删除失败:"+textStatus);
                  }
			});
				
			}	
		});
	}
	
	//修改短信模板	
	function changeSms(obj){
	  var sid=$(obj).attr("sid");
		var url = "${ctx}/dunning/TmisDunningSmsTemplate/changeTemplate?id="+sid;
		$.jBox.open("iframe:" + url, "修改短信模板" , 800, 700, {            
			 buttons: {} ,
			   loaded: function (h) {
                   $(".jbox-content", document).css("overflow-y", "hidden");
               }
      });
	}
	</script>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/TmisDunningSmsTemplate/list">短信模板</a></li>
	</ul>
	
	<sys:message content="${message}"/>
	
	<input id="doTemplate"  class="btn btn-primary" type="button" value="模板配置" />
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="allorder"  /></th>
				<th>模板名称</th>
				<th>发送方式</th>
				<th>发送时间</th>
				<th>短信类型</th>
				<th>接收号码类型</th>
				<th>可发送逾期天数</th>
				<th>模板内容</th>
				<th>操作</th>	
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="smsTemplate" varStatus="vs">
			<tr>
				<td >
					<input type="checkbox" name="orders" />
				</td>
				<td>
					${smsTemplate.templateName}
				</td>
				<td>
					<c:if test="${smsTemplate.sendMethod=='autoSend'}">
					 系统
					</c:if>
					<c:if test="${smsTemplate.sendMethod=='labourSend'}">
					 人工
					</c:if>
				</td>
				
				<td>
				${smsTemplate.sendTime}
				</td>
				
				<td>
				<c:if test="${smsTemplate.smsType=='wordText'}">
					 文字
					</c:if>
					<c:if test="${smsTemplate.smsType=='voice'}">
					语音
					</c:if>
				</td>
				
				<td>
				<c:if test="${smsTemplate.acceptType=='self'}">
					 本人
					</c:if>
					<c:if test="${smsTemplate.acceptType=='others'}">
					第三方
					</c:if>
				</td>
				
				<td>
				      ${smsTemplate.numbefore}≤逾期天数≤${smsTemplate.numafter}
                      
				</td>
				
				<td style="max-width:300px;line-height:30px">
				${smsTemplate.smsCotent}
				</td>
				
				<td>
					<input id="changeSms" onclick="changeSms(this)" sid="${smsTemplate.id}" class="btn btn-primary" type="button" value="修改"/>
					<input id="delete" class="btn btn-primary" onclick="deleteTemplate(this)" sid="${smsTemplate.id}" type="button" value="删除" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
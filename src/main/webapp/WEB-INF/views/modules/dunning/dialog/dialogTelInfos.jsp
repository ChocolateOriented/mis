<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收短信</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
 
		});
		
		//全部选择
		function qbSearch(){
			var i=0;
			$("[name='sendMsgInfo']").each(function(){
		        $(this).prop('checked',true);
		        i++;
		    });
		}
		
		//反选
		function fxSearch(){
			var i=0;
			$("[name='sendMsgInfo']").each(function(){
				if(this.checked){
		        	$(this).prop('checked',false);
		        }else{
		        	$(this).prop('checked',true);
		        	i++;
		        }
		    });
		}
		
		//取消
		function qxSearch(){
			$("[name='sendMsgInfo']").each(function(){
				if(this.checked){
		        	$(this).prop('checked',false);
		        }
		    });
		}
	</script>
</head>
<body>
<div align="center">
	<c:if test="${dialogType != 'tel'}">
		<a href="javascript:qbSearch()" id="qbSearch" >全选</a>
	    <a href="javascript:fxSearch()" id="fxSearch" >反选</a>
	    <a href="javascript:qxSearch()" id="qxSearch" >取消</a>
    </c:if>
</div>
	<form id="inputForm"   class="form-horizontal">
<!-- 	<input id="decription" name="decription" value="aaa" type="text" /> -->
		<div layoutH="90">
			<table class="table" width="99%" targetType="navTab" asc="asc" desc="desc">
				<tbody align="left">
					<tr>
				<c:forEach var="sendMsgInfo" items="${tMisSendMsgInfos}" varStatus="s">
							<td>
								<c:choose> 
									<c:when test="${dialogType eq 'tel'}">
										<input type="radio" name="sendMsgInfo" value="${sendMsgInfo.tel}" namevalue="${sendMsgInfo.name}"/>
											${sendMsgInfo.tel}<span style="color:#3FABE9">(${not empty map[sendMsgInfo.tel] ?map[sendMsgInfo.tel]:0})</span>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="sendMsgInfo" value="${sendMsgInfo.tel}" namevalue="${sendMsgInfo.name}" />
											${sendMsgInfo.tel}<span style="color:#3FABE9">(${not empty map[sendMsgInfo.tel] ?map[sendMsgInfo.tel]:0})</span>
									</c:otherwise>
								</c:choose>
							</td>
				<c:if test="${(s.count % 3) eq '0'}">
					</tr>
				</c:if>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</form>
	
</body>
</html>


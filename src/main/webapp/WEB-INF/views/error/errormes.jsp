<%@page import="com.thinkgem.jeesite.common.web.Servlets"%>
<%@page import="com.thinkgem.jeesite.common.utils.Exceptions"%>
<%@page import="com.thinkgem.jeesite.common.utils.StringUtils"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>友情提示</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
	<div class="container-fluid">
	ssss
		<div class="page-header"><h1><% request.getParameter("mes");  %></h1></div>
	</div>
</body>
</html>

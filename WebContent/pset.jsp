<%@page import="java.util.Date"%>
<%@page import="com.hellhole.hhsoj.common.Problemset"%>
<%@page import="com.hellhole.hhsoj.common.StyleUtil"%>
<%@page import="com.hellhole.hhsoj.tomcat.util.TomcatHelper"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<title>Problemset - HHSOJ</title>
</head>
<body>
	<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container">
		<h1 class="title-left">Problemsets</h1>
		<hr>
		<%
			ArrayList<Problemset> arr=TomcatHelper.getProblemsets();
			for(Problemset p:arr){
				
		%>
				<a href="plist.jsp?id=<%=p.id %>"><h1><%=p.name %></h1></a> opens <b><%=StyleUtil.shortDate(p.stTime) %></b> freezes <b><%=StyleUtil.shortDate(p.edTime) %></b>
		<%
			}
			
		%>
	</div>
</body>
</html>
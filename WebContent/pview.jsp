<%@page import="com.hhstudios.hhsoj.common.Problem"%>
<%@page import="java.util.Date"%>
<%@page import="com.hhstudios.hhsoj.common.Problemset"%>
<%@page import="com.hhstudios.hhsoj.tomcat.util.TomcatHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<title>Problem Viewing - HHSOJ</title>
</head>
<body>
	<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container"> 
		<%
			String set=request.getParameter("set");
			String id=request.getParameter("id");
			if(set==null || id==null){
				response.sendRedirect("pset.jsp");
				return;
			}
			
			Problem p=TomcatHelper.getProblem(set,id);
			if(p==null){
				response.sendRedirect("pset.jsp");
				return;
			}
			
			response.setCharacterEncoding("utf-8");
		%>
		<h1 class="title-left"><%=p.set+"."+p.id+" - "+p.name %></h1>
		<span class="title-right"><br/>Version: v<%=p.ver %></span>
		<hr/>
		<button class="btn btn-secondary" style="float:right;" onclick="reloadMathjax()">Render Mathjax Manually</button>
		<a class="btn btn-primary" style="float:right;" href="submit.jsp?set=<%=p.set%>&id=<%=p.id%>">Submit</a>
		<a class="btn btn-secondary" style="float:right;" href="plist.jsp?id=<%=p.set%>">Problemset</a>
		<span><%=p.tl %>ms / <%=p.ml %> KB<br/>Difficulty:<%=p.diff %>x</span>
		
		<hr/>
		<div id="txt">Loading Statement...</div>
		<div id="diagram"></div>
		<hr/>
		
		<a class="btn btn-primary" href="submit.jsp?set=<%=p.set%>&id=<%=p.id%>">Submit</a>
	</div>
	<script>
		$.post("requireStatement",{
			"set":"<%=p.set%>",
			"id":"<%=p.id%>"
		},function(data,status){
		    var text=data;
		    html=converter.makeHtml(text);
		    document.getElementById("txt").innerHTML=html;
		    reloadAll();
		})
	</script>
</body>
</html>
<%@page import="com.hellhole.hhsoj.common.StyleUtil"%>
<%@page import="com.hellhole.hhsoj.common.Sanitizer"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Date"%>
<%@page import="com.hellhole.hhsoj.common.Submission"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.hellhole.hhsoj.common.Language"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.hellhole.hhsoj.tomcat.util.TomcatHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<title>Status - HHSOJ</title>
</head>
<body>
<%
	int pageId=1,renSize=50;
	try{
		pageId=Integer.parseInt(request.getParameter("page"));
	}catch(Exception e){
		pageId=1;
	}
	
	try{
		renSize=Integer.parseInt(request.getParameter("renSize"));
	}catch(Exception e){
		renSize=50;
	}
	
	ArrayList<Submission> arr=TomcatHelper.getAllSubmissions();
	arr.sort(new Comparator<Submission>(){
		public int compare(Submission a,Submission b){
	return -Long.compare(a.id, b.id);
		}
	});
	renSize=Math.max(1,renSize);
	
	int mxP=(arr.size()+renSize-1)/renSize;
	pageId=Math.min(pageId,mxP);
	pageId=Math.max(1,pageId);
%>

<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container">
		<h1 class="title-left">Status #<%=pageId%>/<%=mxP%></h1>
		<ul class="pagination pagination-sm title-right" style="margin-top:15px;">
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-10%>&renSize=<%=renSize%>">&lt;&lt;10</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-1%>&renSize=<%=renSize%>">&lt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+1%>&renSize=<%=renSize%>">&gt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+10%>&renSize=<%=renSize%>">10&gt;&gt;</a></li>
		</ul>
  		<hr/>
		<table class="table table-bordered table-sm status-table">
			<tr>
				<th>#</th>
				<th>Time</th>
				<th>Author</th>
				<th>Problem</th>
				<th>Score</th>
				<th>Tests</th>
				<th>Lang</th>
				<th>Time</th>
				<th>Memory</th>
			</tr>
			<%
				for(int i=(pageId-1)*renSize;i<Math.min(arr.size(),pageId*renSize);i++){
						Submission s=arr.get(i);
			%>
				<tr bgcolor="<%=(s.author.equals(session.getAttribute("username"))?"#def":"white")%>">
					<td><a href="sview.jsp?id=<%=s.id%>"><%=s.id%></a></td>
					<td><%=new Date(s.submitTime)%></td>
					<td><%=Sanitizer.encodeEntity(s.author)%></td>
					<td><a href="pview.jsp?set=<%=s.problemSet%>&id=<%=s.problemId %>"><%=s.problemSet+"."+s.problemId %></a></td>
					<td align="center"><b style="color:<%=StyleUtil.colorize(s.score) %>;"><%=String.format("%.0f", 100*s.score) %></b></td>
					<td><%=(s.isFinal?"Final":s.test) %></td>
					<td><%=s.lang %></td>
					<td><%=s.getRunTime() %></td>
					<td><%=s.getRunMem() %></td>
				</tr>
			<%
				}
			%>
		
		</table>
		<hr/>
		<ul class="pagination pagination-sm" style="float:right;">
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-10%>&renSize=<%=renSize%>">&lt;&lt;10</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-1%>&renSize=<%=renSize%>">&lt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+1%>&renSize=<%=renSize%>">&gt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+10%>&renSize=<%=renSize%>">10&gt;&gt;</a></li>
		</ul>
		<ul class="pagination pagination-sm">
			<li class="page-item disabled"><a class="page-link" href="#">Submission Per Page:</a></li>
			<li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=1%>">1?</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=10%>">10</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=20%>">20</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=50%>">50</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=100%>">100</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=998244353%>">998244353!</a></li>
		</ul>
	</div>
</body>
</html>
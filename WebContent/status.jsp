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
		<%
			for(int i=(pageId-1)*renSize;i<Math.min(arr.size(),pageId*renSize);i++){
					Submission s=arr.get(i);
		%>
			<div class="status-card row card">
		  		<div class="status-card-col col-sm-3">
		  			<a class="status-card-id" style="flex-grow:1;max-height:30px;"href="sview.jsp?id=<%=s.id%>">#<%=s.id%></a>
		  			<p class="status-card-author"><b><%=Sanitizer.encodeEntity(s.author)%></b></p>
		  		</div>
	  			<div class="status-card-col col-sm-2">
	  				<a class="nostyle" href="sview.jsp?id=<%=s.id%>">
		  				<span class="badge" style="background:<%=StyleUtil.colorize(s.score) %>;"><%=(s.isFinal?"Final":s.test)%></span>
		  				<span class="badge" style="margin-top:4px;background:<%=StyleUtil.colorize(s.score) %>;"><%=String.format("%.0f", 100*s.score) %></span>
	  				</a>
	  			</div>
	  			<div class="status-card-col col-sm-3">
	  				<a href="pview.jsp?set=<%=s.problemSet%>&id=<%=s.problemId %>"><%=s.problemSet%>.<%=s.problemId %></a>
	  			</div>
	  			<div class="status-card-col col-sm-4 d-block d-sm-flex">
	  				<span style="flex-grow:1;">
	  					<%=TomcatHelper.getLangs().get(s.lang).name %><br class="d-none d-sm-block d-md-none"/>
	  					<i class="fa fa-clock-o"></i><%=s.getRunTime() %>ms
	  					<i class="fa fa-database"></i><%=s.getRunMem() %>KB
	  				</span>
	  				<p class="status-card-time"><%=new Date(s.submitTime)%></p>
	  			</div>
	  		</div>
		<%
			}
		%>
		<hr/>
		<ul class="pagination pagination-sm pagenav">
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-10%>&renSize=<%=renSize%>">&lt;&lt;10</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-1%>&renSize=<%=renSize%>">&lt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+1%>&renSize=<%=renSize%>">&gt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+10%>&renSize=<%=renSize%>">10&gt;&gt;</a></li>
		</ul>
		<ul class="pagination pagination-sm">
			<li class="page-item disabled"><a class="page-link" href="#">Submission Per Page:</a></li>
			<li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=1%>">1</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=20%>">20</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=100%>">100</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=998244353%>">INF</a></li>
		</ul>
	</div>
</body>
</html>
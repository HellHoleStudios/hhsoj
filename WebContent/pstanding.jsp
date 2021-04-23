<%@page import="com.hellhole.hhsoj.common.StandingCell"%>
<%@page import="com.hellhole.hhsoj.common.Problem"%>
<%@page import="java.io.File"%>
<%@page import="com.hellhole.hhsoj.common.Problemset"%>
<%@page import="com.hellhole.hhsoj.common.FileUtil"%>
<%@page import="com.hellhole.hhsoj.common.StandingTable"%>
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
<title>Standing - HHSOJ</title>
</head>
<body>
<%
	int pageId=1,renSize=50;
	String pid="";
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
	
	try{
		pid=request.getParameter("id");
	}catch(Exception e){
		out.println("Please enter problemset ID");
		return;
	}
	
	
	renSize=Math.max(1,renSize);
	
	StandingTable st=FileUtil.readStandingTable(TomcatHelper.getConfig().path+"/cache/"+pid);
	Problemset ps=TomcatHelper.getProblemset(pid);
	
	if(ps==null){
		out.println("Cannot find given problemset");
		return;
	}
	if(st==null){
		st=new StandingTable(ps);
	}
	
	ArrayList<Problem> p=TomcatHelper.getAllProblems(pid);
	int mxP=(st.table.size()+renSize-1)/renSize;
	pageId=Math.min(pageId,mxP);
	pageId=Math.max(1,pageId);
%>

<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container">
		<h1 class="title-left">Standing of <%=st.name %> (Page <%=pageId %>/<%=mxP %>)</h1>
		<ul class="pagination pagination-sm title-right" style="margin-top:15px;">
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-10%>&renSize=<%=renSize%>&id=<%=pid%>">&lt;&lt;10</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-1%>&renSize=<%=renSize%>&id=<%=pid%>">&lt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+1%>&renSize=<%=renSize%>&id=<%=pid%>">&gt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+10%>&renSize=<%=renSize%>&id=<%=pid%>">10&gt;&gt;</a></li>
		</ul>
  		<hr/>
		<%if(session.getAttribute("admin")!=null && (Boolean)session.getAttribute("admin")==true){%>
			<a href="javascript:rebase()" class="title-right btn btn-primary">Rebase</a>
		<%} %>
		
		<script>
			function rebase(){
				$.post("rebaseS?id=<%=pid%>",function(rev){
					if(rev=="OK"){
						alert("Success. Please wait for the rebase to finish. This might take a few seconds.")
					}else{
						alert(rev)
					}
				})
			}
		</script>
		
		<div class="container">                                                                                      
		  <div class="table-responsive">          
		  <table class="table">
		    <thead>
		      <tr>
		        <th>Rank</th>
		        <th>User</th>
		      <%for(Problem prob:p){ %>
		      	<th><%=prob.id %></th>
		      <%} %>
		      	<th>∑</th>
		      </tr>
		    </thead>
		    <tbody>
		    <%for(int i=(pageId-1)*renSize;i<Math.min(st.table.size(),pageId*renSize);i++){%>
		      <tr>
		        <td><%=i+1 %></td>
		        <td><%=st.table.get(i).name %></td>
		        <%for(Problem prob:p){
		        	StandingCell cell=st.table.get(i).col.get(prob.id);
		        	if(cell==null){
		        %>
		        <td>-</td>
		        <%  }else{ %>
		        <td>
	  				<a class="nostyle" href="sview.jsp?id=<%=cell.subId%>">
		  				<span class="badge score-badge" style="margin-top:4px;background:<%=StyleUtil.colorize((float)cell.score) %>;"><%=String.format("%.0f", 100*cell.score) %></span>
	  				</a>
	  			</td>
		        <%}} %>
		        <td>
		        
		  			<span class="badge score-badge" style="margin-top:4px;background:<%=StyleUtil.colorize((float)st.table.get(i).score) %>;"><%=String.format("%.0f", 100*st.table.get(i).score) %></span>
	  			
	  			</td>
		      </tr>
		    <%} %>
		    </tbody>
		  </table>
		  </div>
		</div>
		
		<hr/>
		<ul class="pagination pagination-sm sm-float-right">
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-10%>&renSize=<%=renSize%>&id=<%=pid%>">&lt;&lt;10</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId-1%>&renSize=<%=renSize%>&id=<%=pid%>">&lt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+1%>&renSize=<%=renSize%>&id=<%=pid%>">&gt;</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId+10%>&renSize=<%=renSize%>&id=<%=pid%>">10&gt;&gt;</a></li>
		</ul>
		<ul class="pagination pagination-sm">
			<li class="page-item disabled"><a class="page-link" href="#">Person Per Page:</a></li>
			<li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=1%>&id=<%=pid%>">1</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=20%>&id=<%=pid%>">20</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=100%>&id=<%=pid%>">100</a></li>
		    <li class="page-item"><a class="page-link" href="?page=<%=pageId%>&renSize=<%=998244353%>&id=<%=pid%>">INF</a></li>
		</ul>
	</div>
</body>
</html>
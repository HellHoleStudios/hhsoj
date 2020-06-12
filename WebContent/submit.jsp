<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.hhstudios.hhsoj.common.Language"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.hhstudios.hhsoj.tomcat.util.TomcatHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<title>Submit - HHSOJ</title>
</head>
<body>
	<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container">
		<h1 class="title-left">Submit</h1>
	<%
		if(session.getAttribute("username")==null){
	%>
		<hr/>
		<p>Please <a href="login.jsp">Login</a> to submit.</p>
	<%
			return;
		}
	
		String set=request.getParameter("set");
		if(set==null){
			set="";
		}
		String id=request.getParameter("id");
		if(id==null){
			id="";
		}
	%>
		<button onclick="submit()" class="title-right btn btn-primary btn-submit" style="float:right;">Submit!</button>	
		<hr/>
		<div class="input-group" style="width:400px;">
			<div class="input-group-prepend">
	     			<span class="input-group-text">Problem Set ID</span>
	   		</div>
			<input id="set" value="<%=set %>" class="form-control">
		</div>
		<div class="input-group" style="width:400px;">
			<div class="input-group-prepend">
	     			<span class="input-group-text">Problem ID</span>
	   		</div>
			<input id="id" value="<%=id %>" class="form-control">
		</div>
		<div class="input-group" style="width:400px;">
			<div class="input-group-prepend">
	     			<span class="input-group-text">Language</span>
	   		</div>
			<select id="lang" class="form-control">
				<%
				HashMap<String,Language> arr=TomcatHelper.getLangs();
				
				for(Entry<String,Language> l:arr.entrySet()){
				%>
					<option value="<%=l.getKey() %>"><%=l.getValue().name %></option>
				<%
				}
				%>
			</select>
		</div>
		
		<textarea id="code" style="width:80%;height:600px;"></textarea>
		<br/>
		
		<button onclick="submit()" class="btn btn-primary btn-submit">Submit!</button>
	</div>
	<script>
		function submit(){
			var list=$(".btn-submit");
			for(var i=0;i<list.length;i++){
				list[i].disabled=true;
			}
			
			$.post("submitS",{
				"set":document.getElementById("set").value,
				"id":document.getElementById("id").value,
				"lang":document.getElementById("lang").value,
				"code":document.getElementById("code").value,
				
			},function(data,status){
				if(data=="OK"){
					window.location="status.jsp";
				}else{
					var list=$(".btn-submit");
					for(var i=0;i<list.length;i++){
						list[i].disabled=false;
					}
					alert(data);
				}
			});
		}
	</script>
</body>
</html>
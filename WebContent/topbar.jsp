<%@page import="com.hellhole.hhsoj.common.Sanitizer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-md bg-dark navbar-dark fixed-top topbar">
	<a class="navbar-brand" href="<%=request.getContextPath().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")%>/"> 
		<img src="assets/img/HHSOJct128x.png" alt="Logo">
	</a>

	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#collapsibleNavbar">
		<span class="navbar-toggler-icon"></span>
	</button>

	<div class="collapse navbar-collapse" id="collapsibleNavbar">
		<ul class="navbar-nav ml-auto topbar-nav">
			<li class="nav-item topbar-item"><a class="nav-link" href="pset.jsp">Problemsets</a></li>
			<li class="nav-item topbar-item"><a class="nav-link" href="status.jsp">Status</a></li>
			<li class="nav-item topbar-item"><a class="nav-link" href="submit.jsp">Submit</a></li>
		</ul>

		<ul class="navbar-nav mr-auto topbar-nav">
			<!-- This is to create a split line for mobile devices ---Zzzyt -->
			<li class="nav-item">&nbsp;</li>
		<%
			if (session.getAttribute("username") == null) {
		%>
			<li class="nav-item topbar-item"><a class="nav-link" href="login.jsp">Login</a></li>
		<%
			} else {
		%>
			<%-- TODO: "index.jsp" below should be replaced with user page URL --%>
			<li class="nav-item topbar-item"><a class="nav-link" href="index.jsp"><%=Sanitizer.encodeEntity((String)session.getAttribute("username"))%></a></li>
			<li class="nav-item topbar-item"><a class="nav-link" href="logoutS">Logout</a></li>
		<%}%>
		</ul>
	</div>
</nav>

<div style="height:50px;"></div>
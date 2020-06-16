<%@ page isErrorPage="true"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.StringWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>500 Internal Server Error - HHSOJ</title>
<style>
body {
	background: #f8f8f8;
	padding-top: 50px;
	padding-bottom: 50px;
}

.main {
	width: 80%;
	margin: 0px auto;
}

h1, h2, h3, h4, h5, h6 {
	font-weight: bold;
	font-family: "Consolas", "Courier New", Courier, monospace;
}

i {
	color: #777777;
	font-family: "Consolas", "Courier New", Courier, monospace;
}

a {
	font-family: "Consolas", "Courier New", Courier, monospace;
}

span{
	color: #777777;
}

code, kbd, pre, samp {
	padding:5px;
	border-radius: 3px;
	background-color: #f0f0f0;
}
</style>
</head>
<body>
	<div class="main">
		<h1 style="font-size:50px;">500 Internal Server Error</h1>
		<p><i>I guess my IQ was </i><span>&#9320;</span><i> when I was coding this :(</i></p>
		<p style="width:100%;max-width:400px;text-align:right;"><i>---Zzzyt</i></p>
		<br/>
		<a href="javascript:history.back(0);">Back to where I came from</a><br/>
		<a href="<%=request.getContextPath().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")%>/">Back to home</a><br/>
		<br/>
		<br/>
		<h2>Raw stack trace:</h2>
		<pre><%
StringWriter sw = new StringWriter();
exception.printStackTrace(new PrintWriter(sw));
out.print(sw.toString().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"));
%></pre>
	</div>
</body>
</html>
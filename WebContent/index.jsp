<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<title>HHSOJ Essential</title>
</head>
<body>
	<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container">
		<h1 class="title-left">Welcome to HHSOJ Essential</h1>
		<span class="title-right"><br/>OJ by Hell Hole Studios</span>
		<hr/>
		<p>
			The 3rd generation HHSOJ!
			<br/>
			A light-weight, fast and portable OJ!
			<br/>
			Currently in development phase.
			<br/>
			Note: this repository is a fork of
			<a href="https://github.com/XiaoGeNintendo/HHSOJ-Essential" target="_blank"><i class="fa fa-external-link"></i>XiaoGeNintendo/HHSOJ-Essential</a>.
			However I changed the project structure so much that I decided to create a new repo instead of forking.
			This version is equipped with Maven and other development tools.
		</p>
		<h3>Credit</h3>
		<p>
			The frontend is run on <a href="https://tomcat.apache.org/" target="_blank"><i class="fa fa-external-link"></i>Apache Tomcat</a>.
			<br/>
			JSON parsing with <a href="https://github.com/google/gson" target="_blank"><i class="fa fa-external-link"></i>Gson</a>.
			<br/>
			Server-side markdown rendering with <a href="https://github.com/vsch/flexmark-java" target="_blank"><i class="fa fa-external-link"></i>flexmark</a>.
			<br/>
			Flexmark extensions include:
			<ul>
				<li>flexmark-ext-gfm-strikethrough</li>
				<li>flexmark-ext-tables</li>
				<li>flexmark-ext-media-tags (Modified)</li>
			</ul>
		</p>
		<h3>Links</h3>
		<a href="https://xgn.gitbook.io/hhsoj-essential-doc/" target="_blank"><i class="fa fa-external-link"></i>Documentation on everything</a>
		<br/>
		<a href="https://blog.hellholestudios.top/" target="_blank"><i class="fa fa-external-link"></i>Our Blog</a>
		<br/>
		<a href="https://betaoj.hellholestudios.top/HellOJ" target="_blank"><i class="fa fa-external-link"></i>How's it going?</a>
		<br/>
		<a href="https://github.com/HellHoleStudios/hhsoj_maven" target="_blank"><i class="fa fa-external-link"></i>Our repo on Github</a>
		<br/>
		<i>Hell Hole Studios 2019-2020</i>
	</div>
</body>
</html>
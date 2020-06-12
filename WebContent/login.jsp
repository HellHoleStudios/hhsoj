<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<title>Login - HHSOJ</title>
</head>
<body>
	<jsp:include page="topbar.jsp"></jsp:include>
	<div class="container">
		<h1 class="title-left">Login/Register</h1>
		<i class="title-right"><br/>@XGN can u make two seperate pages?? --Zzzyt</i>
		<hr/>
		<div class="card center-form"><div class="card-body">
			<div class="input-group">
				<input type="text" class="form-control" placeholder="Username" id="user" maxlength="50" />
			</div>
			
			<div class="input-group">
				<input type="password" class="form-control" placeholder="Password" id="pass" type="password" maxlength="50" />
			</div>
			
			<div class="input-group">
				<button class="form-control btn btn-primary" style="margin-left:0px;" onclick="login()">Login</button>
				<button class="form-control btn btn-primary" style="margin-right:0px;" onclick="reg()">Register</button>
			</div>
		</div></div>

		<script>
		function login() {
			var user=$("#user").val();
			var pass=$("#pass").val();
			if(user.length>50){
				alert("Username too long!");
				return;
			}
			if(pass.length>50){
				alert("Username too long!");
				return;
			}
			$.post("loginS", {
				"username" : user,
				"password" : pass,
			}, function(data, status) {
				if (data == "OK") {
					window.location = "index.jsp";
				} else {
					alert(data);
				}
			});
		}
		function reg() {
			var user=$("#user").val();
			var pass=$("#pass").val();
			if(user.length>50){
				alert("Username too long!");
				return;
			}
			if(pass.length>50){
				alert("Username too long!");
				return;
			}
			$.post("regS", {
				"username" : user,
				"password" : pass,
			}, function(data, status) {
				alert(data);
			});
		}
		</script>
	</div>
</body>
</html>
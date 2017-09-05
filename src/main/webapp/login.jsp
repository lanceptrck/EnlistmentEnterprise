<html>
<head>
<title>Login</title>
</head>
<body>
<% if(request.getAttribute("noIdFound") != null && (boolean)request.getAttribute("noIdFound")){%>
	<p style="color: red">Student number does not exist</p>
<% }%> 
<form action="login" method="post">
<strong>Enter Your Username: </strong>
<input type="text" name="student_number"/>
<input	type="submit" value="Submit" />
</form>
</body>
</html>
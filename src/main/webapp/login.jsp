<html>
<head>
<title>Login</title>
</head>
<body>
<% if(request.getAttribute("noIdFound") != null){%>
	<p style="color: red">${noIdFound }</p>
<% }%> 
<form action="login" method="post">
<strong>Enter Your Username: </strong>
<input type="text" name="student_number"/><br>
<input type="checkbox" name="isAdmin" value="admin"/> Log in as admin<br>
<input	type="submit" value="Submit" />
</form>
</body>
</html>
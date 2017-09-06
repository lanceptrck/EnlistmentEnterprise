<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Oops</title>
</head>
<body>
	<h2>Oops something went wrong..</h2>
	<h4><%=exception.getMessage()%></h4>
	<p>
		<a href="/enlistment/login.jsp">Go Back Home</a>
	</p>
</body>
</html>
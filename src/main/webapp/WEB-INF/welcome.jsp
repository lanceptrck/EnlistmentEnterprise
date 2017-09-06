<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>
<body>
	<h3>You are logged in as ${ firstName } ${lastName }</h3>
	<h4>ID Number: ${studentNumber }</h4>

	<c:choose>
		<c:when test="${sections.isEmpty() }">
			<p>No enlisted sections</p>
		</c:when>

		<c:otherwise>
			<h3>Enlisted Sections</h3>
			<table style="width: 100%">
				<tr>
					<th></th>
					<th>Section ID</th>
					<th>Subject Id</th>
					<th>Schedule</th>
					<th>Room</th>
					<th>Professor</th>
				</tr>
				<c:forEach var="section" items="${sections }">
					<tr>
						<td><a href="cancel_enlistment/${section.sectionId }">Cancel</a></td>
						<td>${section.sectionId }</td>
						<td>${section.subjectId }</td>
						<td>${section.schedule }</td>
						<td>${section.roomName }</td>
						<td>${section.faculty }</td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>

	<h3>Available Sections</h3>
	<table style="width: 100%">
		<tr>
			<th></th>
			<th>Section ID</th>
			<th>Subject Id</th>
			<th>Schedule</th>
			<th>Room</th>
			<th>Professor</th>
		</tr>
		<c:forEach var="section" items="${availableSections }">
			<tr>
				<td><a href="enlist/${section.sectionId }"> Enlist</a></td>
				<td>${section.sectionId }</td>
				<td>${section.subjectId }</td>
				<td>${section.schedule }</td>
				<td>${section.roomName }</td>
				<td>${section.faculty }</td>
			</tr>
		</c:forEach>
	</table>

	<%@include file="/WEB-INF/footer.jsp"%>
</body>
</html>
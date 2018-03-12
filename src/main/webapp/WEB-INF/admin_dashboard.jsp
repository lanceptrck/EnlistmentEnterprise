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
	<h3>You are logged in as Admin: ${ firstName } ${lastName }</h3>
	<h4>ID Number: ${studentNumber }</h4>

	<h3>Create Section</h3>
	<form action="create_section" method="post">
		<table width="100%">
			<tr>
				<td valign="top">
					<table>
						<tr>
							<th>Section ID</th>
						</tr>
						<tr>
							<td><input type="text" name="sectionId"></td>
						</tr>
					</table>
				</td>
				<td valign="top">
					<table>
						<tr>
							<th>Subject ID</th>
						</tr>
						<c:forEach var="subject" items="${subjects }">
							<tr>
								<td><input type="radio" name="subjectId"
									value="${subject.subjectId }">${subject.subjectId }</td>
							</tr>
						</c:forEach>
					</table>
				</td>
				<td valign="top">
					<table>
						<tr>
							<th>Room Name</th>
						</tr>
						<c:forEach var="room" items="${rooms }" varStatus="stat">
							<tr>
								<td><input type="radio" name="roomName"
									value="${room.name }">${room.name }</td>
							</tr>
						</c:forEach>
					</table>
				</td>
				<td valign="top">
					<table>
						<tr>
							<th>Faculty</th>
						</tr>
						<c:forEach var="faculty" items="${faculties }" varStatus="stat">
							<tr>
								<td><input type="radio" name="facultyNumber"
									value="${faculty.getFacultyNumber()}">${faculty}</td>
							</tr>
						</c:forEach>
					</table>
				</td>

				</td>
				<td valign="top">
					<table>
						<tr>
							<th>Days</th>
						</tr>
							<tr>
								<td><input type="radio" name="day"
									value="MW">Monday/Wednesday</td>
							</tr>
							<tr>
								<td><input type="radio" name="day"
									value="TF">Tuesday/Friday</td>
							</tr>
							<tr>
								<td><input type="radio" name="day"
									value="WS">Wednesday/Saturday</td>
							</tr>
					</table>
				</td>
				<td valign="top">
					<table>
						<tr>
							<th>Period</th>
						</tr>
							<tr>
								<td><input type="radio" name="period" value="08:30-10:00"/>08:30-10:00</td>
							</tr>
							<tr>
								<td><input type="radio" name="period" value="10:00-11:30"/>10:00-11:30</td>
							</tr>
							<tr>
								<td><input type="radio" name="period" value="11:30-13:00"/>11:30-13:00</td>
							</tr>
							<tr>
								<td><input type="radio" name="period" value="13:00-14:30"/>13:00-14:30</td>
							</tr>
							<tr>
								<td><input type="radio" name="period" value="14:30-16:00"/>14:30-16:00</td>
							</tr>
							<tr>
								<td><input type="radio" name="period" value="16:00-17:30"/>16:00-17:30</td>
							</tr>
					</table>
				</td>
			</tr>
		</table>
		<input type="submit" value="Create Section"/>
	</form>
	<c:choose>
		<c:when test="${sections.isEmpty() }">
			<p>No available sections</p>
		</c:when>

		<c:otherwise>
			<h3>Available Sections</h3>
			<table style="width: 100%">
				<tr>
					<th>Section ID</th>
					<th>Subject Id</th>
					<th>Schedule</th>
					<th>Room</th>
					<th>Professor</th>
				</tr>
				<c:forEach var="section" items="${sections }">
					<tr>
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
	<%@include file="/WEB-INF/footer.jsp"%>
</body>
</html>
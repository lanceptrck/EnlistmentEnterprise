package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/get_student_enlistments")
public class StudentEnlistmentsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		int studentNumber = Integer.valueOf((String) session.getAttribute("studentNumber"));

		request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
	}

}

package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String USERNAME = "username";
	private final static String FIRST_NAME = "firstName";
	private final static String LAST_NAME = "lastName";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		session.removeAttribute(USERNAME);
		session.removeAttribute(FIRST_NAME);
		session.removeAttribute(LAST_NAME);
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

}

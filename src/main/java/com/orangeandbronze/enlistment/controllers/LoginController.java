package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.service.EnlistService;
import com.orangeandbronze.enlistment.service.LoginService;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginService service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = (LoginService) getServletContext().getAttribute("loginService");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.valueOf(req.getParameter("student_number"));

		HttpSession session = ((HttpServletRequest) req).getSession();
		service.login(id, session);

		req.getRequestDispatcher("get_student_enlistments").forward(req, resp);

	}
}

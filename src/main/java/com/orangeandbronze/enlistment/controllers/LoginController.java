package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		Map<String, String> studentInfo = (HashMap<String, String>) service.login(id);

		if (!studentInfo.isEmpty()) {
			session.setAttribute("studentNumber", studentInfo.get("studentNumber"));
			session.setAttribute("lastName", studentInfo.get("lastName"));
			session.setAttribute("firstName", studentInfo.get("firstName"));
			req.getRequestDispatcher("get_student_enlistments").forward(req, resp);
		} else {
			req.setAttribute("noIdFound", true);
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}

	}
}

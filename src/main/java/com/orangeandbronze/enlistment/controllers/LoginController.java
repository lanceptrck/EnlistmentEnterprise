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
		int id = Integer.valueOf(req.getParameter("studentNumber"));
		String isAdmin = (String) req.getParameter("isAdmin");
		HttpSession session = ((HttpServletRequest) req).getSession();
		Map<String, String> userInfo;

		if (isAdmin != null) {
			userInfo = (HashMap<String, String>) service.loginAdmin(id);
			if (!userInfo.isEmpty()) {
				setSession(session, userInfo);
				session.setAttribute("isAdmin", true);
				resp.sendRedirect("admin_dashboard");
			} else {
				req.setAttribute("noIdFound", "Admin ID does not exist");
				req.getRequestDispatcher("login.jsp").forward(req, resp);
			}
		} else {
			userInfo = (HashMap<String, String>) service.login(id);
			if (!userInfo.isEmpty()) {
				setSession(session, userInfo);
				//session.setAttribute("studentNumber", userInfo.get("studentNumber"));
				session.setAttribute("isAdmin", false);
				resp.sendRedirect("get_student_enlistments");
			} else {
				req.setAttribute("noIdFound", "Student Number does not exist");
				req.getRequestDispatcher("login.jsp").forward(req, resp);
			}
		}

	}

	private void setSession(HttpSession session, Map<String, String> userInfo) {
		session.setAttribute("studentNumber", userInfo.get("studentNumber"));
		session.setAttribute("lastName", userInfo.get("lastName"));
		session.setAttribute("firstName", userInfo.get("firstName"));
	}
}

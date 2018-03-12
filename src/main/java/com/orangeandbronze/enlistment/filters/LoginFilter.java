package com.orangeandbronze.enlistment.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/login.jsp")
public class LoginFilter implements Filter {
	private final static String STUDENT_NUMBER = "studentNumber";
	private final static String FIRST_NAME = "firstName";
	private final static String LAST_NAME = "lastName";
	private final static String IS_ADMIN = "isAdmin";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String usernameSessionAttr = (String) session.getAttribute(STUDENT_NUMBER);
		if (usernameSessionAttr == null) { // not logged-in
			String studentNumberParam = (String) request.getAttribute(STUDENT_NUMBER);
			String firstNameAttr = (String) request.getAttribute(FIRST_NAME);
			String lastNameAttr = (String) request.getAttribute(LAST_NAME);

			if (studentNumberParam == null) { // go to login page
				((HttpServletRequest) request).getRequestDispatcher("login.jsp").forward(request, response);
			}
		}

		chain.doFilter(request, response);

		if (session.getAttribute(STUDENT_NUMBER) != null) {
			if ((boolean) session.getAttribute("isAdmin")) {
				((HttpServletRequest) request).getRequestDispatcher("admin_dashboard").forward(request, response);
			} else {
				((HttpServletRequest) request).getRequestDispatcher("get_student_enlistments").forward(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}

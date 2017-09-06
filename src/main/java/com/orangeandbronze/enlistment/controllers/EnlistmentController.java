package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangeandbronze.enlistment.service.EnlistService;

@WebServlet({ "/enlist/*", "/cancel_enlistment/*" })
public class EnlistmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EnlistService service;

	private static final String ENLIST = "/enlist";
	private static final String CANCEL_ENLIST = "/cancel_enlistment";

	@Override
	public void init() throws ServletException {
		super.init();
		service = (EnlistService) getServletContext().getAttribute("enlistService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sectionId = request.getPathInfo().substring(1);
		int studentNumber = Integer.valueOf((String) request.getSession().getAttribute("studentNumber"));

		switch (request.getServletPath()) {
		case ENLIST:
			service.enlist(studentNumber, sectionId);
			break;
		case CANCEL_ENLIST:
			service.cancel(studentNumber, sectionId);
			break;
		}
		response.sendRedirect("/enlistment/get_student_enlistments");
		// request.getRequestDispatcher("get_student_enlistments").forward(request,
		// response);
	}

}

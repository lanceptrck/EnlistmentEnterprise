package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.orangeandbronze.enlistment.service.LoginService;
import com.orangeandbronze.enlistment.service.SectionInfo;
import com.orangeandbronze.enlistment.service.SectionService;

@WebServlet("/get_student_enlistments")
public class StudentEnlistmentsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SectionService service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = (SectionService) getServletContext().getAttribute("sectionService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		int studentNumber = Integer.valueOf((String) session.getAttribute("studentNumber"));

		Collection<SectionInfo> sections = service.findSectionByStudentNumber(studentNumber);
		Collection<SectionInfo> availableSections = service.findSectionNotEnlistedByStudent(studentNumber);
		request.setAttribute("sections", sections);
		request.setAttribute("availableSections", availableSections);
		request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
	}

}

package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Subject;
import com.orangeandbronze.enlistment.service.SectionService;

/**
 * Servlet implementation class CreateSectionController
 */
@WebServlet("/create_section")
public class CreateSectionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SectionService service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = (SectionService) getServletContext().getAttribute("sectionService");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sectionId = request.getParameter("sectionId");
		String subjectId = request.getParameter("subjectId");
		String roomName = request.getParameter("roomName");
		String period = parsePeriod(request);
		service.create(new Section(sectionId, new Subject(subjectId), Schedule.valueOf(period), new Room(roomName)));
		response.sendRedirect("admin_dashboard");
	}

	private String parsePeriod(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String[] days = request.getParameterValues("day");
		String period = request.getParameter("period");
		for (String day : days) {
			sb.append(day);
		}
		sb.append(" ");
		sb.append(period);
		return sb.toString();
	}

}

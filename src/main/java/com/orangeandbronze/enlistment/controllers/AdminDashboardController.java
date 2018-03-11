package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangeandbronze.enlistment.domain.Faculty;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Subject;
import com.orangeandbronze.enlistment.service.AdminService;
import com.orangeandbronze.enlistment.service.LoginService;
import com.orangeandbronze.enlistment.service.SectionInfo;

@WebServlet("/admin_dashboard")
public class AdminDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AdminService service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = (AdminService) getServletContext().getAttribute("adminService");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Collection<Subject> subjects = service.getAllSubjects();
		Collection<Room> rooms = service.getAllRooms();
		Collection<SectionInfo> sections = service.getAllSections();
		Collection<Faculty> faculties = service.getAllFaculties();
		request.setAttribute("subjects", subjects);
		request.setAttribute("rooms", rooms);
		request.setAttribute("sections", sections);
		request.setAttribute("faculties", faculties);
		request.getRequestDispatcher("/WEB-INF/admin_dashboard.jsp").forward(request, response);
	}

}

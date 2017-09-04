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

@WebServlet("/enlist")
public class EnlistmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EnlistService service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = (EnlistService) getServletContext().getAttribute("enlistService");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] sectionChecked = request.getParameterValues("section");

		PrintWriter out = response.getWriter();
		out.println("<h1>Your Enlisted Sections:</h1>");
		out.println("<ol>");
		for (String section : sectionChecked) {
			out.println("<li>" + section + "</li>");
		}
		out.println("</ol>");
	}

}

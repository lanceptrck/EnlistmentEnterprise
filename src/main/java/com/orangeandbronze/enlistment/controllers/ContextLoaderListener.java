package com.orangeandbronze.enlistment.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.orangeandbronze.enlistment.service.EnlistService;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		EnlistService enlistService = new MockEnlistService();

		sce.getServletContext().setAttribute("enlistService", enlistService);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}

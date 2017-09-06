package com.orangeandbronze.enlistment.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.service.EnlistService;
import com.orangeandbronze.enlistment.service.LoginService;
import com.orangeandbronze.enlistment.service.SectionService;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DataSource ds = DataSourceManager.getDataSource();

		StudentDAO studentDao = new StudentDaoJdbc(ds);
		SectionDAO sectionDao = new SectionDaoJdbc(ds);

		LoginService loginService = new LoginService(studentDao);
		EnlistService enlistService = new MockEnlistService();
		SectionService sectionService = new SectionService(sectionDao);

		sce.getServletContext().setAttribute("enlistService", enlistService);
		sce.getServletContext().setAttribute("loginService", loginService);
		sce.getServletContext().setAttribute("sectionService", sectionService);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}

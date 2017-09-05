package com.orangeandbronze.enlistment.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.service.EnlistService;
import com.orangeandbronze.enlistment.service.LoginService;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DataSource ds = DataSourceManager.getDataSource();

		StudentDAO studentDao = new StudentDaoJdbc(ds);

		LoginService loginService = new LoginService(studentDao);
		EnlistService enlistService = new MockEnlistService();

		sce.getServletContext().setAttribute("enlistService", enlistService);
		sce.getServletContext().setAttribute("loginService", loginService);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}

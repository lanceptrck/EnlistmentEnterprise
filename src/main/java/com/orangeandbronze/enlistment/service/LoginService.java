package com.orangeandbronze.enlistment.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.Student;

public class LoginService {

	private StudentDAO studentDao;

	public LoginService(StudentDAO studentDao) {
		this.studentDao = studentDao;
	}

	public void login(int studentNumber, HttpSession session) {
		Map<String, String> studentInfo = studentDao.findUserInfobById(studentNumber);
		session.setAttribute("studentNumber", studentInfo.get("studentNumber"));
		session.setAttribute("lastName", studentInfo.get("lastName"));
		session.setAttribute("firstName", studentInfo.get("firstName"));
	}
}

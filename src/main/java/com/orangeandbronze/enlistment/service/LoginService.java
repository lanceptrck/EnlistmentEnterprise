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

	public Map<String, String> login(int studentNumber) {
		Map<String, String> studentInfo = studentDao.findUserInfobById(studentNumber);
		return studentInfo;
	}
}

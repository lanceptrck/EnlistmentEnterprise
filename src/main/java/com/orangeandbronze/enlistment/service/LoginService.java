package com.orangeandbronze.enlistment.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.orangeandbronze.enlistment.dao.AdminDAO;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.Student;

public class LoginService {

	private StudentDAO studentDao;
	private AdminDAO adminDao;

	public LoginService(StudentDAO studentDao, AdminDAO adminDao) {
		this.studentDao = studentDao;
		this.adminDao = adminDao;
	}

	public Map<String, String> login(int studentNumber) {
		Map<String, String> studentInfo = studentDao.findUserInfobById(studentNumber);
		return studentInfo;
	}

	public Map<String, String> loginAdmin(int id) {
		Map<String, String> adminInfo = adminDao.findUserInfobById(id);
		return adminInfo;
	}
}

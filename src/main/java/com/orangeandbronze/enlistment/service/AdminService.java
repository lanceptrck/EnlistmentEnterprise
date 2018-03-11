package com.orangeandbronze.enlistment.service;

import java.util.ArrayList;
import java.util.Collection;

import com.orangeandbronze.enlistment.dao.AdminDAO;
import com.orangeandbronze.enlistment.domain.Faculty;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Subject;

public class AdminService {
	private AdminDAO adminDao;

	public AdminService(AdminDAO adminDao) {
		this.adminDao = adminDao;
	}

	public Collection<Subject> getAllSubjects() {
		Collection<Subject> subjects = adminDao.getAllSubjects();
		return subjects;
	}

	public Collection<Room> getAllRooms() {
		Collection<Room> rooms = adminDao.getAllRooms();
		return rooms;
	}

	public Collection<SectionInfo> getAllSections() {
		Collection<Section> sections = adminDao.getAllSections();
		Collection<SectionInfo> sectionInfos = new ArrayList<>();
		sections.forEach(section -> sectionInfos.add(new SectionInfo(section)));
		return sectionInfos;
	}
	
	public Collection<Faculty> getAllFaculties(){
		return adminDao.getAllFaculties();
		
	}
}

package com.orangeandbronze.enlistment.service;

import java.util.ArrayList;
import java.util.Collection;

import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.domain.Section;

public class SectionService {

	private SectionDAO sectionDao;

	public SectionService(SectionDAO sectionDao) {
		this.sectionDao = sectionDao;
	}

	public Collection<SectionInfo> findSectionByStudentNumber(int studentNumber) {
		Collection<Section> sections = sectionDao.findByStudentNo(studentNumber);
		Collection<SectionInfo> studentSections = new ArrayList<>();
		sections.forEach(section -> studentSections.add(new SectionInfo(section)));
		return studentSections;
	}

	public Collection<SectionInfo> findSectionNotEnlistedByStudent(int studentNumber) {
		Collection<Section> sections = sectionDao.findByNotStudentNo(studentNumber);
		Collection<SectionInfo> studentAvailableSections = new ArrayList<>();
		sections.forEach(section -> studentAvailableSections.add(new SectionInfo(section)));
		return studentAvailableSections;
	}

	public void create(Section section) {
		sectionDao.create(section);
	}
}

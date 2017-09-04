package com.orangeandbronze.enlistment.service;

import com.orangeandbronze.enlistment.dao.*;
import com.orangeandbronze.enlistment.domain.*;
import java.util.*;
import static org.apache.commons.lang3.Validate.*;

public class EnlistService {

	private SectionDAO sectionDao;
	private StudentDAO studentDao;
	private EnlistmentsDAO enlistmentsDao;

	public EnlistService(SectionDAO sectionDao, StudentDAO studentDao,
			EnlistmentsDAO enlistmentsDao) {
		this.sectionDao = sectionDao;
		this.studentDao = studentDao;
		this.enlistmentsDao = enlistmentsDao;
	}

	public void enlist(int studentNo, String sectionId) {
		Student student = studentDao.findWithSectionsBy(studentNo);
		Section section = sectionDao.findBy(sectionId);
		student.enlist(section);
		enlistmentsDao.create(student, section);
	}

	public void cancel(int studentNo, String sectionId) {
		enlistmentsDao.delete(studentNo, sectionId);
	}

	public Collection<SectionInfo> getEnlistedSectionsOfStudent(int studentNo) {
		Collection<Section> sections = sectionDao.findByStudentNo(studentNo);
		Collection<SectionInfo> sectionInfos = new ArrayList<>();
		for (Section section : sections) {
			sectionInfos.add(new SectionInfo(section));
		}
		return sectionInfos;
	}

	public Collection<SectionInfo> getSectionsAvailableForStudent(
			Integer studentNo) {
		notNull(studentNo);
		Collection<Section> sections = sectionDao.findByNotStudentNo(studentNo);
		Collection<SectionInfo> sectionInfos = new ArrayList<>();
		for (Section section : sections) {
			sectionInfos.add(new SectionInfo(section));
		}
		return sectionInfos;
	}
}

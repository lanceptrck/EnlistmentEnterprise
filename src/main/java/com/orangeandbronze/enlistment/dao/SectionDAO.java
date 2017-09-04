package com.orangeandbronze.enlistment.dao;

import java.util.*;

import com.orangeandbronze.enlistment.domain.*;

public interface SectionDAO {

	/**
	 * @return The section that has Section ID @param sectionId . If none found,
	 *         returns Section.NONE.
	 */
	Section findBy(String sectionId);

	/**
	 * @return All the sectionInfos in the database.
	 */
	Collection<Section> findAll();

	/** Retrieve all the sectionInfos associated with the given student number. */
	Collection<Section> findByStudentNo(int studentNumber);

	/** Retrieve all sectionInfos NOT associated with the given student number. */
	Collection<Section> findByNotStudentNo(int studentNumber);
	
	/** Saves a NEW section. **/
	void create(Section section);
}

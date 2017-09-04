package com.orangeandbronze.enlistment.dao;

import java.util.*;

import com.orangeandbronze.enlistment.domain.*;

public interface FacultyDAO {
	
	/** Retrieves faculty without associated sectionInfos. **/
	Collection<Faculty> findAll();
	
	/** Retrieves faculty with associated sectionInfos. **/
	Faculty findBy(int facultyNumber);

}

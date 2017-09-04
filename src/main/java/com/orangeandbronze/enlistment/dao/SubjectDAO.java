package com.orangeandbronze.enlistment.dao;

import java.util.*;

import com.orangeandbronze.enlistment.domain.*;

public interface SubjectDAO {
	
	Collection<Subject> findAll();

	Collection<String> findAllIds();
}

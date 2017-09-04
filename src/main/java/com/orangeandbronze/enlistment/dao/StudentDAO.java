package com.orangeandbronze.enlistment.dao;

import com.orangeandbronze.enlistment.domain.*;

public interface StudentDAO extends UserDAO {

	/**
	 * Returns Student.NONE if the given student number was not found in the
	 * database.
	 */
	Student findWithSectionsBy(int studentNumber);
	
	/**
	 * Returns Student.NONE if the given student number was not found in the
	 * database.
	 */
	Student findWithoutSectionsBy(int studentNumber);
}

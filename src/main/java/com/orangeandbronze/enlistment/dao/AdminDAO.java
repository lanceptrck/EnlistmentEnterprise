package com.orangeandbronze.enlistment.dao;

import java.util.*;

import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Subject;

public interface AdminDAO extends UserDAO {

	Map<String, String> findAdminInfoBy(int adminId);

	Collection<Subject> getAllSubjects();

	Collection<Room> getAllRooms();
	
	Collection<Section> getAllSections();
}

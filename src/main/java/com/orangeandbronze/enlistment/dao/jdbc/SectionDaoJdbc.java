/*
 * 
 * TODO: Use Hamcrest to assert Class properties or assert manually. Do not override equals for every property
 * Don't separate field values (Constants).
 * Instead of util classes. use abstractDAO. parent of each DAOJDBC.
 * 
 * getSql()
 * Datasource
 * most common behaviors of each DAO
 * make most functionality of Dao into abstart methods
 * 
 * */

package com.orangeandbronze.enlistment.dao.jdbc;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.sql.DataSource;

import org.junit.Ignore;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.domain.Faculty;
import com.orangeandbronze.enlistment.domain.FacultyScheduleConflictException;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.RoomScheduleConflictException;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.SectionCreationException;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;
import com.orangeandbronze.enlistment.utils.SQLUtil;

public class SectionDaoJdbc implements SectionDAO {
	private final DataSource dataSource;

	public SectionDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Section findBy(String sectionId) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQLUtil.getInstance().getSql("FindSectionBySectionId.sql"))) {
			stmt.setString(1, sectionId);
			try (ResultSet rs = stmt.executeQuery()) {
				Section section = Section.NONE;
				if (rs.next()) {
					Collection<Student> enlistedStudents = new ArrayList<>(0);
					Integer[] studentNos = (Integer[]) rs.getArray(Section.STUDENTS).getArray();

					if (studentNos != null) {
						for (int studentNo : studentNos) {
							enlistedStudents.add(new Student(studentNo));
						}
					}

					section = new Section(rs, enlistedStudents);
				}
				return section;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving section data for section with section id: " + sectionId,
					e);
		}
	}

	@Override
	public Collection<Section> findAll() {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLUtil.getInstance().getSql("FindAllSections.sql"));) {
			try (ResultSet rs = stmt.executeQuery()) {
				Collection<Section> sections = new ArrayList<>();
				Section section = Section.NONE;
				while (rs.next()) {
					section = new Section(rs);
					sections.add(section);
				}
				return sections;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving all sections", e);
		}
	}

	@Override
	public Collection<Section> findByStudentNo(int studentNumber) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQLUtil.getInstance().getSql("FindSectionByStudentNo.sql"));) {
			stmt.setInt(1, studentNumber);
			try (ResultSet rs = stmt.executeQuery()) {
				Collection<Section> sections = new ArrayList<>();
				Section section = Section.NONE;
				while (rs.next()) {
					section = new Section(rs);
					sections.add(section);
				}
				return sections;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving sections of student with SN# " + studentNumber, e);
		}
	}

	// no students enlisted
	@Override
	public Collection<Section> findByNotStudentNo(int studentNumber) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						SQLUtil.getInstance().getSql("FindSectionsThatStudentHasNotEnlistedIn.sql"));) {
			stmt.setInt(1, studentNumber);
			try (ResultSet rs = stmt.executeQuery()) {
				Collection<Section> sections = new ArrayList<>();
				Section section = Section.NONE;
				while (rs.next()) {
					section = new Section(rs);
					sections.add(section);
				}
				return sections;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving sections not with student with SN# " + studentNumber, e);
		}
	}
	
	private boolean hasFacultyConflict(Section section) {
		try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            		SQLUtil.getInstance().getSql(("CountSectionByFacultyAndSchedule.sql")))) {
			
			stmt.setInt(1, section.getFaculty().getFacultyNumber());
			stmt.setString(2, section.getSchedule().toString());
			
			try (ResultSet rs = stmt.executeQuery()) {
				
		        rs.next();
		        
		        if(rs.getInt(1) > 0)
		        	return true;
		        else 
		        	return false;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Invalid sql statement"+ e);
		}
	}
	
	private boolean hasRoomScheduleConflict(Section section) {
		try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            		SQLUtil.getInstance().getSql(("CountSectionByRoomAndSchedule.sql")))) {
			
			stmt.setString(1, section.getSchedule().toString());
			stmt.setString(2, section.getRoom().getName());
			
			try (ResultSet rs = stmt.executeQuery()) {
				
		        rs.next();
		        
		        if(rs.getInt(1) > 0)
		        	return true;
		        else 
		        	return false;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Invalid sql statement"+ e);
		}
	}
	
	public void test(Section section) {
		Faculty faculty = section.getFaculty();
		faculty.addSection(section);
	}

	@Override
	public void create(Section section) {
		
		if(findBy(section.getSectionId()) != Section.NONE)
		{
			throw new SectionCreationException("Section ID:["+section.getSectionId()+"] exists already!");
		}
		
		if(hasFacultyConflict(section)) {
			throw new FacultyScheduleConflictException(section.getFaculty().toString()+"is already teaching at "+section.getSchedule());
		}
		
		if(hasRoomScheduleConflict(section)) {
			throw new RoomScheduleConflictException("Section "+section.getSectionId()+"is in conflict with an existing section at room "
					+ section.getRoom().getName());
		}
		
		try (Connection conn = dataSource.getConnection()) {
	        conn.setAutoCommit(false);
	        try (PreparedStatement stmt = conn.prepareStatement(
					SQLUtil.getInstance().getSql("CreateSection.sql"));) {
	            stmt.setString(1, section.getSectionId());
	            stmt.setString(2, section.getSubject().getSubjectId());
	            stmt.setString(3, section.getSchedule().toString());
	            stmt.setString(4, section.getRoom().getName());
	            stmt.setInt(5, section.getFaculty().getFacultyNumber());
	            stmt.setInt(6, section.getVersion());
	            if (stmt.executeUpdate() > 0) {
	                conn.commit();
	            } else {
	                conn.rollback();
	            }
	        } catch (SQLException e) {
	            conn.rollback();
	            throw e;
	        }
	        conn.setAutoCommit(true);
	    } catch (SQLException e) {
	        throw new DataAccessException(
	                "while inserting into create table for " + section,   e);
	    }
	}

}

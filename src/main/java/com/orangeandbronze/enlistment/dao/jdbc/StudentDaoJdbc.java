package com.orangeandbronze.enlistment.dao.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;
import com.orangeandbronze.enlistment.utils.SQLUtil;

public class StudentDaoJdbc implements StudentDAO {
	private final DataSource dataSource;

	public StudentDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Student findWithoutSectionsBy(int studentNumber) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM students " + "WHERE student_number = ?")) {
			stmt.setInt(1, studentNumber);
			Student student = Student.NONE;

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String firstName = rs.getString(Student.FIRST_NAME);
					String lastName = rs.getString(Student.LAST_NAME);
					student = new Student(studentNumber, firstName, lastName);
				}
			}
			return student;
		} catch (SQLException e) {
			throw new DataAccessException(
					"Problem retrieving student data for student with student number : " + studentNumber, e);
		}

	}

	@Override
	public Map<String, String> findUserInfobById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Student findWithSectionsBy(int studentNumber) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQLUtil.getInstance().getSql("FindStudentsByStudentNo.sql"))) {
			stmt.setInt(1, studentNumber);
			boolean found = false;
			Collection<Section> sections = new ArrayList<>();
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					if (!found) {
						found = true;
					}
					if (!StringUtils.isBlank(rs.getString(Section.SECTION_ID))) {
						sections.add(new Section(rs.getString(Section.SECTION_ID),
								new Subject(rs.getString(Subject.SUBJECT_ID)),
								Schedule.valueOf(rs.getString(Schedule.SCHEDULE)),
								new Room(rs.getString(Room.ROOM_NAME), rs.getInt(Room.CAPACITY))));
					}
				}
			}
			return found ? new Student(studentNumber, sections) : Student.NONE;
		} catch (SQLException e) {
			throw new DataAccessException(
					"Problem retrieving student data for student with student number " + studentNumber, e);
		}
	}
}

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

public class StudentDaoJdbc implements StudentDAO {
	private final DataSource dataSource;
	private final static Map<String, String> sqlCache = new HashMap<>();

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
					String firstName = rs.getString("firstname");
					String lastName = rs.getString("lastname");
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
				PreparedStatement stmt = conn.prepareStatement(getSql("FindStudentsByStudentNo.sql"))) {
			stmt.setInt(1, studentNumber);
			boolean found = false;
			Collection<Section> sections = new ArrayList<>();
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					if (!found) {
						found = true;
					}
					if (!StringUtils.isBlank(rs.getString("section_id"))) {
						sections.add(new Section(rs.getString("section_id"), Subject.NONE, Schedule.TBA, Room.TBA));
					}
				}
			}
			return found ? new Student(studentNumber, sections) : Student.NONE;
		} catch (SQLException e) {
			throw new DataAccessException(
					"Problem retrieving student data for student with student number " + studentNumber, e);
		}
	}

	private String getSql(String sqlFile) {
		if (!sqlCache.containsKey(sqlFile)) {
			try (Reader reader = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream(sqlFile)))) {
				StringBuilder bldr = new StringBuilder();
				int i = 0;
				while ((i = reader.read()) > 0) {
					bldr.append((char) i);
				}
				sqlCache.put(sqlFile, bldr.toString());
				return bldr.toString();
			} catch (IOException e) {
				throw new DataAccessException("Problem while trying to read file '" + sqlFile + "' from classpath.", e);
			}
		}

		return sqlCache.get(sqlFile);
	}
}

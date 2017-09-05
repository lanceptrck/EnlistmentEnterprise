package com.orangeandbronze.enlistment.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.utils.SQLUtil;

public class EnlistmentDaoJdbc implements EnlistmentsDAO {
	private final DataSource dataSource;

	public EnlistmentDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void create(Student student, Section section) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLUtil.getInstance().getSql("EnlistStudent.sql"))) {
			stmt.setInt(1, student.getStudentNumber());
			stmt.setString(2, section.getSectionId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(
					"Problem enlisting student with student number : " + student.getStudentNumber(), e);
		}
	}

	@Override
	public void delete(int studentNumber, String sectionId) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLUtil.getInstance().getSql("DeleteStudent.sql"))) {
			stmt.setInt(1, studentNumber);
			stmt.setString(2, sectionId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Problem deleting student with student number : " + studentNumber, e);
		}
	}

}

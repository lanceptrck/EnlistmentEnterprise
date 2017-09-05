package com.orangeandbronze.enlistment.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.dao.StaleDataException;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.utils.SQLUtil;

public class EnlistmentDaoJdbc implements EnlistmentsDAO {
	private final DataSource dataSource;

	public EnlistmentDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// @Override
	// public void create(Student student, Section section) {
	// try (Connection conn = dataSource.getConnection();
	// PreparedStatement stmt =
	// conn.prepareStatement(SQLUtil.getInstance().getSql("EnlistStudent.sql"))) {
	// conn.setAutoCommit(false);
	// stmt.setInt(1, student.getStudentNumber());
	// stmt.setString(2, section.getSectionId());
	// stmt.executeUpdate();
	// } catch (SQLException e) {
	// throw new DataAccessException(
	// "Problem enlisting student with student number : " +
	// student.getStudentNumber(), e);
	// }
	// }

	@Override
	public void create(Student student, Section section) {
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement stmtUpdateVersion = conn.prepareStatement(
					"UPDATE sections SET version = version + 1 " + "WHERE section_id = ? AND version = ?")) {
				stmtUpdateVersion.setString(1, section.getSectionId());
				stmtUpdateVersion.setInt(2, section.getVersion());
				int recordsUpdated = stmtUpdateVersion.executeUpdate();
				if (recordsUpdated != 1) {
					conn.rollback();
					throw new StaleDataException(
							"Enlistment data for " + section + " was not updated to current version.");
				}
			} catch (SQLException e) {
				conn.rollback(); // rollback in case there are problems
				throw e;
			}

			try (PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO enlistments(student_number, section_id) VALUES(?, ?) ")) {
				stmt.setInt(1, student.getStudentNumber());
				stmt.setString(2, section.getSectionId());
				int rowsUpdated = stmt.executeUpdate();
				if (rowsUpdated > 0) { // version incremented only if rows were changed
					conn.commit();
				} else {
					conn.rollback();
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
			conn.setAutoCommit(true); // avoids unfinished transaction
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

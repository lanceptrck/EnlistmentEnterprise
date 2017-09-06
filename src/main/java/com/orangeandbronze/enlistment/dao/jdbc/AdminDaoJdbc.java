package com.orangeandbronze.enlistment.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.AdminDAO;
import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.domain.Admin;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;
import com.orangeandbronze.enlistment.utils.SQLUtil;

public class AdminDaoJdbc implements AdminDAO {
	private final DataSource dataSource;

	public AdminDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Map<String, String> findUserInfobById(int id) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQLUtil.getInstance().getSql("FindAdminByAdminId.sql"))) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				Map<String, String> adminInfo = new HashMap<String, String>();
				if (rs.next()) {
					adminInfo.put("studentNumber", rs.getString("id"));
					adminInfo.put("firstName", rs.getString("firstname"));
					adminInfo.put("lastName", rs.getString("lastname"));
				}
				return adminInfo;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving admin data for admin with admin id: " + id, e);
		}
	}

	@Override
	public Map<String, String> findAdminInfoBy(int adminId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Subject> getAllSubjects() {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement("select * from subjects")) {
			try (ResultSet rs = stmt.executeQuery()) {
				Collection<Subject> subjects = new ArrayList<>();
				while (rs.next()) {
					subjects.add(new Subject(rs.getString("subject_id")));
				}
				return subjects;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving subjects", e);
		}
	}

	@Override
	public Collection<Room> getAllRooms() {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement("select * from rooms")) {
			try (ResultSet rs = stmt.executeQuery()) {
				Collection<Room> rooms = new ArrayList<>();
				while (rs.next()) {
					rooms.add(new Room(rs.getString("room_name"), rs.getInt("capacity")));
				}
				return rooms;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving subjects", e);
		}
	}

	@Override
	public Collection<Section> getAllSections() {
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

}

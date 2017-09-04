package com.orangeandbronze.test_jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.postgresql.ds.*;

public class RoomCapacityRetriever {
	private final DataSource ds;

	RoomCapacityRetriever() {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setDatabaseName("enlistment");
		ds.setUser("postgres");
		ds.setPassword("password");
		this.ds = ds;
	}

	int getRoomCapacity(String roomName) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("SELECT capacity FROM rooms WHERE room_name = ?");
			stmt.setString(1, roomName);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				return rs.getInt("capacity");
			}
		} catch (SQLException e) {
			throw new RuntimeException("problem while retrieving capacity of room with name: " + roomName, e);
		}
	}

	public static void main(String[] args) {
		System.out.println("Room capacity: " + new RoomCapacityRetriever().getRoomCapacity("AVR1"));
	}
}

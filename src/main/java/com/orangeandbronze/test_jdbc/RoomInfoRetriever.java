package com.orangeandbronze.test_jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.ds.PGSimpleDataSource;

public class RoomInfoRetriever {

	public static void main(String[] args) {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setDatabaseName("enlistment");
		ds.setUser("postgres");
		ds.setPassword("password");

		try (Connection conn = ds.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("room_name") + " - " + rs.getInt("capacity"));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

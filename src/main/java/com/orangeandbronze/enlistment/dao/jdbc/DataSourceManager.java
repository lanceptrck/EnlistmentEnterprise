package com.orangeandbronze.enlistment.dao.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

public class DataSourceManager {

	private static DataSource dataSource;

	public static DataSource getDataSource() {
		if (dataSource == null) {
			Properties prop = new Properties();
			String propFileName = "pg.datasource.properties";
			try (Reader reader = new BufferedReader(new InputStreamReader(
					DataSourceManager.class.getClassLoader().getResourceAsStream(propFileName)))) {
				prop.load(reader);
			} catch (IOException e) {
				throw new RuntimeException("problem reading file " + propFileName, e);
			}
			PGSimpleDataSource ds = new PGSimpleDataSource();
			ds.setServerName(prop.getProperty("servername"));
			ds.setDatabaseName(prop.getProperty("database"));
			ds.setUser(prop.getProperty("user"));
			ds.setPassword(prop.getProperty("password"));
			dataSource = ds;
		}
		return dataSource;
	}
}

package com.orangeandbronze.enlistment.service;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;

import com.orangeandbronze.enlistment.dao.AdminDAO;
import com.orangeandbronze.enlistment.dao.jdbc.AdminDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.EnlistmentDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.domain.Faculty;

public class AdminServiceTest {
	
	private static AdminDAO adminDao;
	private static DataSource dataSource = DataSourceManager.getDataSource();
	
	@Before
	public void cleanInsert() throws Exception {
		IDatabaseConnection connection = getIDatabaseConnection();
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setDtdMetadata(false);
		IDataSet dataSet = builder
				.build(EnlistServiceIT.class.getClassLoader().getResourceAsStream("DefaultDataset.xml"));
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} finally {
			connection.close();
		}
		
		
		adminDao = new AdminDaoJdbc(dataSource);

	}
	
	private static IDatabaseConnection getIDatabaseConnection() throws SQLException, DatabaseUnitException {
		Connection jdbcConnection = dataSource.getConnection();
		// turn off referential integrity to make it easy to insert and delete
		// test data
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		return new DatabaseConnection(jdbcConnection);
	}
	
	@Test
	public void getAllFaculties() {
		Collection<Faculty> faculties = adminDao.getAllFaculties();
		faculties.forEach(currFaculty -> System.out.print(currFaculty));
	}
	
	

}

package com.orangeandbronze.enlistment.dao.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.*;
import org.postgresql.ds.PGSimpleDataSource;

import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.*;

public class StudentDaoJdbcIT {
	private DataSource dataSource;
	Connection jdbcConnection;
	private IDataSet dataSet;
	FlatXmlDataSetBuilder builder;
	IDatabaseConnection dbUnitConnection;
	StudentDAO dao;

	@Before
	public void init() throws Exception {
		dataSource = DataSourceManager.getDataSource();
		jdbcConnection = dataSource.getConnection();
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");

		builder = new FlatXmlDataSetBuilder();
		dataSet = builder.build(getClass().getClassLoader().getResourceAsStream("DefaultDataset.xml"));
		dbUnitConnection = new DatabaseConnection(jdbcConnection);
		builder.setDtdMetadata(false);

		dao = new StudentDaoJdbc(dataSource);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
		} finally {
			dbUnitConnection.close(); // don't forget to close the connection!
		}
	}

	@Test
	public void findStudentWithNoSections() throws Exception {
		Integer studentNumber = 1;
		Student actualStudent = dao.findWithoutSectionsBy(studentNumber);
		assertEquals(studentNumber, actualStudent.getStudentNumber());
		assertEquals("Mickey", actualStudent.getFirstname());
		assertEquals("Mouse", actualStudent.getLastname());
	}

	@Test
	public void findStudentThatHasSections() throws Exception {
		Student actualStudent = dao.findWithSectionsBy(3);
		Collection<Section> actualSections = actualStudent.getSections();

		assertThat(actualSections, Matchers.containsInAnyOrder(new Section("HASSTUDENTS", new Subject("COM1"),
				Schedule.valueOf("TF 11:30-13:00"), new Room("AVR1", 10))));
	}
	
	@Test
	public void findUserInfoById_Mickey_Mouse() throws Exception {
		  Map<String, String> userInfo = dao.findUserInfobById(1);
		  assertEquals(userInfo.get("firstName"), "Mickey");
	}

}

package com.orangeandbronze.enlistment.dao.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.*;
import org.postgresql.ds.PGSimpleDataSource;

import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.*;

public class StudentDaoJdbcIT {

	@Test
	public void findStudentWithNoSections() throws Exception {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setDatabaseName("enlistment");
		ds.setUser("postgres");
		ds.setPassword("password");
		Connection jdbcConnection = ds.getConnection();
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		IDatabaseConnection dbUnitConnection = new DatabaseConnection(jdbcConnection);
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setDtdMetadata(false);
		IDataSet dataSet = builder.build(getClass().getClassLoader().getResourceAsStream("StudentNoSections.xml"));

		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
		} finally {
			dbUnitConnection.close();
		}

		StudentDAO dao = new StudentDaoJdbc(ds);
		Integer studentNumber = 1;
		Student actualStudent = dao.findWithoutSectionsBy(studentNumber);
		assertEquals(studentNumber, actualStudent.getStudentNumber());
		assertEquals("Mickey", actualStudent.getFirstname());
		assertEquals("Mouse", actualStudent.getLastname());
	}

	@Test
	public void findStudentThatHasSections() throws Exception {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setDatabaseName("enlistment");
		ds.setUser("postgres");
		ds.setPassword("password");
		Connection jdbcConnection = ds.getConnection();
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED;");
		IDatabaseConnection dbUnitConnection = new DatabaseConnection(jdbcConnection);
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setDtdMetadata(false);
		IDataSet dataSet = builder.build(getClass().getClassLoader().getResourceAsStream("StudentHasSections.xml"));
		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
		} finally {
			dbUnitConnection.close(); // don't forget to close the connection!
		}
		
		StudentDAO dao = new StudentDaoJdbc(ds); 
		Student actualStudent = dao.findWithSectionsBy(1);
		Collection<Section> actualSections = actualStudent.getSections();
		
		assertThat(actualSections,
			       Matchers.containsInAnyOrder(new Section("MHX123", Subject.NONE,
			             Schedule.TBA, Room.TBA),
			       new Section("TFX555", Subject.NONE, Schedule.TBA, Room.TBA),
			       new Section("MHW432", Subject.NONE, Schedule.TBA, Room.TBA)));
	}

}

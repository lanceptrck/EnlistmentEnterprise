package com.orangeandbronze.enlistment.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;

public class EnlistmentDaoJdbcIT {
	private DataSource ds;
	Connection jdbcConnection;
	private IDataSet dataSet;
	FlatXmlDataSetBuilder builder;
	IDatabaseConnection dbUnitConnection;

	@Before
	public void setUpDataset() throws Exception {
		ds = DataSourceManager.getDataSource();
		jdbcConnection = ds.getConnection();
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");

		builder = new FlatXmlDataSetBuilder();
		dataSet = builder.build(getClass().getClassLoader().getResourceAsStream("DefaultDataset.xml"));
		dbUnitConnection = new DatabaseConnection(jdbcConnection);
		builder.setDtdMetadata(false);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
		} finally {
			dbUnitConnection.close(); // don't forget to close the connection!
		}
	}

	@Test
	public void enlistStudent() throws Exception {
		EnlistmentsDAO dao = new EnlistmentDaoJdbc(ds);
		Student student = new Student(1);
		Section section = new Section("MHX123", Subject.NONE, Schedule.TBA, Room.TBA);
		dao.create(student, section);

		jdbcConnection = ds.getConnection();
		PreparedStatement stmt = jdbcConnection.prepareStatement("SELECT * FROM enlistments where student_number = ?");
		stmt.setInt(1, 1);
		try (ResultSet rs = stmt.executeQuery()) {
			if (!rs.next()) {
				fail("Error enlisting student");
			} else {
				assertEquals(1, rs.getInt(Student.STUDENT_NUMBER));
				assertEquals("MHX123", rs.getString(Section.SECTION_ID));
			}
		}
	}

	@Test
	public void deleteStudent() throws Exception {
		EnlistmentsDAO dao = new EnlistmentDaoJdbc(ds);
		dao.delete(3, "HASSTUDENTS");

		jdbcConnection = ds.getConnection();
		PreparedStatement stmt = jdbcConnection
				.prepareStatement("SELECT * FROM enlistments where student_number = ?" + " AND section_id = ?");
		stmt.setInt(1, 3);
		stmt.setString(2, "HASSTUDENTS");

		try (ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				fail("Error deleting student");
			}
		}
	}

	@Test
	public void simultaneousCreate() throws Exception {
		SectionDAO sectionDao = new SectionDaoJdbc(ds);
		EnlistmentsDAO enlistmentDao = new EnlistmentDaoJdbc(ds);

		Student student1 = new Student(1);
		Student student2 = new Student(2);
		Thread thread1 = new Thread() {
			public void run() {
				Section section = sectionDao.findBy("CAPACITY1"); // capacity 1
				student1.enlist(section);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				enlistmentDao.create(student1, section);
			}
		};

		Thread thread2 = new Thread() {
			public void run() {
				Section section = sectionDao.findBy("CAPACITY1"); // capacity 1
				student2.enlist(section);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				enlistmentDao.create(student2, section);
			}
		};
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();

		String sql = "SELECT COUNT(*) FROM enlistments " + " WHERE section_id = 'CAPACITY1'";
		ResultSet rs = ds.getConnection().prepareStatement(sql).executeQuery();
		rs.next();
		assertEquals(1, rs.getInt(1));
	}
}

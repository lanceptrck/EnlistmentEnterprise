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
import com.orangeandbronze.enlistment.domain.SameSubjectException;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;

public class EnlistmentDaoJdbcIT {
	private DataSource dataSource;
	Connection jdbcConnection;
	private IDataSet dataSet;
	FlatXmlDataSetBuilder builder;
	IDatabaseConnection dbUnitConnection;
    private EnlistmentsDAO enlistmentDao;
    private SectionDAO sectionDao;
    private StudentDAO studentDao;

	@Before
	public void init() throws Exception {
		dataSource = DataSourceManager.getDataSource();
		jdbcConnection = dataSource.getConnection();
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
		
    	enlistmentDao = new EnlistmentDaoJdbc(dataSource);
    	sectionDao = new SectionDaoJdbc(dataSource);
    	studentDao = new StudentDaoJdbc(dataSource);
	}
	
    @Test (expected = SameSubjectException.class)
    public void createNewEnlistment_with_same_enrolled_subject() throws Exception {

	    Student student = studentDao.findWithSectionsBy(3);
	    Section section = sectionDao.findBy("MHY987");
	    student.enlist(section);
	    enlistmentDao.create(student, section);

	}
    
    @Test
    public void cancelEnlistment_happy_path() throws Exception {

	    Student student = studentDao.findWithSectionsBy(3);
	    Section section = sectionDao.findBy("HASSTUDENTS");

	    student.cancel(section);
	    enlistmentDao.delete(student.getStudentNumber(), section.getSectionId());
	    
		jdbcConnection = dataSource.getConnection();
		PreparedStatement stmt = jdbcConnection.prepareStatement("SELECT COUNT(*) FROM enlistments where section_id = ?");
		stmt.setString(1, "HASSTUDENTS");
		try (ResultSet rs = stmt.executeQuery()) {
			if (!rs.next()) {
				fail("Error canceling enlistment for student "+student);
			} else {
		        assertEquals(1, rs.getInt(1)); // Before 2 students were enrolled, now we have only 1 which is Student number 4
			}
		}
	}

	@Test
	public void enlistStudent() throws Exception {
		Student student = new Student(1);
		Section section = new Section("MHX123", Subject.NONE, Schedule.TBA, Room.TBA);
		enlistmentDao.create(student, section);

		jdbcConnection = dataSource.getConnection();
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
		
		enlistmentDao.delete(3, "HASSTUDENTS");

		jdbcConnection = dataSource.getConnection();
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
		ResultSet rs = dataSource.getConnection().prepareStatement(sql).executeQuery();
		rs.next();
		assertEquals(1, rs.getInt(1));
	}
}

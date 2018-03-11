package com.orangeandbronze.enlistment.service;

import static org.junit.Assert.*;

import java.sql.*;

import javax.sql.*;

import org.dbunit.*;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import org.junit.*;

import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.EnlistmentDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.domain.*;

/** Integration test - accesses database **/
public class EnlistServiceIT {

	// made static so that one instance across multiple unit test objects
	private static EnlistService service;
	private static SectionDAO sectionDao;
	private static StudentDAO studentDao;
	private static EnlistmentsDAO enlistmentsDao;
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
		sectionDao = new SectionDaoJdbc(dataSource);
		studentDao = new StudentDaoJdbc(dataSource);
		enlistmentsDao = new EnlistmentDaoJdbc(dataSource);
		service = new EnlistService(sectionDao, studentDao, enlistmentsDao);

	}

	private static IDatabaseConnection getIDatabaseConnection() throws SQLException, DatabaseUnitException {
		Connection jdbcConnection = dataSource.getConnection();
		// turn off referential integrity to make it easy to insert and delete
		// test data
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		return new DatabaseConnection(jdbcConnection);
	}

	@Test
	public void enlistFirstSection() throws Exception {
		final int STUDENT_NO = 1;
		final String SECTION_ID = "MHX123";
		service.enlist(STUDENT_NO, SECTION_ID);
		ITable table = getIDatabaseConnection().createQueryTable("result",
				"select section_id from enlistments where student_number = " + STUDENT_NO);
		assertEquals(SECTION_ID, table.getValue(0, "section_id"));
	}

	@Test
	public void cancelSection() throws Exception {
		final int STUDENT_NO = 4;
		final String SECTION_ID = "HASSTUDENTS";
		service.cancel(STUDENT_NO, SECTION_ID);
		ITable table = getIDatabaseConnection().createQueryTable("result",
				"select section_id from enlistments where SECTION_ID = '" + SECTION_ID + "'");
		assertEquals(1, table.getRowCount());
	}

	@Test(expected = ScheduleConflictException.class)
	public void enlistSectionSameScheduleAsCurrentSection() throws Exception {
		final int STUDENT_NO = 1;
		final String SECTION_ID_1 = "MHX123";
		final String SECTION_ID_2 = "MHW432";
		service.enlist(STUDENT_NO, SECTION_ID_1);
		service.enlist(STUDENT_NO, SECTION_ID_2);
	}

	@Test(expected = SameSubjectException.class)
	public void enlistSectionSameSubject() throws Exception {
		final int STUDENT_NO = 1;
		final String SECTION_ID_1 = "MHW432"; // COM 1
		final String SECTION_ID_2 = "MHY987"; // COM 1
		service.enlist(STUDENT_NO, SECTION_ID_1);
		service.enlist(STUDENT_NO, SECTION_ID_2);
	}

	@Test
	public void simultaneousEnlistment() throws Exception {
		final int STUDENT_NO1 = 1;
		final int STUDENT_NO2 = 2;
		final String SEC_ID = "CAPACITY1"; // section with capacity 1
		Thread thread1 = new Thread() {
			public void run() {
				service.enlist(STUDENT_NO1, SEC_ID);
			}
		};
		Thread thread2 = new Thread() {
			public void run() {
				service.enlist(STUDENT_NO2, SEC_ID);
			}
		};
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();
		String sql = "SELECT COUNT(*) FROM enlistments WHERE section_id = '" + SEC_ID + "'";
		ResultSet rs = dataSource.getConnection().prepareStatement(sql).executeQuery();
		rs.next();
		assertEquals(1, rs.getInt(1));
	}
}

package com.orangeandbronze.enlistment.service;

import static org.junit.Assert.*;

import java.sql.*;

import javax.sql.*;

import org.dbunit.*;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import org.h2.jdbcx.*;
import org.junit.*;

import com.orangeandbronze.enlistment.domain.*;

/** Integration test - accesses database **/
public class EnlistServiceIT {

	// made static so that one instance across multiple unit test objects
	private final static EnlistService service;
	private final static DataSource dataSource;

	static {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:./enlistment");
		ds.setUser("sa");
		ds.setPassword("");
		dataSource = ds;
		service = new EnlistService(null, null, null);
	}

	@Before
	public void cleanInsert() throws Exception {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setDtdMetadata(false);
		IDataSet dataSet = builder.build(EnlistServiceIT.class.getClassLoader()
				.getResourceAsStream("DefaultDataset.xml"));
		IDatabaseConnection connection = getIDatabaseConnection();
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} finally {
			connection.close();
		}
	}

	private static IDatabaseConnection getIDatabaseConnection()
			throws SQLException, DatabaseUnitException {
		Connection jdbcConnection = dataSource.getConnection();
		// turn off referential integrity to make it easy to insert and delete
		// test data
		jdbcConnection.createStatement()
				.execute("SET REFERENTIAL_INTEGRITY FALSE;");
		return new DatabaseConnection(jdbcConnection);
	}

	@Ignore
	@Test
	public void enlistFirstSection() throws Exception {
		final int STUDENT_NO = 1;
		final String SECTION_ID = "MHX123";
		service.enlist(STUDENT_NO, SECTION_ID);
		ITable table = getIDatabaseConnection().createQueryTable("result",
				"select section_id from student_enlisted_sections where student_number = "
						+ STUDENT_NO);
		assertEquals(SECTION_ID, table.getValue(0, "section_id"));
	}

	@Ignore
	@Test
	public void cancelSection() throws Exception {
		final int STUDENT_NO = 4;
		final String SECTION_ID = "HASSTUDENTS";
		service.cancel(STUDENT_NO, SECTION_ID);
		ITable table = getIDatabaseConnection().createQueryTable("result",
				"select section_id from student_enlisted_sections where SECTION_ID = '"
						+ SECTION_ID + "'");
		assertEquals(1, table.getRowCount());
	}

	@Ignore
	@Test(expected = ScheduleConflictException.class)
	public void enlistSectionSameScheduleAsCurrentSection() throws Exception {
		final int STUDENT_NO = 1;
		final String SECTION_ID_1 = "MHX123";
		final String SECTION_ID_2 = "MHW432";
		service.enlist(STUDENT_NO, SECTION_ID_1);
		service.enlist(STUDENT_NO, SECTION_ID_2);
	}

	@Ignore
	@Test(expected = SameSubjectException.class)
	public void enlistSectionSameSubject() throws Exception {
		final int STUDENT_NO = 1;
		final String SECTION_ID_1 = "MHW432"; // COM 1
		final String SECTION_ID_2 = "MHY987"; // COM 1
		service.enlist(STUDENT_NO, SECTION_ID_1);
		service.enlist(STUDENT_NO, SECTION_ID_2);
	}

	@Ignore
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
		String sql = "SELECT COUNT(*) FROM student_enlisted_sections WHERE section_id = '"
				+ SEC_ID + "'";
		ResultSet rs = dataSource.getConnection().prepareStatement(sql)
				.executeQuery();
		rs.next();
		assertEquals(1, rs.getInt(1));
	}
}

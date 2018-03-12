package com.orangeandbronze.enlistment.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.domain.Days;
import com.orangeandbronze.enlistment.domain.Faculty;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.RoomScheduleConflictException;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.SectionCreationException;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;

public class SectionDaoJdbcIT {
	private DataSource dataSource;
	Connection jdbcConnection;
	private IDataSet dataSet;
	FlatXmlDataSetBuilder builder;
	IDatabaseConnection dbUnitConnection;
	SectionDAO dao;

	@Before
	public void init() throws Exception {
		dataSource = DataSourceManager.getDataSource();
		jdbcConnection = dataSource.getConnection();
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");

		builder = new FlatXmlDataSetBuilder();
		dataSet = builder.build(getClass().getClassLoader().getResourceAsStream("DefaultDataset.xml"));
		dbUnitConnection = new DatabaseConnection(jdbcConnection);
		builder.setDtdMetadata(false);
		
		dao = new SectionDaoJdbc(dataSource);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
		} finally {
			dbUnitConnection.close(); // don't forget to close the connection!
		}
	}

	@Test
	public void findSectionByIdNoSectionFound() {
		Section foundSection = dao.findBy("RANDOMSECTIONID");

		assertEquals(Section.NONE, foundSection);
	}

	@Test
	public void findSectionByIdNoStudents() {
		Section section = new Section("MHX123", new Subject("MATH11"), Schedule.valueOf("MTH 08:30-10:00"),
				new Room("MATH105", 10), new Faculty(1, "Optimus", "Prime"), 0);

		Section foundSection = dao.findBy(section.getSectionId());

		assertEquals(section, foundSection);
		assertEquals(section.getStudents(), foundSection.getStudents());
	}

	@Test
	public void findSectionByIdHasStudents() {
		Collection<Student> students = new ArrayList<>();
		students.add(new Student(3));
		students.add(new Student(4));

		Section section = new Section("HASSTUDENTS", new Subject("COM1"), Schedule.valueOf("TF 11:30-13:00"),
				new Room("AVR1", 10), new Faculty(3, "Clark", "Kent"), students, 0);

		Section foundSection = dao.findBy(section.getSectionId());

		assertEquals(foundSection, section);
		assertEquals(foundSection.getStudents(), section.getStudents());
	}
	
	
	@Test
	public void findAllSections() {
		Collection<Section> expectedSections = new ArrayList<>();
		expectedSections.add(new Section("MHX123", new Subject("MATH11"), Schedule.valueOf("MTH 08:30-10:00"),
				new Room("MATH105", 10), new Faculty(1, "Optimus", "Prime")));
		expectedSections.add(new Section("TFX555", new Subject("PHILO1"), Schedule.valueOf("TF 10:00-11:30"),
				new Room("AS113", 10), new Faculty(2, "Buzz", "Lightyear")));
		expectedSections.add(new Section("MHW432", new Subject("COM1"), Schedule.valueOf("MTH 08:30-10:00"),
				new Room("AVR1", 10), new Faculty(3, "Clark", "Kent")));
		expectedSections.add(new Section("TFZ321", new Subject("PHYSICS71"), Schedule.valueOf("TF 14:30-16:00"),
				new Room("IP103", 10), new Faculty(4, "Tony", "Stark")));
		expectedSections.add(new Section("MHY987", new Subject("COM1"), Schedule.valueOf("MTH 11:30-13:00"),
				new Room("AVR2", 10), new Faculty(1, "Optimus", "Prime")));
		expectedSections.add(new Section("CAPACITY1", new Subject("COM1"), Schedule.valueOf("MTH 11:30-13:00"),
				new Room("CAPACITY1", 1), new Faculty(2, "Buzz", "Lightyear")));
		expectedSections.add(new Section("HASSTUDENTS", new Subject("COM1"), Schedule.valueOf("TF 11:30-13:00"),
				new Room("AVR1", 10), new Faculty(3, "Clark", "Kent")));
		expectedSections.add(new Section("NOSTUDENTS", new Subject("MATH11"), Schedule.valueOf("TF 08:30-10:00"),
				new Room("MATH105", 10), new Faculty(4, "Tony", "Stark")));
		Collection<Section> actual = dao.findAll();

		assertEquals(expectedSections, actual);
	}

	@Test
	public void findSectionsByStudentNo() {
		Collection<Section> expectedSections = new ArrayList<>();
		expectedSections.add(new Section("HASSTUDENTS", new Subject("COM1"), Schedule.valueOf("TF 11:30-13:00"),
				new Room("AVR1", 10), new Faculty(3, "Clark", "Kent"), 0));

		Collection<Section> actual = dao.findByStudentNo(3);

		assertEquals(expectedSections, actual);
	}

	@Test
	public void createSection() {
		Section sectionToBeCreated = new Section("ADDITIONALSECTION", new Subject("MATH11"),
				Schedule.valueOf("MTH 08:30-12:00"), new Room("MATH105", 10), new Faculty(1, "Optimus", "Prime"));

		dao.create(sectionToBeCreated);

		Section sectionCreated = dao.findBy("ADDITIONALSECTION");

		assertNotNull(sectionCreated);
		assertEquals(sectionToBeCreated, sectionCreated);
	}
	
	@Test (expected = SectionCreationException.class)
	public void createSectionThatExistsAlready() {
		
	    Section section = dao.findBy("HASSTUDENTS");
	    dao.create(section);
	    
	}
	
	@Test (expected = SectionCreationException.class)
	public void createSection_that_a_faculty_has_over_lap_with() {
		
	    Section section = new Section("S21", 
	      		 new Subject("OBJECTP"),
	               new Schedule(Days.MTH, LocalTime.of(8, 30), LocalTime.of(10, 00)), 
	               new Room("AVR1"),
	               new Faculty(1, "Optimus", "Prime"));
	    dao.create(section);
	    
	}
	
	@Test (expected = RoomScheduleConflictException.class)
	public void createNewSection_has_roomschedule_conflict_with_another_section() {
		
	    Section section = new Section("S21", 
	      		 new Subject("OBJECTP"),
	               new Schedule(Days.MTH, LocalTime.of(8, 30), LocalTime.of(10, 00)), 
	               new Room("AVR1"),
	               new Faculty(5, "Tom", "Cruz")); // Tom Cruz has no sections yet
	    
	    dao.create(section);
	    
	}
}

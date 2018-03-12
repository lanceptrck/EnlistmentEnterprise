package com.orangeandbronze.enlistment.domain;

import static org.apache.commons.lang3.Validate.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang3.*;

public class Section {

	public static final Section NONE = new Section("NONE", Subject.NONE, Schedule.TBA, Room.TBA);

	private final String sectionId;
	private Schedule schedule = Schedule.TBA;
	private final Subject subject;
	private Room room = Room.TBA;
	private Faculty faculty = Faculty.TBA;
	private final Collection<Student> students = new HashSet<>();
	private final Integer version; // version recorded in database

	public static final String SECTION_ID = "section_id";
	public static final String VERSION = "version";
	public static final String STUDENTS = "students";

	/**
	 * @param version
	 *            is the version recorded in the database
	 **/
	public Section(String sectionId, Subject subject, Schedule schedule, Room room, Faculty faculty,
			Collection<Student> enlistedStudents, Integer version) {
		isTrue(StringUtils.isAlphanumeric(sectionId), "sectionId should be alphanumeric, was %s", sectionId);
		notNull(schedule);
		notNull(subject);
		notNull(room);
		notNull(faculty);
		this.sectionId = sectionId;
		this.schedule = schedule;
		this.subject = subject;
		if (!Room.TBA.equals(room)) {
			this.room = room;
			room.addSection(this);
		}
		if (!Faculty.TBA.equals(faculty)) {
			this.faculty = faculty;
			faculty.addSection(this);
		}
		students.addAll(enlistedStudents);
		this.version = version;
	}

	public Section(String sectionId, Subject subject, Schedule schedule, Room room, Integer version) {
		this(sectionId, subject, schedule, room, Faculty.TBA, new ArrayList<>(0), version);
	}

	public Section(String sectionId, Subject subject, Schedule schedule, Room room) {
		this(sectionId, subject, schedule, room, Faculty.TBA, new ArrayList<>(0), 0);
	}

	public Section(String sectionId, Subject subject, Schedule schedule, Room room,
			Collection<Student> enlistedStudents) {
		this(sectionId, subject, schedule, room, Faculty.TBA, enlistedStudents, 0);
	}

	public Section(String sectionId, Subject subject, Schedule schedule, Room room, Faculty faculty) {
		this(sectionId, subject, schedule, room, faculty, new ArrayList<>(0), 0);
	}

	public Section(String sectionId, Subject subject, Schedule schedule, Room room, Faculty faculty, int version) {
		this(sectionId, subject, schedule, room, faculty, new ArrayList<>(0), version);
	}

	public Section(ResultSet rs) throws SQLException {
		this(rs.getString(Section.SECTION_ID), new Subject(rs.getString(Subject.SUBJECT_ID)),
				Schedule.valueOf(rs.getString(Schedule.SCHEDULE)),
				new Room(rs.getString(Room.ROOM_NAME), rs.getInt(Room.CAPACITY)),
				new Faculty(rs.getInt(Faculty.FACULTY_NUMBER), rs.getString(Faculty.FIRST_NAME),
						rs.getString(Faculty.LAST_NAME)),
				rs.getInt(Section.VERSION));
	}

	public Section(ResultSet rs, Collection<Student> students) throws SQLException {
		this(rs.getString(Section.SECTION_ID), new Subject(rs.getString(Subject.SUBJECT_ID)),
				Schedule.valueOf(rs.getString(Schedule.SCHEDULE)),
				new Room(rs.getString(Room.ROOM_NAME), rs.getInt(Room.CAPACITY)),
				new Faculty(rs.getInt(Faculty.FACULTY_NUMBER), rs.getString(Faculty.FIRST_NAME),
						rs.getString(Faculty.LAST_NAME)),
				students, rs.getInt(Section.VERSION));
	}

	void enlist(Student student) {
		student.checkIsEnlistedIn(this);
		validateSectionCanAccommodateEnlistment();
		students.add(student);
	}

	void cancel(Student student) {
		students.remove(student);
	}

	void validateConflict(Section otherSection) {
		this.schedule.notOverlappingWith(otherSection.schedule);
		if (subject.equals(otherSection.subject)) {
			throw new SameSubjectException("Section " + otherSection.sectionId + " with subject "
					+ subject.getSubjectId() + " has same subject as currently enlisted section " + this.sectionId);
		}
	}

	public void validateSectionCanAccommodateEnlistment() {
		if (students.size() >= room.getCapacity()) {
			throw new RoomCapacityReachedException(
					"Capacity Reached - enlistments: " + students.size() + " capacity: " + room.getCapacity());
		}
	}

	boolean hasScheduleOverlapWith(Section other) {
		return this.schedule.isOverlappingWith(other.schedule);
	}

	public Collection<Student> getStudents() {
		return new ArrayList<>(students);
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public Subject getSubject() {
		return subject;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Room getRoom() {
		return room;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public int getVersion() {
		return version;
	}

	// @Override
	// public String toString() {
	// return sectionId;
	// }

	@Override
	public String toString() {
		return "Section [sectionId=" + sectionId + ", schedule=" + schedule + ", subject=" + subject + ", room=" + room
				+ ", faculty=" + faculty + ", students=" + students + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((faculty == null) ? 0 : faculty.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
		result = prime * result + ((sectionId == null) ? 0 : sectionId.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Section other = (Section) obj;
		if (faculty == null) {
			if (other.faculty != null)
				return false;
		} else if (!faculty.equals(other.faculty))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (schedule == null) {
			if (other.schedule != null)
				return false;
		} else if (!schedule.equals(other.schedule))
			return false;
		if (sectionId == null) {
			if (other.sectionId != null)
				return false;
		} else if (!sectionId.equals(other.sectionId))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

}

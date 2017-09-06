package com.orangeandbronze.enlistment.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin {

	public static final Admin NONE = new Admin(0);

	private final Integer adminNumber;
	private String firstname = "";
	private String lastname = "";

	public Admin(int adminNumber) {
		this(adminNumber, "", "");
	}

	public Admin(int adminNumber2, String firstname, String lastname) {
		this.adminNumber = adminNumber2;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public Admin(ResultSet rs) throws SQLException {
		this.adminNumber = rs.getInt("id");
		this.firstname = rs.getString("firstname");
		this.lastname = rs.getString("lastname");
	}

	public Integer getAdminNumber() {
		return adminNumber;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adminNumber == null) ? 0 : adminNumber.hashCode());
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
		Admin other = (Admin) obj;
		if (adminNumber == null) {
			if (other.adminNumber != null)
				return false;
		} else if (!adminNumber.equals(other.adminNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Admin [adminNumber=" + adminNumber + ", firstname=" + firstname + ", lastname=" + lastname + "]";
	}

}

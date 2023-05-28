package model;

import java.util.Objects;

public class PassengerID {

	private String name;
	private String surname;
	
	public PassengerID(String name, String surname) {
		this.setName(name);
		this.setSurname(surname);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, surname);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassengerID other = (PassengerID) obj;
		return Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
	}
	
}

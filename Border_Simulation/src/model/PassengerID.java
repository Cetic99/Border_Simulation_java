package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;


public class PassengerID implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String surname;
	private boolean valid;
	private transient static Random rand = new Random();
	
	
	public PassengerID(String name, String surname) {
		this.setName(name);
		this.setSurname(surname);
		this.createValidity();
	}
	
	private void createValidity() {
		if(rand.nextDouble() <= 0.03) {
			this.valid= false;
		}
		else
			this.valid= true;
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
	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}
	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.surname;
	}
	
}

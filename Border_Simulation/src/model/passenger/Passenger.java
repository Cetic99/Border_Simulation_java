package model.passenger;

import java.io.Serializable;
import java.util.Objects;

import model.PassengerID;

public class Passenger implements Serializable {

	private static final long serialVersionUID = 1L;
	private PassengerID id = null;
	private static Integer passengerCount = 0;
	
	/*--------- constructor -----------*/
	public Passenger() {
		this("Passenger",passengerCount.toString());
		passengerCount++;
	}
	
	public Passenger(PassengerID id) {
		this.id=id;
	}

	public Passenger(String name, String surname) {
		this(new PassengerID(name, surname));
	}
	/*---------------------------------*/
	/**
	 * @return the id
	 */
	public PassengerID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(PassengerID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Passenger other = (Passenger) obj;
		return Objects.equals(id, other.id);
	}


}

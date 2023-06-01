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
	public String toString() {
		return id.toString();
	}
}

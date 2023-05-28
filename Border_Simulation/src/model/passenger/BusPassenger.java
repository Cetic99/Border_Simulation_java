package model.passenger;

import java.util.Objects;

import model.PassengerID;
import model.Suitcase;

public class BusPassenger extends Passenger{

	private Suitcase suitcase = null;
	public static Integer numBusPassengers = 0;
	/*------ constructors -----------------*/
	public BusPassenger() {
		super("BussPassenger",numBusPassengers.toString());
		numBusPassengers++;
	}
	
	public BusPassenger(PassengerID id) {
		super(id);
		// TODO Auto-generated constructor stub
		this.createSuitcase();
	}
	public BusPassenger(String name, String surname) {
		super(name, surname);
	}
	/*-------------------------------------*/
	
	
	/**
	 * @return the suitcase
	 */
	public Suitcase getSuitcase() {
		return suitcase;
	}

	/**
	 * @param suitcase the suitcase to set
	 */
	public void setSuitcase(Suitcase suitcase) {
		this.suitcase = suitcase;
	}

	/*---------- Helper Functions ----------*/
	private void createSuitcase() {
		if(Math.random()<= 0.7)
			this.suitcase = new Suitcase(this);
		else
			this.suitcase = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(suitcase);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusPassenger other = (BusPassenger) obj;
		return Objects.equals(suitcase, other.suitcase);
	}
	
}

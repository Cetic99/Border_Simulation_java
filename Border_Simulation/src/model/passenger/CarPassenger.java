package model.passenger;

import java.io.Serializable;

public class CarPassenger extends Passenger implements Serializable{

	private static final long serialVersionUID = 1L;
	public static Integer numCarPassengers = 0;
	
	public CarPassenger() {
		super("CarPassenger", numCarPassengers.toString());
		numCarPassengers++;
	}
	public CarPassenger(String name, String surname) {
		super(name, surname);
		// TODO Auto-generated constructor stub
	}

}

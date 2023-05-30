package model.passenger;

import model.PassengerID;

public class DriverPassenger extends Passenger{

	private static final long serialVersionUID = 1L;
	public static Integer numDrivers = 0;
	
	/*------ constructors -----------------*/
	public DriverPassenger() {
		super("Driver",numDrivers.toString());
		numDrivers++;
	}
	public DriverPassenger(PassengerID id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public DriverPassenger(String name, String surname) {
		super(name, surname);
	}
	/*-------------------------------------*/

}

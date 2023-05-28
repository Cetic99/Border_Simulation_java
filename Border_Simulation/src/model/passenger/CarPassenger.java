package model.passenger;

public class CarPassenger extends Passenger{

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

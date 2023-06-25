package model.passenger;

import java.io.Serializable;

import model.PassengerID;
import model.vehicle.BusVehicle;
import model.vehicle.PersonalVehicle;
import model.vehicle.Vehicle;

public class DriverPassenger extends Passenger implements Serializable{

	private static final long serialVersionUID = 1L;
	public static Integer numDrivers = 0;
	
	private transient Vehicle vehicle;
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
	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@Override
	public String toString() {
		String vehicleType;
		if(this.vehicle instanceof PersonalVehicle) {
			vehicleType = "CAR";
		}
		else if(this.vehicle instanceof BusVehicle) {
			vehicleType = "BUS";
		}
		else
			vehicleType = "TRUCK";
		return vehicleType + "::" + super.toString();
	}
}

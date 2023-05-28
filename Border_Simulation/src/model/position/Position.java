package model.position;

import model.vehicle.Vehicle;

public class Position {
	
	private boolean taken = false;
	private Vehicle vehicle;

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

	/**
	 * @return the taken
	 */
	public boolean isTaken() {
		return taken;
	}

	/**
	 * @param taken the taken to set
	 */
	public void setTaken(boolean taken) {
		this.taken = taken;
	}
}

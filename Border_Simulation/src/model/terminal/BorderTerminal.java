package model.terminal;

import model.vehicle.Vehicle;

public abstract class BorderTerminal {

	private Vehicle vehicle = null;

	/**
	 * @return the vehicle
	 */
	public synchronized Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	public synchronized void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}

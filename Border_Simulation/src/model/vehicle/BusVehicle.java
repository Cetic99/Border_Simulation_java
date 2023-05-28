package model.vehicle;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Stream;

import javafx.scene.image.Image;
import model.Suitcase;
import model.passenger.BusPassenger;
import model.passenger.Passenger;
import model.passenger.DriverPassenger;

public class BusVehicle extends Vehicle{

	private Set<Suitcase> cargo = null;
	
	{
		this.setCapacity(52);
	}
	
	/*---------- Constructors ------------------*/
	public BusVehicle(Set<BusPassenger> passengers) {
		super(passengers);
		// TODO Auto-generated constructor stub
		/**
		 * Putting passengers suitcase in cargo
		 */
		this.putSuitcasesInCargo();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src/view/images/bus.jpg"));
	}
	
	public BusVehicle() {
		/**
		 * Create passengers
		 */
		this.createPassengers();
		/**
		 * put passenger suitcases in cargo
		 */
		this.putSuitcasesInCargo();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src/view/images/bus.jpg"));
	}
	/*------------------------------------------*/

	/**
	 * @return the cargo
	 */
	public Set<Suitcase> getCargo() {
		return cargo;
	}

	/**
	 * @param cargo the cargo to set
	 */
	public void setCargo(Set<Suitcase> cargo) {
		this.cargo = cargo;
	}

	/*---------- Helper Functions -----------*/
	private void putSuitcasesInCargo() {
		Stream<? extends Passenger> stream = this.getPassengers().stream();
		this.cargo = new HashSet<>();
		stream.forEach(e ->this.cargo.add(((BusPassenger)e).getSuitcase()));
	}
}

package model.vehicle;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.image.Image;

import java.util.Random;

import model.passenger.BusPassenger;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import model.terminal.BorderTerminal;

public abstract class Vehicle {

	private Set<? extends Passenger> passengers;
	private int capacity = 0;
	private Image image = null;
	private BorderTerminal terminal = null;

	/*----------------- Constructors --------------------*/
	public Vehicle(Set<? extends Passenger> passengers) {
		this.passengers = passengers;
	}
	public Vehicle() {
		
	}
	/*---------------------------------------------------*/
	
	
	public void createPassengers() {
		/**
		 * Create passengers
		 */
		Set<Passenger> passengers = new HashSet<>();
		Random rand = new Random();
		int passengerCount = 1 + (int)rand.nextDouble()*(this.getCapacity() - 1);
		
		/**
		 * Create driver
		 */
		passengers.add(new DriverPassenger());
		if(this instanceof BusVehicle) {
			for(int i = 1; i< passengerCount; i++) {
				passengers.add(new BusPassenger()); 
			}
		}
		else {
			for(int i = 1; i< passengerCount; i++) {
				passengers.add(new Passenger()); 
			}
		}
		
		this.setPassengers(passengers);
	}
	
	

	
	
	
	/**
	 * GETTERS AND SETTERS 
	 */
	
	/**
	 * @return the passengers
	 */
	public Set<? extends Passenger> getPassengers() {
		return passengers;
	}

	/**
	 * @param passengers the passengers to set
	 */
	public void setPassengers(Set<? extends Passenger> passengers) {
		this.passengers = passengers;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	/**
	 * @return the terminal
	 */
	public BorderTerminal getTerminal() {
		return terminal;
	}
	/**
	 * @param terminal the terminal to set
	 */
	public void setTerminal(BorderTerminal terminal) {
		this.terminal = terminal;
	}
}

package model.vehicle;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.Random;

import model.passenger.BusPassenger;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import model.position.CarBusCustomsTerminal;
import model.position.Position;
import model.position.TruckCustomsTerminal;
import model.position.TruckPoliceTerminal;

public abstract class Vehicle extends Task<Position>{

	private Set<? extends Passenger> passengers;
	private int capacity = 0;
	private Image image = null;
	
	private List<Position> linePositions;
	private List<Position> carBusPoliceTerminals;
	private CarBusCustomsTerminal carBusCustomsTerminal;
	private TruckPoliceTerminal truckPoliceTerminal;
	private TruckCustomsTerminal truckCustomsTerminal;
	
	private Position currentPosition = null;

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
	
	/*
	 * Thread method
	 */
	@Override
	protected Position call() throws Exception {
		// TODO Auto-generated method stub
		while(this.linePositions.get(3).isTaken());
		this.setCurrentPosition(this.linePositions.get(3));
		this.currentPosition.takePosition(this);
		this.updateValue(this.currentPosition);
		return this.getCurrentPosition();
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
	 * @return the linePositions
	 */
	public List<Position> getLinePositions() {
		return linePositions;
	}
	/**
	 * @param linePositions the linePositions to set
	 */
	public void setLinePositions(List<Position> linePositions) {
		this.linePositions = linePositions;
	}
	/**
	 * @return the carBusPoliceTerminals
	 */
	public List<Position> getCarBusPoliceTerminals() {
		return carBusPoliceTerminals;
	}
	/**
	 * @param carBusPoliceTerminals the carBusPoliceTerminals to set
	 */
	public void setCarBusPoliceTerminals(List<Position> carBusPoliceTerminals) {
		this.carBusPoliceTerminals = carBusPoliceTerminals;
	}
	/**
	 * @return the carBusCustomsTerminal
	 */
	public CarBusCustomsTerminal getCarBusCustomsTerminal() {
		return carBusCustomsTerminal;
	}
	/**
	 * @param carBusCustomsTerminal the carBusCustomsTerminal to set
	 */
	public void setCarBusCustomsTerminal(CarBusCustomsTerminal carBusCustomsTerminal) {
		this.carBusCustomsTerminal = carBusCustomsTerminal;
	}
	/**
	 * @return the truckPoliceTerminal
	 */
	public TruckPoliceTerminal getTruckPoliceTerminal() {
		return truckPoliceTerminal;
	}
	/**
	 * @param truckPoliceTerminal the truckPoliceTerminal to set
	 */
	public void setTruckPoliceTerminal(TruckPoliceTerminal truckPoliceTerminal) {
		this.truckPoliceTerminal = truckPoliceTerminal;
	}
	/**
	 * @return the truckCustomsTerminal
	 */
	public TruckCustomsTerminal getTruckCustomsTerminal() {
		return truckCustomsTerminal;
	}
	/**
	 * @param truckCustomsTerminal the truckCustomsTerminal to set
	 */
	public void setTruckCustomsTerminal(TruckCustomsTerminal truckCustomsTerminal) {
		this.truckCustomsTerminal = truckCustomsTerminal;
	}
	/**
	 * @return the currentPosition
	 */
	public Position getCurrentPosition() {
		return currentPosition;
	}
	/**
	 * @param currentPosition the currentPosition to set
	 */
	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

}

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

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.*;
import java.util.function.Consumer;
import java.util.ArrayList;
public abstract class Vehicle extends Task<Position> {

	/*
	 * Stores passengers in Vehicle
	 */
	private List<? extends Passenger> passengers;
	/*
	 * Stores capacity of vehicle
	 */
	private int capacity = 0;
	/*
	 * Stores image that will be shown on GUI
	 */
	private Image image = null;

	/*
	 * All positions on Border
	 */
	private List<Position> linePositions;
	private List<Position> carBusPoliceTerminals;
	private CarBusCustomsTerminal carBusCustomsTerminal;
	private TruckPoliceTerminal truckPoliceTerminal;
	private TruckCustomsTerminal truckCustomsTerminal;

	/*
	 * Stores current position of Vehicle
	 */
	private Position currentPosition = null;
	private Position oldPosition = null;
	
	/*
	 * Used for locking positions(Line,Terminals)
	 */
	public Lock oldLock,newLock = null;


	/*
	 * Processing times at PoliceTerminal and CustomsTerminal
	 */
	private long ptTime,ctTime;
	
	/*
	 * Punishment rocessing consumer 
	 */
	private Consumer<Passenger> punishmentConsumer = null;
	


	/*----------------- Constructors --------------------*/
	public Vehicle(List<? extends Passenger> passengers) {
		this.passengers = passengers;
	}

	public Vehicle() {

	}
	
	/*---------------------------------------------------*/

	public void createPassengers() {
		/**
		 * Create passengers
		 */
		List<Passenger> passengers = new ArrayList<>();
		Random rand = new Random();
		int passengerCount = 1 + (int) (rand.nextDouble() * (this.getCapacity() - 1));
		/**
		 * Create driver
		 */
		DriverPassenger driver = new DriverPassenger();
		driver.setVehicle(this);
		passengers.add(driver);
		if (this instanceof BusVehicle) {
			for (int i = 1; i < passengerCount; i++) {
				passengers.add(new BusPassenger());
			}
		} else {
			for (int i = 1; i < passengerCount; i++) {
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
		
		lineMoving();
		
			
			// police terminal
//			oldLock = newLock;
//			newLock = this.truckPoliceTerminal.getLock();
//			newLock.lock();
//			moveForward(this.truckPoliceTerminal);
//			this.updateValue(this.currentPosition);
//			oldLock.unlock();
//			Thread.sleep(ptTime*this.passengers.size());
//			for(Passenger p : passengers) {
//				if(p.getId().isValid() == false) {
//					punishPassenger(p);
//					passengers.remove(p);
//					if(p instanceof DriverPassenger) {
//						this.currentPosition.releasePosition();
//						this.updateValue(this.currentPosition);
//						newLock.unlock();
//						return this.currentPosition;
//					}
//				}
//			}
			if(policeTerminalProcess() == 0) {
				if(customsTerminalProcess()== 0) {
					// successfully crossed the border
				}
				else {
					// failed on customs terminal
				}
			}
			else {
				// failed on police terminal
			}
			moveForward(null);
//			// customs terminal
//			oldLock = newLock;
//			newLock = this.truckCustomsTerminal.getLock();
//			newLock.lock();
//			moveForward(this.truckCustomsTerminal);
//			this.updateValue(this.currentPosition);
//			oldLock.unlock();
//			Thread.sleep(ctTime*this.passengers.size());
//			customsTerminalProcess();
//			Thread.sleep(10000);


		return this.getCurrentPosition();
	}
	
	public void punishPassenger(Passenger p) {
		punishmentConsumer.accept(p);
	}
	
	public abstract int policeTerminalProcess();
	
	public abstract int customsTerminalProcess();
	
	public void moveForward(Position nextPos) {
		// take next
		if(nextPos != null) {
			this.oldPosition = this.currentPosition;
			nextPos.takePosition(this);
			// release current
			if(this.currentPosition != null) {
				this.currentPosition.releasePosition();
				this.oldPosition.updateImage();
				this.updateValue(this.currentPosition);
				
			}
			// update current
			this.setCurrentPosition(nextPos);
			this.updateValue(this.currentPosition);
		}
		else if(this.currentPosition != null){
			this.currentPosition.releasePosition();
			this.currentPosition.updateImage();
			this.updateValue(this.currentPosition);
		}
		
	}
	public void lineMoving() {

		Position nextPosition = null;
		int i = 0;
		while (i < 5) {
			oldLock = newLock;
			nextPosition = this.linePositions.get(i);
			newLock = nextPosition.getLock();
			// lock position
			newLock.lock();
			moveForward(nextPosition);
			if(oldLock != null)
				oldLock.unlock();

			this.updateValue(this.currentPosition);
			i++;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * GETTERS AND SETTERS
	 */

	/**
	 * @return the passengers
	 */
	public Lock getOldLock() {
		return oldLock;
	}

	public void setOldLock(Lock oldLock) {
		this.oldLock = oldLock;
	}

	public Lock getNewLock() {
		return newLock;
	}

	public void setNewLock(Lock newLock) {
		this.newLock = newLock;
	}
	public List<? extends Passenger> getPassengers() {
		return passengers;
	}

	/**
	 * @param passengers the passengers to set
	 */
	public void setPassengers(List<? extends Passenger> passengers) {
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

	/**
	 * @return the ctTime
	 */
	public long getCtTime() {
		return ctTime;
	}

	/**
	 * @param ctTime the ctTime to set
	 */
	public void setCtTime(long ctTime) {
		this.ctTime = ctTime;
	}

	/**
	 * @return the ptTime
	 */
	public long getPtTime() {
		return ptTime;
	}

	/**
	 * @param ptTime the ptTime to set
	 */
	public void setPtTime(long ptTime) {
		this.ptTime = ptTime;
	}

	/**
	 * @return the punishmentConsumer
	 */
	public Consumer<Passenger> getPunishmentConsumer() {
		return punishmentConsumer;
	}

	/**
	 * @param punishmentConsumer the punishmentConsumer to set
	 */
	public void setPunishmentConsumer(Consumer<Passenger> punishmentConsumer) {
		this.punishmentConsumer = punishmentConsumer;
	}
	
	@Override 
	public String toString() {
		String retString;
		String driverName = "";
		String otherPassengers = "";
		for(Passenger p : this.passengers) {
			if(p instanceof DriverPassenger) {
				driverName = p.toString();
			}else {
				otherPassengers = otherPassengers + ", " + p.toString();
			}
		}
		retString = "Drivers name: " + driverName + 
				", Passengers names: " + otherPassengers;
		
		return retString;
	}

}

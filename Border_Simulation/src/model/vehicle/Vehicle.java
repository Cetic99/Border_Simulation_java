package model.vehicle;

import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.Random;

import model.passenger.BusPassenger;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import model.position.CarBusCustomsTerminal;
import model.position.CarBusPoliceTerminal;
import model.position.LinePosition;
import model.position.Position;
import model.position.TruckCustomsTerminal;
import model.position.TruckPoliceTerminal;
import view.MainController;

import java.util.concurrent.locks.*;
import java.util.function.Consumer;
import java.util.ArrayList;

public abstract class Vehicle extends Task<Position> {

	public static boolean RUN = true;
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
	private Image didntPassImage = null;
	private Image passedImage = null;
	private Image incidentImage = null;
	

	/*
	 * All positions on Border
	 */
	private List<LinePosition> linePositions;
	private List<CarBusPoliceTerminal> carBusPoliceTerminals;
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
	 * Punishment processing consumer 
	 */
	private Consumer<Passenger> punishmentConsumer = null;
	
	private boolean crossedBorder = false;
	private boolean havingIncident = false;
	private boolean moving = true;
	private List<String> incidentStatus = new ArrayList<String>();
	


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
		
			if(policeTerminalProcess() == 0) {
				if(customsTerminalProcess()== 0) {
					// successfully crossed the border
					this.setCrossedBorder(true);
					this.setMoving(false);
				}
				else {
					// failed on customs terminal
					this.setCrossedBorder(false);
					this.setMoving(false);
				}
			}
			else {
				// failed on police terminal
				this.setCrossedBorder(false);
				this.setMoving(false);
			}
			
			moveForward(null);
			this.oldLock.unlock();
			this.newLock.unlock();
			
			this.setMoving(false);

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
				//this.updateValue(this.oldPosition);
				
			}
			// update current
			this.setCurrentPosition(nextPos);
			this.currentPosition.updateImage();
			//this.updateValue(this.currentPosition);
		}
		else if(this.currentPosition != null){
			this.currentPosition.releasePosition();
			this.currentPosition.updateImage();
			//this.updateValue(this.currentPosition);
		}
		
	}
	public void lineMoving() {

		Position nextPosition = null;
		int i = 0;
		while (i < 5) {
			if(RUN == false)
			{
				this.setImage(null);
				this.currentPosition.updateImage();
				return;
			}
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
	public List<LinePosition> getLinePositions() {
		return linePositions;
	}

	/**
	 * @param linePositions the linePositions to set
	 */
	public void setLinePositions(List<LinePosition> linePositions) {
		this.linePositions = linePositions;
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

	/**
	 * @return the carBusPoliceTerminals
	 */
	public List<CarBusPoliceTerminal> getCarBusPoliceTerminals() {
		return carBusPoliceTerminals;
	}

	/**
	 * @param carBusPoliceTerminals the carBusPoliceTerminals to set
	 */
	public void setCarBusPoliceTerminals(List<CarBusPoliceTerminal> carBusPoliceTerminals) {
		this.carBusPoliceTerminals = carBusPoliceTerminals;
	}

	/**
	 * @return the didntPassImage
	 */
	public Image getDidntPassImage() {
		return didntPassImage;
	}

	/**
	 * @param didntPassImage the didntPassImage to set
	 */
	public void setDidntPassImage(Image didntPassImage) {
		this.didntPassImage = didntPassImage;
	}

	/**
	 * @return the passedImage
	 */
	public Image getPassedImage() {
		return passedImage;
	}

	/**
	 * @param passedImage the passedImage to set
	 */
	public void setPassedImage(Image passedImage) {
		this.passedImage = passedImage;
	}



	/**
	 * @return the havingIncident
	 */
	public boolean isHavingIncident() {
		return havingIncident;
	}

	/**
	 * @param havingIncident the havingIncident to set
	 */
	public void setHavingIncident(boolean havingIncident) {
		this.havingIncident = havingIncident;
	}

	/**
	 * @return the incidentImage
	 */
	public Image getIncidentImage() {
		return incidentImage;
	}

	/**
	 * @param incidentImage the incidentImage to set
	 */
	public void setIncidentImage(Image incidentImage) {
		this.incidentImage = incidentImage;
	}

	/**
	 * @return the moving
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * @param moving the moving to set
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * @return the incidentStatus
	 */
	public List<String> getIncidentStatus() {
		return incidentStatus;
	}

	/**
	 * @param incidentStatus the incidentStatus to set
	 */
	public void setIncidentStatus(List<String> incidentStatus) {
		this.incidentStatus = incidentStatus;
	}

	/**
	 * @return the crossedBorder
	 */
	public boolean isCrossedBorder() {
		return crossedBorder;
	}

	/**
	 * @param crossedBorder the crossedBorder to set
	 */
	public void setCrossedBorder(boolean crossedBorder) {
		this.crossedBorder = crossedBorder;
	}

}

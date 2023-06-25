package model.vehicle;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import model.position.CarBusPoliceTerminal;
import model.position.Position;

public class PersonalVehicle extends Vehicle implements Serializable {

	private static String IMAGE_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"car.png";
	private static String IMAGE_DIDNT_PASS_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"car_didnt_pass.png";
	private static String IMAGE_PASSED_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"car_passed.png";
	private static String IMAGE_INCIDENT_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"car_had_incident.png";

	private static final long serialVersionUID = 1L;

	{
		this.setCapacity(5);
	}
	
	/*--------------- Constructors ---------------------*/
	public PersonalVehicle(List<Passenger> passengers) {
		super(passengers);
		// TODO Auto-generated constructor stub
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image(IMAGE_PATH));
		this.setPassedImage(new Image(IMAGE_PASSED_PATH));
		this.setDidntPassImage(new Image(IMAGE_DIDNT_PASS_PATH));
		this.setIncidentImage(new Image(IMAGE_INCIDENT_PATH));
		
		this.setCtTime(500);
		this.setPtTime(500);
	}
	public PersonalVehicle() {
		this.createPassengers();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image(IMAGE_PATH));
		this.setPassedImage(new Image(IMAGE_PASSED_PATH));
		this.setDidntPassImage(new Image(IMAGE_DIDNT_PASS_PATH));
		this.setIncidentImage(new Image(IMAGE_INCIDENT_PATH));
		
		this.setCtTime(100);
		this.setPtTime(100);
	}
	/*--------------------------------------------------*/
	@Override
	public int policeTerminalProcess() {
		// TODO Auto-generated method stub
		// police terminal
		boolean locked = false;
		List<CarBusPoliceTerminal> terminals = this.getCarBusPoliceTerminals();
		
		this.oldLock = this.newLock;
		
		while(locked == false) {
			for(CarBusPoliceTerminal p : terminals) {
				if(p.isWorking()) {
					locked = p.getLock().tryLock();
					this.newLock = p.getLock();
					if(locked == true) {
						moveForward(p);
						break;
					}
				}
				
			}
		}
		this.getCurrentPosition().updateImage();
//		this.updateValue(this.getCurrentPosition());
		this.oldLock.unlock();
		if(Vehicle.RUN == false) {
			this.newLock.unlock();
			this.setImage(null);
			this.getCurrentPosition().updateImage();
			return -1;
		}
		try {
			Thread.sleep(this.getPtTime() * this.getPassengers().size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Passenger> toBeRemoved = new ArrayList<>();
		for (Passenger p : this.getPassengers()) {
			if (p.getId().isValid() == false) {
				// punish person
				punishPassenger(p);
				toBeRemoved.add(p);
				
				if (p instanceof DriverPassenger) {
					this.getIncidentStatus().add("Driver "+ p.toString()+ " didn't have valid ID and bus couldn't cross border");
					this.setHavingIncident(true);
					moveForward(null);
					this.getPassengers().removeAll(toBeRemoved);
					this.newLock.unlock();
					return -1;
				}
				else {
					this.getIncidentStatus().add("Passenger "+ p.toString()+ " didn't have valid ID");
					this.setHavingIncident(true);
				}
			}
		}
		this.getPassengers().removeAll(toBeRemoved);
		return 0;

	}

	@Override
	public int customsTerminalProcess() {
		// TODO Auto-generated method stub
		this.oldLock = this.newLock;
		this.newLock = this.getCarBusCustomsTerminal().getLock();
		while(!this.getCarBusCustomsTerminal().isWorking());
		this.newLock.lock();
		moveForward(this.getCarBusCustomsTerminal());
		this.getCurrentPosition().updateImage();
		//this.updateValue(this.getCurrentPosition());
		this.oldLock.unlock();
		if(Vehicle.RUN == false) {
			this.newLock.unlock();
			this.setImage(null);
			this.getCurrentPosition().updateImage();
			return -1;
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.newLock.unlock();
		
		return 0;
		
	}

	@Override
	public String toString() {
		return "CAR:: " +super.toString();
	}
	
}

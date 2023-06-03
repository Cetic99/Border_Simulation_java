package model.vehicle;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import model.position.CarBusPoliceTerminal;
import model.position.Position;

public class PersonalVehicle extends Vehicle implements Serializable {

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
		this.setImage(new Image("file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"car.png"));
		
		this.setCtTime(500);
		this.setPtTime(500);
	}
	public PersonalVehicle() {
		this.createPassengers();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"car.png"));
		
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
		try {
			Thread.sleep(this.getPtTime() * this.getPassengers().size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Passenger p : this.getPassengers()) {
			if (p.getId().isValid() == false) {
				// punish person
				punishPassenger(p);
				this.getPassengers().remove(p);
				
				if (p instanceof DriverPassenger) {
					moveForward(null);
					this.newLock.unlock();
					return -1;
				}
			}
		}
		
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
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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

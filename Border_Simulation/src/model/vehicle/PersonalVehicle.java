package model.vehicle;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
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
		this.setImage(new Image("file:src/view/images/car.png"));
		
		this.setCtTime(500);
		this.setPtTime(500);
	}
	public PersonalVehicle() {
		this.createPassengers();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src/view/images/car.png"));
		
		this.setCtTime(100);
		this.setPtTime(100);
	}
	/*--------------------------------------------------*/
	@Override
	public int policeTerminalProcess() {
		// TODO Auto-generated method stub
		// police terminal
		boolean locked = false;
		List<Position> terminals = this.getCarBusPoliceTerminals();
		
		this.oldLock = this.newLock;
		
		while(locked == false) {
			for(Position p : terminals) {
				locked = p.getLock().tryLock();
				this.newLock = p.getLock();
				if(locked == true) {
					moveForward(p);
					break;
				}
			}
		}
		this.updateValue(this.getCurrentPosition());
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
//		this.oldLock = this.newLock;
//		this.newLock = this.getCarBusCustomsTerminal().getLock();
//		this.newLock.lock();
//		moveForward(this.getCarBusCustomsTerminal());
//		if(oldLock != null)
//			this.oldLock.unlock();
//		this.updateValue(this.getCurrentPosition());
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		this.newLock.unlock();
		
		return 0;

	}

	@Override
	public int customsTerminalProcess() {
		// TODO Auto-generated method stub
		this.oldLock = this.newLock;
		this.newLock = this.getCarBusCustomsTerminal().getLock();
		this.newLock.lock();
		moveForward(this.getCarBusCustomsTerminal());
		this.updateValue(this.getCurrentPosition());
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

package model.vehicle;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import javafx.scene.image.Image;
import model.Suitcase;
import model.passenger.BusPassenger;
import model.passenger.Passenger;
import model.position.CarBusPoliceTerminal;
import model.position.Position;
import model.passenger.DriverPassenger;

import java.io.File;
import java.util.ArrayList;

public class BusVehicle extends Vehicle{

	private Set<Suitcase> cargo = null;
	
	{
		this.setCapacity(52);
	}
	
	/*---------- Constructors ------------------*/
	public BusVehicle(List<BusPassenger> passengers) {
		super(passengers);
		// TODO Auto-generated constructor stub
		/**
		 * Putting passengers suitcase in cargo
		 */
		this.putSuitcasesInCargo();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"bus.jpg"));
		
		this.setCtTime(100);
		this.setPtTime(100);
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
		this.setImage(new Image("file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"bus.jpg"));
		
		this.setCtTime(100);
		this.setPtTime(100);
	}
	/*------------------------------------------*/
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
				if(p instanceof BusPassenger) {
					this.cargo.remove(((BusPassenger)p).getSuitcase());
				}
				else if (p instanceof DriverPassenger) {
					moveForward(null);
					this.newLock.unlock();
					return -1;
				}
			}
		}
		this.getPassengers().removeAll(toBeRemoved);
		return 0;

	}

	@Override
	public int customsTerminalProcess() {
		this.oldLock = this.newLock;
		this.newLock = this.getCarBusCustomsTerminal().getLock();
		while(!this.getCarBusCustomsTerminal().isWorking());
		this.newLock.lock();
		moveForward(this.getCarBusCustomsTerminal());
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
			Thread.sleep(this.getCtTime()*this.getPassengers().size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.newLock.unlock();
		
		return 0;
		
	}

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
		stream.forEach(e ->{
			if(e instanceof BusPassenger) {
				this.cargo.add(((BusPassenger)e).getSuitcase());
			}
		});
	}
	
	@Override
	public String toString() {
		return "BUS:: " +super.toString();
	}


	
}

package model.vehicle;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import javafx.scene.image.Image;
import model.Suitcase;
import model.passenger.BusPassenger;
import model.passenger.Passenger;
import model.position.Position;
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
		this.setImage(new Image("file:src/view/images/bus.jpg"));
		
		this.setCtTime(100);
		this.setPtTime(100);
	}
	/*------------------------------------------*/
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
		return 0;

	}

	@Override
	public int customsTerminalProcess() {
		// TODO Auto-generated method stub
		this.oldLock = this.newLock;
		this.newLock = this.getCarBusCustomsTerminal().getLock();
		this.newLock.lock();
		this.oldLock.unlock();
		moveForward(this.getCarBusCustomsTerminal());
		this.updateValue(this.getCurrentPosition());
		try {
			Thread.sleep(this.getCtTime()*this.getPassengers().size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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


	
}

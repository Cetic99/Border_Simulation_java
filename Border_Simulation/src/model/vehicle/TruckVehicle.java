package model.vehicle;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javafx.scene.image.Image;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;

public class TruckVehicle extends Vehicle implements Serializable{

	private static final long serialVersionUID = 1L;
	private double declaredWeight = 0;
	private double realWeight = 0;
	private boolean neededCustoms = false;
	
	private Consumer<TruckVehicle> customsDoc;
	
	{
		this.setCapacity(3);
	}
	
	public TruckVehicle(List<Passenger> passengers) {
		super(passengers);
		// TODO Auto-generated constructor stub
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"truck.png"));
		
		this.setCtTime(500);
		this.setPtTime(500);
	}
	
	public TruckVehicle() {
		/**
		 * Creating passengers
		 */
		this.createPassengers();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"truck.png"));
		
		this.setCtTime(500);
		this.setPtTime(500);
		
		this.calculateDeclaredMass();
		this.calculateRealMass();
		this.calculateNeededCustoms();
	}
	
	@Override
	public int policeTerminalProcess() {
		// police terminal
		this.oldLock = this.getNewLock();
		this.newLock = this.getTruckPoliceTerminal().getLock();
		while(!this.getTruckPoliceTerminal().isWorking());
		this.newLock.lock();
		moveForward(this.getTruckPoliceTerminal());
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
			Thread.sleep(this.getPtTime() * this.getPassengers().size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Passenger p : this.getPassengers()) {
			if (p.getId().isValid() == false) {
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
		this.oldLock = this.newLock;
		this.newLock = this.getTruckCustomsTerminal().getLock();
		while(!this.getTruckCustomsTerminal().isWorking());
		this.newLock.lock();
		moveForward(this.getTruckCustomsTerminal());
		this.updateValue(this.getCurrentPosition());
		this.oldLock.unlock();
		if(Vehicle.RUN == false) {
			this.newLock.unlock();
			this.setImage(null);
			this.getCurrentPosition().updateImage();
			return -1;
		}

		try {
			Thread.sleep(this.getCtTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(this.isNeededCustoms()) {
			if(createCustomsDocumentation() == -1)
				return -1;
		}
		
		this.newLock.unlock();
		return 0;
		
	}
	
	private int createCustomsDocumentation() {
		if(this.getRealWeight() > this.getDeclaredWeight()) {
			this.customsDoc.accept(this);
			System.out.println("Overweight");
			moveForward(null);
			this.newLock.unlock();
			return -1;
		}
		return 0;
	}
	/**
	 * @return the declaredWeight
	 */
	public double getDeclaredWeight() {
		return declaredWeight;
	}
	/**
	 * @param declaredWeight the declaredWeight to set
	 */
	public void setDeclaredWeight(double declaredWeight) {
		this.declaredWeight = declaredWeight;
	}
	/**
	 * @return the realWeight
	 */
	public double getRealWeight() {
		return realWeight;
	}
	/**
	 * @param realWeight the realWeight to set
	 */
	public void setRealWeight(double realWeight) {
		this.realWeight = realWeight;
	}
	/**
	 * @return the neededCustoms
	 */
	public boolean isNeededCustoms() {
		return neededCustoms;
	}
	/**
	 * @param neededCustoms the neededCustoms to set
	 */
	public void setNeededCustoms(boolean neededCustoms) {
		this.neededCustoms = neededCustoms;
	}
	
	/*------------ Helper functions -------------*/
	private void calculateNeededCustoms() {
		if(Math.random()>0.5)
			this.neededCustoms = true;
		else
			this.neededCustoms = false;
	}
	
	private void calculateDeclaredMass() {
		this.declaredWeight = Math.random()*10000;
	}
	
	private void calculateRealMass() {
		if (Math.random() <= 0.2) {
            double percentageIncrease = Math.random() * 30;
            double adjustedWeight = declaredWeight * (1 + percentageIncrease / 100);
            realWeight = adjustedWeight;
        } else {
            realWeight = declaredWeight * Math.random();
        }
	}

	/**
	 * @return the customsDoc
	 */
	public Consumer<TruckVehicle> getCustomsDoc() {
		return customsDoc;
	}

	/**
	 * @param customsDoc the customsDoc to set
	 */
	public void setCustomsDoc(Consumer<TruckVehicle> customsDoc) {
		this.customsDoc = customsDoc;
	}


	@Override
	public String toString() {
		String retString;
		String passengers = super.toString();
		String declaredWeight;
		String realWeight;
		
		
		declaredWeight = Double.toString(getDeclaredWeight());
		realWeight = Double.toString(getRealWeight());
		
		retString = "TRUCK:: " +passengers + 
				"DeclaredWeight: " + declaredWeight + 
				",RealWeight: "+ realWeight;
		
		return retString;
	}


}

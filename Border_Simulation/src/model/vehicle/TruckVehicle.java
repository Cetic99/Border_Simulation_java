package model.vehicle;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import view.Main;

public class TruckVehicle extends Vehicle implements Serializable{
	
	private static String IMAGE_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"truck.png";
	private static String IMAGE_DIDNT_PASS_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"truck_didnt_pass.png";
	private static String IMAGE_PASSED_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"truck_passed.png";
	private static String IMAGE_INCIDENT_PATH = "file:src"+File.separator+"view"+File.separator+"images"+ File.separator+"truck_had_incident.png";
	

	private static final long serialVersionUID = 1L;
	private double declaredWeight = 0;
	private double realWeight = 0;
	private boolean neededCustoms = false;
	
	private Consumer<TruckVehicle> customsDoc;
	
	private static Logger log;
	
	static{
		try {
			log = Logger.getLogger(Main.class.getName());
			log.addHandler(new FileHandler("Truck_log/TruckVehicle.log"));
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	{
		this.setCapacity(3);
	}
	
	public TruckVehicle(List<Passenger> passengers) {
		super(passengers);
		
		
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
	
	public TruckVehicle() {
		/**
		 * Creating passengers
		 */
		this.createPassengers();
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image(IMAGE_PATH));
		this.setPassedImage(new Image(IMAGE_PASSED_PATH));
		this.setDidntPassImage(new Image(IMAGE_DIDNT_PASS_PATH));
		this.setIncidentImage(new Image(IMAGE_INCIDENT_PATH));
		
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
			log.log(Level.WARNING,e.fillInStackTrace().toString());
		}
		
		List<Passenger> toBeRemoved = new ArrayList<>();
		for (Passenger p : this.getPassengers()) {
			if (p.getId().isValid() == false) {
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
			log.log(Level.WARNING,e.fillInStackTrace().toString());
		}
		if(this.isNeededCustoms()) {
			if(createCustomsDocumentation() == -1) {
				this.getIncidentStatus().add("Truck is overweight and couldn't cross border");
				this.setHavingIncident(true);
				return -1;
			}
				
		}
		
		this.newLock.unlock();
		return 0;
		
	}
	
	private int createCustomsDocumentation() {
		if(this.getRealWeight() > this.getDeclaredWeight()) {
			this.customsDoc.accept(this);
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
		
		retString = "Vehicle Type: TRUCK"+ System.lineSeparator()
		+ passengers
		+ System.lineSeparator()
		+ "Declared Weight: " + declaredWeight
		+ System.lineSeparator() 
		+ "Real Weight:     " + realWeight;
		
		return retString;
	}

	@Override
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
		
			for (int i = 1; i < passengerCount; i++) {
				passengers.add(new Passenger());
			}
		this.setPassengers(passengers);
		
	}


}

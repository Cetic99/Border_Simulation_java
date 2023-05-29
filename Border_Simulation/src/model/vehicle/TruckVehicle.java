package model.vehicle;

import java.util.Set;

import javafx.scene.image.Image;
import model.passenger.Passenger;

public class TruckVehicle extends Vehicle {

	private double declaredWeight = 0;
	private double realWeight = 0;
	private boolean neededCustoms = false;
	
	{
		this.setCapacity(3);
	}
	
	public TruckVehicle(Set<Passenger> passengers) {
		super(passengers);
		// TODO Auto-generated constructor stub
		/**
		 * Setting image for Bus
		 */
		this.setImage(new Image("file:src/view/images/truck.png"));
		
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
		this.setImage(new Image("file:src/view/images/truck.png"));
		
		this.setCtTime(500);
		this.setPtTime(500);
	}
	
	@Override
	public void policeTerminalProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customsTerminalProcess() {
		// TODO Auto-generated method stub
		if(this.getDeclaredWeight() < this.getRealWeight()) {
			System.out.println("Overweight");
		}
		
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
	
	private void calulcateRealMass() {
		if (Math.random() <= 0.2) {
            double percentageIncrease = Math.random() * 30;
            double adjustedWeight = declaredWeight * (1 + percentageIncrease / 100);
            realWeight = adjustedWeight;
        } else {
            realWeight = declaredWeight * Math.random();
        }
	}



}

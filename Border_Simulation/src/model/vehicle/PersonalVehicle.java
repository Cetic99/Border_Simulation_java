package model.vehicle;

import java.util.Set;

import javafx.scene.image.Image;
import model.passenger.Passenger;

public class PersonalVehicle extends Vehicle {

	{
		this.setCapacity(5);
	}
	
	/*--------------- Constructors ---------------------*/
	public PersonalVehicle(Set<Passenger> passengers) {
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
	public void policeTerminalProcess() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void customsTerminalProcess() {
		// TODO Auto-generated method stub
		
	}


	
}

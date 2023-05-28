package model.border;

import javafx.concurrent.Task;
import model.vehicle.BusVehicle;
import model.vehicle.PersonalVehicle;
import model.vehicle.TruckVehicle;
import model.vehicle.Vehicle;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Border extends Task<Vehicle>{
	
	private int numBusses = 5;
	private int numTrucks = 10;
	private int numPersonalVehicles = 35;
	
	private Queue<Vehicle> vehicles = null;
	
	public Border() {
		
		this.createVehicles();

	}
	
	private void createVehicles() {
		
		/**
		 * Creating list of vehicles
		 */
		this.vehicles = new LinkedList<>();
		
		
		List<Vehicle> tmpVehicle = new ArrayList<>();
		
		/**
		 * Creating 50 vehicles of different Types. 5 busses, 10 trucks, 35 personal vehicles
		 */
		for(int i = 0; i < numBusses; i++) {
			tmpVehicle.add(new BusVehicle());
		}
		for(int i = 0; i < numTrucks; i++) {
			tmpVehicle.add(new TruckVehicle());
		}
		for(int i = 0; i < numPersonalVehicles; i++) {
			tmpVehicle.add(new PersonalVehicle());
		}
		
		Random rand = new Random();
		int index = 0;
		while(!tmpVehicle.isEmpty()) {
			// generating index
			index = rand.nextInt(tmpVehicle.size());
			// getting element at that index
			Vehicle v = tmpVehicle.get(index);
			// adding element to list
			this.vehicles.add(v);
			// removing elemet from list
			tmpVehicle.remove(index);
		}
	}

	@Override
	protected Vehicle call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

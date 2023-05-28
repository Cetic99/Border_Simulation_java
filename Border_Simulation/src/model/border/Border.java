package model.border;

import javafx.concurrent.Task;
import model.position.*;
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
	
	private List<Position> linePositions = new ArrayList<>();
	private List<Position> carBusPoliceTerminals = new ArrayList<>();
	private CarBusCustomsTerminal carBusCustomsTerminal = new CarBusCustomsTerminal();
	private TruckPoliceTerminal truckPoliceTerminal = new TruckPoliceTerminal();
	private TruckCustomsTerminal truckCustomsTerminal = new TruckCustomsTerminal();
	
	/*------------ Constructors --------------------*/
	public Border() {
		/*
		 * Creating positions
		 */
		this.createPositions();
		/*
		 * Creating vehicles
		 */
		this.createVehicles();
		
		this.notifyVehiclesAboutPositions();

	}
	/*----------------------------------------------*/
	
	private void notifyVehiclesAboutPositions() {
		vehicles.stream().forEach(e -> {
			e.setCarBusCustomsTerminal(carBusCustomsTerminal);
			e.setCarBusPoliceTerminals(carBusPoliceTerminals);
			e.setLinePositions(linePositions);
			e.setTruckCustomsTerminal(truckCustomsTerminal);
			e.setTruckPoliceTerminal(truckPoliceTerminal);
		});
	}
	
	private void createPositions() {
		for(int i = 0;i<5; i++) {
			linePositions.add(new LinePosition());
		}
		for(int i = 0;i<2; i++) {
			carBusPoliceTerminals.add(new CarBusPoliceTerminal());
		}
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

	/**
	 * @return the linePositions
	 */
	public List<Position> getLinePositions() {
		return linePositions;
	}

	/**
	 * @param linePositions the linePositions to set
	 */
	public void setLinePositions(List<Position> linePositions) {
		this.linePositions = linePositions;
	}

	/**
	 * @return the carBusPoliceTerminals
	 */
	public List<Position> getCarBusPoliceTerminals() {
		return carBusPoliceTerminals;
	}

	/**
	 * @param carBusPoliceTerminals the carBusPoliceTerminals to set
	 */
	public void setCarBusPoliceTerminals(List<Position> carBusPoliceTerminals) {
		this.carBusPoliceTerminals = carBusPoliceTerminals;
	}

	/**
	 * @return the carBusCustomsTerminal
	 */
	public CarBusCustomsTerminal getCarBusCustomsTerminal() {
		return carBusCustomsTerminal;
	}

	/**
	 * @param carBusCustomsTerminal the carBusCustomsTerminal to set
	 */
	public void setCarBusCustomsTerminal(CarBusCustomsTerminal carBusCustomsTerminal) {
		this.carBusCustomsTerminal = carBusCustomsTerminal;
	}

	/**
	 * @return the truckPoliceTerminal
	 */
	public TruckPoliceTerminal getTruckPoliceTerminal() {
		return truckPoliceTerminal;
	}

	/**
	 * @param truckPoliceTerminal the truckPoliceTerminal to set
	 */
	public void setTruckPoliceTerminal(TruckPoliceTerminal truckPoliceTerminal) {
		this.truckPoliceTerminal = truckPoliceTerminal;
	}

	/**
	 * @return the truckCustomsTerminal
	 */
	public TruckCustomsTerminal getTruckCustomsTerminal() {
		return truckCustomsTerminal;
	}

	/**
	 * @param truckCustomsTerminal the truckCustomsTerminal to set
	 */
	public void setTruckCustomsTerminal(TruckCustomsTerminal truckCustomsTerminal) {
		this.truckCustomsTerminal = truckCustomsTerminal;
	}



}

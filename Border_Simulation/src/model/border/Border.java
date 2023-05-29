package model.border;

import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
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

public class Border extends Task<Position>{
	
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
	private Border() {
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
	public Border(ImageView line1,
			ImageView line2,
			ImageView line3,
			ImageView line4,
			ImageView line5,
			ImageView carBusPT1,
			ImageView carBusPT2,
			ImageView carBusCT,
			ImageView truckPT,
			ImageView truckCT) {
		this();
		this.associateImageViewToPosition(line1, line2, line3, line4, line5, carBusPT1, carBusPT2, carBusCT, truckPT, truckCT);
		

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
	
	private void associateImageViewToPosition(ImageView line1,
			ImageView line2,
			ImageView line3,
			ImageView line4,
			ImageView line5,
			ImageView carBusPT1,
			ImageView carBusPT2,
			ImageView carBusCT,
			ImageView truckPT,
			ImageView truckCT) {
		this.linePositions.get(0).setImView(line1);
		this.linePositions.get(1).setImView(line2);
		this.linePositions.get(2).setImView(line3);
		this.linePositions.get(3).setImView(line4);
		this.linePositions.get(4).setImView(line5);
		
		this.carBusPoliceTerminals.get(0).setImView(carBusPT1);
		this.carBusPoliceTerminals.get(1).setImView(carBusPT2);
		
		this.carBusCustomsTerminal.setImView(carBusCT);
		this.truckPoliceTerminal.setImView(truckPT);
		this.truckCustomsTerminal.setImView(truckCT);
		
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

	//===========================================
	/*
	 * FUNCTION THAT DOES THE WORK
	 */
	@Override
	protected Position call() throws Exception {
		// TODO Auto-generated method stub
		
		int run = 1;
		
		this.vehicles.stream().forEach(e -> e.valueProperty().addListener((arg0, arg1, arg2) -> {
			this.updateValue(arg2);
		}));
		
		while(run == 1 && !this.vehicles.isEmpty()) {
			if(this.linePositions.get(0).isTaken() == false) {
				Thread t = new Thread(this.vehicles.poll());
				t.setDaemon(true);
				t.start();
			}
		}
		
		return this.vehicles.peek().getCurrentPosition();
	}
	//============================================

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

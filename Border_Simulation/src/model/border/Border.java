package model.border;

import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import model.passenger.DriverPassenger;
import model.passenger.Passenger;
import model.position.*;
import model.vehicle.BusVehicle;
import model.vehicle.PersonalVehicle;
import model.vehicle.Vehicle;
import model.vehicle.TruckVehicle;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import controller.watcher.*;

public class Border extends Task<Position>{
	
	public static boolean RUN = true;
	private static String PUNISHED_PERSON_OBJECTS = "punished_persons.tmp";
	private static String DID_NOT_CROSS_BORDER = "did_not_cross_border.txt";
	
	private int numBusses = 5;
	private int numTrucks = 10;
	private int numPersonalVehicles = 35;
	
	private Queue<Vehicle> vehicles = null;
	
	private List<LinePosition> linePositions = new ArrayList<>();
	private List<CarBusPoliceTerminal> carBusPoliceTerminals = new ArrayList<>();
	private CarBusCustomsTerminal carBusCustomsTerminal = new CarBusCustomsTerminal();
	private TruckPoliceTerminal truckPoliceTerminal = new TruckPoliceTerminal();
	private TruckCustomsTerminal truckCustomsTerminal = new TruckCustomsTerminal();
	
	
	/*
	 * Punished persons
	 */
	private CopyOnWriteArrayList<Passenger> punishedPersons = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<TruckVehicle> punishedTrucks = new CopyOnWriteArrayList<>();
	
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
		
		readTerminalStatusFromFile();
		
	}
	private void readTerminalStatusFromFile() {
		try {
			List<String> lines = Files.readAllLines(Paths.get("src"+ File.separator+"model"+File.separator+"border"+File.separator+"terminals"));
			updateTerminalStatus(lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateTerminalStatus(List<String> terminalsStatus) {
		for(String terminal : terminalsStatus) {
			if(terminal.trim().startsWith("carBusPoliceTerminal0")) {
				if(terminal.trim().endsWith("true"))
					carBusPoliceTerminals.get(0).setWorking(true);
				else
					carBusPoliceTerminals.get(0).setWorking(false);
			}
			else if(terminal.trim().startsWith("carBusPoliceTerminal1")){
				if(terminal.trim().endsWith("true"))
					carBusPoliceTerminals.get(1).setWorking(true);
				else
					carBusPoliceTerminals.get(1).setWorking(false);
			}
			else if(terminal.trim().startsWith("truckPoliceTerminal0")){
				if(terminal.trim().endsWith("true"))
					truckPoliceTerminal.setWorking(true);
				else
					truckPoliceTerminal.setWorking(false);
			}
			else if(terminal.trim().startsWith("carBusCustomsTerminal")){
				if(terminal.trim().endsWith("true"))
					carBusCustomsTerminal.setWorking(true);
				else
					carBusCustomsTerminal.setWorking(false);
			}
			else if(terminal.trim().startsWith("truckCustomsTerminal")){
				if(terminal.trim().endsWith("true"))
					truckCustomsTerminal.setWorking(true);
				else
					truckCustomsTerminal.setWorking(false);
			}
		}
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
			TruckVehicle truck = new TruckVehicle();
			truck.setCustomsDoc(e -> {
				this.punishedTrucks.add(e);
			});
			tmpVehicle.add(truck);
		}
		for(int i = 0; i < numPersonalVehicles; i++) {
			tmpVehicle.add(new PersonalVehicle());
		}
		
		tmpVehicle.stream().forEach(vehicle -> 
									vehicle.setPunishmentConsumer(person -> 
																	this.punishedPersons.add(person)));
		
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

	public void close() {
		
		writePunishedPersons();
	    writeRecordToFile();
		
	}
	
	private void writePunishedPersons() {
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(PUNISHED_PERSON_OBJECTS);
			try {
				oos = new ObjectOutputStream(fos);
				oos.writeObject(this.punishedPersons);
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeRecordToFile() {
		try (BufferedWriter br = new BufferedWriter(new FileWriter(DID_NOT_CROSS_BORDER));){
			punishedPersons.stream().forEach(e -> {
				String output  = e.toString();
				if(e instanceof DriverPassenger) {
					output = output +" did not have valid id! "+ ((DriverPassenger) e).getVehicle().toString();
				}
				try {
					br.write(output);
					br.newLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			});
			punishedTrucks.stream().forEach(e -> {
				try {
					br.write(e.toString());
					br.newLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//===========================================
	/*
	 * FUNCTION THAT DOES THE WORK
	 */
	@Override
	protected Position call() throws Exception {
		// TODO Auto-generated method stub
		
		this.vehicles.stream().forEach(e -> e.valueProperty().addListener((arg0, arg1, arg2) -> {
			this.updateValue(arg2);
		}));
		
		// filewatcher for checking state of terminal
		FileWatcher fw = new FileWatcher();
		fw.setUpdateStatusConsumer(e -> updateTerminalStatus(e));
		fw.setDaemon(true);
		fw.start();
		// allow vehicle thread to run
		Vehicle.RUN = true;
		
		// starting cars
		while(!this.vehicles.isEmpty()) {
			if(Border.RUN == false) {
				fw.interrupt();
				break;
			}
			if(this.linePositions.get(0).isTaken() == false) {
				Thread t = new Thread(this.vehicles.poll());
				t.setPriority(3);
				t.setDaemon(true);
				t.start();
			}
			Thread.sleep(100);
		}
		//=================================================//
		
		return this.vehicles.peek().getCurrentPosition();
	}
	//============================================


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
	/**
	 * @return the punishedTrucks
	 */
	public CopyOnWriteArrayList<TruckVehicle> getPunishedTrucks() {
		return punishedTrucks;
	}
	/**
	 * @param punishedTrucks the punishedTrucks to set
	 */
	public void setPunishedTrucks(CopyOnWriteArrayList<TruckVehicle> punishedTrucks) {
		this.punishedTrucks = punishedTrucks;
	}
	
	/*
	 * This function is called when you want to stop simulation
	 */
	public void stop() {
		Vehicle.RUN = false;
		try {
			Thread.sleep(200);
		}catch(InterruptedException e ) {
			
		}
		this.close();
		Border.RUN = false;
	}

}

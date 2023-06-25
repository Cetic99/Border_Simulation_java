package view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import model.vehicle.*;

public class AllVehicles implements Initializable{

    @FXML
    private GridPane grid;
    
    private List<Vehicle> vehicles;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	

	/**
	 * @return the vehicles
	 */
	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * @param vehicles the vehicles to set
	 */
	public synchronized void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		for(int i = 0; i< 50;i++) {
			ImageView iv = (ImageView)grid.getChildren().get(i);
			Vehicle v = vehicles.get(i);
			if(v.isMoving())
				iv.setImage(v.getImage());
			else if(v.isCrossedBorder() && v.isHavingIncident()) {
				iv.setImage(v.getIncidentImage());
			}
			else if(v.isCrossedBorder()) {
				iv.setImage(v.getPassedImage());
			}
			else if(!v.isCrossedBorder()) {
				iv.setImage(v.getDidntPassImage());
			}
		}
	}

	public void exit() {
		MainController.allVehiclesController = null;
		System.out.println("Closing AllVehicleController");
	}
}

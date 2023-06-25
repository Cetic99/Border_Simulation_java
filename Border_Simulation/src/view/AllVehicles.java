package view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.vehicle.*;

public class AllVehicles implements Initializable{

    @FXML
    private GridPane grid;
    
    private List<Vehicle> vehicles;
    Map<ImageView,Vehicle> map = new HashMap<>();

    private RelevantInfo rInfo;
    private Stage stage;
    
    private static Logger log;
    
    static {
    	try {
			log = Logger.getLogger(AllVehicles.class.getName());
			log.addHandler(new FileHandler("AllVehicles"));
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
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
		/**
		 * Event handlers
		 */
		for(int i = 0; i< 50; i++) {
			ImageView iv = (ImageView)grid.getChildren().get(i);
			map.put(iv, vehicles.get(i));
			iv.setOnMouseClicked(e -> {
				try {
					
						FXMLLoader loader = new FXMLLoader();
						Parent root = loader.load(getClass().getResourceAsStream("RelevantInfo.fxml"));
						rInfo = loader.getController();
						rInfo.setText(map.get(iv).getRelevantInfo());
						stage = new Stage();
						stage.setTitle("Vehicle Info");

						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();


				} catch (Exception ex) {
					log.log(Level.WARNING,ex.fillInStackTrace().toString());
				}
			});
		}
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

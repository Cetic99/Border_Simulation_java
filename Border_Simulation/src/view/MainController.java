package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.border.Border;
import model.vehicle.Vehicle;

import java.util.Queue;


public class MainController implements Initializable {

	@FXML
	private ImageView c1;

	@FXML
	private ImageView ck;

	@FXML
	private ImageView column0;

	@FXML
	private ImageView column1;

	@FXML
	private ImageView column2;

	@FXML
	private ImageView column3;

	@FXML
	private ImageView column4;

	@FXML
	private ImageView p1;

	@FXML
	private ImageView p2;

	@FXML
	private ImageView pk;

	@FXML
	private Button startStopButton;
	private boolean running = false;

	@FXML
	private Button statusButton;
	
	@FXML
	private Label timeLabel;
	
	public static AllVehicles allVehiclesController;
	private Stage stage;
	
	public static Timeline timeline;
	private int seconds = 0;
	
	private static Logger log;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			log = Logger.getLogger(MainController.class.getName());
			log.addHandler(new FileHandler("MainController.log"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		startStopButton.setOnMouseClicked(event -> {
			if (running == false) {
				Main.border = new Border(column4, column3, column2, column1, column0, p1, p2, c1, pk, ck);
				Border.RUN = true;
				startStopButton.setText("STOP");
				System.out.println("Starting");
				Thread t = new Thread(Main.border);
				t.setPriority(9);
				t.setDaemon(true);
				t.start();
				running = true;
				
				/**
				 * Timer methods
				 */
				seconds = 0;
				updateTimerLabel();
				timeline = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
		            seconds++;
		            updateTimerLabel();
		        }));
		        timeline.setCycleCount(Timeline.INDEFINITE);
		        timeline.play();
			} else {
				timeline.stop();
				
				startStopButton.setText("START");
				Main.border.stop();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					log.log(Level.WARNING,e.fillInStackTrace().toString());
				}
				running = false;
			}
		});

		statusButton.setOnMouseClicked(event -> {
			try {
				if (Main.border != null) {

					FXMLLoader loader = new FXMLLoader();
					Parent root = loader.load(getClass().getResourceAsStream("AllVehicles.fxml"));
					allVehiclesController = loader.getController();
					allVehiclesController.setVehicles(Main.border.getAllVehicles());
					stage = new Stage();

					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();

				}

			} catch (Exception e) {
				log.log(Level.WARNING,e.fillInStackTrace().toString());
			}
		});

	}
	
	private void updateTimerLabel() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, secs);
        timeLabel.setText(timeString);
    }

	public void exit() {
		if (Main.border != null)
			Main.border.close();
		System.out.println("Closing");
	}

}

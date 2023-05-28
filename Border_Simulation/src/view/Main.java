package view;
	
import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Image truck = new Image("file:src/view/images/truck.png");
			Image car = new Image("file:src/view/images/car.png");
			Image bus = new Image("file:src/view/images/bus.jpg");
			ImageView truckView = new ImageView(truck);
			ImageView carView = new ImageView(car);
			ImageView busView = new ImageView(bus);
			
			carView.setFitWidth(400);
			carView.setFitHeight(400);
			carView.setPreserveRatio(true);
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,400,400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

package view;
	
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.border.Border;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
	
	public static MainController controller;
	public static Border border;
	
	private static Logger log;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResourceAsStream("Main.fxml"));
			controller = loader.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Border Simulation");
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(e -> {
				controller.exit();
				Platform.exit();
			});
		} catch(Exception e) {
			log.log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public static void main(String[] args) {
			try {
				log = Logger.getLogger(Main.class.getName());
				log.addHandler(new FileHandler("Main.log"));
			} catch (SecurityException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		launch(args);
	}
}

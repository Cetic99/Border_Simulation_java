module Border_Simulation {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens view to javafx.graphics, javafx.fxml;
}

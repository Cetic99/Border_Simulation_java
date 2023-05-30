package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import model.border.Border;
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
    
    Border border;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		border = new Border(column4,column3,column2,column1,column0,p1,p2,c1,pk,ck);
		
		border.valueProperty().addListener((observable, oldValue, newValue) -> newValue.updateImage());
		
		Thread t = new Thread(border);
		t.setDaemon(true);
		t.start();
	}

	
	public void exit() {
		border.close();
		System.out.println("Closing");
	}

}

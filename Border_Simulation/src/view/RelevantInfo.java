package view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class RelevantInfo {

    @FXML
    private TextArea textArea;

    
    public void setText(List<String> list) {
    	String str = "";
    	for(String s : list) {
    		str = str + s +System.lineSeparator();
    	}
    	textArea.setText(str);
    }
}

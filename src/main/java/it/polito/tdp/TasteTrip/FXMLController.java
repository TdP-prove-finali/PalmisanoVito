package it.polito.tdp.TasteTrip;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.TasteTrip.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController {
    
	private Model model;
	
	 @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private ComboBox<String> cmbProvincia;

	    @FXML
	    private ComboBox<String> cmbComune;

	    @FXML
	    private TextArea txtResult;
	    
	    @FXML
	    void riempiComuni(ActionEvent event) {
	    	
	    	cmbComune.getItems().clear();
	    	
	    	if(cmbProvincia.getValue() != null) {
	    		
	    		String sigla;
	    		switch(cmbProvincia.getValue()){
	    		case "Bari":
	    			sigla = "BA";
	    			break;
	    		case "Barletta-Andria-Trani":
	    			sigla = "BT";
	    			break;
	    		case "Brindisi":
	    			sigla = "BR";
	    			break;
	    		case "Foggia":
	    			sigla = "FG";
	    			break;
	    		case "Lecce":
	    			sigla = "LE";
	    			break;
	    		case "Taranto":
	    			sigla = "TA";
	    			break;
	    		default:
	    			sigla = "";
	    		}
	    		
	    		cmbComune.getItems().addAll(model.getCommuniProvincia(sigla));
	    	}
	    }

	    @FXML
	    void initialize() {
	        assert cmbProvincia != null : "fx:id=\"cmbProvincia\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert cmbComune != null : "fx:id=\"cmbComune\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	    }

	public void setModel(Model model) {
		this.model = model;
		cmbProvincia.getItems().addAll("Bari", "Barletta-Andria-Trani", "Brindisi", "Foggia", "Lecce", "Taranto");
	}
}

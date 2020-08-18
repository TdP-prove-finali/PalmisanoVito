package it.polito.tdp.TasteTrip;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.TasteTrip.model.Comune;
import it.polito.tdp.TasteTrip.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
    
	private Model model;
	
	 @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private ComboBox<String> cmbProvincia;

	    @FXML
	    private ComboBox<Comune> cmbComune;

	    @FXML
	    private CheckBox ckCitta;

	    @FXML
	    private CheckBox ckFormazione;

	    @FXML
	    private CheckBox ckNatura;

	    @FXML
	    private CheckBox ckStoria;

	    @FXML
	    private CheckBox ckMusei;

	    @FXML
	    private CheckBox ckCulto;

	    @FXML
	    private CheckBox ckSport;

	    @FXML
	    private CheckBox ckLocMare;

	    @FXML
	    private CheckBox ckLido;

	    @FXML
	    private DatePicker dpkAndata;

	    @FXML
	    private DatePicker dpkRitorno;

	    @FXML
	    private TextField txtBudget;

	    @FXML
	    private TextField txtDistanzaMax;
	    
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
	        assert ckCitta != null : "fx:id=\"ckCitta\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckFormazione != null : "fx:id=\"ckFormazione\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckNatura != null : "fx:id=\"ckNatura\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckStoria != null : "fx:id=\"ckStoria\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckMusei != null : "fx:id=\"ckMusei\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckCulto != null : "fx:id=\"ckCulto\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckSport != null : "fx:id=\"ckSport\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckLocMare != null : "fx:id=\"ckLocMare\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert ckLido != null : "fx:id=\"ckLido\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert dpkAndata != null : "fx:id=\"dpkAndata\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert dpkRitorno != null : "fx:id=\"dpkRitorno\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert txtBudget != null : "fx:id=\"txtBudget\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert txtDistanzaMax != null : "fx:id=\"txtDistanzaMax\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	    }

	public void setModel(Model model) {
		this.model = model;
		cmbProvincia.getItems().addAll("Bari", "Barletta-Andria-Trani", "Brindisi", "Foggia", "Lecce", "Taranto");
	}
}

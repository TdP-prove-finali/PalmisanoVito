package it.polito.tdp.TasteTrip;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.TasteTrip.model.Comune;
import it.polito.tdp.TasteTrip.model.Model;
import it.polito.tdp.TasteTrip.model.Percorso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	    private TextField txtNumPersone;
	    
	    @FXML
	    private Button btnReset;
	    
	    @FXML
	    private Button btnCalcola;
	    
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
	    		
	    		cmbComune.getItems().addAll(model.getCommuniByProvincia(sigla));
	    	}
	    }
	    
	    @FXML
	    void doCalcolo(ActionEvent event) {
	    	
	    	txtResult.clear();
	    	
	    	// ----- Effettuo i controlli sui dati inseriti dall'utente -----
	    	
	    	if(cmbProvincia.getValue()==null) {
	    		txtResult.setText("Prima di effettuare una ricerca, selezionare la provincia che si vorrebbe visitare.");
	    		return;
	    	}
	    	
	    	if(!ckCitta.isSelected() && !ckFormazione.isSelected() && !ckNatura.isSelected() && 
	    			!ckStoria.isSelected() && !ckMusei.isSelected() && !ckCulto.isSelected() && 
	    			!ckSport.isSelected() && !ckLocMare.isSelected() && !ckLido.isSelected()) {
	    		txtResult.setText("Prima di effettuare una ricerca, selezionare almeno una delle attivita' disponibili.");
	    		return;
	    	}
	    	
	    	if(dpkAndata.getValue()==null) {
	    		txtResult.setText("Prima di effettuare una ricerca, selezionare la data di andata.");
	    		return;
	    	}
	    	
	    	if(dpkRitorno.getValue()==null) {
	    		txtResult.setText("Prima di effettuare una ricerca, selezionare la data di ritorno.");
	    		return;
	    	}
	    	
	    	LocalDate andata = dpkAndata.getValue();
	    	LocalDate ritorno = dpkRitorno.getValue();
	    	
	    	if(andata.compareTo(LocalDate.now()) <= 0) { // Imposto arbitrariamente che non sia possibile cercare un viaggio nello stesso giorno in cui si effettua la ricerca
	    		txtResult.setText("Le date selezionate devono essere successive al giorno odierno.");
	    		return;
	    	}
	    	
	    	if(ritorno.compareTo(andata) < 0) { // Potrei decidere di fare un viaggio di un solo giorno, quindi avrei andata=ritorno
	    		txtResult.setText("Il ritorno dev'essere uguale o seguente all'andata.");
	    		return;
	    	}
	    	
	    	int numPersone;
	    	double spesaMax;
	    	int distanzaMax;
	    	
	    	try {
				numPersone = Integer.parseInt(txtNumPersone.getText());
			}catch(NumberFormatException nfe) {
				txtResult.appendText("Prima di effettuare una ricerca, impostare un valore numerico intero per il numero di viaggiatori.");
				return;
			}
	    	
	    	try {
	    		String s = txtBudget.getText();
	    		if( s.matches("^0(\\.\\d{1,2})?$|^[1-9]\\d*(\\.\\d{1,2})?$") ) {
					spesaMax = Double.parseDouble(s);
	    		}
				else {
					txtResult.appendText("Prima di effettuare una ricerca, impostare un valore valido, in euro, per la spesa massima che si vorrebbee effettuare.\nUtilizzare il punto \".\" per separare le cifre intere dai decimali");
					return;
				}
			}catch(NumberFormatException nfe) {
				txtResult.appendText("Prima di effettuare una ricerca, impostare un valore valido, espresso in euro, per la spesa massima che si vorrebbe effettuare.\nUtilizzare il punto \".\" per separare le cifre intere dai decimali");
				return;
			}
	    	
	    	try {
	    		distanzaMax = Integer.parseInt(txtDistanzaMax.getText());
			}catch(NumberFormatException nfe) {
				txtResult.appendText("Prima di effettuare una ricerca, impostare un valore numerico intero, espresso in km, per la distanza massima che si e' disposti a percorrere.");
				return;
			}
	    	
	    	// ----- Seleziono i comuni in base alle scelte dell'utente -----
	    	
	    	if(cmbComune.getValue()==null) {
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
	    		model.addComuniBySelezioneProvincia(sigla, distanzaMax);
	    	}
	    	else {
	    		model.addComuniBySelezioneSpecificaComune(cmbComune.getValue(), distanzaMax);
	    	}
	    	
	    	// ----- Seleziono i B&B idonei -----
	    	
	    	model.addBeBComune(Period.between(andata, ritorno).getDays(), numPersone);
	    	
	    	// ----- Raccolgo le scelte effettuate sulle attivita' -----
	    	
	    	List<String> tipologieAttivitaTuristiche = new ArrayList<String>();
	    	List<String> tipologieLuoghiInteresse = new ArrayList<String>();
	    	
	    	if(ckCitta.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckCitta.getText());
	    		tipologieAttivitaTuristiche.add(ckCitta.getText());
	    	}
	    	if(ckFormazione.isSelected()) {
	    		tipologieAttivitaTuristiche.add(ckFormazione.getText());
	    	}
	    	if(ckNatura.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckNatura.getText());
	    		tipologieAttivitaTuristiche.add(ckNatura.getText());
	    	}
	    	if(ckStoria.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckStoria.getText());
	    	}
	    	if(ckMusei.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckMusei.getText());
	    	}
	    	if(ckCulto.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckCulto.getText());
	    	}
	    	if(ckSport.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckSport.getText());
	    		tipologieAttivitaTuristiche.add(ckSport.getText());
	    	}
	    	if(ckLocMare.isSelected()) {
	    		tipologieLuoghiInteresse.add(ckLocMare.getText());
	    	}
	    	
	    	// ----- Richiamo i metodi del model per aggiungere le attivita' selezionate -----
	    	
	    	if(ckLido.isSelected()) {
	    		model.addStabilimentiBalneariComuni(numPersone);
	    	}
	    	
	    	if(!tipologieAttivitaTuristiche.isEmpty()) {
	    		model.addAttivitaTuristicheComuni(tipologieAttivitaTuristiche, numPersone);
	    	}
	    	if(!tipologieLuoghiInteresse.isEmpty()) {
	    		model.addLuoghiInteresseComuni(tipologieLuoghiInteresse, numPersone);
	    	}
	    	
	    	// ----- Creo il grafo -----
	    	
	    	model.creaGrafo();
	    	
	    	// ----- Cerco il viaggio più confortevole e 'lussuoso', rispettando i vincoli dell'utente -----
	    	
	    	Percorso bestPercorso = model.ricorsione(spesaMax, Period.between(andata, ritorno).getDays()+1, distanzaMax);
	    	
	    	txtResult.setText("Il viaggio piu' confortevole e lussuoso che rispetta le caratteristiche volute e':\n"+bestPercorso+"\n\nDi seguito vengono elencati i 10 percorsi migliori subito dopo quello già riportato:");
	    	int i = 1;
	    	for(Percorso p : model.getAltriPercorsi()) {
	    		if(i<=10) {
	    			txtResult.appendText("\n\n"+i+") "+p);
	    			i++;
	    		}
	    	}
	    }
	    
	    @FXML
	    void doReset(ActionEvent event) {
	    	
	    	cmbProvincia.setValue(null);
	    	cmbComune.getItems().clear();
	    	
	    	ckCitta.setSelected(false);
	    	ckFormazione.setSelected(false);
	    	ckNatura.setSelected(false);
	    	ckStoria.setSelected(false);
	    	ckMusei.setSelected(false);
	    	ckCulto.setSelected(false);
	    	ckSport.setSelected(false);
	    	ckLocMare.setSelected(false);
	    	ckLido.setSelected(false);

	    	dpkAndata.setValue(null);
	    	dpkRitorno.setValue(null);
	    	
	    	txtBudget.clear();
	    	txtDistanzaMax.clear();
	    	txtNumPersone.clear();
	    	
	    	txtResult.clear();
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
	        assert txtNumPersone != null : "fx:id=\"txtNumPersone\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
	        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	    }

	public void setModel(Model model) {
		this.model = model;
		this.inizializzaScene();
	}
	
	private void inizializzaScene() {
		cmbProvincia.getItems().addAll("Bari", "Barletta-Andria-Trani", "Brindisi", "Foggia", "Lecce", "Taranto");
		txtResult.setText("Selezionare la provincia che si vorrebbe visitare ed almeno una delle attivita' disponibili.\n"
				+ "Il campo comune puo' essere lasciato deselezionato se non si vuole scendere cosi' nello specifico.\n"
				+ "Tutti gli altri campi vanno invece riempiti obbligatoriamente secondo le proprie esigenze.");
	}
}

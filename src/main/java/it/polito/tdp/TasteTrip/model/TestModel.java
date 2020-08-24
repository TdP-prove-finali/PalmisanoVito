package it.polito.tdp.TasteTrip.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
    	
		int numPersone = 1;
		int distanzaMax = 20;
		int spesaMax = 100;
		int numGiorni = 2;
		
    	// ----- Seleziono i comuni in base alle scelte dell'utente -----
    	List<Comune> comuni = model.getCommuniByProvincia("BR");
//    	model.addComuniBySelezioneProvincia("LE", distanzaMax);
    	model.addComuniBySelezioneSpecificaComune(comuni.get(4), distanzaMax);
    	
    	// ----- Seleziono i B&B idonei -----
    	
    	model.addBeBComune(numGiorni-1, numPersone);
    	
    	// ----- Raccolgo le scelte effettuate sulle attivita' -----
    	
    	List<String> tipologieAttivitaTuristiche = new ArrayList<String>();
    	List<String> tipologieLuoghiInteresse = new ArrayList<String>();
    	
    	tipologieLuoghiInteresse.add("Alla scoperta della citta'");
    	tipologieAttivitaTuristiche.add("Alla scoperta della citta'");
    	
    	tipologieAttivitaTuristiche.add("Attivita' di formazione");
    	
    	tipologieLuoghiInteresse.add("Luoghi d'interesse naturale");
    	tipologieAttivitaTuristiche.add("Luoghi d'interesse naturale");
    	
    	// ----- Richiamo i metodi del model per aggiungere le attivita' selezionate -----
    	
    	model.addAttivitaTuristicheComuni(tipologieAttivitaTuristiche, numPersone);
    	model.addLuoghiInteresseComuni(tipologieLuoghiInteresse, numPersone);
    	
    	// ----- Creo il grafo -----
    	
    	model.creaGrafo();
    	
    	// ----- Cerco il viaggio più confortevole e 'lussuoso', rispettando i vincoli dell'utente -----
    	
    	Percorso bestPercorso = model.ricorsione(spesaMax, numGiorni, distanzaMax, comuni.get(4));
    	
    	System.out.print("Il viaggio piu' confortevole e lussuoso che rispetta le caratteristiche volute e':\n"+bestPercorso+"\n\nDi seguito vengono elencati i 10 percorsi migliori subito dopo quello già riportato:");
    	int i = 1;
    	for(Percorso p : model.getAltriPercorsi()) {
    		if(i<=10) {
    			System.out.print("\n\n"+i+") "+p);
    			i++;
    		}
    	}

	}

}

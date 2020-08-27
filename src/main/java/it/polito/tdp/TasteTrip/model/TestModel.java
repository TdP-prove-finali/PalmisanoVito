package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
    	
		int numPersone = 2;
		int distanzaMax = 10;
		double spesaMax = 100;
		int numGiorni = 2;
		
    	// ----- Seleziono i comuni in base alle scelte dell'utente -----
    	List<Comune> comuni = model.getCommuniByProvincia("BR");
    	
    	Comune c = comuni.get(comuni.size()-1);
//    	Comune c = null;
    	
    	model.setVariabiliUtente(numGiorni, numPersone, spesaMax, distanzaMax);
    	
//    	model.addComuniBySelezioneProvincia("BT");
    	model.addComuniBySelezioneSpecificaComune(c);
    	
    	// ----- Seleziono i B&B idonei -----
    	
    	model.addBeBComune();
    	
    	// ----- Raccolgo le scelte effettuate sulle attivita' -----
    	
    	List<String> tipologieAttivitaTuristiche = new ArrayList<String>();
    	List<String> tipologieLuoghiInteresse = new ArrayList<String>();
    	
    	tipologieLuoghiInteresse.add("Alla scoperta della citta'");
    	tipologieAttivitaTuristiche.add("Alla scoperta della citta'");
    	
    	tipologieAttivitaTuristiche.add("Attivita' di formazione");
    	
    	tipologieLuoghiInteresse.add("Luoghi d'interesse naturale");
    	tipologieAttivitaTuristiche.add("Luoghi d'interesse naturale");
    	
    	// ----- Richiamo i metodi del model per aggiungere le attivita' selezionate -----
    	
    	model.addAttivitaTuristicheComuni(tipologieAttivitaTuristiche);
    	model.addLuoghiInteresseComuni(tipologieLuoghiInteresse);
    	model.addStabilimentiBalneariComuni();
    	
    	// ----- Creo il grafo -----
    	
//    	model.creaGrafo();
    	
    	// ----- Cerco il viaggio più confortevole e 'lussuoso', rispettando i vincoli dell'utente -----
    	
    	Percorso bestPercorso = model.ricorsione(c);
    	
    	if( !bestPercorso.getAttivita().isEmpty() ) {
    		System.out.print("Il viaggio piu' confortevole e lussuoso che rispetta le caratteristiche volute e':\n\n");
    	}
    	System.out.print(bestPercorso);
    	if(!model.getAltriPercorsi().isEmpty()) {
	    	if(model.getAltriPercorsi().size()<10) {
	    		System.out.print("\n\nDi seguito vengono elencati i "+model.getAltriPercorsi().size()+" percorsi migliori subito dopo quello già riportato:");
	    	}
	    	else {
	    		System.out.print("\n\nDi seguito vengono elencati i 10 percorsi migliori subito dopo quello già riportato:");
	    	}
    		int i = 1;
	    	for(Percorso p : model.getAltriPercorsi()) {
	    		if(i<=10) {
	    			System.out.print("\n\n"+i+") "+p);
	    			i++;
	    		}
	    	}
    	}
	}

}

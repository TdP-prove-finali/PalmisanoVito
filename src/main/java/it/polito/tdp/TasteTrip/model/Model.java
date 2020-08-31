package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.TasteTrip.db.TasteTripDAO;

public class Model {

	private TasteTripDAO dao;
	private List<Comune> comuni;
	private List<Percorso> altriPercorsi;
	private Percorso bestPercorso;
	
	private long numGiorni;
	private int numPersone;
	private double spesaMax;
	private int distanzaMax;
	private List<String> tipoAttivita;
	
	public Model() {
		dao = new TasteTripDAO();
	}
	
	public void setVariabiliUtente(long numGiorni, int numPersone, double spesaMax, int distanzaMax) {
		this.numGiorni = numGiorni;
		this.numPersone = numPersone;
		this.spesaMax = spesaMax;
		this.distanzaMax = distanzaMax;
		tipoAttivita = new ArrayList<String>();
	}
	
	// ----- Metodi per l'aggiunta dei comuni -----
	
	/**
	 * Seleziona i comuni appartenenti alla provincia, la cui sigla viene passata come parametro.
	 * @param sigla sigla della provincia selezionata
	 */
	public List<Comune> getCommuniByProvincia(String sigla) {
		return dao.getCommuniByProvincia(sigla);
	}
	
	/**
	 * Seleziona tutti i comuni che, a partire dal comune passato come parametro, hanno una distanza massima da esso pari al valore impostato.
	 * @param comune comune di riferimento
	 */
	public void addComuniBySelezioneSpecificaComune(Comune comune){
		comuni = new ArrayList<Comune>();
		comuni.add(comune);
		List<Comune> tempList = dao.getAllCommuni();
		
		for(Comune c : tempList) {
			boolean minore = false;
			if( !c.equals(comune) && !comuni.contains(c) ) {
				for(LatLng coor1 : c.getMapCapCoordinate().values()) {
					for(LatLng coor2 : comune.getMapCapCoordinate().values()) {
						Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
						if(distanza < distanzaMax) {
							minore = true;
						}
					}
				}
				if(minore) {
					comuni.add(c);
				}
			}
		}
	}
	
	/**
	 * Aggiunge alla {@link List} comuni, tutti i comuni facenti parte della provincia passata come parametro.
	 * @param sigla sigla della provincia selezionata
	 */
	public void addComuniBySelezioneProvincia(String sigla){
		comuni = new ArrayList<Comune>();
		comuni.addAll(dao.getCommuniByProvincia(sigla));
	}
	
	// ----- Metodi per l'aggiunta dei B&B -----
	
	/**
	 * Se il comune passato come parametro e' diverso da null, aggiorna solo la sua lista dei B&B, altrimenri aggiorna la
	 * lista dei B&B di tutti i comuni presenti nella lista comuni.
	 * Vengono inseriti i soli B&B che permettono di soggiornare per un numero di notti minore o uguale a quello impostato.
	 * @param comune comune di riferimento
	 */
	public void addBeBComune(Comune comune){
		if(comune == null) {
			if(numGiorni!=1) {
				for(Comune c : comuni) {
					c.addListaBeB(dao.getBeBComune(c, numGiorni-1, numPersone));
				}
			}
		}
		else {
			comune.addListaBeB(dao.getBeBComune(comune, numGiorni-1, numPersone));
		}
	}
	
	// ----- Metodi per l'aggiunta delle attivita' ai singoli comuni -----
	
	/**
	 * Aggiunge alla {@link List} attivita, tutte le attivita' turistiche presenti sul territorio dei comuni idonei.
	 * @param tipologie tipo di attivita turistiche selezionate dall'utente
	 */
	public void addAttivitaTuristicheComuni(List<String> tipologie) {
		for(String t : tipologie) {
			if(!tipoAttivita.contains(t)) {
				tipoAttivita.add(t);
			}
		}
		for(Comune c : comuni) {
			for(String t : tipologie) {
				c.addListaAttivita(dao.getAttivitaTuristicheComuni(c, t, numPersone));
			}
		}
	}

	/**
	 * Aggiunge alla {@link List} attivita, tutti i luoghi d'interesse presenti sul territorio dei comuni idonei.
	 * @param tipologie tipo di luoghi d'interesse selezionati dall'utente
	 */
	public void addLuoghiInteresseComuni(List<String> tipologie) {
		for(String t : tipologie) {
			if(!tipoAttivita.contains(t)) {
				tipoAttivita.add(t);
			}
		}
		for(Comune c : comuni) {
			for(String t : tipologie) {
				c.addListaAttivita(dao.getLuoghiInteresseComuni(c, t, numPersone));
			}
		}
	}

	/**
	 * Aggiunge alla {@link List} attivita, tutti gli stabilimenti balneari presenti sul territorio dei comuni idonei.
	 */
	public void addStabilimentiBalneariComuni() {
		tipoAttivita.add("Stabilimenti Balneari");
		for(Comune c : comuni) {
			c.addListaAttivita(dao.getStabilimentiBalneariComuni(c, numPersone));
		}
	}
	
	// ----- Ricorsione per la ricerca del viaggio migliore per l'utente -----
	
	/**
	 * Imposta tutti i parametri necessari ad effettuare la ricorsione per la ricerca del percorso migliore
	 * @param comune e' il comune scelto dall'utente; se e' uguale a null, vengono considerati tutti i comune della provincia selezionata
	 * @return ritorna bestPercorso, cioe' il percorso migliore trovato, che massimizza il costo del viaggio
	 */
	public Percorso ricorsione(Comune comune){
		
		altriPercorsi = new ArrayList<Percorso>();
		bestPercorso = new Percorso(comune, 0, numGiorni);
		
		if(comune != null && numGiorni>1 && comune.getListaBeB().isEmpty()) {
			return bestPercorso;
		}
		
		Percorso parziale = new Percorso(comune, 0, numGiorni);
		List<Attivita> attivita = new ArrayList<Attivita>();
		
		for(Comune c : comuni) {
			attivita.addAll(c.getListaAttivita());
		}
		int i = 0;
		for(Attivita a : attivita) {
			a.setOrdine(i);
			i++;
		}
		
		if(numGiorni>1) {
			if(comune != null) {
				for(BeB b : comune.getListaBeB()) {
					if( b.getPrezzo() <= spesaMax ) {
						parziale.setBeb(b);
						parziale.addCosto(parziale.getBeb().getPrezzo());
						cerca(b, parziale, attivita, comune, tipoAttivita);
						parziale.removeCosto(parziale.getBeb().getPrezzo());
						parziale.unsetBeb();
					}
				}
			}else {
				for(Comune c : comuni) {
					for(BeB b : c.getListaBeB()) {
						if( b.getPrezzo() <= spesaMax ) {
							parziale.setBeb(b);
							parziale.setComune(b.getComune());
							parziale.addCosto(parziale.getBeb().getPrezzo());
							cerca(b, parziale, attivita, comune, tipoAttivita);
							parziale.removeCosto(parziale.getBeb().getPrezzo());
							parziale.unsetBeb();
						}
					}
				}
			}
		}
		else { // Se numGiorni==1, non ci sono notti di pernottamento da considerare.
			cerca(null, parziale, attivita, comune, tipoAttivita);
		}
		
		return bestPercorso;
	}
	
	private void cerca(BeB b, Percorso parziale, List<Attivita> attivita, Comune comune, List<String> tipiRestanti) {
		if(parziale.getAttivita().size()==2*numGiorni) {
			if( parziale.getCosto() <= spesaMax && !altriPercorsi.contains(parziale) ) {
				altriPercorsi.add(new Percorso(parziale));
				if(parziale.getCosto() >= bestPercorso.getCosto()) {
					if(parziale.getCosto() > bestPercorso.getCosto() ) {
						bestPercorso = new Percorso(parziale);
					}
					else {
						List<String> tipologiePresentiParziale = new ArrayList<String>();
						List<String> tipologiePresentiBest = new ArrayList<String>();
						for(Attivita a : parziale.getAttivita()) {
							if(!tipologiePresentiParziale.contains(a.getTipologia())) {
								tipologiePresentiParziale.add(a.getTipologia());
							}
						}
						for(Attivita a : bestPercorso.getAttivita()) {
							if(!tipologiePresentiBest.contains(a.getTipologia())) {
								tipologiePresentiBest.add(a.getTipologia());
							}
						}
						if(tipologiePresentiParziale.size() > tipologiePresentiBest.size()) {
							bestPercorso = new Percorso(parziale);
						}
					}
				}
			}
			return;
		}
		
		if(numGiorni>1) {
			for(Attivita a : attivita) {
				if( (parziale.getCosto() + a.getPrezzo()) <= spesaMax ) {
					if(parziale.getAttivita().size()==0) {
						// Verifico che la nuova attivita' non sia troppo distante dal B&B attuale
						if(LatLngTool.distance(b.getCoordinate(), a.getCoordinate(), LengthUnit.KILOMETER)<distanzaMax) {
							parziale.addAttivita(a);
							parziale.addCosto(a.getPrezzo());
							List<String> restanti = new ArrayList<String>(tipiRestanti);
							restanti.remove(a.getTipologia());
							cerca(b, parziale, attivita, comune, restanti);
							parziale.removeAttivita(a);
							parziale.removeCosto(a.getPrezzo());
						}
					}
					else if( LatLngTool.distance(b.getCoordinate(), a.getCoordinate(), LengthUnit.KILOMETER)<distanzaMax 
							&& !parziale.getAttivita().contains(a)
							&& parziale.getAttivita().get(parziale.getAttivita().size()-1).getOrdine() < a.getOrdine()) { // Effettuo una ricerca che escluda la possibilità di ripetere piu' volte la stessa lista
						if(tipiRestanti.size() != 0 && tipiRestanti.contains(a.getTipologia())) { 
							parziale.addAttivita(a);
							parziale.addCosto(a.getPrezzo());
							List<String> restanti = new ArrayList<String>(tipiRestanti);
							restanti.remove(a.getTipologia());
							cerca(b, parziale, attivita, comune, restanti);
							parziale.removeAttivita(a);
							parziale.removeCosto(a.getPrezzo());
						}
						else if(tipiRestanti.size() == 0) {
							parziale.addAttivita(a);
							parziale.addCosto(a.getPrezzo());
							List<String> restanti = new ArrayList<String>(tipoAttivita);
							restanti.remove(a.getTipologia());
							cerca(b, parziale, attivita, comune, restanti);
							parziale.removeAttivita(a);
							parziale.removeCosto(a.getPrezzo());
						}
						else {
							boolean trovato = false;
							List<Attivita> temp = new ArrayList<>(attivita);
							temp.removeAll(parziale.getAttivita());
							for(Attivita a1 : temp) {
								if(tipiRestanti.contains(a1.getTipologia()) // esiste un'attivita 'a1' che ha una tipologia che ha la priorita' rispetto alla tipologia dell'attivita attuale 'a', e che rispetta tutti i vincoli per l'entrare all'interno del percorso
										&& LatLngTool.distance(b.getCoordinate(), a1.getCoordinate(), LengthUnit.KILOMETER)<distanzaMax
										&& (parziale.getCosto() + a1.getPrezzo()) <= spesaMax
										&& parziale.getAttivita().get(parziale.getAttivita().size()-1).getOrdine() < a1.getOrdine() ) {
									trovato = true;
								}
							}
							if(!trovato) {
								parziale.addAttivita(a);
								parziale.addCosto(a.getPrezzo());
								List<String> restanti = new ArrayList<String>(tipoAttivita);
								restanti.remove(a.getTipologia());
								cerca(b, parziale, attivita, comune, restanti);
								parziale.removeAttivita(a);
								parziale.removeCosto(a.getPrezzo());
							}
						}
					}
				}
			}
		}
		else {
			for(Attivita a : attivita) {
				if( (parziale.getCosto() + a.getPrezzo()) <= spesaMax ) {
					if(parziale.getAttivita().size()==0) {
						parziale.addAttivita(a);
						parziale.addCosto(a.getPrezzo());
						List<String> restanti = new ArrayList<String>(tipiRestanti);
						restanti.remove(a.getTipologia());
						cerca(b, parziale, attivita, comune, restanti);
						parziale.removeAttivita(a);
						parziale.removeCosto(a.getPrezzo());
					}
					else if( !parziale.getAttivita().contains(a)
							&& parziale.getAttivita().get(parziale.getAttivita().size()-1).getOrdine() < a.getOrdine()) { // Effettuo una ricerca che escluda la possibilità di ripetere piu' volte la stessa lista
						
						// Verifico che la nuova attivita' non sia troppo distante da una delle qualsiasi attivita' gia' inserite all'interno del percorso
						boolean troppoDistante = false;
						for(Attivita a2 : parziale.getAttivita()) {
							if(LatLngTool.distance(a.getCoordinate(), a2.getCoordinate(), LengthUnit.KILOMETER)>distanzaMax) {
								troppoDistante = true;
							}
						}
						if(!troppoDistante) {
							if(tipiRestanti.size() != 0 && tipiRestanti.contains(a.getTipologia())) { 
								parziale.addAttivita(a);
								parziale.addCosto(a.getPrezzo());
								List<String> restanti = new ArrayList<String>(tipiRestanti);
								restanti.remove(a.getTipologia());
								cerca(b, parziale, attivita, comune, restanti);
								parziale.removeAttivita(a);
								parziale.removeCosto(a.getPrezzo());
							}
							else if(tipiRestanti.size() == 0) {
								parziale.addAttivita(a);
								parziale.addCosto(a.getPrezzo());
								List<String> restanti = new ArrayList<String>(tipoAttivita);
								restanti.remove(a.getTipologia());
								cerca(b, parziale, attivita, comune, restanti);
								parziale.removeAttivita(a);
								parziale.removeCosto(a.getPrezzo());
							}
							else {
								boolean trovato = false;
								List<Attivita> temp = new ArrayList<>(attivita);
								temp.removeAll(parziale.getAttivita());
								for(Attivita a1 : temp) {
									if(tipiRestanti.contains(a1.getTipologia()) // esiste un'attivita 'a1' che ha una tipologia che ha la priorita' rispetto alla tipologia dell'attivita attuale 'a', e che rispetta tutti i vincoli per l'entrare all'interno del percorso
											&& (parziale.getCosto() + a1.getPrezzo()) <= spesaMax
											&& parziale.getAttivita().get(parziale.getAttivita().size()-1).getOrdine() < a1.getOrdine() ) {
										boolean troppoDistante2 = false;
										for(Attivita a2 : parziale.getAttivita()) {
											if(LatLngTool.distance(a1.getCoordinate(), a2.getCoordinate(), LengthUnit.KILOMETER)>distanzaMax) {
												troppoDistante2 = true;
											}
										}
										if(!troppoDistante2) {
											trovato = true;
										}
									}
								}
								if(!trovato) {
									parziale.addAttivita(a);
									parziale.addCosto(a.getPrezzo());
									List<String> restanti = new ArrayList<String>(tipoAttivita);
									restanti.remove(a.getTipologia());
									cerca(b, parziale, attivita, comune, restanti);
									parziale.removeAttivita(a);
									parziale.removeCosto(a.getPrezzo());
								}
							}
						}
					}
				}
			}
		}
	}

	public List<Percorso> getAltriPercorsi() {
		
		altriPercorsi.remove(bestPercorso);
		
		Collections.sort(altriPercorsi, new Comparator<Percorso>() {
			public int compare(Percorso p1, Percorso p2) {
				if (p1.getCosto() > p2.getCosto()) return -1;
		        if (p1.getCosto() < p2.getCosto()) return 1;
		        return 0;
			}
		});
		return altriPercorsi;
	}
}

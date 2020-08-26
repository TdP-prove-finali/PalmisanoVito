package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.TasteTrip.db.TasteTripDAO;

public class Model {

	private TasteTripDAO dao;
	private Graph<Comune, DefaultWeightedEdge> grafo;
	private final int distMax; // Imposto una distanza massima, espressa in kilometri, per la quale due comuni non risultano collegati direttamente all'interno del grafo
	private List<Comune> comuni;
	private List<Percorso> altriPercorsi;
	private Percorso bestPercorso;
	
	private int numGiorni;
	private int numPersone;
	private double spesaMax;
	private int distanzaMax;
	private List<String> tipoAttivita;
	
	public Model() {
		dao = new TasteTripDAO();
		distMax = 20;
	}
	
	public void setVariabiliUtente(int numGiorni, int numPersone, double spesaMax, int distanzaMax) {
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
	 * @return {@link List} dei comuni corrispondenti
	 */
	public List<Comune> getCommuniByProvincia(String sigla) {
		return dao.getCommuniByProvincia(sigla);
	}
	
	/**
	 * Seleziona tutti i comuni che, a partire dal comune passato come parametro, hanno una distanza massima da esso pari al valore passato.
	 * @param comune comune di riferimento
	 * @param distanzaMax distanza massima passata dall'utente
	 */
	public void addComuniBySelezioneSpecificaComune(Comune comune){
		
		comuni = new ArrayList<Comune>();
		comuni.add(comune);
		List<Comune> tempList = dao.getAllCommuni();
		
		List<LatLng> coorComune = new ArrayList<LatLng>(comune.getMapCapCoordinate().values());
		
		for(Comune c : tempList) {
			if( !c.equals(comune) && !comuni.contains(c) ) {
				for(LatLng coor1 : c.getMapCapCoordinate().values()) {
					for(LatLng coor2 : coorComune) {
						Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
						if(distanza < distanzaMax) {
							comuni.add(c);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Aggiunge alla {@link List} comini, tutti i comuni facenti parte della provincia passata come parametro.
	 * @param sigla sigla della provincia selezionata
	 * @param distanzaMax distanza massima passata dall'utente
	 */
	public void addComuniBySelezioneProvincia(String sigla){
		comuni = new ArrayList<Comune>();
		comuni.addAll(dao.getCommuniByProvincia(sigla));
	}
	
	// ----- Metodi per l'aggiunta dei B&B -----
	
	/**
	 * Seleziona tutti i BeB dei comuni presenti nella lista comuni,
	 * che permettono di soggiornare per un numero di notti minore o uguale a quello passato come parametro.
	 * @param numNotti
	 * @return {@link List} dei B&B corrispondenti
	 */
	public void addBeBComune(){
		if(numGiorni!=1) {
			for(Comune c : comuni) {
				c.addListaBeB(dao.getBeBComune(c, numGiorni-1, numPersone));
			}
		}
	}
	
	// ----- Metodo per la creazione del grafo -----
	
	/**
	 * Crea un grafo semplice pesato, i cui vertici sono i comuni corrispondenti alla richiesta dell'utente.
	 */
/*	public void creaGrafo() {
		
		grafo = new SimpleWeightedGraph<Comune, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, comuni);
		
		for(Comune c1 : grafo.vertexSet()) {
			for(Comune c2 : grafo.vertexSet()) {
				if(!c1.equals(c2)) {
					if(grafo.getEdge(c1, c2) == null) {
						for(LatLng coor1 : c1.getMapCapCoordinate().values()) {
							for(LatLng coor2 : c2.getMapCapCoordinate().values()) {
								Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
								if(distanza < distMax) {
									Graphs.addEdgeWithVertices(grafo, c1, c2, distanza);
								}
							}
						}
					}
				}
			}
			
		}
		System.out.println("vertici: "+grafo.vertexSet().size()+", archi: "+grafo.edgeSet().size());
	}
*/
	// ----- Metodi per l'aggiunta delle attivita' ai singoli comuni -----
	
	/**
	 * Aggiunge alla {@link List} attivita, tutte le attivita' turistiche presenti sul territorio dei comuni idonei.
	 * @param tipologie tipo di attivita turistiche selezionate dall'utente
	 * @param numPersone numero di viaggiatori
	 */
	public void addAttivitaTuristicheComuni(List<String> tipologie) {
		for(String t : tipologie) {
			if(!tipoAttivita.contains(t)) {
				tipoAttivita.addAll(tipologie);
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
	 * @param numPersone numero di viaggiatori
	 */
	public void addLuoghiInteresseComuni(List<String> tipologie) {
		for(String t : tipologie) {
			if(!tipoAttivita.contains(t)) {
				tipoAttivita.addAll(tipologie);
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
	 * @param numPersone numero di viaggiatori
	 */
	public void addStabilimentiBalneariComuni() {
		tipoAttivita.add("Stabilimenti Balneari");
		for(Comune c : comuni) {
			c.addListaAttivita(dao.getStabilimentiBalneariComuni(c, numPersone));
		}
	}
	
	// ----- Ricorsione per la ricerca del viaggio migliore per l'utente -----
	
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
			if(parziale.getCosto()<spesaMax && !altriPercorsi.contains(parziale)) {
				altriPercorsi.add(new Percorso(parziale));
				if(parziale.getCosto()>=bestPercorso.getCosto()) {
					bestPercorso = new Percorso(parziale);
				}
			}
			return;
		}
		
		if(numGiorni>1) {
			for(Attivita a : attivita) {
				if( (parziale.getCosto() + a.getPrezzo()) >= spesaMax ) {
					return;
				}
				if(parziale.getAttivita().size()==0) {
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
							if(tipiRestanti.contains(a1.getTipologia())) {
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
		else {
			for(Attivita a : attivita) {
				if( (parziale.getCosto() + a.getPrezzo()) >= spesaMax ) {
					return;
				}
				if(parziale.getAttivita().size()==0) {
					// Verifico che la nuova attivita' non sia troppo distante da una delle qualsiasi attivita' gia' inserite all'interno del percorso
					boolean troppoDistante = false;
					for(Attivita a2 : parziale.getAttivita()) {
						if(LatLngTool.distance(a.getCoordinate(), a2.getCoordinate(), LengthUnit.KILOMETER)>distanzaMax) {
							troppoDistante = true;
						}
					}
					if(!troppoDistante) {
						parziale.addAttivita(a);
						parziale.addCosto(a.getPrezzo());
						List<String> restanti = new ArrayList<String>(tipiRestanti);
						restanti.remove(a.getTipologia());
						cerca(b, parziale, attivita, comune, restanti);
						parziale.removeAttivita(a);
						parziale.removeCosto(a.getPrezzo());
					}
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
								if(tipiRestanti.contains(a1.getTipologia())) {
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

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
	
	public Model() {
		dao = new TasteTripDAO();
		distMax = 20;
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
	public void addComuniBySelezioneSpecificaComune(Comune comune, int distanzaMax){
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
	 * Aggiunge alla {@link List} comini, tutti i comuni facenti parte della provincia passata come parametro,
	 * e tutti i comuni aventi distanza massima, da questi ultimi, pari a distMax.
	 * Es: Provincia scelta (BR), nella lista saranno presenti tutti i comuni della Provincia,
	 * ma sara' presente anche Locorotndo (BA), che non fa parte della Provincia, 
	 * ma ha una distanza da Cisternino (BR) minore della distMax.
	 * @param sigla sigla della provincia selezionata
	 * @param distanzaMax distanza massima passata dall'utente
	 */
	public void addComuniBySelezioneProvincia(String sigla, int distanzaMax){
		comuni = new ArrayList<Comune>();
		comuni.addAll(dao.getCommuniByProvincia(sigla));
		List<Comune> tempList1 = dao.getAllCommuni();
		List<Comune> tempList2 = new ArrayList<Comune>(comuni);
		
		for(Comune c1 : tempList1) {
			for(Comune c2 : tempList2) {
				if( !comuni.contains(c1)) {
					for(LatLng coor1 : c1.getMapCapCoordinate().values()) {
						for(LatLng coor2 : c2.getMapCapCoordinate().values()) {
							Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
							if(distanza < distanzaMax) {
								comuni.add(c1);
							}
						}
					}
				}
			}
		}
	}
	
	// ----- Metodi per l'aggiunta dei B&B -----
	
	/**
	 * Seleziona tutti i BeB dei comuni presenti nella lista comuni,
	 * che permettono di soggiornare per un numero di notti minore o uguale a quello passato come parametro.
	 * @param numNotti
	 * @return {@link List} dei B&B corrispondenti
	 */
	public void addBeBComune(int numNotti, int numPersone){
		if(numNotti!=0) {
			for(Comune c : comuni) {
				c.addListaBeB(dao.getBeBComune(c, numNotti, numPersone));
			}
		}
	}
	
	// ----- Metodo per la creazione del grafo -----
	
	/**
	 * Crea un grafo semplice pesato, i cui vertici sono i comuni corrispondenti alla richiesta dell'utente.
	 */
	public void creaGrafo() {
		
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

	// ----- Metodi per l'aggiunta delle attivita' ai singoli comuni -----
	
	/**
	 * Aggiunge alla {@link List} attivita, tutte le attivita' turistiche presenti sul territorio dei comuni idonei.
	 * @param tipologie tipo di attivita turistiche selezionate dall'utente
	 * @param numPersone numero di viaggiatori
	 */
	public void addAttivitaTuristicheComuni(List<String> tipologie, int numPersone) {
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
	public void addLuoghiInteresseComuni(List<String> tipologie, int numPersone) {
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
	public void addStabilimentiBalneariComuni(int numPersone) {
		for(Comune c : comuni) {
			c.addListaAttivita(dao.getStabilimentiBalneariComuni(c, numPersone));
		}
	}
	
	// ----- Ricorsione per la ricerca del viaggio migliore per l'utente -----
	
	public Percorso ricorsione(double spesaMax, int numGiorni, int distanzaMax, Comune comune){
		
		// Impostare selezione comune: se l'utente ha scelto un comune, lo faccio soggiornare lì
		
		altriPercorsi = new ArrayList<Percorso>();
		bestPercorso = new Percorso(null, 0);
		
		Percorso parziale = new Percorso(null, 0);
		List<Attivita> attivita = new ArrayList<Attivita>();
		
		for(Comune c : grafo.vertexSet()) {
			parziale = new Percorso(c, 0);
			attivita.addAll(c.getListaAttivita());
			for(Comune v : Graphs.neighborListOf(grafo, c)) {
				attivita.addAll(v.getListaAttivita());
			}
		}
		System.out.println(comune.getListaBeB().size()+" "+attivita.size());
		
		cerca(spesaMax, parziale, numGiorni, distanzaMax, 0, attivita, comune);
		
		return bestPercorso;
	}
	
	private void cerca(double spesaMax, Percorso parziale, int numGiorni, int distanzaMax, int livello, List<Attivita> attivita, Comune comune) {
		
		if(parziale.getCosto() != 0 && parziale.getCosto()<spesaMax && parziale.getAttivita().size()==2*numGiorni) {
			altriPercorsi.add(new Percorso(parziale));
			if(parziale.getCosto()>bestPercorso.getCosto()) {
				bestPercorso = new Percorso(parziale);
			}
			return;
		}
		
		if(numGiorni>1) {
			for(BeB b : comune.getListaBeB()) {
				parziale.setBeb(b);
				parziale.addCosto(parziale.getBeb().getPrezzo());
				for(Attivita a : attivita) {
					if(parziale.getAttivita().size()<2*numGiorni 
							&& LatLngTool.distance(b.getCoordinate(), a.getCoordinate(), LengthUnit.KILOMETER)<distanzaMax 
							&& !parziale.getAttivita().contains(a)) {
						parziale.addAttivita(a);
						parziale.addCosto(a.getPrezzo());
						cerca(spesaMax, parziale, numGiorni, distanzaMax, livello+1, attivita, comune);
						parziale.removeAttivita(a);
						parziale.removeCosto(a.getPrezzo());
					}
				}
				parziale.removeCosto(parziale.getBeb().getPrezzo());
			}
		}
		else {
			for(Attivita a : attivita) {
				if(parziale.getAttivita().size()<2*numGiorni 
						&& !parziale.getAttivita().contains(a)
						/*&& LatLngTool.distance(b.getCoordinate(), a.getCoordinate(), LengthUnit.KILOMETER)<distanzaMax*/ ) { 
					// risolvere caso in cui si faccia tutto in un giorno: (distanza fra due qualsiasi attivita) < distanzaMax
					parziale.addAttivita(a);
					parziale.addCosto(a.getPrezzo());
					cerca(spesaMax, parziale, numGiorni, distanzaMax, livello+1, attivita, comune);
					parziale.removeAttivita(a);
					parziale.removeCosto(a.getPrezzo());
				}
			}
		}
	}

	public List<Percorso> getAltriPercorsi() {
		Collections.sort(altriPercorsi, new Comparator<Percorso>() {
			public int compare(Percorso p1, Percorso p2) {
				if (p1.getCosto() > p2.getCosto()) return -1;
		        if (p1.getCosto() < p2.getCosto()) return 1;
		        return 0;
			}
		});
		altriPercorsi.remove(bestPercorso);
		return altriPercorsi;
	}
}

package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
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
	private List<Attivita> attivita;
	private Graph<VerticeGrafo, DefaultWeightedEdge> grafo;
	private final double distMax; // Imposto una distanza massima, espressa in kilometri, per la quale due comuni non risultano collegati direttamente all'interno del grafo
	private List<Comune> comuni;
	private List<BeB> listaBeB;
	
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
		attivita = new ArrayList<Attivita>();
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
		attivita = new ArrayList<Attivita>();
	}
	
	// ----- Metodi per l'aggiunta dei B&B -----
	
	/**
	 * Seleziona tutti i BeB dei comuni presenti nella lista comuni,
	 * che permettono di soggiornare per un numero di notti minore o uguale a quello passato come parametro.
	 * @param numNotti
	 * @return {@link List} dei B&B corrispondenti
	 */
	public void addBeBComune(int numNotti){
		listaBeB = new ArrayList<BeB>();
		if(numNotti!=0) {
			for(Comune c : comuni) {
				listaBeB.addAll(dao.getBeBComune(c, numNotti));
			}
		}
	}
	
	// ----- Metodo per la creazione del grafo -----
	
	/**
	 * Crea un grafo semplice pesato, i cui vertici sono i comuni, le attivita ed i BeB corrispondenti alle richieste dell'utente.
	 * Per poter inserire tutte e tre le classi all'interno di uno stesso grafo, si utilizza una classe di supporto 'VerticeGrafo', 
	 * la quale funge da classe padre alle tre precedenti.
	 */
	public void creaGrafo() {
		
		grafo = new SimpleWeightedGraph<VerticeGrafo, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, comuni);
		Graphs.addAllVertices(grafo, listaBeB);
		Graphs.addAllVertices(grafo, attivita);
		
		for(VerticeGrafo v1 : grafo.vertexSet()) {
			if(v1 instanceof Comune) {
				Comune c1 = (Comune) v1;
				for(VerticeGrafo v2 : grafo.vertexSet()) {
					if(v2 instanceof Comune) {
						Comune c2 = (Comune) v2;
						if(!c1.equals(c2)) {
							if(grafo.getEdge(c1, c2) == null) {
								for(LatLng coor1 : c1.getMapCapCoordinate().values()) {
									for(LatLng coor2 : c2.getMapCapCoordinate().values()) {
										Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
										if(distanza < distMax) {
											Graphs.addEdgeWithVertices(grafo, v1, v2, distanza);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		for(VerticeGrafo v1 : grafo.vertexSet()) {
			if(v1 instanceof Comune) {
				Comune c = (Comune) v1;
				for(VerticeGrafo v2 : grafo.vertexSet()) {
					if(v2 instanceof BeB) {
						BeB b = (BeB) v2;
						if( grafo.getEdge(v1, v2) == null && b.getComune().equals(c) ) {
							for(LatLng coor1 : c.getMapCapCoordinate().values()) {
								LatLng coor2 = b.getCoordinate();
								Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
								Graphs.addEdgeWithVertices(grafo, v1, v2, distanza);
							}
						}
					}
				}
			}
		}
		
		for(VerticeGrafo v1 : grafo.vertexSet()) {
			if(v1 instanceof Comune) {
				Comune c = (Comune) v1;
				for(VerticeGrafo v2 : grafo.vertexSet()) {
					if(v2 instanceof Attivita) {
						Attivita a = (Attivita) v2;
						if( grafo.getEdge(c, a) == null && a.getComune().equals(c) ) {
							for(LatLng coor1 : c.getMapCapCoordinate().values()) {
								LatLng coor2 = a.getCoordinate();
								Double distanza = LatLngTool.distance(coor1, coor2, LengthUnit.KILOMETER);
								Graphs.addEdgeWithVertices(grafo, v1, v2, distanza);
							}
						}
					}
				}
			}
		}
		System.out.println("vertici: "+grafo.vertexSet().size()+", archi: "+grafo.edgeSet().size());
	}

	// ----- Metodi per l'aggiunta e la selezione delle attivita' -----
	
	/**
	 * Aggiunge alla {@link List} attivita, tutte le attivita' turistiche presenti sul territorio dei comuni idonei.
	 * @param tipologie tipo di attivita turistiche selezionate dall'utente
	 * @param numPersone numero di viaggiatori
	 */
	public void addAttivitaTuristicheComuni(List<String> tipologie, int numPersone) {
		for(Comune c : comuni) {
			for(String t : tipologie) {
				attivita.addAll(dao.getAttivitaTuristicheComuni(c, t, numPersone));
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
				attivita.addAll(dao.getLuoghiInteresseComuni(c, t, numPersone));
			}
		}
	}

	/**
	 * Aggiunge alla {@link List} attivita, tutti gli stabilimenti balneari presenti sul territorio dei comuni idonei.
	 * @param numPersone numero di viaggiatori
	 */
	public void addStabilimentiBalneariComuni(int numPersone) {
		for(Comune c : comuni) {
			attivita.addAll(dao.getStabilimentiBalneariComuni(c, numPersone));
		}
	}

	public List<Attivita> getAttivita() {
		return attivita;
	}
	
	// ----- Ricorsione -----
	
	public List<VerticeGrafo> ricorsione(double spesaMax){
		
		return null;
	}
	
	private void cerca() {
		
	}
}

package it.polito.tdp.TasteTrip.model;

public class Attivita {

	private String nome;
	private String tipologia;
	private String comune;
	private String indirizzo;
	private int cap;
	private double lat;
	private double lng;
	private int prezzo;
	
	public Attivita(String nome, String tipologia, String comune, String indirizzo, int cap, double lat, double lng, int prezzo) {
		this.nome = nome;
		this.tipologia = tipologia;
		this.comune = comune;
		this.indirizzo = indirizzo;
		this.cap = cap;
		this.lat = lat;
		this.lng = lng;
		this.prezzo = prezzo;
	}
	
	
}

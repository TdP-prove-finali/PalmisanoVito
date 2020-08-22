package it.polito.tdp.TasteTrip.model;

import com.javadocmd.simplelatlng.LatLng;

public class BeB extends VerticeGrafo {
	
	private String nome;
	private String host;
	private Comune comune;
	private String tipoCamera;
	private LatLng coordinate;
	private double prezzo;
	
	public BeB(String nome, String host, Comune comune, String tipoCamera, LatLng coordinate, double prezzo) {
		this.nome = nome;
		this.host = host;
		this.comune = comune;
		this.tipoCamera = tipoCamera;
		this.coordinate = coordinate;
		this.prezzo = prezzo;
	}

	public String getNome() {
		return nome;
	}

	public String getHost() {
		return host;
	}

	public Comune getComune() {
		return comune;
	}

	public String getTipoCamera() {
		return tipoCamera;
	}

	public LatLng getCoordinate() {
		return coordinate;
	}

	public double getPrezzo() {
		return prezzo;
	}

	@Override
	public String toString() {
		return "Fatti ospitare da " +  host + " nel B&B \"" + nome + "\" situato in " + comune + ".\nTipologia di camera: " + tipoCamera + "\nPrezzo: " + prezzo;
	}
	
	
}

package it.polito.tdp.TasteTrip.model;

import com.javadocmd.simplelatlng.LatLng;

public class BeB {
	
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comune == null) ? 0 : comune.hashCode());
		result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		long temp;
		temp = Double.doubleToLongBits(prezzo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((tipoCamera == null) ? 0 : tipoCamera.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeB other = (BeB) obj;
		if (comune == null) {
			if (other.comune != null)
				return false;
		} else if (!comune.equals(other.comune))
			return false;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (Double.doubleToLongBits(prezzo) != Double.doubleToLongBits(other.prezzo))
			return false;
		if (tipoCamera == null) {
			if (other.tipoCamera != null)
				return false;
		} else if (!tipoCamera.equals(other.tipoCamera))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fatti ospitare da " +  host + " nel B&B \"" + nome + "\" situato in " + comune + ".\nTipologia di camera: " + tipoCamera + "\nPrezzo: " + String.format("%.2f €", prezzo);
	}
	
	
}

package it.polito.tdp.TasteTrip.model;

import com.javadocmd.simplelatlng.LatLng;

public class Attivita {

	private String nome;
	private String tipologia;
	private Comune comune;
	private String indirizzo;
	private int cap;
	private LatLng coordinate;
	private double prezzo;
	private int ordine;
	
	public Attivita(String nome, String tipologia, Comune comune, String indirizzo, int cap, LatLng coordinate, double prezzo) {
		this.nome = nome;
		this.tipologia = tipologia;
		this.comune = comune;
		this.indirizzo = indirizzo;
		this.cap = cap;
		this.coordinate = coordinate;
		this.prezzo = prezzo;
		ordine = -1;
	}

	public String getNome() {
		return nome;
	}

	public String getTipologia() {
		return tipologia;
	}

	public Comune getComune() {
		return comune;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public int getCap() {
		return cap;
	}

	public LatLng getCoordinate() {
		return coordinate;
	}

	public double getPrezzo() {
		return prezzo;
	}
	
	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cap;
		result = prime * result + ((comune == null) ? 0 : comune.hashCode());
		result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result + ((indirizzo == null) ? 0 : indirizzo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		long temp;
		temp = Double.doubleToLongBits(prezzo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((tipologia == null) ? 0 : tipologia.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attivita other = (Attivita) obj;
		if (cap != other.cap)
			return false;
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
		if (indirizzo == null) {
			if (other.indirizzo != null)
				return false;
		} else if (!indirizzo.equals(other.indirizzo))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (Double.doubleToLongBits(prezzo) != Double.doubleToLongBits(other.prezzo))
			return false;
		if (tipologia == null) {
			if (other.tipologia != null)
				return false;
		} else if (!tipologia.equals(other.tipologia))
			return false;
		return true;
	}

	public String toString() {
		return nome + ", " + tipologia + ", " + comune + ", costo: " + String.format("%.2f €", prezzo);
	}
	
	
}

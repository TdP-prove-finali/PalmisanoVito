package it.polito.tdp.TasteTrip.model;

import java.util.HashMap;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

public class Comune extends VerticeGrafo {
	
	private String nome;
	private String provincia;
	private String nomeProvincia;
	private Map<Integer, LatLng> mapCapCoordinate;
	
	public Comune(String nome, String provincia, String nomeProvincia, LatLng coordinate, int cap) {
		this.nome = nome;
		this.provincia = provincia;
		this.nomeProvincia = nomeProvincia;
		mapCapCoordinate = new HashMap<Integer, LatLng>();
		mapCapCoordinate.put(cap, coordinate);
	}

	public String getNome() {
		return nome;
	}

	public String getProvincia() {
		return provincia;
	}
	
	public String getNomeProvincia() {
		return nomeProvincia;
	}

	public Map<Integer, LatLng> getMapCapCoordinate() {
		return mapCapCoordinate;
	}

	public void addCapCoordinate(int cap, double lat, double lng) {
		LatLng coordinate = new LatLng(lat, lng);
		mapCapCoordinate.put(cap, coordinate);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Comune other = (Comune) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public String toString() {
		return nome;
	}
}

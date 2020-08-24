package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

public class Comune {
	
	private String nome;
	private String provincia;
	private String nomeProvincia;
	private Map<Integer, LatLng> mapCapCoordinate;
	private List<BeB> listaBeB;
	private List<Attivita> listaAttivita;
	
	public Comune(String nome, String provincia, String nomeProvincia, LatLng coordinate, int cap) {
		this.nome = nome;
		this.provincia = provincia;
		this.nomeProvincia = nomeProvincia;
		mapCapCoordinate = new HashMap<Integer, LatLng>();
		mapCapCoordinate.put(cap, coordinate);
		listaBeB = new ArrayList<BeB>();
		listaAttivita = new ArrayList<Attivita>();
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

	public List<BeB> getListaBeB() {
		return listaBeB;
	}

	public void addListaBeB(List<BeB> listaBeB) {
		this.listaBeB.addAll(listaBeB);
	}

	public List<Attivita> getListaAttivita() {
		return listaAttivita;
	}

	public void addListaAttivita(List<Attivita> listaAttivita) {
		this.listaAttivita.addAll(listaAttivita);
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

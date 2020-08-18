package it.polito.tdp.TasteTrip.model;

public class Comune {
	
	private String nome;
	private String provincia;
	private int cap;
	
	public Comune(String nome, String provincia, int cap) {
		this.nome = nome;
		this.provincia = provincia;
		this.cap = cap;
	}
	
	public String getNome() {
		return nome;
	}

	public String getProvincia() {
		return provincia;
	}

	public int getCap() {
		return cap;
	}

	public String toString() {
		return nome;
	}
}

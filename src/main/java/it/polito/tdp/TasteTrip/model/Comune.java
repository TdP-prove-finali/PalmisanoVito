package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.List;

public class Comune {
	
	private String nome;
	private String provincia;
	private List<Integer> listaCap;
	
	public Comune(String nome, String provincia, int cap) {
		this.nome = nome;
		this.provincia = provincia;
		listaCap = new ArrayList<Integer>();
		listaCap.add(cap);
	}
	
	public String getNome() {
		return nome;
	}

	public String getProvincia() {
		return provincia;
	}

	public List<Integer> getCap() {
		return listaCap;
	}
	
	public void addCap(int cap) {
		listaCap.add(cap);
	}

	public String toString() {
		return nome;
	}
}

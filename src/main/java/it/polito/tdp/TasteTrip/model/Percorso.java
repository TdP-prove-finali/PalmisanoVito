package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.List;

public class Percorso {
	
	private Comune comune;
	private BeB beb;
	private List<Attivita> attivita;
	private double costo;
	
	public Percorso(Comune comune, double costo) {
		this.comune = comune;
		this.costo = costo;
		attivita = new ArrayList<Attivita>();
		beb = null;
	}
	
	public Percorso(Percorso p) {
		this.comune = p.getComune();
		this.beb = p.getBeb();
		this.attivita = new ArrayList<Attivita>(p.getAttivita());
		this.costo = p.getCosto();
	}

	public Comune getComune() {
		return comune;
	}
	
	public BeB getBeb() {
		return beb;
	}
	
	public List<Attivita> getAttivita() {
		return attivita;
	}

	public double getCosto() {
		return costo;
	}
	
	public void setComune(Comune comune) {
		this.comune = comune;
	}

	public void addAttivita(Attivita a) {
		attivita.add(a);
	}
	
	public void removeAttivita(Attivita a) {
		attivita.remove(a);
	}
	
	public void setBeb(BeB beb) {
		this.beb = beb;
	}
	
	public void unsetBeb() {
		this.beb = null;
	}
	
	public void addCosto(double costo) {
		this.costo += costo;
	}
	
	public void removeCosto(double costo) {
		this.costo -= costo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attivita == null) ? 0 : attivita.hashCode());
		result = prime * result + ((beb == null) ? 0 : beb.hashCode());
		long temp;
		temp = Double.doubleToLongBits(costo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Percorso other = (Percorso) obj;
		if (attivita == null) {
			if (other.attivita != null)
				return false;
		} else if (!attivita.equals(other.attivita))
			return false;
		if (beb == null) {
			if (other.beb != null)
				return false;
		} else if (!beb.equals(other.beb))
			return false;
		if (Double.doubleToLongBits(costo) != Double.doubleToLongBits(other.costo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		if(beb != null) {
			s = "Soggiorna nella magnifica cittadina di "+ comune + ".\n" + beb + "\nSul territorio potrai eseguire le seguenti attivita: ";
		}
		else {
			s = "Soggiorna nella magnifica cittadina di "+ comune + ".\nNessun B&B selezionato, data la durata del viaggio di un solo giorno.\nSul territorio potrai eseguire le seguenti attivita: ";
		}
		for(Attivita a : attivita) {
			s += "\n"+a;
		}
		s += String.format("\nCosto totale: %.2f €", costo);
		return s;
	}

	
	
}

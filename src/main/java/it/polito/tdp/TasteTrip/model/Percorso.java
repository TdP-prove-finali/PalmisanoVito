package it.polito.tdp.TasteTrip.model;

import java.util.ArrayList;
import java.util.List;

public class Percorso {
	
	private Comune comune;
	private BeB beb;
	private List<Attivita> attivita;
	private double costo;
	private int numGiorni;
	
	public Percorso(Comune comune, double costo, int numGiorni) {
		this.comune = comune;
		this.costo = costo;
		this.numGiorni = numGiorni;
		attivita = new ArrayList<Attivita>();
		beb = null;
	}
	
	public Percorso(Percorso p) {
		this.comune = p.getComune();
		this.beb = p.getBeb();
		this.attivita = new ArrayList<Attivita>(p.getAttivita());
		this.costo = p.getCosto();
		this.numGiorni = p.getNumGiorni();
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

	public int getNumGiorni() {
		return numGiorni;
	}

	public void setNumGiorni(int numGiorni) {
		this.numGiorni = numGiorni;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attivita == null) ? 0 : attivita.hashCode());
		result = prime * result + ((beb == null) ? 0 : beb.hashCode());
		result = prime * result + ((comune == null) ? 0 : comune.hashCode());
		long temp;
		temp = Double.doubleToLongBits(costo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numGiorni;
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
		if (comune == null) {
			if (other.comune != null)
				return false;
		} else if (!comune.equals(other.comune))
			return false;
		if (Double.doubleToLongBits(costo) != Double.doubleToLongBits(other.costo))
			return false;
		if (numGiorni != other.numGiorni)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		if(numGiorni<=1 && comune != null) {
			s = "Visita i luighi del territorio in cui e' collocata la magnifica cittadina di "+ comune
					+ ".\nNessun B&B selezionato, data la durata del viaggio di un solo giorno."
					+ "\nSul territorio potrai eseguire le seguenti attivita': ";
		}
		else if(comune != null && comune.getListaBeB().isEmpty() && numGiorni>1) {
			s = "Purtroppo non e' stato trovato nessun B&B sul territorio del comune selezionato.";
		}
		else if(comune != null && !comune.getListaBeB().isEmpty() && beb == null && attivita.isEmpty()) {
			s = "Purtroppo, con le scelte effettuate, non e' possibile trovare nessun itinerario di viaggio.";
		}
		else if(beb == null && comune == null) { // Caso in cui scelgo la provincia, ma non il comune
			s = "Sull'ampio territorio da te selezionato, potrai eseguire le seguenti fantastiche attivita': ";
		}
		else {
			s = "Soggiorna nella magnifica cittadina di "+ comune 
					+ ".\n" + beb + "\nSul territorio potrai eseguire le seguenti attivita': ";
		}
		for(Attivita a : attivita) {
			s += "\n"+a;
		}
		s += String.format("\nCosto totale: %.2f €", costo);
		return s;
	}

	
	
}

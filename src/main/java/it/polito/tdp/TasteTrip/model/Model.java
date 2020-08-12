package it.polito.tdp.TasteTrip.model;

import java.util.List;

import it.polito.tdp.TasteTrip.db.TasteTripDAO;

public class Model {

	private TasteTripDAO dao;
	
	public Model() {
		dao = new TasteTripDAO();
	}
	
	public List<String> getNomi(){
		return dao.loadAllBeB();
	}

	public List<String> getCommuniProvincia(String sigla) {
		return dao.getCommuniProvincia(sigla);
	}
}

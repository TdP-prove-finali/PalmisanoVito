package it.polito.tdp.TasteTrip.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.TasteTrip.db.DBConnect;
import it.polito.tdp.TasteTrip.db.TasteTripDAO;
import it.polito.tdp.TasteTrip.model.Attivita;
import it.polito.tdp.TasteTrip.model.Comune;

public class TestDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = DBConnect.getConnection();
			connection.close();
			System.out.println("Test PASSED");

		} catch (Exception e) {
			System.err.println("Test FAILED");
		}

		TasteTripDAO dao = new TasteTripDAO();

//		for(String s : dao.loadAllBeB())
//			System.out.println(s); 
		
		List<Comune> comuni = dao.getCommuniByProvincia("BR");
		for(Comune c : comuni) {
			System.out.println(c.getNomeProvincia()+" "+c.getMapCapCoordinate().keySet()+c.getMapCapCoordinate().values());
		}
		
		Double distanzaMedia = LatLngTool.distance(new LatLng(41.16063, 15.33759), new LatLng(39.90974, 18.23590), LengthUnit.KILOMETER);
		System.out.println(distanzaMedia);
		
		List<String> tipologie = new ArrayList<String>();
		tipologie.add("Attivita' di formazione");
		tipologie.add("Attivita' sportiva");
		List<Attivita> ris = new ArrayList<>();
		
		System.out.println(ris);
	}

}

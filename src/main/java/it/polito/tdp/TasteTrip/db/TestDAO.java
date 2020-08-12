package it.polito.tdp.TasteTrip.db;

import java.sql.Connection;

import it.polito.tdp.TasteTrip.db.DBConnect;
import it.polito.tdp.TasteTrip.db.TasteTripDAO;

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

		for(String s : dao.loadAllBeB())
			System.out.println(s);
		
	}

}

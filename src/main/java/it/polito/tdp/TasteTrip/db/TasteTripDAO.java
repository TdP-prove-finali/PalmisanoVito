package it.polito.tdp.TasteTrip.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.TasteTrip.db.DBConnect;

public class TasteTripDAO {

	public List<String> loadAllBeB() {

		String sql = "SELECT name FROM puglia";
		List<String> beb = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s = res.getString("name");
				beb.add(s);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return beb;
	}

	public List<String> getCommuniProvincia(String sigla) {
		
		String sql = "SELECT DISTINCT nomeProvincia FROM comuni WHERE nomePRovincia LIKE ? ORDER BY nomeProvincia";
		List<String> comuni = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, "%("+sigla+")");
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s = res.getString("nomeProvincia");
				comuni.add(s);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return comuni;
	}
}

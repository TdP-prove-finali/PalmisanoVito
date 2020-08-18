package it.polito.tdp.TasteTrip.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.TasteTrip.db.DBConnect;
import it.polito.tdp.TasteTrip.model.Attivita;
import it.polito.tdp.TasteTrip.model.Comune;

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

	public List<Comune> getCommuniProvincia(String sigla) {
		
		String sql = "SELECT DISTINCT nomeProvincia, cap FROM comuni_puglia WHERE nomeProvincia LIKE ? ORDER BY nomeProvincia";
		List<Comune> comuni = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, "%("+sigla+")");
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String nome = res.getString("nomeProvincia");
				nome = nome.substring(0, nome.length()-5);
				Comune c = new Comune(nome, sigla, res.getInt("cap"));
				comuni.add(c);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return comuni;
	}
	
	public void getAttivitaTuristicheComune(String comune, List<Attivita> attivita){
		
		String sql = "";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
//			st.setString(1, "%("+sigla+")");
			ResultSet res = st.executeQuery();

			while (res.next()) {
				attivita.add(new Attivita(res.getString("nome"), res.getString("nome"), comune, res.getString("nome"), cap, res.getString("nome"), res.getString("nome"), res.getString("nome")));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

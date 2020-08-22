package it.polito.tdp.TasteTrip.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.TasteTrip.db.DBConnect;
import it.polito.tdp.TasteTrip.model.Attivita;
import it.polito.tdp.TasteTrip.model.BeB;
import it.polito.tdp.TasteTrip.model.Comune;

public class TasteTripDAO {

	// ----- Metodi per la selezione dei comuni -----
	
	public List<Comune> getCommuniByProvincia(String sigla) {
		
		String sql = "SELECT * FROM comuni_puglia WHERE provincia = ? ORDER BY nome";
		List<Comune> comuni = new ArrayList<Comune>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, sigla);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				boolean esistente = false;
				for(Comune c : comuni) {
					if( c.getNome().equals(res.getString("nome"))) {
						c.addCapCoordinate(res.getInt("cap"), res.getDouble("lat"), res.getDouble("lng"));
						esistente = true;
					}
				}
				if(!esistente) {
					comuni.add(new Comune(res.getString("nome"), res.getString("provincia"), res.getString("nomeProvincia"), new LatLng(res.getDouble("lat"), res.getDouble("lng")), res.getInt("cap")));
				}
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return comuni;
	}
	
	public List<Comune> getAllCommuni() {
		
		String sql = "SELECT * FROM comuni_puglia";
		List<Comune> comuni = new ArrayList<Comune>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				boolean esistente = false;
				for(Comune c : comuni) {
					if( c.getNome().equals(res.getString("nome"))) {
						c.addCapCoordinate(res.getInt("cap"), res.getDouble("lat"), res.getDouble("lng"));
						esistente = true;
					}
				}
				if(!esistente) {
					comuni.add(new Comune(res.getString("nome"), res.getString("provincia"), res.getString("nomeProvincia"), new LatLng(res.getDouble("lat"), res.getDouble("lng")), res.getInt("cap")));
				}
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return comuni;
	}
	
	// ----- Metodi per la selezione dei B&B -----
	
	public List<BeB> getBeBComune(Comune comune, int numNotti) {

		String sql = "SELECT `name`, host_name, neighbourhood, room_type, latitude, longitude, price FROM beb WHERE minimum_nights<=? AND neighbourhood=?";
		List<BeB> listaBeb = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, numNotti);
			st.setString(2, comune.getNome());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				listaBeb.add(new BeB(res.getString("name"), res.getString("host_name"), comune, res.getString("name"), new LatLng( res.getDouble("latitude"), res.getDouble("longitude") ), res.getDouble("price")));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return listaBeb;
	}
	
	// ----- Metodi per la selezione delle varie attivita' -----
	
	public List<Attivita> getAttivitaTuristicheComuni(Comune comune, String tipologia, int numPersone){ // Ricevo la lista di comuni che è possibile visitare a partire dal nodo scelto e la lista di tipologie di attivita' scelte dall'utente
		
		List<Attivita> attivita = new ArrayList<Attivita>();
		
		String sql = "SELECT nome, tipologia, comune, indirizzo, cap, latitudine, longitudine, prezzoIntero FROM attivita_turistiche WHERE comune = ? AND tipologia = ?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, comune.getNomeProvincia());
			st.setString(2, tipologia);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				attivita.add(new Attivita(res.getString("nome"), tipologia, comune, res.getString("indirizzo"), res.getInt("cap"), new LatLng(res.getDouble("latitudine"), res.getDouble("longitudine")), res.getDouble("prezzoIntero")*numPersone));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return attivita;
	}
	
	public List<Attivita> getLuoghiInteresseComuni(Comune comune, String tipologia, int numPersone){ // Ricevo la lista di comuni che è possibile visitare a partire dal nodo scelto e la lista di tipologie di attivita' scelte dall'utente
		
		List<Attivita> attivita = new ArrayList<Attivita>();

		String sql = "SELECT nomeAttrattore, tipologia, comune, indirizzo, cap, latitudine, longitudine FROM luoghi_interesse WHERE comune = ? AND tipologia = ?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, comune.getNomeProvincia());
			st.setString(2, tipologia);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				attivita.add(new Attivita(res.getString("nomeAttrattore"), tipologia, comune, res.getString("indirizzo"), res.getInt("cap"), new LatLng(res.getDouble("latitudine"), res.getDouble("longitudine")), 0.0*numPersone ));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return attivita;
	}
	
	public List<Attivita> getStabilimentiBalneariComuni(Comune comune, int numPersone) {

		List<Attivita> attivita = new ArrayList<Attivita>();
		String lettini = "lettini (";
		String ombrellonne = "ombrellone (";
		
		String sql = "SELECT Nome_operatore, Comune, Indirizzo, CAP, Latitudine, Longitudine, Prezzi_alta_o_unica_stagione FROM stabilimenti_balneari WHERE comune = ?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, comune.getNome());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String s = res.getString("Prezzi_alta_o_unica_stagione");
				Double prezzo = 0.0;
				
				if( s!=null ) {
					if(s.indexOf(lettini) != -1) {
						
						String pLett = s.substring( s.indexOf(lettini) + lettini.length() , s.indexOf(lettini) + lettini.length() + 2 );
						if(pLett.contains("-")) {
							pLett = pLett.substring(0, 1);
						}
						prezzo += Double.parseDouble(pLett)*numPersone;
					}
					if(s.indexOf(ombrellonne) != -1) {
						
						String pOmbr = s.substring( s.indexOf(ombrellonne) + ombrellonne.length() , s.indexOf(ombrellonne) + ombrellonne.length() + 2 );
						if(pOmbr.contains("-")) {
							pOmbr = pOmbr.substring(0, 1);
						}
						prezzo += Double.parseDouble(pOmbr)*Math.round(((double)numPersone)/2);
					}
				}
				
				attivita.add(new Attivita(res.getString("Nome_operatore"), "Stabilimenti Balneari", comune, res.getString("Indirizzo"), res.getInt("CAP"), new LatLng(res.getDouble("latitudine"), res.getDouble("longitudine")), prezzo));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return attivita;
	}

	
}

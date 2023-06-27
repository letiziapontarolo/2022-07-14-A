package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.nyc.model.Adiacenza;
import it.polito.tdp.nyc.model.Hotspot;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> listaBorghi(){
		String sql = "SELECT Borough "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "GROUP BY Borough "
				+ "ORDER BY Borough";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("Borough"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public void creaGrafo(List<String> lista, String borgo){
		String sql = "SELECT NTACode "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE Borough = (?) "
				+ "GROUP BY NTACode";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, borgo);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				lista.add(res.getString("NTACode"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

	}
	
	public List<Adiacenza> getAdiacenze (List<String> listaBorghi, String borgo) {
		String sql = "SELECT a.NTACode AS NTACode1, b.NTACode AS NTACode2, COUNT(DISTINCT a.SSID) + COUNT(DISTINCT b.SSID) AS peso "
				+ "FROM nyc_wifi_hotspot_locations AS a "
				+ "JOIN nyc_wifi_hotspot_locations AS b ON a.NTACode < b.NTACode "
				+ "WHERE a.Borough = (?) AND b.Borough = (?) "
				+ "GROUP BY a.NTACode, b.NTACode";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, borgo);
		st.setString(2, borgo);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
		String NTA1 = rs.getString("NTACode1");
		String NTA2 = rs.getString("NTACode2");
		int peso = rs.getInt("peso");
		Adiacenza ad = new Adiacenza(NTA1, NTA2, peso);
		result.add(ad);
		}
		rs.close();
		conn.close();
		return result;
		} catch (SQLException e) {
		e.printStackTrace();
		return null;
		}
		}

	
}
